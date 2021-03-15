package com.ceva.listener;

import java.util.HashMap;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	private int sessionCount = 0;

	public void sessionCreated(HttpSessionEvent event) {

		/*synchronized (this) {
			sessionCount++;
		}

		System.out.println("Session Created: " + event.getSession().getId());
		System.out.println("Total Sessions: " + sessionCount);*/
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		/*synchronized (this) {
			sessionCount--;
		}

	System.out.println("Session Destroyed: " + event.getSession().getId());
	*/
		try {

			HashMap hMap = (HashMap) event.getSession().getServletContext()
					.getAttribute("hMap");
			System.out.println("hMap::" + hMap);
			event.getSession()
					.getServletContext()
					.removeAttribute(
							(String) hMap.remove(event.getSession().getId()));
			System.out.println("hMap after removing::" + hMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Total Sessions: " + sessionCount);
	}
}