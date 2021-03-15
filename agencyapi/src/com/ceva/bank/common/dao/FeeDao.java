package com.ceva.bank.common.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ceva.db.model.AgentCommision;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Incentive;
import com.ceva.db.model.Service;
import com.ceva.db.model.ServiceProducts;

public interface FeeDao {
		public Fee getFee(String serviceId, long channel, String merchantId, BigDecimal Amount, String txnType);
		public Fee getFee(long spid, BigDecimal amount);
		public Fee getbyId(long id);
		public Service getServiceByCode(String serviceCode);
		public boolean updateCommision(String merchantId, Fee fee, String status, Date date, boolean isAgent);
		public boolean saveCommision(AgentCommision commision);
		public boolean inactiveCommisions(String date, String fetchStatus, String newStatus);
		public List<AgentCommision> getCommisionsToSettlement(String status, String responseCode);
		public boolean update(AgentCommision commision);
		public AgentCommision commisionByMerchantId(String merchantId);
		public AgentCommision commisionByStoreId(String storeId);
		public boolean addCommision(AgentCommision commision);
		public ServiceProducts getProductService(long serviceId, long productId);
		public Incentive getIncentive(String storeId, long txnCount, Fee fee);
		Service getService(String serviceCode);
		ServiceProducts getServiceProducts(long sid, long prodId);
		public BigDecimal getAgentStoreCommission(String store);
		public BigDecimal getAgentStoreCurrentMonthCommission(String store);
		BigDecimal getAgentUserCurrentMonthCommission(String userId, String StoreId);

}
