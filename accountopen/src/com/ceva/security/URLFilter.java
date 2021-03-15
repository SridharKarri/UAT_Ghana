package com.ceva.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.ceva.bank.common.dao.impl.SecurityDAOImpl;
import com.ceva.crypto.tools.AESCBCEncryption;
import com.ceva.db.model.ChannelUsers;
import com.nbk.util.Constants;

import net.sf.json.JSONObject;

public class URLFilter implements Filter,Modes{
	ServletContext context;
	private SecurityDAOImpl securityDAO;
	private SecurityUtil securityUtil;
	private static Logger logger = Logger.getLogger(URLFilter.class);
	
	ResourceBundle bundle=FilterFactory.getBundle();

	private String chkey;
	private String encInp;
	
	String sessionkey = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("INIT FILTER REQUEST");
		this.context = filterConfig.getServletContext();
		this.sessionkey = securityDAO.fetchMasterKey(bundle.getString("ANDROID.CHANNEL"));
	}

	public Map<Inputs,String> splitInput(String uri) throws UnsupportedEncodingException, DecoderException{

		Map<Inputs,String> inpMap = new HashMap<Inputs, String>();
		String[] inputs = uri.split("/");
		Integer i=0;
		if(inputs.length != 17)
			return null;
		for(Inputs eInp : Inputs.values()){
			if(i >= inputs.length)
				break;
			inpMap.put(eInp,inputs[i]);
			i++;
		}
		return inpMap;
	}
	
	public Map<Inputs,String> getBody(HttpServletRequest request, BodyRequestWrapper wrappedRequest) throws DecoderException, IOException{
		
		String body = IOUtils.toString(wrappedRequest.getReader());
		JSONObject obj = JSONObject.fromObject(body);
		System.out.println(body);
		Map<Inputs,String> inpMap = new HashMap<Inputs, String>();
		inpMap.put(Inputs.INPUT, obj.getString("data"));
		inpMap.put(Inputs.DHASH, obj.getString("hash"));
		inpMap.put(Inputs.APPID, obj.getString("appId"));
		return inpMap;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest newRequest = (HttpServletRequest)request;
		String urlEndPoint = null;
		HttpServletRequest enhancedHttpRequest=null;
		String requestURL = newRequest.getRequestURL().toString();
		logger.debug("REQUEST URL : "+requestURL);
		logger.info(request.getServerName());
		BodyRequestWrapper wrappedRequest =  new BodyRequestWrapper(newRequest);
		
			String man = newRequest.getHeader("man");
			String serial = newRequest.getHeader("serial");
			logger.info("man..:"+man+", serial..:"+serial);
			Map<Inputs, String> inputMap=null;
			try {
				inputMap = getBody(newRequest, wrappedRequest);
			} catch (DecoderException e1) {
				e1.printStackTrace();
			}
	
			urlEndPoint = getAction(requestURL);
			if(inputMap != null && Arrays.asList(Constants.ANDROID_END_POINTS).indexOf(urlEndPoint) > -1)
			{
				String appid = "";
				ChannelUsers bean=null;
				
				try{
					appid = SecurityUtil.getAndroidPath(inputMap, bundle, Inputs.APPID);
					logger.info("APPID : "+appid);
					bean = securityDAO.fetchSecurityData(appid);
					byte[] key = AESCBCEncryption.base64Decode(sessionkey);
					byte[] iv = AESCBCEncryption.base64Decode(bean.getsIv());
					String hashCode = inputMap.get(Inputs.DHASH);
					String path = SecurityUtil.getAndroidPath(inputMap,Inputs.INPUT,key, iv);
					String imei = "";// SecurityUtil.getAndroidPath(inputMap, Inputs.IMEI, key, iv);

					if(!hashCode.equals(SecurityUtil.generateBodyHashString(path)))
					{
						noteFraud(newRequest, response, "Digital Signature Mismatched for "+inputMap.get(Inputs.ENDPOINT), bundle.getString("ANDROID.CHANNEL"), appid);
						return;
					}
					logger.info("body : "+path);
					wrappedRequest.resetInputStream(path.getBytes());
					filterChain.doFilter(wrappedRequest, response);
			}
			catch(Exception e){
				//e.printStackTrace();
				noteFraud(newRequest, response, "Unable to Decrypt the request parameters sent for the Tranaction "+inputMap.get(Inputs.ENDPOINT), bundle.getString("ANDROID.CHANNEL"), appid);
				return;
			}
		}else
		{
			logger.info("End point is not there in list, please check");
			noteFraud(newRequest, response, "Invalid Endpoint found.", " ", " ");
			return;
		}
	}
	
	private void noteFraud(HttpServletRequest newRequest, ServletResponse response, String message, String channel, String appid){
		if(securityDAO.noteFraud(SecurityUtil.buildErrorMap(newRequest, message, channel, appid))){
			logger.info("[URLFilter][filter][ENCRYPT][FRAUD FOUND]");
		}
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.reset();
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		resp.setHeader("Location", "");
	}

	public String getChkey() {
		return chkey;
	}

	public void setChkey(String chkey) {
		this.chkey = chkey;
	}

	public String getEncInp() {
		return encInp;
	}

	public void setEncInp(String encInp) {
		this.encInp = encInp;
	}

	@Override
	public void destroy() {
		logger.debug("destroy");

	}

	public String toHex(String arg) throws UnsupportedEncodingException {
		return String.format("%040x", new BigInteger(1, arg.getBytes("UTF-8")));
	}

	public static void main(String args[]) throws Exception
	{
		/*String path = "307649366c397568337762315054575534306b5655396c4e69417042737a464d68616d356a35553533504355312f6d6365485975694b36366143556d46336e753363726b62456a66682b5a650d0a616c2b304d66546b386769596578536877586c666c736b71665246733641754d6345447538317351456865575a5153716648547a6543657147312f4f2b716e6b506d4e4974742b516c3331590d0a746c6a654259732b7a7773705168626e722f53773638475574505a546c6d386b35465538336a54793854522b6d5a726e694a6b6b313939554d53436a6c4c43437170706466516b5632424e720d0a6430614d653178302b534c635569412f57675837352b4c784b6a4959784757753437595a434b6b535a647349744d344175566f35636f636f3672682f696c31366c64415459717045486562700d0a684649796c2b4c726c6a6a625067624a41416d756f63726a6b6e66644d6845702f457a6b376659386d4e6e2f75466c686b7a7634783878534a65425a38463752645a4e6743445861496673620d0a707953426d676a2f7859524d627268612b786b2f4b455a693161494b6566634c39757674574f6e6535322b72653963762f71335679305757486a50624875585939315645435550424e4463730d0a61554863784258745848424f68323967397564332b5846794a5239754b7835335469357a366d4248414d702b6f4d63717237346b424a36367159614c775270423853384176572b74792f2f440d0a6a73656d6b706f6d5a554830756b65537a6f7256426c692f4365456f4e3074574e332b6a41413153784b4c6750524c554452375a734b6a355a764b6d365548566f33714f2b417731385147310d0a50387648726942797349646b6c554d723579525a7568346d674e76507969486e713273474e713930533658626c5a7545585a2b4176322b3478755941744d6d586270595a3934735a344f716d0d0a6f687543382f48364b3574574a6359625978584d65494b306d7179716b4e696d444766796f563374305146472b53676e35676c6e2f615a79417770346e6b7a75715056766c7a3545486a77470d0a686d757043554870574a6464627a504758463053446f6934743176592b5a7236794f6f74736a44495530574d7a376339493449516250766a6457766646455550436631634d64417a764345370d0a30546d4a4d6344566b7867704a546b4d3946487353646e794668744839762b347773432f6666585062664d546857465737335069346c617064364a3776576f437151376d50356a624b727a730d0a75705a443053393272674a50593062446d634b494c39764661584a6d4855303368716748683970684f545930544c557871424d552f57574e4632676470436f5576567938446a6434545871520d0a7548657a473954317449723368636a436d575a4565707957544270642b6b2b7546645671415941706c5a4f78776855566b4234314e6f2f2b5a4f582b797566724b73583774516a587474744c0d0a6d2f54434a344b59376635786671633752554e7a5275534c4b4e4f45717551713366613078523877396a723467394447696f466374663956642b4564676836396765546750684361586453640d0a2b546a442f66593163567a4267444f374f75706e465a4f4870706c622b535044426d43336d536b3666355a42466c4d556b6e7a4144436679732b56797a7a364a697858432b49556e516c49720d0a634e6e4144614254585166706e572b6e3456515555344b3069427642474c4d5079534251345a4b5a4c794a46624c2b4e306b7a366d706f37487a55464a5a6a57436e4e32455538504b5979530d0a5038396e794c55376e413d3d0d0a";
		String str = SecurityUtil.toString(path);
		String key = "DeAMlO1GFlDfs9h0Uf6TRaX7juzscByB6aUdWy029sc=";

		String dec = EnDesEncyp.decryptRequestWithSessionKey(EnDesEncyp.base64Decode(key), str);
		logger.info("Path : "+dec);*/


		//logger.info("Security KEY : "+new securityDAO().fetchMasterKey("11"));

		//SecurityDAOImpl dao = new SecurityDAOImpl();
		//dao.updateLastLoginTime("detach", "7167619691987319", "TLD39G7SGU23IE0HYGL4DPCQWAWXOJ8NM032STELYTMO");
	}

	@Override
	public boolean equals(Object ob){

		return false;
	}

	@Override
	public int hashCode(){

		return 0;
	}

	public SecurityDAOImpl getSecurityDAO() {
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAOImpl securityDAO) {
		this.securityDAO = securityDAO;
	}

	public SecurityUtil getSecurityUtil() {
		return securityUtil;
	}
	

	public void setSecurityUtil(SecurityUtil securityUtil) {
		this.securityUtil = securityUtil;
	}
	
	private static String getAction(String url){
		return url.substring(url.lastIndexOf("/")+1, url.lastIndexOf("."));
	}

}
