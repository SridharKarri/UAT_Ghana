package com.ceva.fbn.helper;

import java.net.URL;
import java.util.List;

import org.datacontract.schemas._2004._07.custdetailsviaacctno.AccountBalance;
import org.datacontract.schemas._2004._07.custdetailsviaacctno.AccountDetails;
import org.datacontract.schemas._2004._07.custdetailsviaacctno.ArrayOfAccountDetails;
import org.tempuri.GetDetails;
import org.tempuri.IGetDetails;

public class CustDetailsViaAccountHelper {

	public AccountDetails QueryAccount(String destUrl, String accountNumber){
		GetDetails details = null;
		IGetDetails iGetDetails = null;
		ArrayOfAccountDetails accountDetails = null;
		List<AccountDetails> list = null;
		try{
			details = new GetDetails(new URL(destUrl));
			iGetDetails = details.getBasicHttpBindingIGetDetails();
			accountDetails = iGetDetails.fetchCustDetails(accountNumber);
			list = accountDetails.getAccountDetails();
			if(list.size() > 0){
				for (AccountDetails acd : list) {
					return acd;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		return null;
	}

	public AccountBalance balanceEnquirey(String destUrl, String accountNumber) {
		GetDetails details = null;
		IGetDetails iGetDetails = null;
		AccountBalance accountBalance = null;
		try{
			details = new GetDetails(new URL(destUrl));
			iGetDetails = details.getBasicHttpBindingIGetDetails();
			accountBalance = iGetDetails.getAccountBalance(accountNumber);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		return accountBalance;
	}

}
