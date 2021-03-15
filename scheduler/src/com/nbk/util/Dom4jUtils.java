package com.nbk.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

public class Dom4jUtils implements Constants {

	private Logger logger = Logger.getLogger(Dom4jUtils.class);

	public Map<String, Object> data = null;

	public Map<String, Object> getTxnData(String typeOfFund, String searchXml) {
		return getProcessingData(typeOfFund, searchXml);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getProcessingData(String typeOfFund,
			String searchXml) {
		Map<String, Object> data = null;
		SAXReader reader = null;
		Document document = null;
		InputStream ins = null;

		List<Element> list = null;
		try {
			ins = Dom4jUtils.class.getResourceAsStream(RESOURCE + searchXml);
			reader = new SAXReader();
			document = reader.read(ins);

			list = (List<Element>) document
					.selectNodes("/financialtxndetails/txncode[@name='"
							+ typeOfFund + "']");

			logger.debug("The transaction code size is ::: " + list.size());
			if (list.size() == 1) {
				for (Iterator<Element> iter = list.listIterator(); iter
						.hasNext();) {
					Element ele = iter.next();
					data = new ConcurrentHashMap<String, Object>();
					for (Iterator<?> itr = ele.elementIterator(); itr.hasNext();) {
						DefaultElement de = (DefaultElement) itr.next();

						data.put(de.getName(), de.getText());
						de = null;
					}

					ele = null;
					break;
				}

			} else if (list.size() == 1) {
				logger.debug("The transaction code [" + typeOfFund
						+ "] has duplicate entries.... size is " + list.size());
			} else {
				logger.debug("The transaction code [" + typeOfFund
						+ "] is empty....");
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				reader = null;
			if (document != null)
				document = null;
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
				}
				ins = null;
			}
		}
		return data;
	}

	public static void main(String[] args) {
		/*System.out.println(new Dom4jUtils().getTxnData("FTWIOA",
				NHIF_TRAN_DET));*/
	}

}
