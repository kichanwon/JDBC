package common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	// 필드
		private static Connection conn = null;
		// -> static 메서드에서 사용 가능한 필드로 static 필드 선언

		// 메서드
		/**
		 * 호출 시 Connection 객체를 생성해서 반환하는 메서드 + AutoCommit 끄기
		 * 
		 * @return conn
		 */
		public static Connection getConnection() {

			try {

				// 이전에 참조하던 Connection 객체가 존재하고
				// 아직 close 된 상태가 아니라면
				// 새로 만들지 않고 기존 Connection 반환
				if (conn != null && !conn.isClosed()) {
					return conn;
				}

				// 1. Properties 객체 생성
				// Properties.storeToXML() -> xml 파일 만들기
				// Properties.loadFromXML() -> xml 파일 읽어오기
				Properties prop = new Properties();

				// 2. Properties 메서드를 이용해서 driver.xml 파일 내용을 읽어오기
				String filePath = "driver.xml";
				// 프로젝트 폴더 바로 아래 driver.xml 파일 경로

				prop.loadFromXML(new FileInputStream(filePath));
				// prop에 저장된 값(driver.xml 에서 읽어온 값)을 이용해서
				// Connection 객체 생성

				// prop.getPropertry("key") : key가 일치하는 value 를 반환
				Class.forName(prop.getProperty("driver"));
				String url = prop.getProperty("url");
				String userName = prop.getProperty("userName");
				String password = prop.getProperty("password");

				conn = DriverManager.getConnection(url, userName, password);

				// 만들어진 Connection 에서 AutoCommit 끄기
				conn.setAutoCommit(false);

			} catch (Exception e) {
				System.out.println("커넥션 생성 중 예외 발생..");
				e.printStackTrace();
			}
			return conn;
		}

		/**
		 * 전달 받은 커넥션에서 수행한 SQL을 COMMIT 하는 메서드
		 * 
		 * @param conn
		 */
		public static void commit(Connection conn) {

			try {
				if (conn != null && !conn.isClosed())
					conn.commit();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/**
		 * 전달 받은 커넥션에서 수행한 SQL을 ROLLBACK 하는 메서드
		 * 
		 * @param conn
		 */
		public static void rollback(Connection conn) {

			try {
				if (conn != null && !conn.isClosed())
					conn.rollback();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// -------------------------------------------

		/**
		 * 전달 받은 커넥션을 close(자원 반환)하는 메서드
		 * 
		 * @param conn
		 */
		public static void close(Connection conn) {

			try {
				if (conn != null && !conn.isClosed())
					conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 전달 받은 Statement + PreparedStatment 둘 다 close() + 다형성 업캐스팅 적용 ->
		 * PreparedStatement는 Statement 자식
		 * 
		 * @param stmt
		 */
		public static void close(Statement stmt) {

			try {
				if (stmt != null && !stmt.isClosed())
					stmt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/**
		 * 전달 받은 ResultSet을 close() 하는 메서드
		 * 
		 * @param rs
		 */
		public static void close(ResultSet rs) {
			try {
				if (rs != null && !rs.isClosed())
					rs.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
