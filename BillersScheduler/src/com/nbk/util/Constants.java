package com.nbk.util;

public interface Constants {

	public String YEAR_FRMT = "MM/dd/yyyy H:mm:ss a";

	public String CELLULANT_YEAR_FRMT = "yyyy-MM-dd H:mm:ss";
	public String BILLPAYMENT_DATE_FRMT = "yyyy-MM-dd HH:mm:ss.SS";
	public String ORACLE_FORMAT = "dd-MM-yyyy HH:mm";
	public String MAIL_DATE_FORMAT = "EEEE d MMM yyyy hh:mm:ss (z)";

	public String YEAR = "yyyy";

	public String GEN_MALE = "M";

	public String SUCCESSFULL_RESP_MESSAGE = "Successful";
	/*public String TRANSFER_SUCCESSFULL_RESP_MESSAGE = "Transfer Successful";*/
	public String SUCCESS_RESP_CODE = "00";
	public String FAILURE_RESP_CODE = "90";
	public String SEARCH_FAIL = "No Record Found With Given Input.";

	public String TRANSACTION_FAIL = "Your Transaction Failed. Please Try Later.";
	public String EXTERNAL_SYSTEM_FAIL = "External System is not responding.";

	public String PHONE_NUMBER_FAILED_CODE = "02";
	public String PHONE_NUMBER_FAILED_MESSAGE = "No Phone Numbers Registerd for this account number.";

	public String ACCOUNT_NUMBER_FAILED_CODE = "03";
	public String ACCOUNT_NUMBER_FAILED_MESSAGE = "Account Validation Failed, Please Try with valid Account number.";

	public String OTP_FAILED_CODE = "04";
	public String OTP_FAILED_MESSAGE = "Invalid Otp Entered, Please Enter Valid Otp.";

	public String OTP_RETRY_LIMITS = "05";
	public String OTP_RETRY_MESSAGE = "Your otp will be invalidated.";

	public String BALANCE_LOW= "06";
	public String BALANCE_LOW_MESSAGE = "Insufficient Balance In account";

	public String ACC_OPEN_PEN_FLAG = "FP";

	public String NAT3_ACC_OPEN_PEN_FLAG = "NP";

	public String ACC_OPEN_ID_FRMT = "yyddMMHmmssSSS";

	public String ACC_OPEN_CHANNEL_ID_MOBILE = "AGBANK";

	public String CONFIG_BUNDLE = "config";
	public String NARR_PATTERN_COMP = "\\$(.*?)\\$";
	public String NARR_DOLL_SYM = "\\$";
	public String BAL_INQ_REG_SYM = "(?<=\\G.{12})";
	public String MINI_STMT_REG_SYM = "(?<=\\G.{61})";
	public String MINI_SUCCESS_ZERO = "000000000000";
	public String MINI_AVAIL_BALANCE = "AVAIL BALANCE";
	public String BALANCE_FORMAT = "###,###.###";

	public String CONFIG_BUNDLE_ERROR = "resources/errors";
	public String RESOURCE = "/resources/";

	// The below is for switch constant codes

	public String SUCESS_SMALL = "Success";
	public String PENDING_CAPS = "PENDING";
	public String SUCESS_CAPS = "SUCCESS";
	public String FAIL_CAPS = "FAILURE";
	public String MESSAGE = "message";
	public String ERROR_CODE = "errorcode";
	public String ERROR_MESSAGE = "errormessage";
	public String ERROR_FLAG = "errorflag";

	public int NHIF_PAY_METHOD = 2;

	public String GENERIC_FAILURE_MESS  = "Sorry, an internal system error occurred while processing your request.Please try again later or contact customer care on 0703088900 if the problem persists.";

	public final String STMT_RESP_CODE="ResponseCode";
	public final String STMT_RESP_MSG="ResponseMessage";
	public final String STMT_RESP_DATA="TransactionHistory";
	public final String STMT_RESP_STATUS="Status";

	public final String ERROR_MESSAGE_VALUE = "Error While Procesing Request.";
	public final String ERROR_CODE_NUMBER = "9000";
	public final String FIN_SUCCESS_RESPONSE_CODE = "00";

	public String NO_FEE = "91";
	public String NO_FEE_MESSAGE = "No Fee Configured For This Service.";

	public String NO_MERCHANT = "92";
	public String NO_MERCHANT_MESSAGE = "Agent Account deactivated.";

	public String SERVICE_LIMIT_EXCEED_CODE = "93";
	public String SERVICE_LIMIT_EXCEED_MESSAGE = "Limit Exceeded for Service per Agent Product.";

	public String BILLER_INVALID_DENOMINATION_CODE = "94";
	public String BILLER_INVALID_DENOMINATION_MESSAGE = "Incorrect Subscription Amount. Please try With correct Amount";

	public String INVALID_REFERENCE_NUMBER_CODE= "95";
	public String INVALID_REFERENCE_NUMBER_MESSAGE = "Invalid Transaction Reference, Please Input Valid Reference Number";


	public String UNEXPECTED_ERROR_CODE = "99";
	public String UNEXPECTED_ERROR_MESSAGE = "UnKnown Error.";

	public String USER_NOTFOUNT_CODE = "006";
	public String USER_NOTFOUNT_MESSAGE = "User ID not found/ User Deactivated.";

	public final String DATE_PATTERN = "dd-MM-yyyy";

	public String FEE_NARRATION = "Comision posting for agent.";

}
