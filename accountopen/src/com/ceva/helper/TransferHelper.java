package com.ceva.helper;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import org.datacontract.schemas._2004._07.AgentBKGTransfers_Models.UtilityNameLookupRequest;
import org.datacontract.schemas._2004._07.AgentBKGTransfers_Models.UtilityNameLookupResponse;
import org.datacontract.schemas._2004._07.AgentBKGTransfers_Models.UtilityResponses;
import org.datacontract.schemas._2004._07.AgentBKGTransfers_Models.UtilityTSQResponse;
import org.datacontract.schemas._2004._07.AgentBKGTransfers_Models.UtilityTransferRequest;
import org.datacontract.schemas._2004._07.AgentBKGTransfers_Models.UtilityTransferResponse;
import org.tempuri.BasicHttpBinding_IInitiateTransferStub;

import com.nbk.util.Constants;

public class TransferHelper {

	public UtilityTransferResponse processIntraBank(
			UtilityTransferRequest utilityTransferRequest, String destUrl,
			String bankCode) {
		URL url = null;
		BasicHttpBinding_IInitiateTransferStub stub = null;
		UtilityTransferResponse transferResponse = null;
		try {
			utilityTransferRequest.setBankCode(bankCode);
			url = new URL(destUrl);
			stub = new BasicHttpBinding_IInitiateTransferStub(url, null);
			transferResponse = stub.processIntraBank(utilityTransferRequest);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			url = null;
			stub = null;
		}
		return transferResponse;
	}

	public UtilityTransferResponse processInterBank(
			UtilityTransferRequest utilityTransferRequest, String destUrl,
			String bankCode) {
		URL url = null;
		BasicHttpBinding_IInitiateTransferStub stub = null;
		UtilityTransferResponse transferResponse = null;
		try {
			//utilityTransferRequest.setBankCode(bankCode);
			url = new URL(destUrl);
			stub = new BasicHttpBinding_IInitiateTransferStub(url, null);
			transferResponse = stub.processInterBank(utilityTransferRequest);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			url = null;
			stub = null;
		}
		return transferResponse;
	}
	
	public UtilityTSQResponse transactionStatusQuery(String refNum,
			String destUrl) {
		URL url = null;
		BasicHttpBinding_IInitiateTransferStub stub = null;
		UtilityTSQResponse tsqResponse = null;
		try {
			url = new URL(destUrl);
			stub = new BasicHttpBinding_IInitiateTransferStub(url, null);
			tsqResponse = stub.transactionStatusQuery(refNum);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			url = null;
			stub = null;
			refNum = null;
		}
		return tsqResponse;
	}
	public UtilityNameLookupResponse nameEnquirey(
			UtilityNameLookupRequest lookupRequest, String destUrl) {
		URL url = null;
		BasicHttpBinding_IInitiateTransferStub stub = null;
		UtilityNameLookupResponse lookupResponse = null;
		try {
			url = new URL(destUrl);
			stub = new BasicHttpBinding_IInitiateTransferStub(url, null);
			stub.setTimeout(30000);
			lookupResponse = stub.nameEquiry(lookupRequest);
		}catch (Exception e) {
			if(e instanceof SocketTimeoutException){
				UtilityResponses responses = new UtilityResponses();
				responses.setResponseCode(Constants.FAILURE_RESP_CODE);
				responses.setResponseMessage("Transaction Timed Out");
				lookupResponse = new UtilityNameLookupResponse("","","", responses);
			}else{
				UtilityResponses responses = new UtilityResponses();
				responses.setResponseCode(Constants.FAILURE_RESP_CODE);
				responses.setResponseMessage(Constants.GENERIC_FAILURE_MESS);
				lookupResponse = new UtilityNameLookupResponse("","","", responses);
			}
			e.printStackTrace();
		} finally {
			url = null;
			stub = null;
			lookupRequest = null;
		}
		return lookupResponse;
	}

}
