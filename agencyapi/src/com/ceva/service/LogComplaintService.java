package com.ceva.service;

import com.ceva.bank.common.beans.LogComplaint;
import com.ceva.dto.ResponseDTO;


public interface LogComplaintService {

	public ResponseDTO listAll(String userId);
	public ResponseDTO save(LogComplaint complaint);
	public ResponseDTO getById(Long Id);
}
