package com.ceva.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.BalanceEnquirey;
import com.ceva.bank.common.dao.AdvertsDao;
import com.ceva.bank.utils.Status;
import com.ceva.db.model.Adverts;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.AdvertsService;
import com.nbk.util.Constants;

@Service("advertsService")
public class AdvertsServiceImpl implements AdvertsService {
	private Logger log = Logger.getLogger(AdvertsService.class);

	@Autowired
	AdvertsDao advertsDao;


	@Override
	public ResponseDTO listAdverts(BalanceEnquirey enquirey) {
		log.info("loading adds for user.:"+enquirey.getUserId());
		List<Adverts> adds =null;
		ResponseDTO dto = new ResponseDTO();
		try{
			log.info("=======================loading adds=======================");
			adds = advertsDao.listAdverts(enquirey);
			log.info("=======================loading adds=======================");
			//updateStatus(adds);
			log.info("loading adds for user.:"+enquirey.getUserId());
			dto.setMessage(Constants.SUCESS_SMALL);
			dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			dto.setData(adds);
		}catch(Exception e){
			log.error("loading adds for user.:");
			log.error("Reason.:"+e.getLocalizedMessage());
			e.printStackTrace();
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		}
		return dto;
	}

	private void updateStatus(List<Adverts> adds) {
		try{
			for(Adverts add : adds){
				add.setStatus(Status.D.toString());
				advertsDao.update(add);
			}
		}catch(Exception e){
			log.error("updating adds for user.:");
			log.error("Reason.:"+e.getLocalizedMessage());
		}
	}

	@Override
	public ResponseDTO update(Adverts add) {
		log.info("loading adds for user.:"+add.getId());
		ResponseDTO dto = new ResponseDTO();
		boolean updated =false;
		try{
			log.info("=======================loading adds=======================");
			Adverts adds = advertsDao.getById(add.getId());
			if(adds !=null){
				adds.setStatus(Status.D.toString());
				updated = advertsDao.update(adds);
			}
			log.info("=======================loading adds=======================");
			dto.setMessage(Constants.SUCESS_SMALL);
			dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			dto.setData(new HashMap<String, Object>().put("updated", updated));
		}catch(Exception e){
			log.error("loading adds for user.:");
			log.error("Reason.:"+e.getLocalizedMessage());
			e.printStackTrace();
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			dto.setData(new HashMap<String, Object>().put("updated", updated));
		}
		return dto;
	}

	@Override
	public Adverts getById(long id) {
		 return advertsDao.getById(id);
	}

}
