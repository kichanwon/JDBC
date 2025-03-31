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

		// 메서드
		/**
		 * 호출 시 Connection 객체를 생성해서 반환하는 메서드 + AutoCommit 끄기
		 * 
		 * @return conn
		 */
		public static Connection getConnection() {

			try {

				if (conn != null && !conn.isClosed()) {
					return conn;
				}

				Properties prop = new Properties();

				String filePath = "driver.xml";

				prop.loadFromXML(new FileInputStream(filePath));

				Class.forName(prop.getProperty("driver"));
				String url = prop.getProperty("url");
				String userName = prop.getProperty("userName");
				String password = prop.getProperty("password");

				conn = DriverManager.getConnection(url, userName, password);

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
