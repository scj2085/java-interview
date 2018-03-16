package com.java.interview.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DynamicConfig {

	/**
	 * 动态获取配置文件值(不重启获取修改后的配置文件值) 效率有一定影响
	 * 
	 * @param propertisFile
	 *            配置文件名称（ class目录）
	 * @param property
	 *            需要获取的配置项的名称
	 * @return
	 */
	@RequestMapping(value = "/loadProperties", method = RequestMethod.GET)
	public void loadProperties(String propertisFile, String value) {
		Map<String, String> properties = null;

		// 获取执行根目录
		String path = Thread.currentThread().getContextClassLoader().getResource(propertisFile).getPath();
		Properties props = new Properties();
		InputStream is = null;
		try {
			// 读取配置文件
			is = new FileInputStream(new File(path));
			properties = new HashMap<String, String>();
			// 用文件流的方式加载配置文件
			props.load(is);

			// 获取
			props.getProperty("key");
			// 设置
			props.setProperty("key", value);
			// 写到配置文件
			FileOutputStream outputStream = new FileOutputStream(path);
			props.store(outputStream, "update message");
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {
			try {
				if (null != is)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Properties ht = new Properties();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String strName, strNumber;
		FileInputStream fin = null;
		boolean changed = false;

		// Try to open phonebook.dat file.
		try {
			fin = new FileInputStream("phonebook.dat");
		} catch (FileNotFoundException e) {
			// ignore missing file
		}

		/*
		 * If phonebook file already exists,load exsiting telephone numbers.
		 */
		try {
			if (fin != null) {
				ht.load(fin);
				fin.close();
			}
		} catch (IOException e) {
			System.out.println("Error reading file!");
		}

		// Let user enter new names and numbers.
		do {
			System.out.println("Enter new name" + " ('quit' to stop): ");
			strName = br.readLine();
			if (strName.equals("quit"))
				continue;

			System.out.println("Enter number:");
			strNumber = br.readLine();

			ht.put(strName, strNumber);
			changed = true;
		} while (!strName.equals("quit"));

		// If phonebook data has changed,save it.
		if (changed) {
			FileOutputStream fOut = new FileOutputStream("phonebook.dat");
			ht.store(fOut, "Telephone Book");
			fOut.close();
		}

		// Look up numbers given a name.
		do {
			System.out.println("Enter name to find ('quit' to stop): ");
			strName = br.readLine();
			if (strName.equals("quit"))
				continue;
			strNumber = (String) ht.get(strName);
			System.out.println(strNumber);
		} while (!strName.equals("quit"));
	}
}
