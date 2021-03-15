package com.ceva.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.TransactionReport;
import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.ReportDao;
import com.ceva.db.model.Transaction;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.ReportService;
import com.nbk.util.Constants;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
	Logger log = Logger.getLogger(ReportService.class);

	@Autowired
	ReportDao reportDao;
	
	@Autowired
	FeeDao feeDao; 
	
	@Autowired
	LoginDao loginDao; 

	@Override
	public ResponseDTO generate(TransactionReport report) {
		ResponseDTO responseDTO = new ResponseDTO();
		List<Transaction> transactions= null;
		List<Transaction> summary = null;
		Map<String, Object> data = new HashMap<String, Object>();
		
 		try {
 			UserLoginCredentials credentials = loginDao.getById(report.getUserId());
 			report.setMerchantId(credentials.getAgent());
 			transactions = reportDao.sucessTransactionReport(report);
 			summary = reportDao.summaryReport(report);
 			
 			BigDecimal userCommission = feeDao.getAgentUserCurrentMonthCommission(report.getUserId(), credentials.getStore());
 			BigDecimal storeCommission = feeDao.getAgentStoreCurrentMonthCommission(credentials.getStore());
 			data.put("transaction", transactions);
 			data.put("summary", summary);
 			data.put("userCommission", userCommission);
 			data.put("storeCommission", storeCommission);
 			responseDTO.setData(data);
 			responseDTO.setMessage(Constants.SUCESS_SMALL);
 			responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error..:" + e.getLocalizedMessage());
			responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
 			responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
		}finally{
			transactions=null;
			data = null;
			summary = null;
		}
		return responseDTO;
	}

}
