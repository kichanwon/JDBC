package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JDBC_EX7 {
	public static void main(String[] args) {

//EMPLOYEE	테이블에서
//사번, 이름, 성별, 급여, 직급명, 부서명을 조회
//단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것
		
//- 조건 1 : 성별 (M, F)
//- 조건 2 : 급여 범위
//- 조건 3 : 급여 오름차순/내림차순
		
//[실행화면]
//조회할 성별(M/F) : F
//급여 범위(최소, 최대 순서로 작성) :
//3000000
//4000000
//급여 정렬(1.ASC, 2.DESC) : 2
		
//사번 | 이름   | 성별 | 급여    | 직급명 | 부서명
//--------------------------------------------------------
//218  | 이오리 | F    | 3890000 | 사원   | 없음
//203  | 송은희 | F    | 3800000 | 차장   | 해외영업2부
//212  | 장쯔위 | F    | 3550000 | 대리   | 기술지원부
//222  | 이태림 | F    | 3436240 | 대리   | 기술지원부
//207  | 하이유 | F    | 3200000 | 과장   | 해외영업1부
//210  | 윤은해 | F    | 3000000 | 사원   | 해외영업1부
		Connection conn = null; // DB 연결정보 저장 객체
		PreparedStatement psmt = null; // SQL 수행, 결과 반환용 객체
		ResultSet rs = null;	// SELECT 수행 결과 저장 객체

		Scanner sc = null; // 키보드 입력용 객체

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "kh";
			String password = "kh1234";

			conn = DriverManager.getConnection(url, userName, password);

			sc = new Scanner(System.in);

			System.out.print("조회할 성별(M/F) : ");	//toUpperCase
			String sex = sc.next().toUpperCase();
			System.out.println("급여범위");	//min,max
			int min = sc.nextInt();
			int max = sc.nextInt();
			sc.nextLine();
			System.out.print("급여 정렬(1.ASC, 2.DESC) : ");
			int sort = sc.nextInt();
			
			String sql = """
					SELECT 
					EMP_ID AS "사번", 
					e.EMP_NAME AS "이름", 
					decode(substr(EMP_NO,8,1),1,'M',2,'F') AS "성별",
					e.SALARY AS "급여",
					j.JOB_NAME AS "직급명", 
					NVL(d.DEPT_TITLE,'없음') AS "부서명"
				FROM EMPLOYEE e
				LEFT JOIN DEPARTMENT d ON e.DEPT_CODE = d.DEPT_ID
				JOIN JOB j USING (JOB_CODE)
				WHERE
					decode(substr(EMP_NO,8,1),1,'M',2,'F') = ?
					AND e.SALARY BETWEEN ? AND ?
				ORDER BY e.SALARY 
				""";
			
			
			if(sort==1) {
				sql += "ASC";
			}else if (sort==2){
				sql += "DESC";
			}else {
				System.out.println("Wrong Sort Order");
				return;
			}
			
			if(min>max) {
				int temp;
				temp = min;
				min = max;
				max = temp;
			}
			

			psmt = conn.prepareStatement(sql);
			psmt.setString(1, sex);
			psmt.setInt(2, min);
			psmt.setInt(3, max);
			rs = psmt.executeQuery();
			conn.setAutoCommit(false);
			
			boolean flag = true;
			
			System.out.println("사번\t이름\t성별\t급여\t직급\t부서명");
			System.out.println("====================================================");
			
			while(rs.next()) {	// rs.next() <- 테이블이 반환되어야 실행됨 
				flag=false;
				
				String empId = rs.getString("사번");
				String empName = rs.getString("이름");
				String empSex = rs.getString("성별");
				int salary = rs.getInt("급여");
				String jobName = rs.getString("직급명");
				String deptTitle = rs.getString("부서명");

				System.out.printf(
						"%s\t%s\t%s\t%s\t%s\t%s \r\n",
						empId,empName,empSex,salary,jobName,deptTitle
						);
			}
			if(flag) System.out.println("조회 결과가 없습니다");
			
			
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
