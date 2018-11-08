package com.star.dubbo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static Properties conf;
	private static Properties properties = null;
	private static Properties prt = null;

	public PropertiesUtil() {
	}

	public static String getValue(String key) {
		try {
			if (null == properties) {
				properties = new Properties();
				properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("dubbo.properties"));
			}

			return properties.get(key).toString();
		} catch (Throwable var2) {
			logger.error("failed to load {} from application.properties {}", key, var2.getMessage());
			return null;
		}
	}

	public static String getConfigValue(String key) {
		try {
			if (null == prt) {
				prt = new Properties();

				try {
					prt.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties"));
				} catch (Exception var3) {
					logger.warn("load application.properties failed {}", var3.getMessage());
				}
			}

			Object obj = prt.get(key);
			if (null != conf) {
				obj = conf.get(key);
				if (null != obj) {
					logger.info("key {} uses conf value {}", key, obj);
				}
			}

			if (null != obj) {
				return obj.toString();
			}

			if ("dubbo.timeout,dubbo.consumer.timeout,dubbo.protocol.threads,dubbo.validation,dubbo.monitor,hessian.protocol.port".indexOf(key) >= 0) {
				String value = getValue(key);
				logger.info("key {} uses default value {}", key, value);
				return value;
			}

			logger.warn("failed to load {} from application.properties or conf", key);
		} catch (Throwable var4) {
			logger.warn("load application.properties failed {}", var4.getMessage());
		}

		return "28880";
	}

	public static Properties getConf() {
		return conf;
	}

	public static void setConf(Properties conf) {
		conf = conf;
	}
}
