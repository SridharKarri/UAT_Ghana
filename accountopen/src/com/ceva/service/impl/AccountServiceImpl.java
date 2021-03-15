package com.ceva.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.AccountBean;
import com.ceva.bank.common.dao.AccountDao;
import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.bank.services.BankAccountService;
import com.ceva.bank.utils.Status;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.bank.utils.TransactionStatus;
import com.ceva.db.model.AccountImage;
import com.ceva.db.model.Alert;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.Store;
import com.ceva.db.model.Transaction;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.AccountService;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;


@Service("accountService")
public class AccountServiceImpl implements AccountService {

	static Logger logger = Logger.getLogger(AccountServiceImpl.class);

	@Autowired
	private BankAccountService bankAccountService;
	
	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private MerchantDao merchantDao;
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private AlertDao alertDao;
	
	@Override
	public ResponseDTO loadParameters() {
		ResponseDTO responseDTO = new ResponseDTO(Constants.SUCESS_SMALL, Constants.SUCCESS_RESP_CODE);
		Map<String, Object> data =  new HashMap<String, Object>();
		data.put("branches", bankAccountService.loadBranches().getData());
		data.put("countries", bankAccountService.loadCountries().getData());
		data.put("cities", bankAccountService.loadCities().getData());
		data.put("idCategories", bankAccountService.loadIdentityCategories().getData());
		data.put("maritalStasus", bankAccountService.loadMaritalStasuses().getData());
		data.put("occupations", bankAccountService.loadOccupations().getData());
		data.put("salutations", bankAccountService.loadSalutaions().getData());
		data.put("nationalities", bankAccountService.loadNationalities().getData());
		data.put("states", bankAccountService.loadStates().getData());
		responseDTO.setData(data);
		return responseDTO;
	}

	@Override
	public ResponseDTO openAccount(AccountBean bean) {
		ResponseDTO dto = null;
		AccountImage accountImage = null;
		Transaction transaction = null;
		Merchant merchant = null;
		Store store = null;
		UserLoginCredentials userLoginCredentials =new UserLoginCredentials(bean.getUserId(), bean.getPin(), Status.A.toString());
		try{
			dto = loginDao.validate(userLoginCredentials);
			if(Constants.SUCESS_SMALL.equals(dto.getMessage())){
				userLoginCredentials = loginDao.getById(bean.getUserId());
				bean.setMobileNumber(userLoginCredentials.getMobileNo());
				bean.setMerchantId(userLoginCredentials.getAgent());
				com.ceva.db.model.AccountBean accountBean = generateAccountBean(bean);
				accountBean.setStoreId(userLoginCredentials.getStore());
				boolean isInserted = accountDao.save(accountBean);
				boolean isValid = loginDao.validateOtp(bean.getPhone(), bean.getOtp());
				merchant = merchantDao.getById(userLoginCredentials.getAgent());
				store= merchantDao.getStoreById(userLoginCredentials.getStore());
				if(isValid && isInserted &&merchant != null){
					accountImage = accountDao.getById(Long.parseLong(bean.getIdRefNumber()));
					bean.setMandateCard(accountImage.getMandate());
					bean.setIdImage(accountImage.getIdImage());
					bean.setPin("");
					bean.setOtp("");
					if("NA".equals(bean.getEmail())){
						bean.setEmail("");
					}
					if("NA".equals(bean.getMidName())){
						bean.setMidName("");
					}
					dto = bankAccountService.openAccount(bean);
					if(dto!=null){
						if(Constants.SUCCESS_RESP_CODE.equals(dto.getResponseCode())){
							//accountBean.setStatus(dto.getMessage());
							accountBean.setStatus(TransactionStatus.SUCCESS.toString());
							Map<String, String> data = (Map<String, String>) dto.getData();
							if(data.containsKey("accountNumber"))
								accountBean.setAccountNumber(data.get("accountNumber"));
							accountDao.update(accountBean);
							transaction = createTxn(accountBean, data.get("accountNumber"), dto.getResponseCode(), TransactionStatus.SUCCESS.toString());
							if(Constants.SUCESS_SMALL.equals(dto.getMessage())){
								logger.debug("Account successfully opened.");
								prepareSMSAlert(bean, userLoginCredentials, accountBean.getAccountNumber());
							}
						}else{
							accountBean.setStatus(dto.getMessage());
							accountDao.update(accountBean);
							transaction = createTxn(accountBean, "0", dto.getMessage(),TransactionStatus.FAILURE.toString());
						}
					}else{
						transaction = createTxn(accountBean, "0", Constants.FAILURE_RESP_CODE,TransactionStatus.FAILURE.toString());
					}
					transactionDao.save(transaction);
				}else{
					logger.debug("Otp Validation Failed.");
					dto = commonUtil.generateFailureMessage(Constants.FAILURE_RESP_CODE, "Invalid Otp Entered.");
				}
			}
		}catch(Exception e){
			logger.error("Error whil opening account..:"+e.getLocalizedMessage());
			e.printStackTrace();
			dto = commonUtil.generateFailureMessage(Constants.FAILURE_RESP_CODE, Constants.GENERIC_FAILURE_MESS);
		}finally{
			userLoginCredentials= null;
			bean = null;
			accountImage = null;
			transaction = null;
		}
		
		return dto;
	}

	private void prepareSMSAlert(AccountBean bean, UserLoginCredentials credentials, String accountNumber) {
		Alert alert = new Alert();
		StringBuffer message = new StringBuffer();
		String template = null;
		try{
			message.append("Dear ").append(bean.getFirstName()).append(" ").append(bean.getLastName()).append(", ");
			template=ConfigLoader.convertResourceBundleToMap("config").get("accountopen.message");
			template=template.replace("$ACCOUNT$", accountNumber);
			logger.debug("template..:"+template);
			alert.setMessage(message.toString()+template);
			alert.setMobile(bean.getMobileNumber());
			alert.setAppl("SMS");
			alert.setTxnType("ACCOUNTOPEN");
			alertDao.save(alert);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error while sending notification."+e.getLocalizedMessage());
		}
	}

	private com.ceva.db.model.AccountBean generateAccountBean(AccountBean accountBean){
		com.ceva.db.model.AccountBean bean = new com.ceva.db.model.AccountBean();
		try{
			bean.setAddress(accountBean.getAccountNumber());
			bean.setChannel(accountBean.getChannel());
			bean.setCity(accountBean.getCity());
			bean.setDob(accountBean.getDob());
			bean.setEmail(accountBean.getEmail());
			bean.setFirstName(accountBean.getFirstName());
			bean.setGender(accountBean.getGender());
			bean.setLastName(accountBean.getLastName());
			bean.setMaritalStatus(accountBean .getMaritalStatus());
			bean.setMerchantId(accountBean.getMerchantId());
			bean.setMidName(accountBean.getMidName());
			bean.setPhone(accountBean.getPhone());
			bean.setSalutation(accountBean.getSalutation());
			bean.setMobileNumber(accountBean.getMobileNumber());
			bean.setState(accountBean.getState());
			bean.setUserId(accountBean.getUserId());
			bean.setStatus(TransactionStatus.FAILURE.toString());
			bean.setId(Long.parseLong(accountBean.getIdRefNumber()));
		}catch(Exception e){
			logger.error("Error While generating bean..!"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return bean;
	}
	
	
	
	private Transaction createTxn(com.ceva.db.model.AccountBean accountBean, String accountNumber, String respCode, String status) {
		Transaction transaction = new Transaction();
		try{
			transaction.setApprovedBy(accountBean.getUserId());
			transaction.setMerchantId(accountBean.getMerchantId());
			transaction.setBankId("011");
			transaction.setRefNumber(accountBean.getId()+"");
			transaction.setChannel(accountBean.getChannel());
			transaction.setStatus(status);
			transaction.setResponseCode(respCode);
			transaction.setToAcNum(accountBean.getAccountNumber());
			transaction.setFromAccountNum("");
			transaction.setNarration("");
			transaction.setTerminalNumber(accountBean.getMobileNumber());
			transaction.setTxnCode(TransactionCodes.OPENACT.toString());
			transaction.setTxndateTime(new Date());
			transaction.setAmount(new BigDecimal(0));
			transaction.setErrorDescription(accountBean.getStatus());
		}catch(Exception e){
			e.printStackTrace();
		}
		return transaction;
	}

	@Override
	public ResponseDTO loadBranches() {
		return bankAccountService.loadBranches();
	}

	@Override
	public ResponseDTO loadCountries() {
		return bankAccountService.loadCountries();
	}

	@Override
	public ResponseDTO loadStates() {
		return bankAccountService.loadStates();
	}

	@Override
	public ResponseDTO loadIdentityCategories() {
		return bankAccountService.loadIdentityCategories();
	}

	@Override
	public ResponseDTO loadCities() {
		return bankAccountService.loadCities();
	}

	@Override
	public ResponseDTO loadNationalities() {
		return bankAccountService.loadNationalities();
	}

	@Override
	public ResponseDTO loadMaritalStasuses() {
		return bankAccountService.loadMaritalStasuses();
	}

	@Override
	public ResponseDTO loadOccupations() {
		return bankAccountService.loadOccupations();
	}

	@Override
	public ResponseDTO loadSalutaions() {
		return bankAccountService.loadSalutaions();
	}

	@Override
	public ResponseDTO loadIdentityTypeByCategories(String catId) {
		return bankAccountService.loadIdentityTypeByCategories(catId);
	}

	@Override
	public Long save(AccountImage image) {
		return accountDao.save(image);
	}

	@Override
	public boolean update(AccountImage image) {
		return accountDao.update(image);
	}

	@Override
	public AccountImage getById(Long id) {
		return accountDao.getById(id);
	}

}
