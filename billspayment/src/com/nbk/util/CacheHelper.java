package com.nbk.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;

import com.ceva.cacheutils.HelloEhCache;

public class CacheHelper {

	private static Logger logger = Logger.getLogger(CacheHelper.class);

	private static CacheManager cacheManager = null;

	private static Cache cache = null;

	static {
		cacheManager = loadCacheManager();
	}

	private static CacheManager loadCacheManager() {

		CacheManager cahceManager = null;
		try {
			cahceManager = CacheManager.newInstance(HelloEhCache.class
					.getResourceAsStream("/resources/ehcache.xml"));
		} catch (Exception e) {

		}
		return cahceManager;
	}

	public static void createCache(String cacheName) {
		cache = cacheManager.getCache(cacheName);
	}

	public static void stop() {
		cacheManager.shutdown();
	}

	public static void main(String[] args) {

	}

}
