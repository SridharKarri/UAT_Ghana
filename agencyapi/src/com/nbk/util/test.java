package com.nbk.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class test {
	public static void main1(String[] args) {

	      // create 2 BigDecimal objects
	      BigDecimal appVersion, dbAppVersion;

	      appVersion = new BigDecimal("1.36");
	      dbAppVersion = new BigDecimal("1.40");

	      //create int object
	      int res;

	      res = appVersion.compareTo(dbAppVersion); // compare bg1 with bg2

	      String str1 = "Both values are equal ";
	      String str2 = "appVersion is greater ";
	      String str3 = "dbAppVersion is greater";

	      if( res == 0 )
	         System.out.println( str1 );
	      else if( res == 1 )
	         System.out.println( str2 );
	      else if( res == -1 )
	         System.out.println( str3 );
	   }
	public static void main(String[] args) {
		System.out.println(System.nanoTime());
		System.out.println((System.nanoTime()+"").length());
		System.out.println("1053943320660931210".length());
		/*long time = new Date().getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		System.out.println(hours);*/
	}
}
