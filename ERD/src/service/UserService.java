package service;

import java.sql.Connection;
import java.util.List;

import common.JDBCTemplate;
import dao.DAO;
import dto.Member;

public class UserService {
    
    private DAO dao = new DAO();
    
    /** 아이디 중복 확인 서비스 */
    public int idCheck(String memId) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        int count = dao.idCheck(conn, memId);
        
        JDBCTemplate.close(conn);
        
        return count;
    }
    
    /** 회원 가입 서비스 */
    public int insertUser(Member member) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        int result = dao.insertUser(conn, member);
        
        if(result > 0) {
            JDBCTemplate.commit(conn);
        } else {
            JDBCTemplate.rollback(conn);
        }
        
        JDBCTemplate.close(conn);
        
        return result;
    }
    
    /** 로그인 서비스 */
    public String selectUser(String memberId, String memberPw) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        String memName = dao.selectUser(conn, memberId, memberPw);
        
        JDBCTemplate.close(conn);
        
        return memName;
    }
    
    /** 전체 회원 조회 서비스 */
    public List<Member> selectAll() throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        List<Member> memberList = dao.selectAll(conn);
        
        JDBCTemplate.close(conn);
        
        return memberList;
    }
    
    /** 이름으로 회원 검색 서비스 */
    public List<Member> selectByName(String name) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        List<Member> memberList = dao.selectByName(conn, name);
        
        JDBCTemplate.close(conn);
        
        return memberList;
    }
    
    /** 회원 이름 수정 서비스 */
    public int updateName(String userName, String memId) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        int result = dao.updateName(conn, userName, memId);
        
        if(result > 0) {
            JDBCTemplate.commit(conn);
        } else {
            JDBCTemplate.rollback(conn);
        }
        
        JDBCTemplate.close(conn);
        
        return result;
    }
    
    /** 회원 삭제 서비스 */
    public int deleteUser(String memId) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        int result = dao.deleteUser(conn, memId);
        
        if(result > 0) {
            JDBCTemplate.commit(conn);
        } else {
            JDBCTemplate.rollback(conn);
        }
        
        JDBCTemplate.close(conn);
        
        return result;
    }
}