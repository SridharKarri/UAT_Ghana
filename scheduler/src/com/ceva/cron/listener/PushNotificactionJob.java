package com.ceva.cron.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ceva.service.NotificationService;
import com.nbk.util.ConfigLoader;

import net.sf.json.JSONObject;

//@Component("pushNotificactionJob")
public class PushNotificactionJob {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	static String url = ConfigLoader.convertResourceBundleToMap("config").get("bank.push.url");
	static String notificationurl = ConfigLoader.convertResourceBundleToMap("config").get("bank.notification.url");
	static String notificationnamesape = ConfigLoader.convertResourceBundleToMap("config").get("bank.namespace");
	
	//@Scheduled(fixedRate=(2*60000))
	public void sendNotification() throws SQLException {
		System.out.println("sendNotification Process started..:");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Calendar cal = Calendar.getInstance();
		Connection connection = null;
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String current = sdf.format(cal.getTime());
		Session session = sessionFactory.openSession();
		SessionImpl sessionImpl = (SessionImpl) session;
        connection = sessionImpl.connection();
		try {
			System.out.println("Poller Start ...::::" + current);
			connection.setAutoCommit(false);
			String selectSQL = "select PN.MESSAGE AS MESSAGE, PN.SUBJECT AS SUBJECT, PN.USERID AS USERID, PN.ROWID AS RID, "
					+ "  PN.DEVICE_TOKEN AS TOKEN, PN.REF_NUM as REF_NUM, PN.MSG_DATE as MSG_DATE from PUSH_NOTIFICATION PN where "
					+ " PN.SEND_TYPE ='PUSH' and FETCH_STATUS='P' and to_date(PN.MSG_DATE,'dd/mm/yyyy')=to_date(SYSDATE,'dd/mm/yyyy')";

			preparedStatement = connection.prepareStatement(selectSQL);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				JSONObject object = new JSONObject();

				object.put("message", rs.getString("MESSAGE"));
				object.put("subject", rs.getString("SUBJECT"));
				object.put("username", rs.getString("USERID"));
				object.put("sdate", rs.getString("MSG_DATE"));
				object.put("ostype", "android");
				
				object.put("notificationurl", notificationurl);//if this tag is not sent url will be take from router push.roperties
				object.put("notificationnamesape", notificationnamesape);//if this tag is not sent url will be take from router push.roperties
				

				Boolean bool = prepareAndSend(object);
				String query = "update PUSH_NOTIFICATION  set FETCH_STATUS=? where rowid=?";
				preparedStatement = connection.prepareStatement(query);
				if (bool)
					preparedStatement.setString(1, "S");
				else
					preparedStatement.setString(1, "F");

				preparedStatement.setString(2, rs.getString("RID"));
				result = preparedStatement.executeUpdate();
				if (result > 0) {
					connection.commit();
				} else {
				}
				System.out.println("Push Notification Process Completed:::::" + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public static boolean prepareAndSend(JSONObject object) {
		boolean bool = false;
		try {
			System.out.println("sending...:"+object);
			JSONObject response =  NotificationService.push(object);
			System.out.println("response"+response.toString());
			 bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

		return bool;
	}

}
