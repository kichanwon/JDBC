package edu.kh.jdbc.common;

import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

public class CreateXMLFile {
//		XML eXtendsible Markup Language
//		Key:Value 데이터 저장 형식 (String:String)
	
//		need IO Class to read write XML File
//		Properties (extend Map)
//			Key:Value (String:String)
//			efficient to read,write XML File
	
	public static void main(String[] args) {
		
		try {
			Scanner sc = new Scanner(System.in);
			
			Properties prop = new Properties();
			
			System.out.print("File name :");
			String fileName = sc.next();
			
			FileOutputStream fos = new FileOutputStream(fileName+".xml");
			
			prop.storeToXML(fos, fileName+".xml file");
			System.out.printf("%s.xml file created\n",fileName);
			
		} catch (Exception e) {
			System.out.println("Exception while create XML File");
			e.printStackTrace();
		}
		
	}
}
