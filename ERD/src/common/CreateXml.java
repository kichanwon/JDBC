package common;

import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

public class CreateXml {
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
