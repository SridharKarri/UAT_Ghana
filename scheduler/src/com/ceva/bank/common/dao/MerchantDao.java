package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.Merchant;
import com.ceva.db.model.Store;
import com.ceva.db.model.SuperAgent;
import com.ceva.dto.ResponseDTO;

public interface MerchantDao {
	public Merchant getById(String Id);
	public Store getStoreById(String Id);
	public SuperAgent superAgentById(String Id);
	public List<Merchant> getAgents();
	public List<Store> getStores();
	public List<SuperAgent> getSuperAgents();
	public ResponseDTO getServiceProducts(String serviceId, String productId);
	public SuperAgent superAgentByName(String agentName);
}
