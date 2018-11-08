package com.star.dubbo.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtil
{
	private static Logger log = LoggerFactory.getLogger(BeanUtil.class);
	private static List<Class> myClassName;

	public static List<Class> getClasses(String packageName)
			throws IOException
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace(".", "/");
		log.info("reflect Path:" + path);
		Enumeration<URL> resources = classLoader.getResources(path);
		List<Class> classes = new ArrayList();
		List<File> dirs = new ArrayList();
		URL urlResource;
		while (resources.hasMoreElements())
		{
			urlResource = (URL)resources.nextElement();
			String type = urlResource.getProtocol();
			log.info("urlResource type:" + type);
			if (type.equals("file")) {
				dirs.add(new File(urlResource.getFile()));
			} else if (type.equals("jar")) {
				classes.addAll(getClassNameByJar(urlResource.getPath()));
			}
		}
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		log.info("classSize:" + classes.size());
		return classes;
	}

	private static List<Class> findClasses(File directory, String packageName)
	{
		List<Class> classes = new ArrayList();
		if (!directory.isDirectory()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory())
			{
				assert (!file.getName().contains("."));
				log.info(packageName + "." + file.getName());
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			}
			else if (file.getName().endsWith(".class"))
			{
				String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
				try
				{
					classes.add(Class.forName(className));
				}
				catch (ClassNotFoundException e)
				{
					log.error("Class.forName '" + className + "' ClassNotFoundException:", e);
				}
				catch (ExceptionInInitializerError e)
				{
					log.error("Class.forName '" + className + "' ExceptionInInitializerError:", e);
				}
				catch (Throwable e)
				{
					log.error("Class.forName '" + className + "' Throwable:", e);
				}
			}
		}
		return classes;
	}

	public static List<Class> getClassNameByJar(String jarPath)
	{
		myClassName = new ArrayList();
		String[] jarInfo = jarPath.split("!");
		String jarFilePath = null;
		try
		{
			jarFilePath = URLDecoder.decode(jarInfo[0].substring(jarInfo[0].indexOf("/")), "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			log.error("扫描jar包中class名编码不匹配异常:", e);
		}
		String packagePath = jarInfo[1].substring(1);
		JarFile jarFile = null;
		try
		{
			jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements())
			{
				JarEntry jarEntry = (JarEntry)entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					if (entryName.startsWith(packagePath))
					{
						entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
						try
						{
							myClassName.add(Class.forName(entryName));
						}
						catch (ClassNotFoundException e)
						{
							log.error("Class.forName '" + entryName + "' ClassNotFoundException:", e);
						}
						catch (ExceptionInInitializerError e)
						{
							log.error("Class.forName '" + entryName + "' ExceptionInInitializerError:", e);
						}
						catch (Throwable e)
						{
							log.error("Class.forName '" + entryName + "' Throwable:", e);
						}
					}
				}
			}
			return myClassName;
		}
		catch (Exception e)
		{
			log.error("扫描jar包中class名异常:", e);
		}
		finally
		{
			if (jarFile != null) {
				try
				{
					jarFile.close();
				}
				catch (IOException e)
				{
					log.error("扫描jar包中class名关闭jar文件异常:", e);
				}
			}
		}
		return null;
	}
}
