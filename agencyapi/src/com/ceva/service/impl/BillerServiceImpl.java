package com.ceva.service.impl;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.AgentBKGBillPayment_Model.UtilityPayBillRequest;
import org.datacontract.schemas._2004._07.AgentBKGBillPayment_Model.UtilityPayBillResponse;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.bank.common.beans.AccountBalance;
import com.ceva.bank.common.beans.BillPayment;
import com.ceva.bank.common.beans.BillePaymentItems;
import com.ceva.bank.common.beans.BillersBean;
import com.ceva.bank.common.beans.QueryTransactionResponse;
import com.ceva.bank.common.beans.Services;
import com.ceva.bank.common.beans.ValidateCustomer;
import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.common.dao.BillPaymentDao;
import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.bank.qt.beans.Response;
import com.ceva.bank.services.FinacleService;
import com.ceva.bank.utils.SimpleXMLParser;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.bank.utils.TransactionStatus;
import com.ceva.db.model.Alert;
import com.ceva.db.model.BillerMakets;
import com.ceva.db.model.BillerPackages;
import com.ceva.db.model.Billers;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Incentive;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.Service;
import com.ceva.db.model.ServiceProducts;
import com.ceva.db.model.Store;
import com.ceva.db.model.Transaction;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.fbn.helper.PaybillHelper;
import com.ceva.service.BillerService;
import com.ibm.icu.text.SimpleDateFormat;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

@org.springframework.stereotype.Service("billerService")
public class BillerServiceImpl implements BillerService {
	Logger log = Logger.getLogger(BillerServiceImpl.class);

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
	private FinacleService finacleService;

	private String respCode;

	@Override
	public List<Service> getServices(Services services) {
		log.info("loading services...!");
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
		log.info("Loading biler packages from database.");
		ResponseDTO dto = new ResponseDTO();
		try {
			dto.setMessage(Constants.SUCESS_SMALL);
			dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			dto.setData(billPaymentDao.getBillerPackages(billerPackages.getId()));
		} catch (Exception e) {
			log.error("Error while loading biller packages from database..!");
			log.error("Reason..!" + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public List<Billers> MMObillers(BillersBean bean) {
		log.info("loading MMObillers ...!");
		return billPaymentDao.MMObillers("MMO");
	}

	@Override
	public ResponseDTO validateCustomer(ValidateCustomer customer) {
		Billers biller = null;
		ResponseDTO responseDTO = new ResponseDTO();
		String paymentCode = null;
		PaybillHelper helper = new PaybillHelper();
		String destUrl = null;
		String terminalId = null;
		String sslType = null;
		try {
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get(
					"bank.qt.url");
			terminalId = ConfigLoader.convertResourceBundleToMap("config").get(
					"bank.qt.temId");
			sslType = ConfigLoader.convertResourceBundleToMap("config").get(
					"bank.qt.sslType");

			biller = billPaymentDao.getBiller(Long.parseLong(customer
					.getBillerId()));
			if (biller != null) {
				if("0".equals(customer.getPaymentCode())||customer.getPaymentCode() == null)
					paymentCode = biller.getAccountNumber();
				else
					paymentCode = "01".equals(customer.getPaymentCode()) ? biller.getAccountNumber() : customer.getPaymentCode();

				log.info("destUrl..:"+destUrl);
				log.info("paymentCode..:"+paymentCode);
				String response = helper.validateCustomer(
						customer.getCustomerId(), paymentCode, terminalId,
						destUrl, sslType);
				if (response != null) {
					SimpleXMLParser parser = new SimpleXMLParser();
					Map<String, String> data = parser.parseXML(response);
					if ("90000".equals(data.get("ResponseCode"))) {
						responseDTO.setMessage(Constants.SUCESS_SMALL);
						responseDTO
								.setResponseCode(Constants.SUCCESS_RESP_CODE);
						responseDTO.setData(data);
					} else {
						responseDTO.setMessage(data.get("ResponseDescription"));
						responseDTO.setResponseCode(data.get("ResponseCode"));
						responseDTO.setData(data);
					}
				} else {
					responseDTO.setMessage(Constants.SEARCH_FAIL);
					responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
				}
			} else {
				log.debug("invalid id for biller, no biller withthhis id..:"
						+ customer.getBillerId());
				responseDTO = constructFailureRespose("invalid Biller.",
						Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			log.error("Error wjile validating customer..:"
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}
		log.info(responseDTO.toString());
		return responseDTO;
	}

	@Override
	public ResponseDTO payBill(BillPayment billPayment) {
		ResponseDTO responseDTO = null;
		Billers biller = null;
		Fee fee = null;
		Merchant merchant = null;
		Store store = null;
		log.debug("payBill.." + billPayment.getId());
		String transactionReference = null;
		com.ceva.db.model.Service service = null;
		ServiceProducts serviceProducts = null;
		UserLoginCredentials credentials = null;
		BillerPackages packages = null;
		Transaction logTxn = null;
		Incentive incentive = null;
		try {
			String prefix = ConfigLoader.convertResourceBundleToMap("config")
					.get("bank.qt.prefix");
			transactionReference = prefix + System.currentTimeMillis();
			billPayment.setRequestReference(transactionReference);
			credentials = loginDao.getById(billPayment.getUserId());
			biller = billPaymentDao.getBiller(billPayment.getId());
			billPayment.setBillerId(biller.getBillerId());
			billPayment.setTxnCode(TransactionCodes.BILLPAYMENT.toString());
				if(Double.parseDouble(billPayment.getAmount())>=Constants.MIN_TRANSACTION_AMOUNT){
				if (credentials != null) {
					merchant = merchantDao.getById(billPayment.getMerchantId());
					store = merchantDao.getStoreById(credentials.getStore());
					if (merchant != null || store != null) {
						billPayment.setAccountNumber(store.getAccountNumber());
						log.debug("getting FEE START..");
						logTxn = createTxn(billPayment);
						logTxn.setStoreId(credentials.getStore());
						long txnId = transactionDao.save(logTxn);
						if (txnId != 0) {
							service = feeDao.getServiceByCode(billPayment
									.getTxnCode());
							if (service != null) {
								serviceProducts = feeDao.getProductService(
										service.getsId(),
										Long.parseLong(merchant.getProduct()));
							}
							fee = feeDao.getFee(serviceProducts.getSpId(),
									new BigDecimal(billPayment.getAmount()));
							if (fee == null) {
								fee = new Fee();
							}
							int txnCount = transactionDao
									.getStoreTxnCount(credentials.getStore());
							log.info("transaction Count..:" + txnCount);
							incentive = getIncentive(credentials, txnCount, fee);
							BigDecimal agentPercIncome = calculateAgentIncome(incentive, fee);
							billPayment.setAgentIncentive(agentPercIncome);
	
							long txnAmt = fee.getFeeVal().longValue()
									+ new BigDecimal(billPayment.getAmount())
											.longValue();
	
							if (serviceProducts != null&& txnAmt <= serviceProducts.getTxnLimit().longValue()) {
	
								//biller = billPaymentDao.getBiller(billPayment.getId());
	
								if (biller != null) {
	
									BigDecimal txnAmount = fee.getFeeVal().add(new BigDecimal(billPayment.getAmount()));
									log.debug("Total Transaction Amount..:"+ txnAmount);
									AccountBalance balance = finacleService.balanceEnquirey(store.getAccountNumber());
									log.info("balance Enquirey.:"+ balance.getBalance());
									boolean hasBalance = balance.getBalance().doubleValue() >= txnAmount.doubleValue() ? true : false;
									log.debug("has balance...:" + hasBalance);
									if (hasBalance) {
										billPayment.setBillerName(biller.getBillerName());
										billPayment.setFee(fee.getFeeVal());
										billPayment.setBillerId(biller.getBillerId());
										if ("0".equals(billPayment.getPackId())) {
											log.debug("setting default pack id and payment code");
											billPayment.setPackId("01");
											billPayment.setPaymentCode(biller.getAccountNumber());
										} else {
											log.debug("loading packages from database..!");
											packages = billPaymentDao
													.getBillerPackage(billPayment.getId()+ "", billPayment.getPackId());
											if (packages != null) {
												String packid = billPayment	.getPackId().length() < 2 ? "0"+ billPayment.getPackId(): billPayment.getPackId();
												billPayment.setPackId(packid);
												billPayment.setPaymentCode(packages.getPaymentCode());
												billPayment.setBillerId(packages.getQtbillerId());
											} else {
												log.debug("setting payement code form billers default paymentCode");
												billPayment.setPackId("01");
												billPayment.setPaymentCode(biller.getAccountNumber());
											}
										}
	
										log.info(billPayment.toString());
										if (!"INVALID DENOMINATION".equals(billPayment.getNarration())) {
											switch ((int) biller.getChannel()) {
											case 0:
												log.debug("getting billers from database");
												break;
											case 1:
												log.debug("billpayment through quicktller");
												logTxn.setReqdateTime(new Date());
												responseDTO = sendBillPaymentAdviceThruQuickteller(billPayment);
												logTxn.setRespDateTime(new Date());
												log.info(responseDTO.toString());
												Response response = (Response) responseDTO.getData();
												if (Constants.SUCESS_SMALL.equals(responseDTO.getMessage())) {
													responseDTO.setFee(fee.getAgntCmsn());
													HashMap<String, String> data= new HashMap<String, String>();
													data.put("refNumber", response.getRequestReference());
													data.put("amount", response.getAmount());
													data.put("extSysId", getRespCode());
													responseDTO.setData(data);
													
													try {
														logTxn.setExtSysId(response.getTransactionRef());//missing transaciton reference
														logTxn.setFee(fee.getFeeVal());
														logTxn.setAgentCmsn(fee.getAgntCmsn());
														logTxn.setBankCmsn(fee.getBankFee());
														logTxn.setResponseCode(responseDTO.getResponseCode());
														//logTxn.setExtSysId(getRespCode());
														//logTxn.setExtSysId(response.getTransactionRef());
														logTxn.setStatus(TransactionStatus.SUCCESS.toString());
														if (incentive.getId() > 0) {
															logTxn.setIncentive(incentive.getIncVal());
															fee.setAgntCmsn(fee.getAgntCmsn().add(incentive.getIncVal()));
														}
														responseDTO.setFee(fee.getAgntCmsn());
														transactionDao.update(logTxn);
													} catch (Exception e) {
														log.info("Error while updating commmission in txn_log for success txn, update manually.");
														log.info("RefNumber:"+ logTxn.getRefNumber()+ "fee:"+ fee.toString());
													}
													/*try {
														transactionDao.updateCommisions(store, fee);
													} catch (Exception e) {
														log.info("Error while updating commmission for success txn, update manually.");
														log.info("RefNumber:"+ logTxn.getRefNumber()+ "fee:"+ fee.toString());
													}*/
	
													insertAlerts(merchant,credentials, balance,fee, billPayment,response);
												} else {
													log.info("failure response..:"+ responseDTO.getMessage());
												}
												break;
											case 2:
												log.debug("billpayment through nibbs, nibbs not ready for now ");
												// packages =
												// billPaymentDao.getBillerPackages(biller.getId());
												break;
											default:
												log.debug("biller channel value not matched.."
														+ billPayment.getId());
												break;
											}
										} else {
											log.debug("Incorrect Subscription Amount.");
											responseDTO = constructFailureRespose(
													Constants.BILLER_INVALID_DENOMINATION_MESSAGE,
													Constants.BILLER_INVALID_DENOMINATION_CODE);
										}
									} else {
										log.debug("Insufficient funds in account."
												+ balance.getBalance());
										responseDTO = constructFailureRespose(
												Constants.BALANCE_LOW_MESSAGE,
												Constants.BALANCE_LOW);
									}
								} else {
									log.debug("Biller query failed from database.");
									responseDTO = constructFailureRespose(
											Constants.SEARCH_FAIL,
											Constants.FAILURE_RESP_CODE);
								}
							} else {
								log.debug("Service Limit validation failed.");
								responseDTO = constructFailureRespose(
										Constants.SERVICE_LIMIT_EXCEED_MESSAGE,
										Constants.SERVICE_LIMIT_EXCEED_CODE);
							}
						} else {
							log.debug("System Challenge..!");
							responseDTO = constructFailureRespose(
									Constants.SYSYEM_CHALLENGE_MESSAGE,
									Constants.SYSTEMCHALLENGE_CODE);
						}
					} else {
						log.debug("Agent Deactivated.");
						responseDTO = constructFailureRespose(
								Constants.NO_MERCHANT_MESSAGE,
								Constants.NO_MERCHANT);
					}
				} else {
					log.debug("user Inactive Or Deactivated....");
					responseDTO = constructFailureRespose(
							Constants.USER_NOTFOUNT_MESSAGE,
							Constants.USER_NOTFOUNT_CODE);
				}
			} else {
			log.debug("less than minimum amount allowed....");
			responseDTO = constructFailureRespose(
					Constants.MIN_TRANSACTION_AMOUNT_MESSAGE,
					Constants.MIN_TRANSACTION_AMOUNT_CODE);
			}

		} catch (Exception e) {
			log.error("payBill..ERROR");
			log.error("Error Occured..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			log.info("in finally..");
			if(logTxn != null){
				logTxn.setErrorCode(responseDTO.getResponseCode());
				logTxn.setErrorDescription(responseDTO.getMessage());
				transactionDao.update(logTxn);
			}
			com.ceva.db.model.BillPayment payment = createDbRequest(billPayment);
			payment.setResponseMessage(responseDTO.getMessage());
			if(Constants.SUCESS_SMALL.toString().equalsIgnoreCase(responseDTO.getMessage()))
				payment.setStatus(TransactionStatus.SUCCESS.toString());
			else
				payment.setStatus(TransactionStatus.FAILURE.toString());
			
			boolean inserted = billPaymentDao.save(payment);
			log.debug("inserted data success..:"+inserted);
			biller = null;
			merchant = null;
			credentials = null;
			service = null;
			serviceProducts = null;
			packages = null;
			store = null;
		}
		log.info(responseDTO.toString());
		return responseDTO;
	}
	private com.ceva.db.model.BillPayment createDbRequest(
			BillPayment billPayment) {
		log.debug("createDbRequest..");
		com.ceva.db.model.BillPayment payment = new com.ceva.db.model.BillPayment();
		try{
			payment.setAccountNumber(billPayment.getAccountNumber());
			payment.setAmount(billPayment.getAmount());
			payment.setBillerId(billPayment.getBillerId());
			payment.setChannel(billPayment.getChannel());
			payment.setCustomerId(billPayment.getCustomerId());
			payment.setFee(billPayment.getFee());
			payment.setMerchantId(billPayment.getMerchantId());
			payment.setMobileNumber(billPayment.getMobileNumber());
			payment.setPackId(billPayment.getPackId());
			payment.setPaymentCode(billPayment.getPaymentCode());
			payment.setRequestReference(billPayment.getRequestReference());
			payment.setTerminalId(billPayment.getTerminalId());
			payment.setUserId(billPayment.getUserId());
			payment.setSid(billPayment.getSid());
			payment.setStatus("0");
			payment.setBillerName(billPayment.getBillerName());
		}catch(Exception e){
			log.error("Error While saving db Request;");
			e.printStackTrace();
		}
		
		return payment;
	}
	
	private UtilityPayBillRequest createUtilityPayBillRequest(BillPayment payment){
		UtilityPayBillRequest payBillRequest = null;
		try{
			payBillRequest =  new UtilityPayBillRequest();
			payBillRequest.setAccountNumber(payment.getAccountNumber());
			payBillRequest.setBillAmount(new BigDecimal(payment.getAmount()));
			payBillRequest.setBillerID(payment.getBillerId());
			payBillRequest.setBillerName(payment.getBillerName());
			payBillRequest.setEmail(payment.getCustomerEmail());
			payBillRequest.setBillFee(payment.getFee());
			payBillRequest.setBillReference(payment.getCustomerId());
			payBillRequest.setCategoryID(payment.getSid());
			payBillRequest.setDateTime(new SimpleDateFormat(Constants.BILLPAYMENT_DATE_FRMT).format(new Date()));
			payBillRequest.setMobile(payment.getCustomerMobile());
			payBillRequest.setNarration(payment.getNarration());
			payBillRequest.setPaymentCode(payment.getPaymentCode());
			payBillRequest.setPaymentItemID(payment.getPackId());
			payBillRequest.setReferenceCode(payment.getRequestReference());
			payBillRequest.setAgentIncomePercentage(payment.getAgentIncentive());
			payBillRequest.setReferenceCode(payment.getRequestReference());
			if("MMO".equals(payment.getTxnCode())){
				payBillRequest.setIsAirtime(true);
			}else{
				payBillRequest.setIsAirtime(false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return payBillRequest;

	}
	
	private ResponseDTO sendBillPaymentAdviceThruQuickteller(
			BillPayment billPayment) {

		ResponseDTO responseDTO = new ResponseDTO();
		PaybillHelper helper = new PaybillHelper();
		String destUrl = null;
		String terminalId = null;
		String[] pendingCodes = null;
		try {
			
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get(
					"bank.billpayment.url");
			terminalId = ConfigLoader.convertResourceBundleToMap("config").get(
					"bank.qt.temId");
			pendingCodes = ConfigLoader.convertResourceBundleToMap("config")
					.get("bank.qt.pending.code").split(",");
			billPayment.setTerminalId(terminalId);
			//billPayment.setRequestReference(payment.getRequestReference());
			billPayment.setNarration(createNarration(billPayment));
			UtilityPayBillRequest payBillRequest = createUtilityPayBillRequest(billPayment);
			log.info("Request..:"+ CommonUtil.logSoapRequest(UtilityPayBillRequest.class,payBillRequest));
			UtilityPayBillResponse payBillResponse = helper
					.sendBillPaymentAdvice(payBillRequest, terminalId, destUrl,
							pendingCodes);
			if (payBillResponse != null) {
				if (Constants.SUCCESS_RESP_CODE.equals(payBillResponse
						.getResponseCode())) {
					setRespCode(payBillResponse.getISWResponseCode() + "");
					responseDTO.setData(createReponse(billPayment,
							payBillResponse));
					responseDTO.setMessage(Constants.SUCESS_SMALL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				} else {
					setRespCode(payBillResponse.getISWResponseCode());
					responseDTO
							.setMessage(payBillResponse.getResponseMessage());
					responseDTO.setResponseCode(payBillResponse
							.getResponseCode());
				}
			} else {
				responseDTO.setMessage(Constants.TRANSACTION_FAIL);
				responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while billpaymentpayment :"+e.getLocalizedMessage());
		}
		return responseDTO;
	}

	private String createNarration(BillPayment payment) {
		String narration ="";
		try{
			narration = ConfigLoader.convertResourceBundleToMap("config")
					.get(payment.getTxnCode()+"_narration");
		if(TransactionCodes.MMO.toString().equals(payment.getTxnCode())){
			narration= narration.replaceAll("billerName", payment.getBillerName());
			narration= narration.replaceAll("mobileNumber", payment.getCustomerId());
			narration= narration.replaceAll("description", payment.getUserId());
		}else{
			narration= narration.replaceAll("billerName", payment.getBillerName());
			narration= narration.replaceAll("customerId", payment.getCustomerId());
			narration= narration.replaceAll("description", payment.getUserId());
		}
		narration= narration.replaceAll("sysId", payment.getRequestReference());
		}catch(Exception e){
			e.printStackTrace();
		}
		return narration;

	}
	private Response createReponse(BillPayment payment,
			UtilityPayBillResponse payBillResponse) {
		Response response = new Response();
		try {
			response.setAmount(payment.getAmount());
			response.setRequestReference(payment.getRequestReference());
			//response.setTransactionRef(payBillResponse.getTransRef());
			response.setTransactionRef(payBillResponse.getReferenceCode());
			response.setResponseCode(payBillResponse.getISWResponseCode());
			response.setResposeDescription(payBillResponse.getISWTraceID());

		} catch (Exception e) {
			log.error("Error Occured." + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return response;
	}

	private BigDecimal calculateAgentIncome(Incentive incentive, Fee fee) {
		log.debug("calculateAgentIncome");
		BigDecimal agentIncome = null;
		try {
			if (incentive != null) {
				if (incentive.getIncVal().intValue() > 0) {
					agentIncome = getPercentageIncome(
							fee.getAgntCmsn().add(incentive.getIncVal()),
							fee.getFeeVal());
				} else {
					agentIncome = getPercentageIncome(fee.getAgntCmsn(),
							fee.getFeeVal());
				}
			} else {
				agentIncome = getPercentageIncome(fee.getAgntCmsn(),
						fee.getFeeVal());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error While calculateAgentIncome..:"
					+ e.getLocalizedMessage());
		} finally {
			incentive = null;
			fee = null;
		}
		log.debug("agentIncome..:" + agentIncome);
		return agentIncome;
	}

	public BigDecimal getPercentageIncome(BigDecimal agentCommission,
			BigDecimal totalFee) {
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

	private Incentive getIncentive(UserLoginCredentials credentials,
			int txnCount, Fee fee) {
		Incentive incentive = feeDao.getIncentive(credentials.getStore(),
				txnCount, fee);
		return incentive;
	}

	private Transaction createTxn(BillPayment transferRequest) {
		Transaction transaction = new Transaction();
		try {
			transaction.setApprovedBy(transferRequest.getUserId());
			transaction.setMerchantId(transferRequest.getMerchantId());
			transaction.setBankId(transferRequest.getPaymentCode());
		//	transaction.setBillerId(transferRequest.getBillerId());
			transaction.setRefNumber(transferRequest.getRequestReference());
			transaction
					.setChannel(Long.parseLong(transferRequest.getChannel()));
			transaction.setStatus(TransactionStatus.FAILURE.toString());
			transaction.setResponseCode(Constants.FAILURE_RESP_CODE);
			transaction.setAgentAccountNumber(transferRequest.getAccountNumber());
			transaction.setCustomerAccountNumber(transferRequest.getCustomerId());
			transaction.setNarration(transferRequest.getNarration());
			transaction.setTerminalNumber(transferRequest.getMobileNumber());
			transaction.setTxnCode(transferRequest.getTxnCode());
			transaction.setTxndateTime(new Date());
			transaction.setAmount(new BigDecimal(transferRequest.getAmount()));
			transaction.setRequestString(transaction.getRequestString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transaction;
	}

	@Override
	public ResponseDTO mobileRecharge(BillPayment billPayment) {
		ResponseDTO responseDTO = null;
		Billers biller = null;
		Fee fee = null;
		Merchant merchant = null;
		Store store = null;
		log.debug("payBill.." + billPayment.getId());
		String transactionReference = null;
		com.ceva.db.model.Service service = null;
		ServiceProducts serviceProducts = null;
		UserLoginCredentials credentials = null;
		BillerPackages packages = null;
		Transaction logTxn = null;
		Incentive incentive = null;
		try {
			String prefix = ConfigLoader.convertResourceBundleToMap("config")
					.get("bank.qt.prefix");
			transactionReference = prefix + System.currentTimeMillis();
			billPayment.setRequestReference(transactionReference);
			credentials = loginDao.getById(billPayment.getUserId());
			billPayment.setTxnCode(TransactionCodes.MMO.toString());
			biller = billPaymentDao.getBiller(billPayment.getId());
			billPayment.setBillerId(biller.getBillerId());
			if(Double.parseDouble(billPayment.getAmount())>=Constants.MIN_TRANSACTION_AMOUNT){
				if (credentials != null) {
					merchant = merchantDao.getById(billPayment.getMerchantId());
					store = merchantDao.getStoreById(credentials.getStore());
					if (merchant != null || store != null) {
						billPayment.setAccountNumber(store.getAccountNumber());
						log.debug("getting FEE START..");
						logTxn = createTxn(billPayment);
						logTxn.setStoreId(credentials.getStore());
						long txnId = transactionDao.save(logTxn);
						if (txnId != 0) {
							service = feeDao.getServiceByCode(billPayment.getTxnCode());
							if (service != null) {
								serviceProducts = feeDao.getProductService(
										service.getsId(),
										Long.parseLong(merchant.getProduct()));
							}
							fee = feeDao.getFee(serviceProducts.getSpId(),
									new BigDecimal(billPayment.getAmount()));
							if (fee == null) {
								fee = new Fee();
							}
							log.info(fee.toString());
							log.debug("getting FEE END..");
							int txnCount = transactionDao
									.getStoreTxnCount(credentials.getStore());
							log.debug("transaction Count..:" + txnCount);
							incentive = getIncentive(credentials, txnCount, fee);
							BigDecimal agentPercIncome = calculateAgentIncome(incentive, fee);
							billPayment.setAgentIncentive(agentPercIncome);
	
							long txnAmt = fee.getFeeVal().longValue()
									+ new BigDecimal(billPayment.getAmount())
											.longValue();
	
							if (serviceProducts != null
									&& txnAmt <= serviceProducts.getTxnLimit()
											.longValue()) {
	
								//biller = billPaymentDao.getBiller(billPayment.getId());
	
								if (biller != null) {
	
									BigDecimal txnAmount = fee.getFeeVal()
											.add(new BigDecimal(billPayment
													.getAmount()));
									log.debug("Total Transaction Amount..:"
											+ txnAmount);
									AccountBalance balance = finacleService.balanceEnquirey(store.getAccountNumber());
									log.info("balance Enquirey.:"+ balance.getBalance());
									boolean hasBalance = balance.getBalance()
											.doubleValue() >= txnAmount
											.doubleValue() ? true : false;
									log.debug("has balance...:" + hasBalance);
									if (hasBalance) {
										billPayment.setBillerName(biller.getBillerName());
										billPayment.setFee(fee.getFeeVal());
										billPayment.setBillerId(biller.getBillerId());
										log.debug("loading packages from database..!");
										billPayment.setPackId("01");
										billPayment.setPaymentCode(biller.getBillerId()+biller.getAccountNumber());
										/*if ("0".equals(billPayment.getPackId())) {
											log.debug("setting default pack id and payment code");
											billPayment.setPackId("01");
											billPayment.setPaymentCode(biller.getBillerId()+biller.getAccountNumber());
										} else {
											log.debug("loading packages from database..!");
											log.debug("setting payement code form billers default paymentCode");
											billPayment.setPackId("01");
											billPayment.setPaymentCode(biller.getBillerId()+biller.getAccountNumber());
										}*/
	
										log.debug("Constructed billpayment..");
										log.info(billPayment.toString());
										if (!"INVALID DENOMINATION"
												.equals(billPayment.getNarration())) {
											log.debug("found pack id and payment code..:");
											switch ((int) biller.getChannel()) {
											case 0:
												log.debug("getting billers from database");
												// packages =
												// billPaymentDao.getBillerPackages(biller.getId());
												break;
											case 1:
												log.debug("billpayment through quicktller");
												logTxn.setReqdateTime(new Date());
												responseDTO = sendBillPaymentAdviceThruQuickteller(billPayment);
												logTxn.setRespDateTime(new Date());
												log.info(responseDTO.toString());
												Response response = (Response) responseDTO.getData();
												if (Constants.SUCESS_SMALL
														.equals(responseDTO.getMessage())) {
													responseDTO.setFee(fee.getAgntCmsn());
													HashMap<String, String> data= new HashMap<String, String>();
													data.put("refNumber", response.getRequestReference());
													data.put("amount", response.getAmount());
													data.put("extSysId", getRespCode());
													responseDTO.setData(data);
													try {
														logTxn.setExtSysId(response.getTransactionRef());//missing transaciton reference
														logTxn.setFee(fee.getFeeVal());
														logTxn.setAgentCmsn(fee.getAgntCmsn());
														logTxn.setBankCmsn(fee.getBankFee());
														logTxn.setResponseCode(responseDTO.getResponseCode());
														//logTxn.setExtSysId(getRespCode());
														logTxn.setExtSysId(response.getTransactionRef());
														logTxn.setStatus(TransactionStatus.SUCCESS.toString());
														if (incentive.getId() > 0) {
															logTxn.setIncentive(incentive.getIncVal());
															fee.setAgntCmsn(fee.getAgntCmsn().add(incentive.getIncVal()));
														}
														responseDTO.setFee(fee.getAgntCmsn());
														transactionDao.update(logTxn);
													} catch (Exception e) {
														log.error("Error while updating commmission in txn_log for success txn, update manually.");
														log.error("RefNumber:"+ logTxn.getRefNumber()+ "fee:"+ fee.toString());
													}
													/*try {
														transactionDao.updateCommisions(store, fee);
													} catch (Exception e) {
														log.error("Error while updating commmission for success txn, update manually.");
														log.error("RefNumber:"+ logTxn.getRefNumber()+ "fee:"+ fee.toString());
													}
													log.debug("updating commissions END.");*/
													insertAlerts(merchant,credentials, balance,fee, billPayment,response);
	
												} else {
													log.info("Failure Respons from bill payment wirh quickteller.");
													log.info("failure response..:"+ responseDTO.getMessage());
												}
												break;
											case 2:
												log.debug("billpayment through nibbs, nibbs not ready for now ");
												// packages =
												// billPaymentDao.getBillerPackages(biller.getId());
												break;
											default:
												log.debug("biller channel value not matched.."
														+ billPayment.getId());
												break;
											}
										} else {
											log.debug("Incorrect Subscription Amount.");
											responseDTO = constructFailureRespose(
													Constants.BILLER_INVALID_DENOMINATION_MESSAGE,
													Constants.BILLER_INVALID_DENOMINATION_CODE);
										}
									} else {
										log.debug("Insufficient funds in account."
												+ balance.getBalance());
										responseDTO = constructFailureRespose(
												Constants.BALANCE_LOW_MESSAGE,
												Constants.BALANCE_LOW);
									}
								} else {
									log.debug("Biller query failed from database.");
									responseDTO = constructFailureRespose(
											Constants.SEARCH_FAIL,
											Constants.FAILURE_RESP_CODE);
								}
							} else {
								log.debug("Service Limit validation failed.");
								responseDTO = constructFailureRespose(
										Constants.SERVICE_LIMIT_EXCEED_MESSAGE,
										Constants.SERVICE_LIMIT_EXCEED_CODE);
							}
						} else {
							log.debug("System Challenge..!");
							responseDTO = constructFailureRespose(
									Constants.SYSYEM_CHALLENGE_MESSAGE,
									Constants.SYSTEMCHALLENGE_CODE);
						}
					} else {
						log.debug("Agent Deactivated.");
						responseDTO = constructFailureRespose(
								Constants.NO_MERCHANT_MESSAGE,
								Constants.NO_MERCHANT);
					}
				} else {
					log.debug("user Inactive Or Deactivated....");
					responseDTO = constructFailureRespose(
							Constants.USER_NOTFOUNT_MESSAGE,
							Constants.USER_NOTFOUNT_CODE);
				}
			} else {
			log.debug("less than minimum amount allowed....");
			responseDTO = constructFailureRespose(
					Constants.MIN_TRANSACTION_AMOUNT_MESSAGE,
					Constants.MIN_TRANSACTION_AMOUNT_CODE);
			}

		} catch (Exception e) {
			log.error("payBill..ERROR");
			log.error("Error Occured..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			log.error("in finally..");
			if(logTxn != null){
				logTxn.setErrorCode(responseDTO.getResponseCode());
				logTxn.setErrorDescription(responseDTO.getMessage());
				transactionDao.update(logTxn);
			}
			com.ceva.db.model.BillPayment payment = createDbRequest(billPayment);
			payment.setResponseMessage(responseDTO.getMessage());
			if(Constants.SUCESS_SMALL.toString().equalsIgnoreCase(responseDTO.getMessage()))
				payment.setStatus(TransactionStatus.SUCCESS.toString());
			else
				payment.setStatus(TransactionStatus.FAILURE.toString());
			
			boolean inserted = billPaymentDao.save(payment);
			log.debug("inserted data success..:"+inserted);
			biller = null;
			merchant = null;
			credentials = null;
			service = null;
			serviceProducts = null;
			packages = null;
			store = null;
			payment=null;
		}
		log.info(responseDTO.toString());
		return responseDTO;
	}
	
	@Override
	public ResponseDTO queryStatus(String refnumber) {
		ResponseDTO responseDTO = new ResponseDTO();
		PaybillHelper helper = new PaybillHelper();
		String destUrl = null;
		String terminalId = null;
		try {
			
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get(
					"bank.billpayment.url");
			terminalId = ConfigLoader.convertResourceBundleToMap("config").get(
					"bank.qt.temId");
			String sslType = ConfigLoader.convertResourceBundleToMap("config").get(
					"bank.qt.sslType");
			String[] successRepoCode = ConfigLoader
					.convertResourceBundleToMap("config")
					.get("bank.qt.suc.respcode").split(",");
			String response = helper.queryPayment(refnumber, terminalId, destUrl, sslType, successRepoCode);
			if (response != null) {
				Response resp = unMarshal(response);
				if (ArrayUtils
						.contains(successRepoCode, resp.getTransactionResponseCode())) {
					QueryTransactionResponse queryTransactionResponse = createGenerealResponse(resp);
					responseDTO.setData(queryTransactionResponse);
					responseDTO.setMessage(Constants.SUCESS_SMALL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				} else {
					responseDTO.setData(resp);
					responseDTO.setMessage("Transaction Completed Requery failed. Transaction Reference Number..:"
							+ refnumber);
					responseDTO.setResponseCode(resp.getResponseCode());
				}
			} else {
				responseDTO.setMessage(Constants.SEARCH_FAIL);
				responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return responseDTO;
	}
	private QueryTransactionResponse createGenerealResponse(Response resp) {
		QueryTransactionResponse transactionResponse = null;
		try {
			transactionResponse = new QueryTransactionResponse();
			transactionResponse.setAmount(new BigDecimal(resp.getAmount()));
			transactionResponse.setBeneficiaryName(resp.getCustomer());
			transactionResponse.setBeneficiaryAccount(resp.getServiceProviderId());
			transactionResponse.setDateTime(resp.getPaymentDate());
			transactionResponse.setResponseMesage(resp.getResposeDescription());
			transactionResponse.setStatus(resp.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionResponse;
	}

	private Response unMarshal(String xml) {
		Response response = null;
		JAXBContext context = null;
		Unmarshaller un = null;
		try {
			context = JAXBContext.newInstance(Response.class);
			un = context.createUnmarshaller();
			response = (Response) un.unmarshal(new ByteArrayInputStream(xml
					.getBytes(StandardCharsets.UTF_8)));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			un = null;
			context = null;
		}
		return response;
	}
	private void insertAlerts(Merchant merchant,
			UserLoginCredentials credentials, AccountBalance balance, Fee fee,
			BillPayment billPayment, Response response) {
		try {
			if ("3".equals(merchant.getNotiFicationType())) {
				log.debug("agent has notifications of sms, email.");
				prepareSMSAlert(billPayment, credentials, fee, balance);
				prepareAlert(billPayment, credentials, response, fee);
			}
			if ("2".equals(merchant.getNotiFicationType())) {
				log.debug("agent has notifications EMAIL only.");
				prepareAlert(billPayment, credentials, response, fee);
			}
			if ("1".equals(merchant.getNotiFicationType())) {
				log.debug("agent has notifications SMS only.");
				prepareSMSAlert(billPayment, credentials, fee, balance);
			}
			if ("0".equals(merchant.getNotiFicationType())) {
				log.debug("All notifications are disabled for this merchant");
			}
		} catch (Exception e) {

		} finally {

		}
	}

	private void prepareSMSAlert(BillPayment billPayment,
			UserLoginCredentials credentials, Fee fee, AccountBalance balance) {
		log.debug("preparing sms for txn:" + billPayment.getTxnCode());
		Alert alert = new Alert();
		StringBuffer message = new StringBuffer();
		try {
			if (TransactionCodes.BILLPAYMENT.toString().equals(
					billPayment.getTxnCode())) {
				message.append("Bill payment was successful.");
			} else if (TransactionCodes.MMO.toString().equals(
					billPayment.getTxnCode())) {
				message.append(billPayment.getBillerName()
						+ " Airtime purchase was successful for mobile number "
						+ billPayment.getCustomerId() + ".");
			} else if (TransactionCodes.AIRTIME.toString().equals(
					billPayment.getTxnCode())) {
				message.append(billPayment.getBillerName()
						+ " Airtime purchase was successful for mobile number "
						+ billPayment.getCustomerId() + ".");
			}

			message.append(" Amount : ")
					.append(billPayment.getAmount() + "")
					.append(", Commission: ")
					.append(fee.getAgntCmsn() + "")
					.append(", Ref: ")
					.append(billPayment.getRequestReference() + "")
					.append(", Time: ")
					.append(new SimpleDateFormat(Constants.MAIL_DATE_FORMAT)
							.format(new Date()) + "")
					.append(", Balance:")
					.append(balance.getBalance().subtract(
							new BigDecimal(billPayment.getAmount()).add(fee
									.getFeeVal()))
							+ "");

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

	private void prepareAlert(BillPayment billPayment,
			UserLoginCredentials credentials, Response response, Fee fee) {
		Alert alert = new Alert();
		StringBuffer message = new StringBuffer();
		try {
			String[] pendingCodes = ConfigLoader
					.convertResourceBundleToMap("config")
					.get("bank.qt.pending.code").split(",");
			message.append("Dear <b>").append(credentials.getUserName())
					.append("</b><br/><br/>");
			if (TransactionCodes.BILLPAYMENT.toString().equals(
					billPayment.getTxnCode())) {
				if (ArrayUtils.contains(pendingCodes,
						response.getResponseCode())) {
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
				if (ArrayUtils.contains(pendingCodes,
						response.getResponseCode())) {
					log.debug("Identified Airtime Purchase pending.");
					message.append("Your Airtime Purchase was Processing.");
				} else {
					message.append("Your Airtime Purchase was successful.");
				}
				message.append(", <br/>")
						.append("<tr><td>Mobile Number </td><td>")
						.append(billPayment.getCustomerId() + "</td></tr>");
			}
			message.append("<tr><td>Reference number  </td><td>")
					.append(billPayment.getRequestReference() + "</td></tr>")
					.append("<tr><td>Amount  </td><td>")
					.append(billPayment.getAmount() + "</td></tr>")
					.append("<tr><td>Commission Earned  </td><td>")
					.append(fee.getAgntCmsn() + "")
					.append("</td></tr>")
					.append("<tr><td>Date  And Time  </td><td>")
					.append(new SimpleDateFormat(Constants.MAIL_DATE_FORMAT)
							.format(new Date()) + "</td></tr>");

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

	private ResponseDTO constructFailureRespose(Object object,
			String responsCode) {
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
	public ResponseDTO getPaymentItemsAndMarkets(BillePaymentItems billerPackages) {
		log.info("Loading PaymentItems And Markets database.");
		ResponseDTO dto = new ResponseDTO();
		Map<String, Object> data = new HashMap<>();
		try {
			dto.setMessage(Constants.SUCESS_SMALL);
			dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			List<BillerPackages> packages = billPaymentDao.getBillerPackages(billerPackages.getId());
			data.put("packages", packages);
			List<BillerMakets> makets = billPaymentDao.getBillerMarkets(billerPackages.getId());
			data.put("makets", makets);
			dto.setData(data);
		} catch (Exception e) {
			log.error("Error while loading PaymentItems And Markets from database..!");
			log.error("Reason..!" + e.getLocalizedMessage());
			//e.printStackTrace();
		}
		return dto;
	}

}
