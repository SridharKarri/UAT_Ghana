package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.Merchant;
import com.ceva.db.model.Product;
import com.ceva.db.model.Store;
import com.ceva.db.model.SuperAgent;
import com.ceva.dto.ResponseDTO;

public interface MerchantDao {
		public Merchant getById(String Id);
		public Product getProducts(Long merchantId);
		public List<Merchant> getMerchants(String property, String value);
		public Store getStoreById(String Id);
		public SuperAgent getSuperAgentById(String Id);
		ResponseDTO getServiceProducts(String serviceId, String productId);
}
