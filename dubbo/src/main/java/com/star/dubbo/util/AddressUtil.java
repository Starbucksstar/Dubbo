package com.star.dubbo.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressUtil {
	private static Logger log = LoggerFactory.getLogger(AddressUtil.class);

	public AddressUtil() {
	}

	public static String getLocalIP() {
		StringBuilder ifconfig = new StringBuilder();

		try {
			Enumeration en = NetworkInterface.getNetworkInterfaces();

			while(true) {
				while(en.hasMoreElements()) {
					NetworkInterface intf = (NetworkInterface)en.nextElement();
					Enumeration enumIpAddr = intf.getInetAddresses();

					while(enumIpAddr.hasMoreElements()) {
						InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
							ifconfig.append(inetAddress.getHostAddress());
							break;
						}
					}
				}

				return ifconfig.toString();
			}
		} catch (Exception var5) {
			log.error("getLocalIP exception", var5);
			return ifconfig.toString();
		}
	}
}
