package com.ceva.service;

import com.ceva.bank.common.beans.TransactionReport;
import com.ceva.dto.ResponseDTO;

public interface ReportService {

	public ResponseDTO generate(TransactionReport report);

}
