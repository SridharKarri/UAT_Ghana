package com.ceva.bank.services;

import java.net.URL;

import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.dataaccess.settlement.ArrayOfCommissionPayout;
import org.datacontract.schemas._2004._07.dataaccess.settlement.CustomResponse;
import org.datacontract.schemas._2004._07.dataaccess.settlement.StatusByBatchNoResponse;
import org.datacontract.schemas._2004._07.dataaccess.settlement.StatusByCIPDResponse;
import org.datacontract.schemas._2004._07.dataaccess.settlement.TransactionStatus;
import org.springframework.stereotype.Service;
import org.tempuri.settlement.IService1;
import org.tempuri.settlement.Service1;

import com.ceva.dto.ResponseDTO;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

@Service("settlementSoap")
public class SettlementSoap implements Constants{
	
	Logger logger = Logger.getLogger(SettlementSoap.class);
	
	public ResponseDTO postAgentCommission(ArrayOfCommissionPayout commisions){
		logger.info("postAgentCommission start.");
		String dest = null;
		ResponseDTO dto = new ResponseDTO();
		URL url = null;
		try{
			long startTime = System.currentTimeMillis();

				dest = ConfigLoader.convertResourceBundleToMap("config").get("bank.settlement.url");
				logger.info("url..." + dest);
				url = new URL(dest);
				Service1 service1 = new Service1(url);
				IService1 iService1 = service1.getBasicHttpBindingIService1();
				CustomResponse response = iService1.postAgentCommission(commisions);
				logger.info("End Time of settlement ..:"+(System.currentTimeMillis()-startTime));
				if("111".equals(response.getResponseCode().getValue())){
					dto.setResponseCode("111");
					dto.setMessage(response.getResponseMessage().getValue());
				}else{
					dto.setResponseCode(response.getResponseCode().getValue());
					dto.setMessage(response.getResponseMessage().getValue());
				}
		}catch(Exception e){
			logger.info("Error..." + e.getLocalizedMessage());
			e.printStackTrace();
			dto.setMessage(GENERIC_FAILURE_MESS);
			dto.setResponseCode(FAILURE_RESP_CODE);
		}finally {
			url = null;
			dest = null;
		}
		logger.info("postAgentCommission...Response..:" + dto.toString());
		return dto;
	}
	public ResponseDTO getCommissionStatusByBatchNumber(String batchNumber){
		logger.info("getCommissionStatusByBatchNumber start.");
		String dest = null;
		ResponseDTO dto = new ResponseDTO();
		URL url = null;
		try{
			long startTime = System.currentTimeMillis();

				dest = ConfigLoader.convertResourceBundleToMap("config").get("bank.settlement.url");
				logger.info("url..." + dest);
				url = new URL(dest);
				Service1 service1 = new Service1(url);
				IService1 iService1 = service1.getBasicHttpBindingIService1();
				StatusByBatchNoResponse response = iService1.getAllCommissionStatusByBatchNo(batchNumber);
				logger.info("End Time of settlement ..:"+(System.currentTimeMillis()-startTime));
				if("112".equals(response.getResponseCode().getValue())){
					dto.setResponseCode(SUCCESS_RESP_CODE);
					dto.setMessage(SUCESS_CAPS);
				}else{
					dto.setResponseCode(response.getResponseCode().getValue());
					dto.setMessage(response.getResponseMessage().getValue());
				}
				logger.info("getCommissionStatusByBatchNumber...Response..:" + dto.toString());
		
		}catch(Exception e){
			logger.error("Error...:" + e.getLocalizedMessage());
			e.printStackTrace();
			dto.setMessage(GENERIC_FAILURE_MESS);
			dto.setResponseCode(FAILURE_RESP_CODE);
		}finally {
			url = null;
			dest = null;
		}
		return dto;
	}
	public ResponseDTO getCommissionStatusByPayoutId(String payoutId){
		logger.info("getCommissionStatusByPayoutId start..:"+payoutId);
		String dest = null;
		ResponseDTO dto = new ResponseDTO();
		URL url = null;
		try {
			long startTime = System.currentTimeMillis();

			dest = ConfigLoader.convertResourceBundleToMap("config").get("bank.settlement.url");
			logger.info("url..." + dest);
			url = new URL(dest);
			Service1 service1 = new Service1(url);
			IService1 iService1 = service1.getBasicHttpBindingIService1();
			StatusByCIPDResponse response = iService1.getCommissionStatusByCommissionPayoutId(payoutId);
			logger.info("End Time of settlement ..:"
					+ (System.currentTimeMillis() - startTime));
			if ("117".equals(response.getResponseCode().getValue())) {
				TransactionStatus transactionStatus = response.getTransaction().getValue();
				String status = transactionStatus.getStatus().getValue();
				if("Success".equals(status)){
					dto.setResponseCode(SUCCESS_RESP_CODE);
					dto.setMessage(SUCESS_CAPS);
					dto.setData(response.getTransaction());
				}else{
					dto.setResponseCode(response.getResponseCode().getValue());
					dto.setMessage(status);
				}
			} else {
				dto.setResponseCode(response.getResponseCode().getValue());
				dto.setMessage(response.getResponseMessage().getValue());
			}
			logger.info("getCommissionStatusByPayoutId...Response..:"
					+ dto.toString());

		}catch(Exception e){
			logger.error("Error..." + e.getLocalizedMessage());
			e.printStackTrace();
			dto.setMessage(GENERIC_FAILURE_MESS);
			dto.setResponseCode(FAILURE_RESP_CODE);
		}finally {
			url = null;
			dest = null;
		}
		return dto;
	}
}
