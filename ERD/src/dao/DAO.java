package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import common.JDBCTemplate;
import dto.Member;
import dto.ToDo;

public class DAO {

    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    /** 아이디 중복 확인 DAO */
    public int idCheck(Connection conn, String memId) throws Exception {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM TB_MEMBER WHERE MEM_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }
        return count;
    }

    /** MEMBER 등록 DAO */
    public int insertUser(Connection conn, Member member) throws Exception {
        int result = 0;
        try {
            String sql = "INSERT INTO TB_MEMBER VALUES(?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMemId());
            pstmt.setString(2, member.getMemPw());
            pstmt.setString(3, member.getMemName());
            result = pstmt.executeUpdate();
        } finally {
            JDBCTemplate.close(pstmt);
        }
        return result;
    }

    /** 로그인 (ID, PW가 일치하는 회원의 MEM_NAME 조회) */
    public String selectUser(Connection conn, String memberId, String memberPw) throws Exception {
        String memName = null;
        try {
            String sql = "SELECT MEM_NAME FROM TB_MEMBER WHERE MEM_ID = ? AND MEM_PW = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.setString(2, memberPw);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                memName = rs.getString("MEM_NAME");
            }
        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }
        return memName;
    }

    /** 전체 회원 조회 DAO */
    public List<Member> selectAll(Connection conn) throws Exception {
        List<Member> memberList = new ArrayList<>();
        try {
            String sql = "SELECT MEM_ID, MEM_PW, MEM_NAME FROM TB_MEMBER";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Member member = new Member(
                    rs.getString("MEM_ID"),
                    rs.getString("MEM_PW"),
                    rs.getString("MEM_NAME")
                );
                memberList.add(member);
            }
        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }
        return memberList;
    }
    
    /** 이름으로 회원 검색 DAO */
    public List<Member> selectByName(Connection conn, String name) throws Exception {
        List<Member> memberList = new ArrayList<>();
        try {
            String sql = "SELECT MEM_ID, MEM_PW, MEM_NAME FROM TB_MEMBER WHERE MEM_NAME LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Member member = new Member(
                    rs.getString("MEM_ID"),
                    rs.getString("MEM_PW"),
                    rs.getString("MEM_NAME")
                );
                memberList.add(member);
            }
        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }
        return memberList;
    }

    /** ID가 일치하는 User 의 이름 수정 DAO */
    public int updateName(Connection conn, String userName, String memId) throws Exception {
        int result = 0;
        try {
            String sql = "UPDATE TB_MEMBER SET MEM_NAME = ? WHERE MEM_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, memId);
            result = pstmt.executeUpdate();
        } finally {
            JDBCTemplate.close(pstmt);
        }
        return result;
    }

    /** ID가 일치하는 회원 삭제 */
    public int deleteUser(Connection conn, String memId) throws Exception {
        int result = 0;
        try {
            String sql = "DELETE FROM TB_MEMBER WHERE MEM_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memId);
            result = pstmt.executeUpdate();
        } finally {
            JDBCTemplate.close(pstmt);
        }
        return result;
    }

    /** 특정 회원의 TODO 목록 조회 DAO */
    public List<ToDo> selectTodo(Connection conn, String memId) throws Exception {
        List<ToDo> todoList = new ArrayList<>();

        try {
            String sql = """
                    SELECT MEM_ID, TODO_LIST, TODO_CHECK, TODO_DATE, TODO_DDAY
                    FROM TB_TODO
                    WHERE MEM_ID = ?
                    """;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ToDo todo = new ToDo(
                    rs.getString("MEM_ID"),
                    rs.getString("TODO_LIST"),
                    rs.getString("TODO_CHECK").equals("Y"), // 'Y' → true, 'N' → false
                    rs.getDate("TODO_DATE").toLocalDate(),
                    rs.getDate("TODO_DDAY").toLocalDate()
                );
                todoList.add(todo);
            }

        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }

        return todoList;
    }
    
    /** 모든 TODO 조회 DAO */
    public List<ToDo> selectAllTodo(Connection conn) throws Exception {
        List<ToDo> todoList = new ArrayList<>();

        try {
            String sql = """
                    SELECT MEM_ID, TODO_LIST, TODO_CHECK, TODO_DATE, TODO_DDAY
                    FROM TB_TODO
                    """;

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ToDo todo = new ToDo(
                    rs.getString("MEM_ID"),
                    rs.getString("TODO_LIST"),
                    rs.getString("TODO_CHECK").equals("Y"), // 'Y' → true, 'N' → false
                    rs.getDate("TODO_DATE").toLocalDate(),
                    rs.getDate("TODO_DDAY").toLocalDate()
                );
                todoList.add(todo);
            }

        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }

        return todoList;
    }

    /** TODO 추가 DAO */
    public int insertTodo(Connection conn, ToDo todo) throws Exception {
        int result = 0;

        try {
            String sql = """
                    INSERT INTO TB_TODO (MEM_ID, TODO_LIST, TODO_CHECK, TODO_DATE, TODO_DDAY)
                    VALUES (?, ?, ?, ?, ?)
                    """;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, todo.getMemId());
            pstmt.setString(2, todo.getTodoList());
            pstmt.setString(3, todo.isTodoCheck() ? "Y" : "N"); // boolean → 'Y' 또는 'N'
            pstmt.setDate(4, Date.valueOf(todo.getTodoDate())); 
            pstmt.setDate(5, Date.valueOf(todo.getTodoDday()));

            result = pstmt.executeUpdate();

        } finally {
            JDBCTemplate.close(pstmt);
        }

        return result;
    }

    /** TODO 수정 DAO */
    public int updateTodo(Connection conn, ToDo todo) throws Exception {
        int result = 0;

        try {
            String sql = """
                    UPDATE TB_TODO
                    SET TODO_LIST = ?, TODO_CHECK = ?, TODO_DATE = ?, TODO_DDAY = ?
                    WHERE MEM_ID = ? AND TODO_LIST = ?
                    """;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, todo.getTodoList());
            pstmt.setString(2, todo.isTodoCheck() ? "Y" : "N"); // boolean → 'Y' 또는 'N'
            pstmt.setDate(3, Date.valueOf(todo.getTodoDate()));
            pstmt.setDate(4, Date.valueOf(todo.getTodoDday()));
            pstmt.setString(5, todo.getMemId());
            pstmt.setString(6, todo.getTodoList()); // 기존 TODO_LIST를 기준으로 업데이트

            result = pstmt.executeUpdate();

        } finally {
            JDBCTemplate.close(pstmt);
        }

        return result;
    }

    /** TODO 삭제 DAO */
    public int deleteTodo(Connection conn, String memId, String todoList) throws Exception {
        int result = 0;

        try {
            String sql = """
                    DELETE FROM TB_TODO
                    WHERE MEM_ID = ? AND TODO_LIST = ?
                    """;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memId);
            pstmt.setString(2, todoList);

            result = pstmt.executeUpdate();

        } finally {
            JDBCTemplate.close(pstmt);
        }

        return result;
    }
}