package com.ceva.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.bank.common.beans.BillPayment;
import com.ceva.bank.common.beans.BillePaymentItems;
import com.ceva.bank.common.beans.BillerFormItems;
import com.ceva.bank.common.beans.BillersBean;
import com.ceva.bank.common.beans.Services;
import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.common.dao.BillPaymentDao;
import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.bank.services.RequestProcessor;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.bank.utils.TransactionStatus;
import com.ceva.db.model.Alert;
import com.ceva.db.model.BillerPackages;
import com.ceva.db.model.Billers;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Incentive;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.PackageFormItem;
import com.ceva.db.model.Service;
import com.ceva.db.model.ServiceProducts;
import com.ceva.db.model.Store;
import com.ceva.db.model.StoreAccount;
import com.ceva.db.model.Transaction;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.BillerService;
import com.ibm.icu.text.SimpleDateFormat;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@org.springframework.stereotype.Service("billerService")
public class BillerServiceImpl implements BillerService {
	private static Logger log = Logger.getLogger(BillerServiceImpl.class);

	@Autowired
	private BillPaymentDao billPaymentDao;

	@Autowired
	private AlertDao alertDao;

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

	private String respCode;

	private static String countryId = ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId");
	private static String mobileRechargeurl = ConfigLoader.convertResourceBundleToMap("config").get("bank.bill.airtime");
	private static String validateBillpaymenturl = ConfigLoader.convertResourceBundleToMap("config").get("bank.bill.validatebiller");
	private static String confirmBillpaymenturl = ConfigLoader.convertResourceBundleToMap("config").get("bank.bill.confirmbiller");
	private static String appId = ConfigLoader.convertResourceBundleToMap("config").get("bank.billpayment.appid");
	private static String appKey = ConfigLoader.convertResourceBundleToMap("config").get("bank.billpayment.appKey");
	//private static BigDecimal bankVat = new BigDecimal(ConfigLoader.convertResourceBundleToMap("config").get("bank.vat"));
	
	@Override
	public List<Service> getServices(Services services) {
		return billPaymentDao.getServices(services);
	}
	
	@Override
	public List<Billers> getBillers(BillersBean bean) {
		log.info("loading Billers for service...!" + bean.getServiceId());
		return billPaymentDao.getBillers(bean.getServiceId());
	}

	@Override
	public Billers getById(long id) {
		return null;
	}

	@Override
	public ResponseDTO getBillerPackages(BillePaymentItems billerPackages) {
		ResponseDTO dto = new ResponseDTO();
		List<BillerPackages> billerPackagesList = null;
		try {
			billerPackagesList = billPaymentDao.getBillerPackages(billerPackages.getBillerId());
			String language=billPaymentDao.getLanguage(billerPackages.getUserId());
			log.debug("[getBillerPackages] Language :"+language+", User Id : "+billerPackages.getUserId());
			ListIterator<BillerPackages> it=billerPackagesList.listIterator();
			
			while(it.hasNext()){
				BillerPackages billerPackages1 = (BillerPackages) it.next();
				if(language.equals("fr")){
					billerPackages1.setDisplayName(billerPackages1.getFrDisplayName());
					billerPackages1.setFrDisplayName(null);
				}else{
					billerPackages1.setFrDisplayName(null);
				}
			}
			
			dto.setMessage(Constants.SUCESS_SMALL);
			dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			dto.setData(billerPackagesList);
		} catch (Exception e) {
			log.error("Error while loading biller packages from database..!");
			log.error("Reason..!" + e.getLocalizedMessage());
		}
		return dto;
	}

	@Override
	public List<Billers> MMObillers(BillersBean bean) {
		return billPaymentDao.getBillers(1);
	}

	@Override
	public ResponseDTO validatePaybill(BillPayment billPayment) {
		ResponseDTO responseDTO = new ResponseDTO();
		Billers biller = null;
		Fee fee = null;
		Merchant merchant = null;
		Store store = null;
		log.debug("payBill.." + billPayment.getId());
		String transactionReference = null;
		com.ceva.db.model.Service service = null;
		ServiceProducts serviceProducts = null;
		UserLoginCredentials credentials = null;
		Transaction logTxn = null;
		Incentive incentive = null;
		String storeAccount = null;
		BigDecimal vat = null;
		try {
			transactionReference = CommonUtil.createAlphaNumericString(15);
			billPayment.setRequestReference(transactionReference);
			credentials = loginDao.getById(billPayment.getUserId());
			biller = billPaymentDao.getBiller(billPayment.getId());
			billPayment.setBillerId(biller.getBillerId());
			billPayment.setTxnCode(TransactionCodes.BILLPAYMENT.toString());
			// if(Double.parseDouble(billPayment.getAmount())>=Constants.MIN_TRANSACTION_AMOUNT){
			if (credentials != null) {
				billPayment.setMerchantId(credentials.getAgent());
				merchant = merchantDao.getById(credentials.getAgent());
				store = merchantDao.getStoreById(credentials.getStore());
				if (merchant != null || store != null) {
					billPayment.setAccountNumber(store.getAccountNumber());
					logTxn = createTxn(billPayment);
					logTxn.setStoreId(credentials.getStore());
					// long txnId = transactionDao.save(logTxn);
					service = feeDao.getServiceByCode(billPayment.getTxnCode());
					if (service != null) {
						if ("03".equals(billPayment.getCountryId())) {
							StoreAccount account = merchantDao.getStoreAccountByCurrency(credentials.getStore(),
									billPayment.getCurrency());
							storeAccount = account.getAccountNumber();
							serviceProducts = feeDao.getProductService(service.getsId(),
									Long.parseLong(merchant.getProduct()), billPayment.getCurrency());
						} else {
							storeAccount = store.getAccountNumber();
							serviceProducts = feeDao.getProductService(service.getsId(),
									Long.parseLong(merchant.getProduct()));
						}

						//billPayment.setAccountNumber(store.getAccountNumber());
						billPayment.setAccountNumber(storeAccount);
						fee = feeDao.getFee(serviceProducts.getSpId(), billPayment.getAmount());
						if (fee == null) {
							fee = new Fee();
						}
						int txnCount = 1; // transactionDao.getStoreTxnCount(credentials.getStore());
						log.info("transaction Count..:" + txnCount);
						incentive = new Incentive();
						BigDecimal agentPercIncome = calculateAgentIncome(incentive, fee);
						billPayment.setAgentIncentive(agentPercIncome);

						long txnAmt = billPayment.getAmount().add(fee.getFeeVal()).longValue();

						if (serviceProducts != null && txnAmt <= serviceProducts.getTxnLimit().longValue()) {
							billPayment.setBillerName(biller.getBillerName());
							billPayment.setFee(fee.getFeeVal());
							billPayment.setBillerId(biller.getBillerId());
							log.info(billPayment.toString());
							logTxn.setReqdateTime(new Date());
							JSONObject request = createBillpaymentValidateRequest(billPayment, fee, agentPercIncome);
							JSONObject response = requestProcessor.excecuteService(request, validateBillpaymenturl,
									getHeaders());
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
			biller = null;
			merchant = null;
			credentials = null;
			service = null;
			serviceProducts = null;
			store = null;
		}
		log.info(responseDTO.toString());
		return responseDTO;
	}

	@Override
	public ResponseDTO commitPaybill(BillPayment billPayment) {
		ResponseDTO responseDTO = null;
		log.debug("commitPaybill payBill 1 .." + billPayment.getId());
		Transaction txn = null;
		BigDecimal vat;
		String vatpercantage="0";
		BigDecimal vatcal=new BigDecimal(0);
		try {
			txn = transactionDao.getById(billPayment.getId());
			vatpercantage = ConfigLoader.convertResourceBundleToMap("config").get("bank.vat");
			vat = new BigDecimal(vatpercantage);
			if (txn != null) {
				JSONObject request = createBillpaymentRequest(txn);
				JSONObject response = requestProcessor.excecuteService(request, confirmBillpaymenturl, getHeaders());
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

	private String createNarration(BillPayment payment) {
		String narration = "";
		try {
			narration = ConfigLoader.convertResourceBundleToMap("config").get(payment.getTxnCode() + "_narration");
			if (TransactionCodes.AIRTIME.toString().equals(payment.getTxnCode())) {
				narration = narration.replaceAll("billerName", payment.getBillerName());
				narration = narration.replaceAll("mobileNumber", payment.getCustomerId());
				narration = narration.replaceAll("description", payment.getUserId());
			} else {
				narration = narration.replaceAll("billerName", payment.getBillerName());
				narration = narration.replaceAll("customerId", payment.getCustomerId());
				narration = narration.replaceAll("description", payment.getUserId());
			}
			narration = narration.replaceAll("sysId", payment.getRequestReference());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return narration;

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

	private Incentive getIncentive(UserLoginCredentials credentials, int txnCount, Fee fee) {
		Incentive incentive = feeDao.getIncentive(credentials.getStore(), txnCount, fee);
		return incentive;
	}

	private Transaction createTxn(BillPayment transferRequest) {
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

	@Override
	public ResponseDTO mobileRecharge(BillPayment billPayment) {
		ResponseDTO responseDTO = new ResponseDTO();
		Billers biller = null;
		Fee fee = null;
		Merchant merchant = null;
		Store store = null;
		log.info("mobileRecharge.." + billPayment.toString());
		String transactionReference = null;
		com.ceva.db.model.Service service = null;
		ServiceProducts serviceProducts = null;
		UserLoginCredentials credentials = null;
		Transaction logTxn = null;
		Incentive incentive = null;
		int minAmountValidation;
		String amountFm = null;
		
		try {

			transactionReference = CommonUtil.createAlphaNumericString(15);
			billPayment.setRequestReference(transactionReference);
			credentials = loginDao.getById(billPayment.getUserId());
			billPayment.setTxnCode(TransactionCodes.AIRTIME.toString());
			biller = billPaymentDao.getBiller(billPayment.getId());
			billPayment.setBillerId(biller.getBillerId());
			
			if (credentials != null) {
				
				if((ConfigLoader.convertResourceBundleToMap("config").get("MIN_TRANSACTION_AMOUNT_"+billPayment.getCurrency()))!=null){
					amountFm=(ConfigLoader.convertResourceBundleToMap("config").get("MIN_TRANSACTION_AMOUNT_"+billPayment.getCurrency()));
					minAmountValidation=Integer.parseInt(amountFm);
				}else{
					minAmountValidation=Constants.MIN_TRANSACTION_AMOUNT;
				}
				
				if (billPayment.getAmount().doubleValue() >= minAmountValidation) {
					billPayment.setMerchantId(credentials.getAgent());
					merchant = merchantDao.getById(credentials.getAgent());
					store = merchantDao.getStoreById(credentials.getStore());
					if (merchant != null || store != null) {
						billPayment.setAccountNumber(store.getAccountNumber());
						logTxn = createTxn(billPayment);
						logTxn.setStoreId(credentials.getStore());
						long txnId = transactionDao.save(logTxn);
						if (txnId != 0) {
							service = feeDao.getServiceByCode(billPayment.getTxnCode());
							if (service != null) {
								serviceProducts = feeDao.getProductService(service.getsId(),
										Long.parseLong(merchant.getProduct()));
							}
							fee = feeDao.getFee(serviceProducts.getSpId(), billPayment.getAmount());
							if (fee == null) {
								fee = new Fee();
							}
							BigDecimal agentPercIncome = calculateAgentIncome(incentive, fee);
							long txnAmt = billPayment.getAmount().add(fee.getFeeVal()).longValue();
							if (serviceProducts != null && txnAmt <= serviceProducts.getTxnLimit().longValue()) {
								billPayment.setBillerName(biller.getBillerName());
								billPayment.setFee(fee.getFeeVal());
								billPayment.setBillerId(biller.getBillerId());
								log.info(billPayment.toString());
	
								logTxn.setReqdateTime(new Date());
								JSONObject request = createMobileRecharge(billPayment, fee, agentPercIncome);
								JSONObject response = requestProcessor.excecuteService(request, mobileRechargeurl,
										getHeaders());
								logTxn.setRespDateTime(new Date());
								if (Constants.SUCCESS_RESP_CODE.equals(response.getString("ResponseCode"))
										|| Constants.PARTIAL_SUCCESS_RESP_CODE.equals(response.getString("ResponseCode"))) {
									responseDTO = new ResponseDTO(Constants.SUCESS_SMALL, Constants.SUCCESS_RESP_CODE);
									responseDTO.setFee(fee.getAgntCmsn());
									HashMap<String, String> data = new HashMap<String, String>();
									data.put("refNumber", response.getString("TransactionReference"));
									data.put("amount", billPayment.getAmount() + "");
									responseDTO.setData(data);
									try {
										logTxn.setExtSysId(response.getString("TransactionReference"));// missing
																										// transaciton
																										// reference
										logTxn.setFee(fee.getFeeVal());
										logTxn.setAgentCmsn(fee.getAgntCmsn());
										logTxn.setBankCmsn(fee.getBankFee());
										logTxn.setResponseCode(responseDTO.getResponseCode());
										logTxn.setStatus(TransactionStatus.SUCCESS.toString());
										responseDTO.setFee(fee.getAgntCmsn());
										transactionDao.update(logTxn);
									} catch (Exception e) {
										log.error(
												"Error while updating commmission in txn_log for success txn, update manually.");
										log.error("RefNumber:" + logTxn.getRefNumber() + "fee:" + fee.toString());
									}
									insertAlerts(merchant, credentials, fee, billPayment, response);
								} else {
									responseDTO = new ResponseDTO(response.getString("ResponseMessage"),
										response.getString("ResponseCode"));
									log.info("failure response..:" + responseDTO.getMessage());
								}
							} else {
								responseDTO = constructFailureRespose(Constants.SERVICE_LIMIT_EXCEED_MESSAGE,
									Constants.SERVICE_LIMIT_EXCEED_CODE);
							}
						} else {
							responseDTO = constructFailureRespose(Constants.SYSYEM_CHALLENGE_MESSAGE,
								Constants.SYSTEMCHALLENGE_CODE);
						}
					} else {
						responseDTO = constructFailureRespose(Constants.NO_MERCHANT_MESSAGE, Constants.NO_MERCHANT);
					}
				}else{
					responseDTO = constructFailureRespose(Constants.MIN_TRANSACTION_AMOUNT_CODE, Constants.MIN_TRANSACTION_AMOUNT_MESSAGE_SUB+" "+minAmountValidation);
				}
			} else {
				responseDTO = constructFailureRespose(Constants.USER_NOTFOUNT_MESSAGE, Constants.USER_NOTFOUNT_CODE);
			}
		} catch (Exception e) {
			log.error("Error Occured while doing billpayment..:" + e.getLocalizedMessage());
			responseDTO = constructFailureRespose(Constants.GENERIC_FAILURE_MESS, Constants.FAILURE_RESP_CODE);
			e.printStackTrace();
		} finally {
			log.error("in finally..");
			if (logTxn != null) {
				logTxn.setErrorCode(responseDTO.getResponseCode());
				logTxn.setErrorDescription(responseDTO.getMessage());
				transactionDao.update(logTxn);
			}
			biller = null;
			merchant = null;
			credentials = null;
			service = null;
			serviceProducts = null;
			store = null;
		}
		log.info(responseDTO.toString());
		return responseDTO;
	}

	private static Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("AppId", appId);
		headers.put("AppKey", appKey);
		return headers;
	}

	private JSONObject createBillpaymentRequest(Transaction transaction) {
		JSONObject object = new JSONObject();
		object.put("RequestId", transaction.getId());
		object.put("CountryId", countryId);
		object.put("ValidationReference", transaction.getExtSysId());
		return object;
	}

	private JSONObject createBillpaymentValidateRequest(BillPayment billPayment, Fee fee, BigDecimal agentPercIncome) {
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

	private JSONObject createMobileRecharge(BillPayment billPayment, Fee fee, BigDecimal agentPercIncome) {
		JSONObject object = new JSONObject();
		object.put("RequestId", billPayment.getRequestReference());
		object.put("CountryId", countryId);
		object.put("BillerId", billPayment.getBillerId());
		object.put("CustomerAccountNumber", billPayment.getAccountNumber());
		object.put("CustomerMsisdn", billPayment.getCustomerMobile());
		object.put("Provider", billPayment.getBillerName());
		object.put("ReferenceNumber", billPayment.getRequestReference());
		object.put("BeneficiaryMobileNumber", billPayment.getCustomerMobile());
		object.put("Amount", billPayment.getAmount());

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

	private void insertAlerts(Merchant merchant, UserLoginCredentials credentials, Fee fee, BillPayment billPayment,
			JSONObject response) {
		try {
			if ("3".equals(merchant.getNotiFicationType())) {
				log.debug("agent has notifications of sms, email.");
				prepareSMSAlert(billPayment, credentials, fee);
				prepareAlert(billPayment, credentials, response, fee);
			}
			if ("2".equals(merchant.getNotiFicationType())) {
				log.debug("agent has notifications EMAIL only.");
				prepareAlert(billPayment, credentials, response, fee);
			}
			if ("1".equals(merchant.getNotiFicationType())) {
				log.debug("agent has notifications SMS only.");
				prepareSMSAlert(billPayment, credentials, fee);
			}
			if ("0".equals(merchant.getNotiFicationType())) {
				log.debug("All notifications are disabled for this merchant");
			}
		} catch (Exception e) {

		} finally {

		}
	}

	private void prepareSMSAlert(BillPayment billPayment, UserLoginCredentials credentials, Fee fee) {
		log.debug("preparing sms for txn:" + billPayment.getTxnCode());
		Alert alert = new Alert();
		StringBuffer message = new StringBuffer();
		try {
			if (TransactionCodes.BILLPAYMENT.toString().equals(billPayment.getTxnCode())) {
				message.append("Bill payment was successful.");
			} else if (TransactionCodes.MMO.toString().equals(billPayment.getTxnCode())) {
				message.append(billPayment.getBillerName() + " Airtime purchase was successful for mobile number "
						+ billPayment.getCustomerId() + ".");
			} else if (TransactionCodes.AIRTIME.toString().equals(billPayment.getTxnCode())) {
				message.append(billPayment.getBillerName() + " Airtime purchase was successful for mobile number "
						+ billPayment.getCustomerId() + ".");
			}

			message.append(" Amount : ").append(billPayment.getAmount() + "").append(", Commission: ")
					.append(fee.getAgntCmsn() + "").append(", Ref: ").append(billPayment.getRequestReference() + "")
					.append(", Time: ")
					.append(new SimpleDateFormat(Constants.MAIL_DATE_FORMAT).format(new Date()) + "");

			alert.setMessage(message.toString());
			alert.setMobile(credentials.getMobileNo());
			alert.setAppl("SMS");
			alert.setTxnType("TRANSUCCESS");
			alertDao.save(alert);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error..!" + e.getLocalizedMessage());
		} finally {
			alert = null;
			message = null;
		}
	}

	private void prepareAlert(BillPayment billPayment, UserLoginCredentials credentials, JSONObject response, Fee fee) {
		Alert alert = new Alert();
		StringBuffer message = new StringBuffer();
		try {
			String[] pendingCodes = ConfigLoader.convertResourceBundleToMap("config").get("bank.qt.pending.code")
					.split(",");
			message.append("Dear <b>").append(credentials.getUserName()).append("</b><br/><br/>");
			if (TransactionCodes.BILLPAYMENT.toString().equals(billPayment.getTxnCode())) {
				if (ArrayUtils.contains(pendingCodes, response.getString("ResponseCode"))) {
					log.debug("Identified billpayment pending.");
					message.append("Your Bill Payment Request was Processing.");
				} else {
					message.append("Your Bill Payment was successful.");
				}
				// message.append("Your Bill Payment was successful.")
				message.append(
						"<br/> <table class='datatable' bgcolor='#FAFAFA' width='100%' cellspacing='0' cellpadding='0' style=' border: 4px solid rgba(229, 229, 229, 0.88);  color: rgba(98, 103, 114, 0.94); font-family: monospace; font-size: 12px;'>")
						.append("<tr><td>Smart Card Number  </td><td> ")
						.append(billPayment.getCustomerId() + "</td></tr>");
				;
			} else {
				if (ArrayUtils.contains(pendingCodes, response.getString("ResponseCode"))) {
					log.debug("Identified Airtime Purchase pending.");
					message.append("Your Airtime Purchase was Processing.");
				} else {
					message.append("Your Airtime Purchase was successful.");
				}
				message.append(", <br/>").append("<tr><td>Mobile Number </td><td>")
						.append(billPayment.getCustomerId() + "</td></tr>");
			}
			message.append("<tr><td>Reference number  </td><td>")
					.append(billPayment.getRequestReference() + "</td></tr>").append("<tr><td>Amount  </td><td>")
					.append(billPayment.getAmount() + "</td></tr>").append("<tr><td>Commission Earned  </td><td>")
					.append(fee.getAgntCmsn() + "").append("</td></tr>").append("<tr><td>Date  And Time  </td><td>")
					.append(new SimpleDateFormat(Constants.MAIL_DATE_FORMAT).format(new Date()) + "</td></tr>");

			alert.setMessage(message.toString());
			alert.setMobile(credentials.getMobileNo());
			alert.setMailTo(credentials.getEmail());
			alert.setAppl("MAIL");
			alert.setTxnType("TRANSUCCESS");
			alert.setSubject("Transaction Notification.");
			alertDao.save(alert);
		} catch (Exception e) {
			log.error("error while preparing alert for billpayment.:");
			log.error("Reason" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			alert = null;
			message = null;
		}

	}

	private ResponseDTO constructFailureRespose(Object object, String responsCode) {
		ResponseDTO dto = new ResponseDTO();
		dto.setResponseCode(responsCode);
		dto.setMessage(object.toString());
		dto.setData(object);
		log.debug(dto.toString());
		return dto;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	@Override
	public ResponseDTO getBillerFormItems(BillerFormItems formItems) {
		ResponseDTO dto = new ResponseDTO();
		List<PackageFormItem> packageFormItemList = null;
		try {
			packageFormItemList = billPaymentDao.getBillerFormItems(formItems.getPackId());
			String language=billPaymentDao.getLanguage(formItems.getUserId());
			log.debug("[getBillerFormItems] Language :"+language+", User Id : "+formItems.getUserId());
			ListIterator<PackageFormItem> it=packageFormItemList.listIterator();
			
			while(it.hasNext()){
				PackageFormItem packageFormItem1= it.next();
				if(language.equals("fr")){
					packageFormItem1.setFormItems(packageFormItem1.getFrformItems());
					packageFormItem1.setFrformItems(null);
				}else{
					packageFormItem1.setFrformItems(null);
				}
			}
			dto.setMessage(Constants.SUCESS_SMALL);
			dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			dto.setData(packageFormItemList);
		} catch (Exception e) {
			log.error("Error while loading Biller FormItems from database..!");
			log.error("Reason..!" + e.getLocalizedMessage());
		}
		return dto;
	}

	@Override
	public String getLanguage(String userId) {
		return billPaymentDao.getLanguage(userId);
	}

}
