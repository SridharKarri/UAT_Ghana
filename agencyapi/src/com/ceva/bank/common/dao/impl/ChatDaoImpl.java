package com.ceva.bank.common.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.ChatDao;
import com.ceva.db.model.ChatItem;
import com.ceva.service.ChatService;

@Repository("chatDao")
public class ChatDaoImpl implements ChatDao {
	private Logger log = Logger.getLogger(ChatService.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<ChatItem> listAll(String userId) {
		log.info("listing all chats fro user.");
		List<ChatItem> chatItems = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ChatItem.class);
			criteria.add(Restrictions.eq("userId", userId));
			criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
					.add(Projections.property("message"), "message").add(Projections.property("status"), "status")
					.add(Projections.property("creationDate"), "creationDate")
					// .add(Projections.sqlProjection("to_date(creationDate,
					// '"+Constants.SHORT_DATE_PATTERN+"') as creationDate", new
					// String[] { "creationDate" }, new Type[] {
					// StandardBasicTypes.DATE }))
					/*
					 * .add(Projections.sqlProjection(
					 * "date(creationDate) as creationDate", "creationDate", new
					 * String[] { "creationDate" }, new Type[] {
					 * StandardBasicTypes.DATE }))
					 */
					.add(Projections.property("responseMessage"), "responseMessage")
					.add(Projections.property("responseTime"), "responseTime")
					// .add(Projections.sqlProjection("to_date(responseTime,
					// '"+Constants.SHORT_DATE__TIME_PATTERN+"') as
					// responseTime", new String[] { "responseTime" }, new
					// Type[] { StandardBasicTypes.DATE }))
					.add(Projections.property("makerId"), "makerId")).addOrder(Order.asc("creationDate"))
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			chatItems = criteria.list();
		} catch (Exception e) {
			log.error("Error Occured while getting chat items.");
			log.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			session = null;
		}
		return chatItems;
	}

	@Override
	public boolean save(ChatItem item) {
		boolean isUpdated = false;
		log.info("=============== saving chat item START===============");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.save(item);
			session.flush();
			isUpdated = true;
		} catch (Exception e) {
			isUpdated = false;
			log.error("error while saving chat item.:");
			log.error("Reason.:" + e.getLocalizedMessage());
		} finally {
			session.close();
			session = null;
		}
		log.info("=============== saving chat item END===============");
		return isUpdated;
	}

	@Override
	public boolean update(ChatItem item) {
		boolean isUpdated = false;
		log.info("=============== updating chat item START===============");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.merge(item);
			session.flush();
			isUpdated = true;
		} catch (Exception e) {
			isUpdated = false;
			log.error("error while updating chat item.:");
			log.error("Reason.:" + e.getLocalizedMessage());
		} finally {
			session.close();
			session = null;
		}
		log.info("=============== saving chat item END===============");
		return isUpdated;
	}

	@Override
	public ChatItem getById(long id) {
		ChatItem item = null;
		log.info("=============== updating chat item START===============");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			item = (ChatItem) session.get(ChatItem.class, id);
		} catch (Exception e) {
			log.error("error while updating chat item.:");
			log.error("Reason.:" + e.getLocalizedMessage());
		} finally {
			session.close();
			session = null;
		}
		log.info("=============== saving chat item END===============");
		return item;
	}

}
