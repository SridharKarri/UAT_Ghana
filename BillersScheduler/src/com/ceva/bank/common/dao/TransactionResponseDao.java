package com.ceva.bank.common.dao;

import com.ceva.db.model.TransactionResponse;
import com.ceva.dto.ResponseDTO;

public interface TransactionResponseDao {
public TransactionResponse getById(String id, String code);
public ResponseDTO generateResponseForError(String id, String code);
}
