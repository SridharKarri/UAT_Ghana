package com.nbk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {

	public static String getAcOpenDate(String date){
    	try {
    		return new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(date));
    	} catch (ParseException e) {
    		e.printStackTrace();
    		return date;
    	}
    }	
	
	public static String getDateInString(Date date, String format){
    	try {
    		return new SimpleDateFormat(format).format(date);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }	
}
