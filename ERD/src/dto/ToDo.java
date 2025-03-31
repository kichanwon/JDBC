package dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {	
    private String memId;
    private String todoList;
    private boolean todoCheck;
    private LocalDate todoDate;
    private LocalDate todoDday;
    
    // String 파라미터를 받는 생성자 추가
    public ToDo(String memId, String todoList, String todoCheck, String todoDate, String todoDday) {
        this.memId = memId;
        this.todoList = todoList;
        this.todoCheck = todoCheck.equalsIgnoreCase("Y");
        this.todoDate = LocalDate.parse(todoDate);
        // todoDday가 빈 문자열이면 오늘 날짜로 설정
        this.todoDday = todoDday == null || todoDday.trim().isEmpty() ? 
            LocalDate.now() : LocalDate.parse(todoDday);
    }
}