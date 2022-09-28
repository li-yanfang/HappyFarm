package com.zqsoft.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

//读取信息的操作
public class FileUtils {
	
	/**
	 * 读取信息
	 * @param fileName
	 * @return
	 */
	public static List<String> readFile(String fileName) {
		// TODO Auto-generated method stub
		BufferedReader reader = null;
		List<String> allLine = new ArrayList<String>();
		File file = new File(fileName);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {

			reader = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str=reader.readLine())!=null) {
				allLine.add(str);
				
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if(reader!=null) {
				try {
					reader.close();
				}
				catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return allLine;
	}
	
	
	/**
	 * 写入信息
	 */
	public static void writeFile(String fileName,String str) {
		BufferedWriter writer=null;
		try {
			writer= new BufferedWriter(new FileWriter(new File(fileName)));
			writer.write(str);	
			writer.flush();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException ex) {
					// TODO: handle exception
					ex.printStackTrace();
				}
			}
		}
	}
}
