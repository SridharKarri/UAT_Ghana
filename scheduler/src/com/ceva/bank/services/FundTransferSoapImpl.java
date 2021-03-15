package com.ceva.bank.services;

import java.net.URL;

import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.AgentBKGTransfers_Models.UtilityTSQResponse;
import org.springframework.stereotype.Service;
import org.tempuri.BasicHttpBinding_IInitiateTransferStub;

import com.ceva.dto.ResponseDTO;
import com.ceva.service.FundTransferService;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

@Service("fundTransferSoap")
public class FundTransferSoapImpl implements FundTransferService {
	Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ResponseDTO transactionStatusQuery(String refNum) {
		logger.info("transactionStatusQuery...");
		String dest = null;
		ResponseDTO dto = new ResponseDTO();
		URL url = null;
		BasicHttpBinding_IInitiateTransferStub stub = null;
		UtilityTSQResponse tsqResponse = null;
		try {
			long start = System.currentTimeMillis();
			dest = ConfigLoader.convertResourceBundleToMap("config").get("fund.transfer.url");
			logger.info("url..." + dest);
			url = new URL(dest);
			stub = new BasicHttpBinding_IInitiateTransferStub(url, null);
			logger.info("Request..:"+refNum);
			tsqResponse = stub.transactionStatusQuery(refNum);
			logger.info(refNum+", Time taken for requry is..:"+(System.currentTimeMillis()-start));
			if (tsqResponse != null) {
				logger.info("transaction Status Query..:"+tsqResponse.getResponse().getResponseMessage());
				if(Constants.FIN_SUCCESS_RESPONSE_CODE.equals(tsqResponse.getResponse().getResponseCode())){
					logger.info("transaction Status Query..:SUCCESS");
					//QueryTransactionResponse queryTransactionResponse = createGenerealResponse(tsqResponse);
					dto.setMessage(SUCESS_SMALL);
					dto.setResponseCode(SUCCESS_RESP_CODE);
					try{
					if(tsqResponse.getRequest().getBankCode() == null && tsqResponse.getRequest().getBankCode().length()==0){
						tsqResponse.getRequest().setBankCode("011");
					}
					}catch(NullPointerException npe){
						logger.error("error..:"+npe.getLocalizedMessage());
						tsqResponse.getRequest().setBankCode("011");
					}
					dto.setData(tsqResponse);
					//dto.setData(queryTransactionResponse);

				}else{
					logger.info("transaction Status Query..:FAIL");
					dto.setMessage(tsqResponse.getResponse().getResponseMessage());
					dto.setResponseCode(tsqResponse.getResponse().getResponseCode());
					//dto = transactionResponseDao.generateResponseForError(tsqResponse.getResponse().getResponseCode(), "FINACLE");
				}
			} else {
				dto.setMessage(SEARCH_FAIL);
				dto.setResponseCode(SUCCESS_RESP_CODE);
			}
			logger.info("transactionStatusQuery...Response..:" + dto.toString());
		} catch (Exception e) {
			logger.info("Error..." + e.getLocalizedMessage());
			e.printStackTrace();
			dto.setMessage(ERROR_MESSAGE);
			dto.setResponseCode(FAILURE_RESP_CODE);
		} finally {
			url = null;
			stub = null;
			refNum = null;
			tsqResponse = null;
			dest = null;
		}
		return dto;
	}

}
