package com.ceva.cron.listener;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ceva.bank.common.dao.ReversalDao;
import com.ceva.db.model.Reversals;
import com.ceva.service.AirtimeTokenReversalServcie;
import com.ceva.service.WalletReversalServcie;

//@Component("reversalJob")
public class ReversalJob {

	Logger log = Logger.getLogger(ReversalJob.class);

	@Autowired
	private ReversalDao reversalDao;
	
	@Autowired
	private WalletReversalServcie walletReversalServcie;
	
	@Autowired
	private AirtimeTokenReversalServcie airtimeTokenReversalServcie;
	
	//@Scheduled(fixedRate=10000)
	public void reversalJob() {
		log.debug("Startig reversalJob.");
		List<Reversals> reversals = reversalDao.getByStatus("P");
		if(reversals!= null && reversals.size()>0){
			log.info("records for reversal ..:"+reversals.size());
			//updateStatusToProgress(alerts);
			for (Reversals reversal : reversals) {
				String interfaceName = reversal.getInterfaceName();
				switch (interfaceName) {
				case "FMW":
					reversal= walletReversalServcie.reverseTransactionWithFMW(reversal);
					break;
				case "WALLETCBA":
					reversal= walletReversalServcie.reverseTransactionWithCBA(reversal);
					break;
				case "VAS2NET":
					reversal= reverseTransactionWithV2N(reversal);
					break;
				case "TOKENCBA":
					reversal= airtimeTokenReversalServcie.reverseTransactionWithTokenCBA(reversal);
					break;
				default:
					break;
				}
				reversalDao.update(reversal);
			}
		}
	}

	/*private Reversals reverseTransactionWithTokenCBA(Reversals reversal) {
		
		JSONObject request = new JSONObject();
		request.put("ReferenceCode", reversal.getRefNumber());
		JSONObject response = requestProcessor.excecuteService(request, reversalUrl);
		if(response != null){
			responseDTO = new ResponseDTO(response.getString("ResponseMessage"), response.getString("ResponseCode"));
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("refNumber", response.getString("TransactionReference"));
		}
		return null;
	}*/

	private Reversals reverseTransactionWithV2N(Reversals reversal) {
		return null;
	}
	
	
}
