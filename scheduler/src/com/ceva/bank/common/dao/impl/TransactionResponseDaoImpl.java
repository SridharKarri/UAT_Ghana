package com.ceva.bank.common.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.TransactionResponseDao;
import com.ceva.db.model.TransactionResponse;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.Constants;

@Repository("transactionResponseDao")
public class TransactionResponseDaoImpl implements TransactionResponseDao {

	Logger log = Logger.getLogger(TransactionResponseDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public TransactionResponse getById(String id, String code) {
		log.info("Getting Description for error code..:"+id+" with channel code..:"+code);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(TransactionResponse.class);
			criteria.add(Restrictions.eq("posCode", id));
			criteria.add(Restrictions.eqOrIsNull("bankId", code));
			return (TransactionResponse) criteria.uniqueResult();
		} catch (Exception e) {
			log.error("Error while getting Description for response for description."+e.getLocalizedMessage());
			return null;
		}finally{
			session.clear();
			session.close();
		}
	}

	@Override
	public ResponseDTO generateResponseForError(String id, String code) {
		log.info("generateResponseForError..:START");
		TransactionResponse transactionResponse = null;
		ResponseDTO dto = new ResponseDTO();
		try{
			transactionResponse = getById(id, code);
			if(transactionResponse !=null){
				dto.setMessage(transactionResponse.getFullDescription());
			}else{
				dto.setMessage("No Description found for the error code :"+id);
			}
			dto.setResponseCode(Constants.ERROR_CODE_NUMBER);
			dto.setData(transactionResponse);
		}catch(Exception e){
			log.error("Error while getting Description for response for description."+e.getLocalizedMessage());
			e.printStackTrace();
		}
		log.info("generateResponseForError..:END");
		return dto;
	}

}
