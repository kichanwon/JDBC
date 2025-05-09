package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @java.sql.PreparedStatement
 * 
 * SQL 중간에 ? (placeholder)를 짝성하여 ?자리에 java 값을 자동 대입
 * PreparedStatement = Statement 의 자식
 */
public class JDBC_EX6 {

	public static void main(String[] args) {

		Connection conn = null; // DB 연결정보 저장 객체
		PreparedStatement psmt = null; // SQL 수행, 결과 반환용 객체

		Scanner sc = null; // 키보드 입력용 객체

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "kh";
			String password = "kh1234";

			conn = DriverManager.getConnection(url, userName, password);

			sc = new Scanner(System.in);

			System.out.print("ID : ");
			String id = sc.nextLine();
			System.out.print("PW : ");
			String pw = sc.nextLine();
			System.out.print("Name : ");
			String name = sc.nextLine();

			String sql = """
					UPDATE TB_USER
					SET USER_NAME = ?
					WHERE USER_ID = ? AND USER_PW =?
					""";

			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, name);
			psmt.setString(2, id);
			psmt.setString(3, pw);
			
//			need to off auto-commit
//			->to handle transaction
			conn.setAutoCommit(false);
			
			int res = psmt.executeUpdate();
			
			if(res>0) {
				System.out.println("수정 성공!");
				conn.commit();
			}else {
				System.out.println("아이디 또는 비밀번호 불일치");
				conn.rollback();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
