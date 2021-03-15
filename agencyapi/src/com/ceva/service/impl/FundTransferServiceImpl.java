package com.ceva.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.NameEnquirey;
import com.ceva.bank.common.beans.QueryAccount;
import com.ceva.bank.common.beans.QueryPayment;
import com.ceva.bank.common.beans.TransferRequest;
import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.common.dao.SuspectedFraudDao;
import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.bank.services.FinacleService;
import com.ceva.bank.services.TransferService;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.bank.utils.TransactionStatus;
import com.ceva.db.model.Alert;
import com.ceva.db.model.BanksAndWallets;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Incentive;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.ServiceProducts;
import com.ceva.db.model.Store;
import com.ceva.db.model.Transaction;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.exception.AgentLockedException;
import com.ceva.exception.FeeNotMappedException;
import com.ceva.exception.ServiceLimitException;
import com.ceva.exception.StoreLockedException;
import com.ceva.exception.SystemChallengeException;
import com.ceva.exception.UserLockedException;
//import com.ceva.helper.TransferHelper;
import com.ceva.service.FundTransferService;
import com.ceva.service.NotificationService;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONObject;

@Service("fundTransferService")
public class FundTransferServiceImpl implements FundTransferService {

	@Autowired
	TransactionDao transactionDao;

	@Autowired
	MerchantDao merchantDao;
	
	@Autowired
	FeeDao feeDao;

	@Autowired
	LoginDao loginDao;

	@Autowired
	AlertDao alertDao;

	@Autowired
	SuspectedFraudDao suspectedFraudDao;

	@Autowired
	FinacleService finacleService;
	
	@Autowired
	NotificationService notificationService; 
	
	Logger log = Logger.getLogger(FundTransferServiceImpl.class);
	
	@Override
	public ResponseDTO getBanks() {
		log.debug("get Banks..:");
		ResponseDTO dto = null;
		try {
			
			List<BanksAndWallets> banksAndWallets = transactionDao.getBanks("2");
			dto= new ResponseDTO(Constants.SUCESS_SMALL, Constants.SUCCESS_RESP_CODE);
			dto.setData(banksAndWallets);
		} catch (Exception e) {
			log.error("Error while doing name enquiry..:" + e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		} finally {
		}

		log.info("Response..:" + dto.toString());
		return dto;
	}

	@Override
	public ResponseDTO nameEnquirey(NameEnquirey enquirey) {
		log.debug("name Enquirey..:");
		ResponseDTO dto = null;
		try {
			dto = finacleService.queryAccount(enquirey.getAccountNumber());
			if(Constants.SUCCESS_RESP_CODE.equals(dto.getResponseCode())){
				QueryAccount queryAccount = (QueryAccount) dto.getData(); 
				Map<String, Object> data =new HashMap<>();
				data.put("accountName", queryAccount.getAccountName());
				data.put("accountNumber", queryAccount.getAccountNumber());
				data.put("accountCurrency", queryAccount.getAcctCurrCode());
				dto.setData(data);
				
			}
		} catch (Exception e) {
			log.error("Error while doing name enquiry..:" + e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		} finally {
		}

		log.info("Response..:" + dto.toString());
		return dto;
	}
	
	@Override
	public ResponseDTO nameEnquireyOtherBank(NameEnquirey enquirey) {
		JSONObject request = prepareNameEnquireyOtherBank(enquirey);
		log.debug("nameEnquireyOtherBank..:");
		ResponseDTO dto = null;
		try {
			dto = new TransferService().nameEnquireyOtherBank(request);
			if(Constants.SUCCESS_RESP_CODE.equals(dto.getResponseCode())){
				JSONObject queryAccount = (JSONObject) dto.getData(); 
				Map<String, Object> data =new HashMap<>();
				data.put("accountName", queryAccount.getString("AccountName"));
				data.put("accountNumber", enquirey.getAccountNumber());
				data.put("accountCurrency", "NA");
				dto.setData(data);
			}
			
		} catch (Exception e) {
			log.error("Error while doing name enquiry..:" + e.getLocalizedMessage());
			e.printStackTrace();
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		} finally {
		}

		log.info("Response..:" + dto.toString());
		return dto;
	}

	private JSONObject prepareNameEnquireyOtherBank(NameEnquirey enquirey) {
		JSONObject object = new JSONObject();
		object.put("AccountNumber", enquirey.getAccountNumber());
		object.put("BankCode", enquirey.getBankCode());
		object.put("CountryId", ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId"));
		/*object.put("Language", ConfigLoader.convertResourceBundleToMap("config").get("bank.language"));*/
		object.put("ChannelId", ConfigLoader.convertResourceBundleToMap("config").get("bank.channelId"));
		object.put("ReferenceId", System.nanoTime() + "");
		return object;
	}
	
	private JSONObject prepareGetBanks() {
		JSONObject object = new JSONObject();
		object.put("CountryId", ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId"));
		object.put("ReferenceId", System.nanoTime() + "");
		return object;
	}

	@Override
	public ResponseDTO processIntraBank(TransferRequest transferRequest) {
		log.debug("intra bank Transfer..");
		return processTransfer(transferRequest);
	}

	@Override
	public ResponseDTO processInterBank(TransferRequest transferRequest) {
		log.debug("interbank Transfer");
		return processTransfer(transferRequest);
	}

	@Override
	public ResponseDTO transactionStatusQuery(QueryPayment transfer) {
		log.debug("transaction Status Query .." + transfer.getRequestReference());
		return null;
	}

	public ResponseDTO processTransfer(TransferRequest transferRequest) {
		ResponseDTO responseDTO = new ResponseDTO();
		double txnAmt = 0.0;
		String bankCode = null;
		Fee fee = null;
		BigDecimal feeAmount = null;
		Merchant merchant = null;
		Store store = null;
		com.ceva.db.model.Service service = null;
		ServiceProducts serviceProducts = null;
		UserLoginCredentials credentials = null;
		Transaction logtxn = null;
		Incentive incentive = null;
		String accountToCheck = null;
		JSONObject request = null;
		try {
			bankCode = ConfigLoader.convertResourceBundleToMap("config").get("fin.homebank.code");
			credentials = loginDao.getById(transferRequest.getMakerId());
			merchant = merchantDao.getById(credentials.getAgent());
			store = merchantDao.getStoreById(credentials.getStore());
			transferRequest.setMerchantId(credentials.getAgent());
			String refNumber = generateReference();
			request = new JSONObject();
			request.put("ReferenceId", refNumber);
			
			if (transferRequest.getAmount().doubleValue() >= Constants.MIN_TRANSACTION_AMOUNT) {
				if (credentials != null) {
					if (merchant != null) {
						if (store != null) {
							if (TransactionCodes.FTINTERBANK.toString().equals(transferRequest.getTxnCode())
									|| TransactionCodes.FTINTRABANK.toString().equals(transferRequest.getTxnCode())
									|| TransactionCodes.CASHDEP.toString().equals(transferRequest.getTxnCode())) {
								transferRequest.setAccountNumber(store.getAccountNumber());
								accountToCheck = transferRequest.getBeneficiaryAccount();
							}else if (TransactionCodes.CWDBYACT.toString().equals(transferRequest.getTxnCode())||
									TransactionCodes.CWDBYCARD.toString().equals(transferRequest.getTxnCode())) {
								transferRequest.setBeneficiaryAccount(store.getAccountNumber());
								transferRequest.setBeneficiaryName(store.getAccountName());
								accountToCheck = transferRequest.getAccountNumber();
							} else {

							}
							
							service = feeDao.getServiceByCode(transferRequest.getTxnCode());
							serviceProducts = feeDao.getProductService(service.getsId(),
									Long.parseLong(merchant.getProduct()));
							transferRequest.setServiceId(service.getsId() + "");
							transferRequest.setReferenceCode(refNumber);
							logtxn = createTxn(transferRequest);
							logtxn.setStoreId(credentials.getStore());
							long txnid = transactionDao.save(logtxn);
							if (serviceProducts != null) {
								if (serviceProducts.getTxnLimit().compareTo(transferRequest.getAmount())>0) {
									if (txnid != 0) {
										fee = getFee(serviceProducts.getSpId(), transferRequest.getAmount(),
												transferRequest, serviceProducts, bankCode);
										if (fee != null) {
											log.debug(fee.toString());
											txnAmt = fee.getFeeVal().doubleValue()
													+ transferRequest.getAmount().doubleValue();
											feeAmount = fee.getFeeVal();
										} else {
											throw new FeeNotMappedException(Constants.NO_FEE, Constants.NO_FEE_MESSAGE);
										}
										if(txnAmt <= serviceProducts.getTxnLimit().longValue()){	
											int txnCount = 0; //transactionDao.getStoreTxnCount(credentials.getStore());
											incentive = getIncentive(credentials, txnCount, fee);
											log.debug(incentive.toString());
											BigDecimal agentPercIncome = calculateAgentIncome(incentive, fee);
											log.debug("before sending..:" + transferRequest.toString());
											
											logtxn.setReqdateTime(new Date());
											if (TransactionCodes.CASHDEP.toString().equals(transferRequest.getTxnCode())) {
												request = prepareDeposit(transferRequest, store, feeAmount,
														agentPercIncome);
												request.put("TransactionType", "Deposit");
												responseDTO = new TransferService().deposit(request);
											} else if (TransactionCodes.CWDBYACT.toString().equals(transferRequest.getTxnCode())) {
												request = prepareDeposit(transferRequest, store, feeAmount,
														agentPercIncome);
												request.put("TransactionType", "Withdrawal");
												responseDTO = new TransferService().withdraw(request);
											} else if (TransactionCodes.FTINTRABANK.toString().equals(transferRequest.getTxnCode())) {
												request = prepareDeposit(transferRequest, store, feeAmount,
														agentPercIncome);
												request.put("TransactionType", "Transfer");
												responseDTO = new TransferService().deposit(request);
											} else if (TransactionCodes.FTINTERBANK.toString().equals(transferRequest.getTxnCode())) {
	
												request = prepareInterBankTransfer(transferRequest, store, feeAmount,
														agentPercIncome);
												responseDTO = new TransferService().interbankTransfer(request);
											}
											logtxn.setRespDateTime(new Date());
											JSONObject obj= (JSONObject) responseDTO.getData();
											String extRefNumber = null;
											try{
												if(Constants.SUCCESS_RESP_CODE.equals(responseDTO.getResponseCode()))
													extRefNumber = obj.getString("TransactionReference");
												else
													extRefNumber = request.getString("ReferenceId");
											}catch(Exception e){
												log.error("TransactionReference not found in response.");
												extRefNumber = request.getString("ReferenceId");
											}
											request.put("ReferenceId", extRefNumber);
											request.put("datetime", CommonUtil.convertDateToStirng(logtxn.getTxndateTime(), Constants.BILLPAYMENT_DATE_FRMT));
											logtxn.setExtSysId(extRefNumber);
											responseDTO.setData(request);
											if (Constants.SUCCESS_RESP_CODE.equals(responseDTO.getResponseCode())) {
												responseDTO.setCommission(fee.getAgntCmsn());
												try {
													logtxn.setFeeCode(fee.getId());
													logtxn.setFee(fee.getFeeVal());
													logtxn.setAgentCmsn(fee.getAgntCmsn());
													logtxn.setBankCmsn(fee.getBankFee());
													logtxn.setSupAgentCmsn(fee.getSupAgentCmsn());
													logtxn.setStatus(TransactionStatus.SUCCESS.toString());
													logtxn.setResponseCode(responseDTO.getResponseCode());
													logtxn.setErrorCode(responseDTO.getResponseCode());
													logtxn.setErrorDescription(responseDTO.getMessage());
													if (incentive.getId() > 0) {
														fee.setAgntCmsn(fee.getAgntCmsn().add(incentive.getIncVal()));
														responseDTO.setFee(fee.getAgntCmsn());
														logtxn.setIncentive(incentive.getIncVal());
													}
													responseDTO.setCommission(fee.getAgntCmsn());
													responseDTO.setFee(fee.getFeeVal());
													transactionDao.update(logtxn);
													log.debug(logtxn.toString());
												} catch (Exception e) {
													log.error(
															"Error while updating commmission for success txn, update manually.");
													log.error(
															"RefNumber:" + logtxn.getRefNumber() + "fee:" + fee.toString());
													e.printStackTrace();
												}
												log.debug("========== preparing alerts START ==========");
												insertNofifications(merchant, transferRequest, credentials, fee);
												log.debug("========== preparing alerts END ==========");
	
											} else{
												
												logtxn.setStatus(TransactionStatus.FAILURE.toString());
												logtxn.setErrorCode(responseDTO.getResponseCode());
												logtxn.setErrorDescription(responseDTO.getMessage());
												transactionDao.update(logtxn);
												responseDTO.setData(request);
											}
										} else 
											throw new ServiceLimitException(Constants.SERVICE_LIMIT_EXCEED_CODE, Constants.SERVICE_LIMIT_EXCEED_MESSAGE);
									} else 
										throw new SystemChallengeException(Constants.SYSTEMCHALLENGE_CODE, Constants.SYSYEM_CHALLENGE_MESSAGE);
								} else 
									throw new ServiceLimitException(Constants.SERVICE_LIMIT_EXCEED_CODE, Constants.SERVICE_LIMIT_EXCEED_MESSAGE);
							
							} else 
								throw new ServiceLimitException(Constants.SERVICE_NOT_MAPPED_CODE, Constants.SERVICE_NOT_MAPPED_MESSAGE);
							
						} else 
							throw new StoreLockedException(Constants.NO_STORE, Constants.NO_STORE_MESSAGE);
					} else 
						throw new AgentLockedException(Constants.NO_MERCHANT, Constants.NO_MERCHANT_MESSAGE);
				} else 
					throw new UserLockedException(Constants.USER_NOTFOUNT_CODE, Constants.USER_NOTFOUNT_MESSAGE);
			} else 
				throw new ServiceLimitException(Constants.MIN_TRANSACTION_AMOUNT_CODE, Constants.MIN_TRANSACTION_AMOUNT_MESSAGE);
			
		} catch (FeeNotMappedException e) {
			log.error("FeeNotMappedException....");
			log.error("Reason..:" + e.getLocalizedMessage());
			responseDTO = constructFailureRespose(e.getMessage(), e.getErrorCode(), request);
		}catch (SystemChallengeException e) {
			log.error("SystemChallengeException....");
			log.error("Reason..:" + e.getLocalizedMessage());
			responseDTO = constructFailureRespose(e.getMessage(), e.getErrorCode(), request);
		} catch (ServiceLimitException e) {
			log.error("ServiceLimitException....");
			log.error("Reason..:" + e.getLocalizedMessage());
			responseDTO = constructFailureRespose(e.getMessage(), e.getErrorCode(), request);
		}catch (StoreLockedException e) {
			log.error("StoreLockedException....");
			log.error("Reason..:" + e.getLocalizedMessage());
			responseDTO = constructFailureRespose(e.getMessage(), e.getErrorCode(), request);
		}catch (AgentLockedException e) {
			log.error("AgentLockedException....");
			log.error("Reason..:" + e.getLocalizedMessage());
			responseDTO = constructFailureRespose(e.getMessage(), e.getErrorCode(), request);
		}catch (UserLockedException e) {
			log.error("UserLockedException....");
			log.error("Reason..:" + e.getLocalizedMessage());
			responseDTO = constructFailureRespose(e.getMessage(), e.getErrorCode(), request);
		}catch (Exception e) {
			log.error("Error While Processing....");
			log.error("Reason..:" + e.getLocalizedMessage());
			responseDTO = constructFailureRespose("External System Error.", "100", request);
		} finally {
			if (logtxn != null && TransactionStatus.PROCESS.toString().equals(logtxn.getStatus())) {
				logtxn.setStatus(TransactionStatus.FAILURE.toString());
				logtxn.setErrorCode(responseDTO.getResponseCode());
				logtxn.setErrorDescription(responseDTO.getMessage());
				transactionDao.update(logtxn);
			}
			txnAmt = 0.0;
			bankCode = null;
			service = null;
			merchant = null;
			credentials = null;
			incentive = null;
		}
		log.info(responseDTO.toString());
		return responseDTO;
	}

	private String generateReference() {
		String ref = CommonUtil.createRandomNumber(15) + "" + CommonUtil.createRandomNumber(4);
		log.debug("ref...:"+ref);
		return ref;
	}

	private JSONObject prepareDeposit(TransferRequest transferRequest, Store store, BigDecimal feeAmount,
			BigDecimal agentPercIncome) {
		JSONObject object = new JSONObject();
		object.put("Amount", transferRequest.getAmount());
		object.put("DestinationAccount", transferRequest.getBeneficiaryAccount());
		object.put("SourceAccount", transferRequest.getAccountNumber());
		object.put("Fee", feeAmount);
		object.put("AgentIncomePercentage", agentPercIncome);
		object.put("Narration", createNarration(transferRequest, store));
		object.put("ReferenceId", transferRequest.getReferenceCode());
		
		object.put("CountryId", ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId"));
		//object.put("Language", ConfigLoader.convertResourceBundleToMap("config").get("bank.language"));
		object.put("ChannelId", ConfigLoader.convertResourceBundleToMap("config").get("bank.channelId"));
		return object;
	}
	
	private JSONObject prepareInterBankTransfer(TransferRequest transferRequest, 
			Store store, BigDecimal feeAmount,
			BigDecimal agentPercIncome) {
		JSONObject deposit = new JSONObject();
		BanksAndWallets banks = transactionDao.getBank(transferRequest.getBankCode());
		deposit.put("Amount", transferRequest.getAmount());
		deposit.put("AgentIncomePercentage", agentPercIncome);
		deposit.put("BankCode", transferRequest.getBankCode());
		deposit.put("BankRouteNumber", banks.getInstCode());
		deposit.put("BeneficiaryBank", banks.getInstName());
		deposit.put("BeneficiaryName", transferRequest.getBeneficiaryName());
		deposit.put("DestinationAccount", transferRequest.getBeneficiaryAccount());
		deposit.put("Fee", feeAmount);
		deposit.put("Narration", createNarration(transferRequest, store));
		deposit.put("SourceAccount", transferRequest.getAccountNumber());
		deposit.put("ReferenceId", transferRequest.getReferenceCode());
		
		deposit.put("CountryId", ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId"));
		/*deposit.put("Language", ConfigLoader.convertResourceBundleToMap("config").get("bank.language"));*/
		deposit.put("ChannelId", ConfigLoader.convertResourceBundleToMap("config").get("bank.channelId"));
		return deposit;
	}


	private BigDecimal calculateAgentIncome(Incentive incentive, Fee fee) {
		log.debug("calculateAgentIncome");
		BigDecimal agentIncome = null;
		try {
			if (incentive != null && incentive.getId() > 0) {
				if (incentive.getIncVal().intValue() > 0) {
					agentIncome = getPercentageIncome(fee.getAgntCmsn().add(incentive.getIncVal()), fee.getFeeVal());
				} else {
					agentIncome = getPercentageIncome(fee.getAgntCmsn(), fee.getFeeVal());
				}
			} else {
				agentIncome = getPercentageIncome(fee.getAgntCmsn(), fee.getFeeVal());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error While calculateAgentIncome..:" + e.getLocalizedMessage());
		} finally {
			incentive = null;
			fee = null;
		}
		log.debug("agentIncome..:" + agentIncome);
		return agentIncome;
	}

	public BigDecimal getPercentageIncome(BigDecimal agentCommission, BigDecimal totalFee) {
		BigDecimal incomePercent = null;
		try {
			//incomePercent = agentCommission.divide(totalFee);
			incomePercent = agentCommission.divide(totalFee, 1, RoundingMode.HALF_UP);
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			incomePercent = new BigDecimal(0);
		}
		return incomePercent;
	}

	private Incentive getIncentive(UserLoginCredentials credentials, int txnCount, Fee fee) {
		Incentive incentive = feeDao.getIncentive(credentials.getStore(), txnCount, fee);
		return incentive;
	}

	private Fee getFee(long spId, BigDecimal amount, TransferRequest transferRequest, ServiceProducts serviceProducts,
			String bankCode) {
		log.debug("getting fee..!");
		Fee fee = null;
		try {
			if (transferRequest.getBankCode().equals(bankCode)) {
				fee = feeDao.getFee(serviceProducts.getSpId(), transferRequest.getAmount());
			} else {
				fee = feeDao.getFee(serviceProducts.getSpId(), transferRequest.getAmount());
			}
		} catch (Exception e) {
			log.error("Error While getting fee..");
			log.error("Reason.:" + e.getLocalizedMessage());
		} finally {
			amount = null;
			transferRequest = null;
			serviceProducts = null;
			bankCode = null;
		}
		return fee;
	}

	private void insertNofifications(Merchant merchant, TransferRequest transferRequest,
			UserLoginCredentials credentials, Fee fee) {
		Alert alert = null;
		if ("3".equals(merchant.getNotiFicationType())) {
			alert = notificationService.sendEmail(transferRequest, credentials, fee);
			alert = notificationService.sendSMS(transferRequest, credentials, fee);
			/*prepareSMSAlert(transferRequest, credentials, fee);
			prepareAlert(transferRequest, credentials, fee);*/
		}
		if ("2".equals(merchant.getNotiFicationType())) {
			alert  = notificationService.sendEmail(transferRequest, credentials, fee);
		}
		if ("1".equals(merchant.getNotiFicationType())) {
			alert = notificationService.sendSMS(transferRequest, credentials, fee);
		}
		if ("0".equals(merchant.getNotiFicationType())) {
			log.debug("All notifications are disabled for this merchant.");
		}
		if(alert != null)
			alertDao.save(alert);
		log.debug("========== preparing alerts END ==========");
	}

	private Transaction createTxn(TransferRequest transferRequest) {
		Transaction transaction = new Transaction();
		try {
			transaction.setApprovedBy(transferRequest.getReferenceCode());
			transaction.setMerchantId(transferRequest.getMerchantId());
			transaction.setBankId(transferRequest.getBankCode());
			transaction.setRefNumber(transferRequest.getReferenceCode());
			transaction.setChannel(transferRequest.getChannel());
			transaction.setStatus(TransactionStatus.PROCESS.toString());
			transaction.setResponseCode(Constants.FAILURE_RESP_CODE);
			/*transaction.setToAcNum(transferRequest.getAccountNumber());
			transaction.setFromAccountNum(transferRequest.getBeneficiaryAccount());*/
			//added swaping of account logging in db
			if (TransactionCodes.CWDBYACT.toString().equals(transferRequest.getTxnCode())||
					TransactionCodes.CWDBYCARD.toString().equals(transferRequest.getTxnCode())) {
				transaction.setAgentAccountNumber(transferRequest.getBeneficiaryAccount());
				transaction.setCustomerAccountNumber(transferRequest.getAccountNumber());
			} else {
				transaction.setAgentAccountNumber(transferRequest.getAccountNumber());
				transaction.setCustomerAccountNumber(transferRequest.getBeneficiaryAccount());
			}
			transaction.setNarration(transferRequest.getNarration());
			transaction.setTerminalNumber(transferRequest.getMobileNumber());
			transaction.setTxnCode(transferRequest.getTxnCode());
			transaction.setTxndateTime(new Date());
			transaction.setApprovedBy(transferRequest.getMakerId());
			transaction.setAmount(transferRequest.getAmount());
			transaction.setRequestString(transferRequest.getRequestString());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transaction;
	}

	private String createNarration(TransferRequest payment, Store store) {
		String narration = ConfigLoader.convertResourceBundleToMap("config").get(payment.getTxnCode() + "_narration");
		try {
			log.debug("txnCode..:" + payment.getTxnCode());
			if (TransactionCodes.FTINTRABANK.toString().equals(payment.getTxnCode())) {
				narration = narration.replaceAll("agentName", store.getAccountName());
				narration = narration.replaceAll("description", payment.getNarration());
			} else if (TransactionCodes.FTINTERBANK.toString().equals(payment.getTxnCode())
					|| TransactionCodes.DEPWALLET.toString().equals(payment.getTxnCode())) {
				BanksAndWallets banksAndWallets = transactionDao.getBank(payment.getBankCode());
				narration = narration.replaceAll("beneficiarybank", banksAndWallets.getInstName());
				narration = narration.replaceAll("description", payment.getNarration());
			} else if (TransactionCodes.CWDBYACT.toString().equals(payment.getTxnCode())) {
				narration = narration.replaceAll("agentName", store.getAccountName());
				narration = narration.replaceAll("description", payment.getNarration());
			} else if (TransactionCodes.CASHDEP.toString().equals(payment.getTxnCode())) {
				narration = narration.replaceAll("agentName", store.getAccountName());
				narration = narration.replaceAll("description", payment.getNarration());
			} else {
				log.error("No Transaction codes are atched to construct narrations");
			}
			narration = narration.replaceAll("sysId", payment.getReferenceCode());
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			payment = null;
		}
		return narration;

	}

	private ResponseDTO constructFailureRespose(Object object, String responsCode, JSONObject request) {
		log.info(request);
		ResponseDTO dto = new ResponseDTO();
		dto.setResponseCode(responsCode);
		dto.setMessage(object.toString());
		dto.setData(request);
		log.debug(dto.toString());
		return dto;
	}

}
