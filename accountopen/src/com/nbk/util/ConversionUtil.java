package com.nbk.util;

import java.math.BigDecimal;

public class ConversionUtil {

	public static BigDecimal convert(BigDecimal amount, String currency, String sourceCurrency) {
		BigDecimal convertedAmount=null;
		if("USD".equals(currency)&&"CDF".equals(currency)){
			convertedAmount = convertUsdToCdf(amount);
		}else if("EURO".equals(currency)&&"CDF".equals(currency)){
			
		}else if("USD".equals(currency)&&"CDF".equals(currency)){
			
		}
		
		return convertedAmount;
	}

	private static BigDecimal convertUsdToCdf(BigDecimal amount) {
		return null;
	}

}
