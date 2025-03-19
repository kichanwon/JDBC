package common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class LoadXML {
	public static void main(String[] args) {

		// XML 파일 읽어오기 (Properties, FileInputStream)
				try {
					Properties prop = new Properties();

		// driver.xml 파일을 읽기위한 InputStream 객체 생성
					FileInputStream fis = new FileInputStream("driver.xml");

		// 연결된 driver.xml 파일에 있는 내용을 모두 읽어
		// Properties 객체에 K:V 형식으로 저장
					prop.loadFromXML(fis);

		// Property : 속성(데이터)
		// prop.getProperty("key") : key 가 일치하는 속성값을 얻어옴
					String driver = prop.getProperty("driver");
					String url = prop.getProperty("url");
					String userName = prop.getProperty("userName");
					String password = prop.getProperty("password");

					Class.forName(driver);
					Connection conn = DriverManager.getConnection(url, userName, password);

					System.out.println(conn);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
}
