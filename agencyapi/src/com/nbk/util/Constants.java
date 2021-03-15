package com.nbk.util;

import java.util.ResourceBundle;

public interface Constants {
	
	public String YEAR_FRMT = "MM/dd/yyyy H:mm:ss a";
	public String RESP_CODE = "responsecode";
	public String CELLULANT_YEAR_FRMT = "yyyy-MM-dd H:mm:ss";
	public String BILLPAYMENT_DATE_FRMT = "yyyy-MM-dd HH:mm:ss.SS";
	public String ORACLE_FORMAT = "dd-MM-yyyy HH:mm";
	public String MAIL_DATE_FORMAT = "EEEE d MMM yyyy hh:mm:ss (z)";
	public String DATE_WIT_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	public String YEAR = "yyyy";

	public String GEN_MALE = "M";

	public String SUCCESSFULL_RESP_MESSAGE = "Successful";
	public String SUCCESS_RESP_CODE = "00";
	public String FAILURE_RESP_CODE = "90";

	public String ACC_OPEN_PEN_FLAG = "FP";

	public String ACC_OPEN_ID_FRMT = "yyddMMHmmssSSS";
	public final String DATE_PATTERN = "dd-MM-yyyy";

	public String CONFIG_BUNDLE = "config";
	public String NARR_PATTERN_COMP = "\\$(.*?)\\$";
	public String NARR_DOLL_SYM = "\\$";
	public String BAL_INQ_REG_SYM = "(?<=\\G.{12})";
	public String MINI_STMT_REG_SYM = "(?<=\\G.{61})";
	public String MINI_SUCCESS_ZERO = "000000000000";
	public String MINI_AVAIL_BALANCE = "AVAIL BALANCE";
	public String BALANCE_FORMAT = "###,###.###";
	
	public final String SHORT_DATE_PATTERN = "dd-MM-yy";
	public final String SHORT_DATE__TIME_PATTERN = "dd-MM-yy hh24:mi:ss";

	public String CONFIG_BUNDLE_ERROR = "resources/errors";
	public String RESOURCE = "/resources/";
	public String FUNDS_TRAN_DET = "transactiondetails.xml";
	public String NHIF_TRAN_DET = "nhiftransactiondetails.xml";
	public String FIMI_TRAN_CODES = "fimi_tran_codes";
	public String ISO_RESPONSE_CODES = "iso_response_codes";

	// The below is for switch constant codes.

	public char CHAR_VAL_0 = '0';
	public String VAL_0 = "0";
	public String VAL_139 = "139";
	public int INT_VAL_0 = 0;
	public int INT_VAL_1 = 1;
	public String GMT = "GMT";
	public String YES = "Y";
	public String NHIF_IDEN_CRD = "IC";
	public String NO = "N";

	public String STATUS = "status";
	public String SUCESS_SMALL = "Success";
	public String PENDING_CAPS = "PENDING";
	public String SUCESS_CAPS = "SUCCESS";
	public String TXN_CODE = "txncode";
	public String MESSAGE = "message";
	

	public String KEN_CURR = "KES";
	public String SERVICE_ID = "serviceid";

	public String VAL_404 = "404";
	public String AMOUNT = "amount";
	public String F_12 = "f12";

	public String ERROR_CODE = "errorcode";
	public String ERROR_MESSAGE = "errormessage";
	public String ERROR_FLAG = "errorflag";

	public int NHIF_PAY_METHOD = 2;

	public String KITS_REG = "REG";
	public String KITS_GET = "GET";

	public String BASE64_OTP = "b64otp";
	public String SERVER_GEN_KEY = "sgk";

	public final String STMT_RESP_CODE="ResponseCode";
	public final String STMT_RESP_MSG="ResponseMessage";
	public final String STMT_RESP_DATA="TransactionHistory";
	public final String STMT_RESP_STATUS="Status";

	public final String FIN_SUCCESS_RESPONSE_CODE = "00";
	public final String ERROR_CODE_NUMBER = "9000";

	public String PHONE_NUMBER_FAILED_CODE = "02";
	public String ACCOUNT_NUMBER_FAILED_CODE = "03";
	public String OTP_FAILED_CODE = "04";

	public String OTP_RETRY_LIMITS = "05";
	public String BALANCE_LOW= "06";
	
	public String MIN_TRANSACTION_AMOUNT_CODE = "111";
	
	public String SUSPECTED_FRAUD_CODE = "112";
	public String NO_FEE = "91";
	public String NO_MERCHANT = "92";
	public String NO_STORE = "97";
	public String SERVICE_NOT_MAPPED_CODE = "94";
	public String SERVICE_LIMIT_EXCEED_CODE = "93";
	public String BILLER_INVALID_DENOMINATION_CODE = "94";
	public String INVALID_REFERENCE_NUMBER_CODE= "95";
	public String UNEXPECTED_ERROR_CODE = "99";
	public String USER_NOTFOUNT_CODE = "006";
	public String SYSTEMCHALLENGE_CODE = "96";
	public String INVALID_CREDENTIALS = "002";
	public String USER_LOCK_CODE = "991";
	public String USER_INACTIVE_CODE = "993";
	public String AGENT_STORE_INACTIVE_CODE = "994";
	public String INVALID_REG_MOBILE = "005";
	public String DEVICE_REGISTER_FAIL_CODE = "007";
	public String MULTI_CURRENCY_CODE = "011";
	
	public String GENERIC_FAILURE_MESS  = GetProperties.load("GENERIC_FAILURE_MESS");
	public String ERROR_MESSAGE_VALUE = GetProperties.load("ERROR_MESSAGE_VALUE");
	public String SEARCH_FAIL = GetProperties.load("SEARCH_FAIL");
	public String TRANSACTION_FAIL = GetProperties.load("TRANSACTION_FAIL");
	public String EXTERNAL_SYSTEM_FAIL = GetProperties.load("EXTERNAL_SYSTEM_FAIL");
	public String PHONE_NUMBER_FAILED_MESSAGE = GetProperties.load("PHONE_NUMBER_FAILED_MESSAGE");
	public String ACCOUNT_NUMBER_FAILED_MESSAGE = GetProperties.load("ACCOUNT_NUMBER_FAILED_MESSAGE");
	public String OTP_FAILED_MESSAGE = GetProperties.load("OTP_FAILED_MESSAGE");
	public String OTP_RETRY_MESSAGE = GetProperties.load("OTP_RETRY_MESSAGE");
	public String BALANCE_LOW_MESSAGE = GetProperties.load("BALANCE_LOW_MESSAGE");
	public String MIN_TRANSACTION_AMOUNT_MESSAGE = GetProperties.load("MIN_TRANSACTION_AMOUNT_MESSAGE");
	public String SUSPECTED_FRAUD_MESSAGE = GetProperties.load("SUSPECTED_FRAUD_MESSAGE");
	public String NO_FEE_MESSAGE = GetProperties.load("NO_FEE_MESSAGE");
	public String NO_MERCHANT_MESSAGE = GetProperties.load("NO_MERCHANT_MESSAGE");
	public String NO_STORE_MESSAGE = GetProperties.load("NO_STORE_MESSAGE");
	public String SERVICE_NOT_MAPPED_MESSAGE = GetProperties.load("SERVICE_NOT_MAPPED_MESSAGE");
	public String SERVICE_LIMIT_EXCEED_MESSAGE = GetProperties.load("SERVICE_LIMIT_EXCEED_MESSAGE");
	public String BILLER_INVALID_DENOMINATION_MESSAGE = GetProperties.load("BILLER_INVALID_DENOMINATION_MESSAGE");
	public String INVALID_REFERENCE_NUMBER_MESSAGE = GetProperties.load("INVALID_REFERENCE_NUMBER_MESSAGE");
	public String UNEXPECTED_ERROR_MESSAGE = GetProperties.load("UNEXPECTED_ERROR_MESSAGE");
	public String USER_NOTFOUNT_MESSAGE = GetProperties.load("USER_NOTFOUNT_MESSAGE");
	public String FEE_NARRATION = GetProperties.load("FEE_NARRATION");
	public String SYSYEM_CHALLENGE_MESSAGE = GetProperties.load("SYSYEM_CHALLENGE_MESSAGE");
	public String NO_MERCHANT_OR_STORE_MESSAGE = GetProperties.load("NO_MERCHANT_OR_STORE_MESSAGE");
	public String INVALID_CREDENTIALS_MESSAGE = GetProperties.load("INVALID_CREDENTIALS_MESSAGE");
	public String USER_LOCK_MESSAGE = GetProperties.load("USER_LOCK_MESSAGE");
	public String USER_INACTIVE_MESSAGE = GetProperties.load("USER_INACTIVE_MESSAGE");
	public String AGENT_STORE_INACTIVE_MESSAGE = GetProperties.load("AGENT_STORE_INACTIVE_MESSAGE");
	public String INVALID_REG_MOBILE_MESSAGE = GetProperties.load("INVALID_REG_MOBILE_MESSAGE");
	public String DEVICE_REGISTER_FAIL_MESSAGE = GetProperties.load("DEVICE_REGISTER_FAIL_MESSAGE");
	public String MULTI_CURRENCY_MESSAGE = GetProperties.load("MULTI_CURRENCY_MESSAGE");
	public String USER_TRANSACT_OTHERDEVICE_MESSAGE = GetProperties.load("USER_TRANSACT_OTHERDEVICE_MESSAGE");
	public String INVALID_PIN = GetProperties.load("INVALID_PIN");
	public String ATTEMPTS_LEFT = GetProperties.load("ATTEMPTS_LEFT");
	public String INVALID_USERID_MESSAGE = GetProperties.load("INVALID_USERID_MESSAGE");
	public String NO_ANDROID_CHANNEL_MESSAGE = GetProperties.load("NO_ANDROID_CHANNEL_MESSAGE");
	public String DEVICE_EXCEEDED_MESSAGE = GetProperties.load("DEVICE_EXCEEDED_MESSAGE");
	public String OTP_EXPIRED_MESSAGE = GetProperties.load("OTP_EXPIRED_MESSAGE");
	
	public String MASTER_KEY=null;
	public String KEYGEN_ALG = "AES";
	public int SYMETRIC_KEY_SIZE = 256;
	public String PKI_PROVIDER="BC";
	public String SYMETRICKEY_ALG = "AES/CBC/PKCS7Padding";

	public String KEYFACTORY_ALG = "RSA";
	public final String SECURE_DATA = "secureData";
	public final String CHANNEL_DATA = "channelData";
	static final String[] ANDROID_END_POINTS = { "devReg", "changepin",
			"login", "getbanks", "getwallets", "nameenq", "balenquirey",
			"stmt", "interbank", "intrabank", "txnenquirey", "queryacct",
			"depwallet", "services", "getbillers", "MMObillers",
			"mobileRecharge", "dobillpayment", "cashbyaccountinit",
			"cashbyaccountconfirm", "genrpt", "inbox", "pic", "getfee",
			"updateProfile", "billpaymentitems", "validateCustomer", "logout",
			"generateotp", "generatecustomerotp", "cities", "states", 
			"openaccount","loadparameters","getAll","loadbranches","loadcountries",
			"loadstates", "loadcities","appVersion","loadidcate","loadidtypebycate",
			"loadnationalities","loadmaritalstatuses","echo","location","loadoccupations",
			"loadsalutations","createaccount"};
	
	static final String EXCLUDE_END_POINTS = "/image/";
	
	
	public String KEY_MESSAGE= "message";
	public String KEY_RESP_CODE= "responseCode";
	public String KEY_DATA= "data";
	public String KEY_FEE= "fee";
	//public final int MIN_TRANSACTION_AMOUNT=100;
	public final int MIN_TRANSACTION_AMOUNT=Integer.parseInt(GetProperties.load("MIN_TRANSACTION_AMOUNT"));
	
	public String RESPONSE_CODE_KEY="ResponseCode";
	public String RESPONSE_MESSAGE_KEY="ResponseMessage";
	public String NO_PHONE_TO_ACCOUNT = GetProperties.load("NO_PHONE_TO_ACCOUNT");
	
	public class GetProperties{
		
		private static ResourceBundle bundle = ResourceBundle.getBundle("config");

		static String load(String key) {
			String language = bundle.getString("bank.language");
			ResourceBundle bundle2 = ResourceBundle.getBundle("language_"+language);
			return bundle2.getString(key);
		}
		
	}

}

