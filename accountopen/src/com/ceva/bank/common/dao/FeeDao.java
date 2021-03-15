package com.ceva.bank.common.dao;

import java.math.BigDecimal;

import com.ceva.db.model.Fee;
import com.ceva.db.model.Incentive;
import com.ceva.db.model.Service;
import com.ceva.db.model.ServiceProducts;

public interface FeeDao {
		public Fee getFee(String serviceId, long channel, String merchantId, BigDecimal Amount, String txnType);
		public Fee getFee(long spid, BigDecimal amount);
		public Fee getbyId(long id);
		public Service getServiceByCode(String serviceCode);
		public ServiceProducts getProductService(long serviceId, long productId);
		public Incentive getIncentive(String storeId, long txnCount, Fee fee);
		Service getService(String serviceCode);
		ServiceProducts getServiceProducts(long sid, long prodId);
		public BigDecimal getAgentStoreCurrentMonthCommission(String store);
		BigDecimal getAgentUserCurrentMonthCommission(String userId, String StoreId);
		public Fee calculateSuperAgentCommission(Fee fee, String feeType, String FeeValue);
}
