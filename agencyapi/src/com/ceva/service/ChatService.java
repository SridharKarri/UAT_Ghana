package com.ceva.service;

import com.ceva.bank.common.beans.ChatItem;
import com.ceva.dto.ResponseDTO;


public interface ChatService {

	public ResponseDTO listAll(String userId);
	public ResponseDTO save(ChatItem item);
	public ResponseDTO update(ChatItem item);
}
