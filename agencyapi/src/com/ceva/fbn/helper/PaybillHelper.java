package com.ceva.fbn.helper;

import java.net.URL;

import javax.net.ssl.SSLContext;

import org.datacontract.schemas._2004._07.AgentBKGBillPayment_Model.UtilityPayBillRequest;
import org.datacontract.schemas._2004._07.AgentBKGBillPayment_Model.UtilityPayBillResponse;
import org.paybill.tempuri.BasicHttpsBinding_IProcessBillPaymentStub;

import com.interswitchng.services.quicktellerservice.BasicHttpBinding_QuickTellerServiceStub;

public class PaybillHelper {

	private String response;
	private String respCode;
	
	public UtilityPayBillResponse sendBillPaymentAdvice(UtilityPayBillRequest payBillRequest,String terminalId,
			String billPaymentUrl, String[] pendingCodes) {
		UtilityPayBillResponse payBillResponse=null;
		try {
			BasicHttpsBinding_IProcessBillPaymentStub stub = new BasicHttpsBinding_IProcessBillPaymentStub(new URL(billPaymentUrl), null);
			payBillResponse = stub.processBillTransaction(payBillRequest);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			payBillRequest = null;
			pendingCodes = null;
		}
		return payBillResponse;
	}


	public String queryPayment(String ref, String terminalId, String dest, String sslType, String[] successRepoCode) {
		BasicHttpBinding_QuickTellerServiceStub stub = null;
		URL url = null;
		String response = null;
		try {
			url = new URL(dest);
			SSLContext.setDefault(getSSLContext(sslType));
			stub = new BasicHttpBinding_QuickTellerServiceStub(url, null);
			String biller = "<RequestDetails>" + "<TerminalId>" + terminalId
					+ "</TerminalId>" + "<RequestReference>" + ref
					+ "</RequestReference>" + "</RequestDetails>";
			response = stub.queryTransaction(biller);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			url = null;
			stub = null;
			dest = null;
		}
		return response;
	}
	
	public String validateCustomer(String customerId, String paymentCode, String terminalId, String dest, String sslType) {
		BasicHttpBinding_QuickTellerServiceStub stub = null;
		String response = null;
		URL url = null;
		try {
			url = new URL(dest);
			SSLContext.setDefault(getSSLContext(sslType));
			stub = new BasicHttpBinding_QuickTellerServiceStub(url, null);
			String biller = "<RequestDetails><TerminalId>"+terminalId+"</TerminalId><Customer><PaymentCode>"+paymentCode+"</PaymentCode><CustomerId>"+customerId+"</CustomerId><CustomerValidationField></CustomerValidationField ><WithDetails>False</WithDetails ></Customer><Customer><PaymentCode>"+paymentCode+"</PaymentCode> <CustomerId>"+customerId+"</CustomerId></Customer></RequestDetails>";
			System.out.println("validateCustomer..:"+biller);
			response = stub.validateCustomer(biller);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			url = null;
			stub = null;
			dest = null;
		}
		return response;
	}

	private SSLContext getSSLContext(String sslType) {
		SSLContext ctx = null;
		try {
			if (sslType != null) {
				ctx = SSLContext.getInstance(sslType);
				ctx.init(null, null, null);
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ctx;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}



}
