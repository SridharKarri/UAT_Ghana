package com.ceva.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.ChatItem;
import com.ceva.bank.common.dao.ChatDao;
import com.ceva.bank.utils.Status;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.ChatService;
import com.nbk.util.Constants;

@Service("chatService")
public class ChatServiceImpl implements ChatService {

	private Logger logger = Logger.getLogger(ChatService.class);

	@Autowired
	ChatDao chatDao;

	@Override
	public ResponseDTO listAll(String userId) {
		logger.info("listing all for user..:"+userId);
		ResponseDTO dto =new ResponseDTO();
		List<com.ceva.db.model.ChatItem> chatItems = null;
		try{
			if(userId !=null){
				chatItems = chatDao.listAll(userId);
			}
			dto.setData(chatItems);
			dto.setMessage(Constants.SUCESS_SMALL);
			dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
		}catch(Exception e){
			logger.error("Error Occured while getting chat items.");
			logger.error("Reason..:"+e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		}finally{
			chatItems = null;
		}
		logger.info("list all");
		return dto;
	}

	@Override
	public ResponseDTO save(ChatItem item) {
		logger.info("save....");
		ResponseDTO dto =new ResponseDTO();
		try{
			logger.info("persisting....");
			boolean isInserted = chatDao.save(convert(item));
			logger.info("persisted.:"+isInserted);
			if(isInserted){
				dto.setData(item);
				dto.setMessage(Constants.SUCESS_SMALL);
				dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			}else{
				dto.setMessage(Constants.ERROR_MESSAGE_VALUE);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		}catch(Exception e){
			logger.error("Error Occured while save chat items.");
			logger.error("Reason..:"+e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		}
		return dto;
	}

	@Override
	public ResponseDTO update(ChatItem item) {
		logger.info("update....");
		ResponseDTO dto =new ResponseDTO();
		boolean isInserted = false;
		try{
			logger.info("persisting....");
			com.ceva.db.model.ChatItem chatItem = chatDao.getById(item.getId());
			if(chatItem !=null){
				chatItem.setResponseTime(new Date());
				chatItem.setResponseMessage(item.getMessage());
				chatItem.setStatus(Status.P.toString());
				chatItem.setMakerId(item.getUserId());
				isInserted = chatDao.update(chatItem);
			}
			logger.info("persisted.:"+isInserted);
			if(isInserted){
				dto.setData(chatItem);
				dto.setMessage(Constants.SUCESS_SMALL);
				dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			}else{
				dto.setMessage(Constants.ERROR_MESSAGE_VALUE);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		}catch(Exception e){
			logger.error("Error Occured while update chat items.");
			logger.error("Reason..:"+e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		}
		return dto;
	}


	private com.ceva.db.model.ChatItem convert(ChatItem item) {
		com.ceva.db.model.ChatItem chatItem = null;
		try{
			chatItem = new com.ceva.db.model.ChatItem();
			chatItem.setChannel(item.getChannel());
			chatItem.setUserId(item.getUserId());
			chatItem.setId(item.getId());
			chatItem.setMessage(item.getMessage());
			chatItem.setStatus(Status.N.toString());
			chatItem.setCreationDate(item.getDateTime());
		}catch(Exception e){
			logger.error("Error while converting object.");
		}
		return chatItem;
	}

}

