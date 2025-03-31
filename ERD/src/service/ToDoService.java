package service;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import common.JDBCTemplate;
import dao.DAO;
import dto.ToDo;

public class ToDoService {
    
    private DAO dao = new DAO();
    
    /** 특정 회원의 ToDo 목록 조회 서비스 */
    public List<ToDo> selectUserToDo(String memId) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        List<ToDo> todoList = dao.selectTodo(conn, memId);
        
        JDBCTemplate.close(conn);
        
        return todoList;
    }
    
    /** 모든 ToDo 조회 서비스 */
    public List<ToDo> selectAllToDo() throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        List<ToDo> todoList = dao.selectAllTodo(conn);
        
        JDBCTemplate.close(conn);
        
        return todoList;
    }
    
    /** ToDo 추가 서비스 */
    public int insertToDo(ToDo todo) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        int result = dao.insertTodo(conn, todo);
        
        if(result > 0) {
            JDBCTemplate.commit(conn);
        } else {
            JDBCTemplate.rollback(conn);
        }
        
        JDBCTemplate.close(conn);
        
        return result;
    }
    
    /** ToDo 완료 상태 수정 서비스 */
    public int updateToDo(String memId, String todoList, String todoCheck) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        // 먼저 해당 ToDo를 조회
        List<ToDo> todos = dao.selectTodo(conn, memId);
        ToDo targetTodo = null;
        
        // 수정할 ToDo 찾기
        for(ToDo todo : todos) {
            if(todo.getTodoList().equals(todoList)) {
                targetTodo = todo;
                break;
            }
        }
        
        int result = 0;
        
        if(targetTodo != null) {
            // todoCheck가 "Y"면 true, 아니면 false로 설정
            boolean isCompleted = todoCheck.equalsIgnoreCase("Y");
            
            // 기존 ToDo 정보를 유지하면서 완료 상태만 변경
            ToDo updatedTodo = new ToDo(
                memId,
                todoList,
                isCompleted,
                targetTodo.getTodoDate(),
                targetTodo.getTodoDday()
            );
            
            result = dao.updateTodo(conn, updatedTodo);
            
            if(result > 0) {
                JDBCTemplate.commit(conn);
            } else {
                JDBCTemplate.rollback(conn);
            }
        }
        
        JDBCTemplate.close(conn);
        
        return result;
    }
    
    /** ToDo 삭제 서비스 */
    public int deleteToDo(String memId, String todoList) throws Exception {
        Connection conn = JDBCTemplate.getConnection();
        
        int result = dao.deleteTodo(conn, memId, todoList);
        
        if(result > 0) {
            JDBCTemplate.commit(conn);
        } else {
            JDBCTemplate.rollback(conn);
        }
        
        JDBCTemplate.close(conn);
        
        return result;
    }
}