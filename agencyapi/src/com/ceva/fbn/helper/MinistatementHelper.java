package com.ceva.fbn.helper;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.BindingProvider;

import org.datacontract.schemas._2004._07.agentbankingministatement.MiniStatementRequest;
import org.datacontract.schemas._2004._07.agentbankingministatement.MiniStatementResponse;
import org.ministmt.tempuri.AgentBanking;
import org.ministmt.tempuri.IAgentBanking;
import org.ministmt.tempuri.MinistatementHeaderInjectHandler;

public class MinistatementHelper {

	public MiniStatementResponse getMinistatement(MiniStatementRequest request,
			String url, String pkey) {
		AgentBanking agentBanking;
		MiniStatementResponse miniStatementResponse= null;
		try {
			agentBanking = new AgentBanking(new URL(url));
			IAgentBanking ibanking = agentBanking.getBasicHttpBindingIAgentBanking();
			((BindingProvider)ibanking).getBinding().getHandlerChain().add(new MinistatementHeaderInjectHandler(pkey));
			miniStatementResponse= ibanking.getMiniStatement(request);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return miniStatementResponse;
	}

}
