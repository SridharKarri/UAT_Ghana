package com.ceva.service;

import java.math.BigDecimal;

import com.ceva.db.model.Fee;
import com.ceva.dto.ResponseDTO;

public interface FeeService {
		public Fee getFee(String serviceId, long channel, String merchantId, BigDecimal Amount, String txnType);
		public ResponseDTO getFee(String serviceCode, String agentId, BigDecimal amount, String userId);
}
