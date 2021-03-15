package com.ceva.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.bank.common.beans.MMOperators;
import com.ceva.bank.common.beans.MMProductFormItems;
import com.ceva.bank.common.beans.MMProducts;
import com.ceva.bank.common.beans.MMValidateRequest;
import com.ceva.bank.common.beans.MMobileValidPay;
import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.bank.services.BankMobileService;
import com.ceva.bank.services.RequestProcessor;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.bank.utils.TransactionStatus;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Incentive;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.ServiceProducts;
import com.ceva.db.model.Store;
import com.ceva.db.model.StoreAccount;
import com.ceva.db.model.Transaction;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.MobileMoneyService;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@org.springframework.stereotype.Service("mobileMoneyService")
public class MobileMoneyServiceImpl implements MobileMoneyService {
	private static Logger log = Logger.getLogger(MobileMoneyServiceImpl.class);
	
	private static String countryId = ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId");
	private static String validateBillpaymenturl = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.validatebiller");
	private static String appId = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.appid");
	private static String appKey = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.appKey");
	private static String commitMobpaymenturl = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.commitmobile");

	@Autowired
	private BankMobileService bankMobileService;
	
	@Autowired
	private LoginDao loginDao;

	@Autowired
	private MerchantDao merchantDao;

	@Autowired
	private TransactionDao transactionDao;

	@Autowired
	private FeeDao feeDao;

	@Autowired
	private RequestProcessor requestProcessor;

	@Override
	public ResponseDTO getMMOperators(MMOperators bean) {

		return bankMobileService.loadOperators(bean);		
	}
	
	public ResponseDTO getMMProducts(MMProducts bean) {

		return bankMobileService.loadMMProducts(bean);		
	}

	public ResponseDTO getMMProductFormItems(MMProductFormItems bean) {

		return bankMobileService.loadMMProductFormItems(bean);		
	}
	
	public ResponseDTO mmvalidatereq(MMValidateRequest mmvalidateRequest) {

		ResponseDTO responseDTO = new ResponseDTO();
		Fee fee = null;
		Merchant merchant = null;
		Store store = null;
		log.debug("mmbean.." + mmvalidateRequest.getId());
		String transactionReference = null;
		com.ceva.db.model.Service service = null;
		ServiceProducts serviceProducts = null;
		UserLoginCredentials credentials = null;
		Transaction logTxn = null;
		Incentive incentive = null;
		String storeAccount = null;
		MMobileValidPay mmbean=new MMobileValidPay();
		try {
			//transactionReference = CommonUtil.createAlphaNumericString(15);
			transactionReference = generateReference();
			BeanUtils.copyProperties(mmvalidateRequest, mmbean);
			mmbean.setRequestReference(transactionReference);
			credentials = loginDao.getById(mmbean.getUserId());
			mmbean.setTxnCode(TransactionCodes.BILLPAYMENT.toString());
			// if(Double.parseDouble(billPayment.getAmount())>=Constants.MIN_TRANSACTION_AMOUNT){
			if (credentials != null) {
				mmbean.setMerchantId(credentials.getAgent());
				merchant = merchantDao.getById(credentials.getAgent());
				store = merchantDao.getStoreById(credentials.getStore());
				if (merchant != null || store != null) {
					mmbean.setAccountNumber(store.getAccountNumber());
					logTxn = createTxn(mmbean);
					logTxn.setStoreId(credentials.getStore());
					// long txnId = transactionDao.save(logTxn);
					service = feeDao.getServiceByCode(mmbean.getTxnCode());
					if (service != null) {
						if ("03".equals(mmbean.getCountryId())) {
							StoreAccount account = merchantDao.getStoreAccountByCurrency(credentials.getStore(),
									mmbean.getCurrency());
							storeAccount = account.getAccountNumber();
							serviceProducts = feeDao.getProductService(service.getsId(),
									Long.parseLong(merchant.getProduct()), mmbean.getCurrency());
						} else {
							storeAccount = store.getAccountNumber();
							serviceProducts = feeDao.getProductService(service.getsId(),
									Long.parseLong(merchant.getProduct()));
						}

						//billPayment.setAccountNumber(store.getAccountNumber());
						mmbean.setAccountNumber(storeAccount);
						fee = feeDao.getFee(serviceProducts.getSpId(), mmbean.getAmount());
						if (fee == null) {
							fee = new Fee();
						}
						int txnCount = 1; // transactionDao.getStoreTxnCount(credentials.getStore());
						log.info("transaction Count..:" + txnCount);
						incentive = new Incentive();
						BigDecimal agentPercIncome = calculateAgentIncome(incentive, fee);
						mmbean.setAgentIncentive(agentPercIncome);

						long txnAmt = mmbean.getAmount().add(fee.getFeeVal()).longValue();

						if (serviceProducts != null && txnAmt <= serviceProducts.getTxnLimit().longValue()) {
							mmbean.setFee(fee.getFeeVal());
							log.info(mmbean.toString());
							logTxn.setReqdateTime(new Date());
							JSONObject request = createBillpaymentValidateRequest(mmbean, fee, agentPercIncome);
							JSONObject response = requestProcessor.excecuteService(request, validateBillpaymenturl,getHeaders());
							logTxn.setRespDateTime(new Date());
							if (Constants.SUCCESS_RESP_CODE.equals(response.getString("ResponseCode"))) {
								responseDTO = new ResponseDTO(Constants.SUCESS_SMALL, Constants.SUCCESS_RESP_CODE);
								responseDTO.setFee(fee.getAgntCmsn());
								HashMap<String, String> data = new HashMap<String, String>();
								data.put("billerName", response.getString("BillerName"));
								data.put("productName", response.getString("ProductName"));
								data.put("amount", response.getString("Amount"));
								data.put("chargeAmount", response.getString("ChargeAmount"));
								data.put("currencyCode", response.getString("CurrencyCode"));
								responseDTO.setData(data);
								try {
									logTxn.setFromAccountNum(storeAccount);
									logTxn.setExtSysId(response.getString("ValidationReference"));
									logTxn.setFee(fee.getFeeVal());
									logTxn.setFeeCode(fee.getId());//added recently
									logTxn.setAgentCmsn(fee.getAgntCmsn());
									logTxn.setBankCmsn(fee.getBankFee());
									logTxn.setResponseCode(responseDTO.getResponseCode());
									logTxn.setStatus(TransactionStatus.INITIATED.toString());
									responseDTO.setFee(fee.getAgntCmsn());
									long id = transactionDao.save(logTxn);
									data.put("refNumber", id + "");
								} catch (Exception e) {
									log.error(
											"Error while updating commmission in txn_log for success txn, update manually.");
									log.error("RefNumber:" + logTxn.getRefNumber() + "fee:" + fee.toString());
								}
							} else {
								log.info("failure response..:" + responseDTO.getMessage());
								responseDTO = new ResponseDTO(response.getString("ResponseMessage"),
										response.getString("ResponseCode"));
							}

						} else {
							responseDTO = constructFailureRespose(Constants.SERVICE_LIMIT_EXCEED_MESSAGE,
									Constants.SERVICE_LIMIT_EXCEED_CODE);
						}
					} else {
						responseDTO = constructFailureRespose(Constants.SERVICE_NOT_MAPPED_MESSAGE,
								Constants.SERVICE_NOT_MAPPED_CODE);
					}
				} else {
					responseDTO = constructFailureRespose(Constants.NO_MERCHANT_MESSAGE, Constants.NO_MERCHANT);
				}
			} else {
				responseDTO = constructFailureRespose(Constants.USER_NOTFOUNT_MESSAGE, Constants.USER_NOTFOUNT_CODE);
			}
		} catch (Exception e) {
			log.error("Error Occured..:" + e.getLocalizedMessage());
			e.printStackTrace();
			responseDTO = constructFailureRespose(Constants.GENERIC_FAILURE_MESS, Constants.FAILURE_RESP_CODE);
		} finally {
			log.info("in finally..");
			merchant = null;
			credentials = null;
			service = null;
			serviceProducts = null;
			store = null;
		}
		log.info(responseDTO.toString());
		return responseDTO;
	}
	
	
	
	private Transaction createTxn(MMobileValidPay transferRequest) {
		Transaction transaction = new Transaction();
		try {
			transaction.setApprovedBy(transferRequest.getUserId());
			transaction.setMerchantId(transferRequest.getMerchantId());
			transaction.setBillerId(transferRequest.getBillerId());
			transaction.setBillerChannel(transferRequest.getPackId());
			transaction.setRefNumber(transferRequest.getRequestReference());
			transaction.setChannel(Long.parseLong(transferRequest.getChannel()));
			transaction.setStatus(TransactionStatus.FAILURE.toString());
			transaction.setResponseCode(Constants.FAILURE_RESP_CODE);
			transaction.setToAcNum(transferRequest.getAccountNumber());
			transaction.setFromAccountNum(transferRequest.getCustomerMobile());
			transaction.setNarration(transferRequest.getNarration());
			transaction.setTerminalNumber(transferRequest.getMobileNumber());
			transaction.setTxnCode(transferRequest.getTxnCode());
			transaction.setTxndateTime(new Date());
			transaction.setAmount(transferRequest.getAmount());
			transaction.setRequestString(transaction.getRequestString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transaction;
	}
	
	private BigDecimal calculateAgentIncome(Incentive incentive, Fee fee) {
		log.debug("calculateAgentIncome");
		BigDecimal agentIncome = null;
		try {
			if (incentive != null) {
				if (incentive.getIncVal().intValue() > 0) {
					agentIncome = getPercentageIncome(fee.getAgntCmsn().add(incentive.getIncVal()), fee.getFeeVal());
				} else {
					agentIncome = getPercentageIncome(fee.getAgntCmsn(), fee.getFeeVal());
				}
			} else {
				if(fee.getFeeVal().compareTo(new BigDecimal("0"))==0)
					agentIncome = fee.getAgntCmsn();
				else
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

	private JSONObject createBillpaymentValidateRequest(MMobileValidPay billPayment, Fee fee, BigDecimal agentPercIncome) {
		JSONObject object = new JSONObject();
		object.put("RequestId", billPayment.getRequestReference());
		object.put("CountryId", countryId);
		object.put("BillerId", billPayment.getBillerId());
		// object.put("ProductId", billPayment.getPackId());
		object.put("ProductId", billPayment.getPackId());
		object.put("Amount", billPayment.getAmount());
		object.put("SourceAccountNumber", billPayment.getAccountNumber());
		// object.put("PhoneNumber", billPayment.getCustomerMobile());
		object.put("CustomerMsisdn", billPayment.getCustomerMobile());
		object.put("ReferenceNumber", billPayment.getRequestReference());
		object.put("ProductFormItems", billPayment.getFormItems());

		JSONArray additionalParts = new JSONArray();
		JSONObject additionalFees = new JSONObject();
		additionalFees.put("AccountNumber", billPayment.getAccountNumber());
		additionalFees.put("FeeAmount", fee.getFeeVal());
		additionalFees.put("IsPercentage", "Y");
		additionalFees.put("agentPercIncome", agentPercIncome);
		additionalParts.add(additionalFees);
		object.put("AdditionalFees", additionalParts);
		return object;
	}

	private static Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("AppId", appId);
		headers.put("AppKey", appKey);
		return headers;
	}

	private String generateReference() {
		String ref = CommonUtil.createRandomNumber(15) + "" + CommonUtil.createRandomNumber(4);
		log.debug("Bill ref...:"+ref);
		return ref;
	}
	
	private ResponseDTO constructFailureRespose(Object object, String responsCode) {
		ResponseDTO dto = new ResponseDTO();
		dto.setResponseCode(responsCode);
		dto.setMessage(object.toString());
		dto.setData(object);
		log.debug(dto.toString());
		return dto;
	}
	
	public BigDecimal getPercentageIncome(BigDecimal agentCommission, BigDecimal totalFee) {
		BigDecimal incomePercent = null;
		try {
			// incomePercent = (agentCommission.divide(totalFee)).multiply(new
			// BigDecimal(100));
			incomePercent = agentCommission.divide(totalFee);
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			incomePercent = new BigDecimal(0);
		}
		return incomePercent;
	}

	@Override
	public ResponseDTO commitMobilePayment(MMValidateRequest bean) {
		ResponseDTO responseDTO = null;
		log.debug("commitPaybill payBill 1 .." + bean.getId());
		Transaction txn = null;
		BigDecimal vat;
		String vatpercantage="0";
		BigDecimal vatcal=new BigDecimal(0);
		try {
			txn = transactionDao.getById(bean.getId());
			vatpercantage = ConfigLoader.convertResourceBundleToMap("config").get("bank.vat");
			vat = new BigDecimal(vatpercantage);
			if (txn != null) {
				JSONObject request = createBillpaymentRequest(txn);
				JSONObject response = requestProcessor.excecuteService(request, commitMobpaymenturl, getHeaders());
				if (Constants.SUCCESS_RESP_CODE.equals(response.getString("ResponseCode"))) {
					responseDTO = new ResponseDTO(Constants.SUCESS_SMALL, Constants.SUCCESS_RESP_CODE);
					txn.setStatus(TransactionStatus.SUCCESS.toString());
					// txn.setExtSysId(response.getString("ValidationReference"));
					txn.setExtSysId(response.getString("TransactionReference"));
					Map<String, String> data = new HashMap<>();
					data.put("refNumber", txn.getExtSysId());
					data.put("fee", txn.getFee() + "");
					data.put("dateTime", txn.getTxndateTime() + "");
					data.put("commission", txn.getAgentCmsn() + "");
					vatcal=vat.multiply(txn.getFee()).divide(new BigDecimal(100));
					log.debug("payBill Vat : " + vatcal+"");
					data.put("vat", vatcal+"");
					responseDTO.setData(data);
				} else {
					txn.setStatus(TransactionStatus.FAILURE.toString());
					txn.setErrorDescription(response.getString("ResponseMessage"));
					txn.setResponseCode(response.getString("ResponseCode"));
					responseDTO = new ResponseDTO(response.getString("ResponseMessage"),
							response.getString("ResponseCode"));
				}
				transactionDao.update(txn);
			} else {
				responseDTO = new ResponseDTO("Invalid Reference", Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			log.error("Error Occured..:" + e.getLocalizedMessage());
		} finally {
			log.info("in finally..");
			txn = null;
		}
		log.info(responseDTO.toString());
		return responseDTO;
	}
	
	private JSONObject createBillpaymentRequest(Transaction transaction) {
		JSONObject object = new JSONObject();
		object.put("RequestId", transaction.getId());
		object.put("CountryId", countryId);
		object.put("ValidationReference", transaction.getExtSysId());
		return object;
	}
}