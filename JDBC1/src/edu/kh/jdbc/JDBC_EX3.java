package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBC_EX3 {
	
	public static void main(String[] args) {
	
//		최소급여 이상
//		최대급여 이하
//		급여 내림차순 조희
		
			Connection conn = null;	// DB 연결정보 저장 객체
			Statement stmt = null;	// SQL 수행, 결과 반환용 객체
			ResultSet rs = null;	// SELECT 수행 결과 저장 객체
			
			Scanner sc = null;		// 키보드 입력용 객체

			try {
				
				Class.forName("oracle.jdbc.driver.OracleDriver");
			
				String type = "jdbc:oracle:thin:";
				String host = "@localhost";
				String port = ":1521";
				String dbName = ":XE";
				String userName = "kh";
				String password = "kh1234";
				
				conn = DriverManager.getConnection(
						type+host+port+dbName,
						userName,
						password);
				
				sc = new Scanner(System.in);
				
				System.out.print("최소급여 : ");
				int minSal = sc.nextInt();
				System.out.print("최대급여 : ");
				int maxSal = sc.nextInt();
				
/*				
				String sql = "SELECT e.EMP_ID, e.EMP_NAME, e.SALARY\r\n"
						+ "FROM EMPLOYEE e\r\n"
						+ "WHERE SALARY BETWEEN "+minSal+" AND "+maxSal+"\r\n"
//						+ "WHERE SALARY > "+minSal+"\r\n"
//						+ "AND SALARY <"+maxSal+"\r\n"
						+ "ORDER BY SALARY";
*/			
				
				String sql = """
						SELECT e.EMP_ID, e.EMP_NAME, e.SALARY
						FROM EMPLOYEE e
						WHERE SALARY BETWEEN 
						""" +minSal+" AND " +maxSal+ " ORDER BY SALARY"
						;
				
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				while(rs.next()) {
					String empId = rs.getString("EMP_ID");
					String empName = rs.getString("EMP_NAME");
					int salary = rs.getInt("SALARY");
					
					System.out.printf(
							"사번: %s / 이름: %s / 급여: %d\r\n",
							empId,empName,salary
							);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(rs!=null) rs.close();
					if(stmt!=null) stmt.close();
					if(conn!=null) conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
}


