package com.ceva.bank.common.dao.impl;

import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.QuicktellerDao;

@Repository("quicktellerDao")
public class QuicktellerDaoImpl implements QuicktellerDao {/*

	Logger log = Logger.getLogger(QuicktellerDao.class);
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public boolean save(Category category) {
		log.info("saving billers...!");
		boolean updated = false;
		BillerCategory billerCategory = null;
		Session session = null;
		Criteria criteria = null;
		try {
			billerCategory = new BillerCategory();
			billerCategory.setId(category.getId());
			billerCategory.setName(category.getName());
			billerCategory.setDescription(category.getDescription());
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.merge(billerCategory);
			for (com.ceva.bank.qt.beans.Billers billers : category.getBiller()) {
				log.info("saving biller with billerId..:"+billers.getId());
				criteria = session.createCriteria(Billers.class);
				criteria.add(Restrictions.eq("billerId", billers.getId()));
				Billers dbbillers = (Billers) criteria.uniqueResult();
				if (dbbillers == null) {
					dbbillers = new Billers();
				}
				dbbillers.setSid(billerCategory.getId());
				dbbillers.setId(Long.parseLong(billers.getId()));
				dbbillers.setBillerId(billers.getId());
				dbbillers.setBillerName(billers.getName());
				dbbillers.setChannel(1);
				dbbillers.setBillerDesc(billers.getNarration());
				dbbillers.setStatus("A");
				dbbillers.setSupportEmail(billers.getSupportEmail());
				dbbillers.setAddress(billers.getCustomerField1());
				session.persist(dbbillers);
				session.flush();
				updated = true;
				// session.getTransaction().commit();
			}
			session.getTransaction().commit();
			session.flush();
		} catch (Exception e) {
			log.error("error while saving transaction.");
			log.error("Reason..:" + e.getLocalizedMessage());
			e.printStackTrace();
			updated = false;
		} finally {
			session.close();
		}
		return updated;
	}

	@Override
	public boolean saveBillpaymentItems(List<BillerPackages> packages) {
		log.info("Saving billpayment items into db..");
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = null;
		try{
			for(BillerPackages packag : packages){
				try{
				criteria=session.createCriteria(BillerPackages.class);
				criteria.add(Restrictions.eq("billerId", packag.getBillerId()));
				criteria.add(Restrictions.eq("packId", packag.getPackId()));
				BillerPackages dbPack = (BillerPackages) criteria.uniqueResult();
				if(dbPack == null){
					dbPack = new BillerPackages();
				}
				if(dbPack != null){
					dbPack.setBillerId(packag.getBillerId());
					dbPack.setCharge(packag.getCharge());
					//dbPack.setDisplayName(packag.getDisplayName());
					dbPack.setPackDescription(packag.getPackDescription());
					dbPack.setPackId(packag.getPackId());
					dbPack.setPaymentCode(packag.getPaymentCode());
					dbPack.setPackName(packag.getPackName());
					dbPack.setStatus(packag.getStatus());
					session.saveOrUpdate(dbPack);
					session.flush();
				}
				//session.evict(dbPack);
				}catch(Exception e){
					log.error("Error while saving payment item..:"+packag.getPaymentCode());
					log.error("reason..:"+e.getLocalizedMessage());
				}finally{
					packag = null;
					criteria = null;
				}
			}
			session.getTransaction().commit();
			session.flush();
		}catch(Exception e){
			log.error("Error while saving payment item..:"+e.getLocalizedMessage());
		}finally{
			session.close();
			session = null;
			packages=null;
		}
		return false;
	}

*/}
