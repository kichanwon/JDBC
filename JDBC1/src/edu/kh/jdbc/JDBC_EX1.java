package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * JDBC - Java DataBase Connectivity
 * 
 * Connecting Java-DB API
 */
public class JDBC_EX1 {
	
	public static void main(String[] args) {
		
//		1. JDBC 객체 참조용 변수 선언
//			java.sql.Connection
//			-> 특정 DBMS와 연결하기 위한 정보를 저장한 객체
//			DB Server Address , PortNumber , DB Name , Account ID/PW
			Connection conn = null;
			
//			java.sql.Statement
//			1) SQL		Java -> DB
//			2) result	DB -> Java
			Statement stmt = null;
//			
//			java.sql.ResultSet
//			saving SELECT result
			ResultSet rs = null;
		
		
//		2. DriverManager 객체를 이용하여 Connection 객체 생성
//			java.sql.DriverManager
//			DB연결 정보와 JDBC Driver를 이용해서
//			Connection 객체 생성
			
			try {
//				2-1) Oracle JDBC Driver 객체를 Memory Load
				Class.forName("oracle.jdbc.driver.OracleDriver");	// need to catch ClassNotFoundException
//				Class.forName("PackageName + ClassName");
//				->해당 클래스를 읽어 메모리에 적재
//				->JVM이 프로그램 동작에 사용할 객체를 생성하는 구문
				
//				oracle.jdbc.driver.OracleDriver
//				Oracle DMS 연결시 필요한 코드가 담
//				ojdbs 라이브러리 파일 내에 존재
//				Oracle에서 만들어서 제공하는 Class
			
				
//				2-2) DB 연결 정보 작성
				String type = "jdbc:oracle:thin:";	// 드라이버 종류
				String host = "@localhost";			// server ip,domain
				String port = ":1521";				// port num
				String dbName = ":XE";				// dbms name (XE == eXpress Edition)

				String userName = "kh";				// user name
				String password = "kh1234";			// pw
				
				
//				2-3) DB연결 정보와 DriverManager 이용하여 Connection 객체 생성
				conn = DriverManager.getConnection(
						type+host+port+dbName,
						userName,
						password);
//				need to catch SQLException > DB 연결과 관련된 모든 예외의 최상위 부모
				
				
				
//				Connection 객체 테스트
				System.out.println(conn);
				
				
				
//		3.sql 작성
//				세미콜론 ; 작성 X
				
//				EMPLOYEE 테이블에서 사번,이름,부서코드, 직급코드, 급여, 입사일 조회
				String sql = "SELECT e.EMP_ID, e.EMP_NAME, e.DEPT_CODE, e.JOB_CODE , e.SALARY, e.HIRE_DATE\r\n"
						+ "FROM EMPLOYEE e";
				
				
//		4. Statement 객체 생성
				stmt = conn.createStatement();
				
//		5. Statement 객체를 이용하여 SQL 수행후 결과 반환
				rs = stmt.executeQuery(sql);
//				Result 객체 반환
				
//				Statement.exeuteUpdate(sql)
//				DML(INSERT, UPDATE, DELETE) 일 때 실행 메소드 -> int 반환
				
				
//		6. 결과 출력
//				Cursor 를 이용하여 1행씩 접근해 각 행에 작성된 Column 값 반환
//				ResultSet.next()다음 행으로 이동 후 이동한 행에 값의 유무에 따라 boolean 반환 
				while(rs.next()) {
//					ResultSet.getType(column name | order);
//					현재 행에서 지정된 컬럼의 값을 얻어와 지정된 자료형 형태로 값 반환 (type miss match exception)
					String empId = rs.getString("EMP_ID");
					String empName = rs.getString("EMP_NAME");
					String deptCode = rs.getString("DEPT_CODE");
					String jobCode = rs.getString("JOB_CODE");
					int salary = rs.getInt("SALARY");
					Date hireDate = rs.getDate("HIRE_DATE");
					
					System.out.printf(
							"사번: %s / 이름: %s / 부서코드: %s / 직급코드: %s / 급여: %d / 입사일: %s\r\n",
							empId,empName,deptCode,jobCode,salary,hireDate.toString()
							);
				}
				
				
			} catch (ClassNotFoundException e) {
				System.out.println("Can not found Class");
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
//		7.사용완료된 JDBC 객체 자원 반환 (close) (만들어진 역순으로 close 수행을 권장)
//				close X 시 DB와 Connection 이 남아있어서 다른 클라이언트가 추가적으로 연결되지 못하는 문제 발생 가능
//				-> DBMS 최대 Connection 개수 제한이 있기 때문!
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


