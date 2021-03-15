package com.ceva.bank.common.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.OtpDao;
import com.ceva.db.model.OtpBean;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.Constants;

@Repository("otpDao")
public class OtpDaoImpl implements OtpDao {
	Logger log = Logger.getLogger(OtpDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ResponseDTO save(OtpBean bean) {
		log.info("Persisting OTP into database.START");
		ResponseDTO dto= new ResponseDTO();
		Session session=null;
		try{
			//invalidatePrevious(bean.getMobileNo());

			session = sessionFactory.openSession();

			//session = sessionFactory.openSession();
			session.beginTransaction();
			log.info("invalidating previous OTP is start..:");
			Criteria criteria = session.createCriteria(OtpBean.class);
			criteria.add(Restrictions.eq("mobileNo", bean.getMobileNo()));
			criteria.add(Restrictions.eq("status", "N"));
			List<OtpBean> validotps =  criteria.list();
			for(OtpBean otpBean: validotps){
				otpBean.setStatus("X");
				session.saveOrUpdate(otpBean);
				log.info("invalidating otp for..:"+otpBean.getId());
			}
			log.info("invalidating previous is done..:");
			Long id = (Long) session.save(bean);
			log.info("generated id..:"+id);
			if (!session.getTransaction().wasCommitted()){
				session.getTransaction().commit();
			}
			//session.flush();
			dto.setMessage(Constants.SUCESS_CAPS);
			dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			dto.setData(id);
		}catch(Exception e){
			e.printStackTrace();
			log.info("Error while Saving otp..:"+e.getLocalizedMessage());
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		log.info("Persisting into database END.");
		return dto;
	}

	private void invalidatePrevious(String mobileNo) {
		log.info("Invalidating old otp for the mobile.:"+mobileNo);
		Session session = null;
		try{
			try{
				session = sessionFactory.getCurrentSession();
			}catch(Exception e){
				session = sessionFactory.openSession();
				log.info("getting synchronized session..:"+e.getLocalizedMessage());
			}
			Criteria criteria = session.createCriteria(OtpBean.class);
			criteria.add(Restrictions.eq("mobileNo", mobileNo));
			criteria.add(Restrictions.eq("status", "N"));
			List<OtpBean> validotps =  criteria.list();
			for(OtpBean bean: validotps){
				bean.setStatus("X");
			}
			session.flush();
		}catch(Exception e){
			e.printStackTrace();
			log.info("Error while invalidatePrevious otp..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("Invalidating old otp for the mobile.:END");
	}

	@Override
	public ResponseDTO getOtp(OtpBean bean) {
		return null;
	}

	@Override
	public ResponseDTO updateOtp(OtpBean bean) {
		ResponseDTO responseDTO = new ResponseDTO();
		log.info("updating otp.");
		try{

		}catch(Exception e){

		}
		return responseDTO;
	}
}
