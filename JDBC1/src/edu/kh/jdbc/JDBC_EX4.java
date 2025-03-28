package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBC_EX4 {
	
	public static void main(String[] args) {
	
//		부서 검색하여 사번,이름,부서,직급명 / 직급코드 오름차순정렬
		
			Connection conn = null;	// DB 연결정보 저장 객체
			Statement stmt = null;	// SQL 수행, 결과 반환용 객체
			ResultSet rs = null;	// SELECT 수행 결과 저장 객체
			
			Scanner sc = null;		// 키보드 입력용 객체

			try {
				
				Class.forName("oracle.jdbc.driver.OracleDriver");
			
				String url = "jdbc:oracle:thin:@localhost:1521:XE";
				String userName = "kh";
				String password = "kh1234";
				
				conn = DriverManager.getConnection(
						url,
						userName,
						password);
				
				sc = new Scanner(System.in);
				
				System.out.print("부서명 : ");
				String deptname = sc.nextLine();
				String content = String.format("'%s'",deptname);
				
				String sql = """
						SELECT e.EMP_ID, e.EMP_NAME , d.DEPT_TITLE , j.JOB_NAME
						FROM EMPLOYEE e
						JOIN DEPARTMENT d ON e.DEPT_CODE = d.DEPT_ID
						JOIN JOB j USING (JOB_CODE)
						WHERE d.DEPT_TITLE = 
						""" +content+ " ORDER BY JOB_CODE"
						;
				
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

//				사용 시 do-while 해줘야함 
//				if(!rs.next()) {		// <- 이미 rs.next 실행 해서
//					System.out.println("no match dept found!");
//					return;
//				}
				
				boolean flag = true;

				while(rs.next()) {	// rs.next() <- 테이블이 반환되어야 실행됨 
					flag=false;
					
					String empId = rs.getString("EMP_ID");
					String empName = rs.getString("EMP_NAME");
					String deptTitle = rs.getString("DEPT_TITLE");
					String JobName = rs.getString("JOB_NAME");
					
					System.out.printf(
							"사번: %s / 이름: %s / 부서명: %s / 직급: %s\r\n",
							empId,empName,deptTitle,JobName
							);
				}
				if(flag) System.out.println("일치하는 부서가 없습니다");
				
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


