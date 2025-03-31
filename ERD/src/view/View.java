package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dto.Member;
import dto.ToDo;
import service.ToDoService;
import service.UserService;

public class View {        
    private Scanner sc = new Scanner(System.in);
    private UserService userService = new UserService();
    private ToDoService toDoService = new ToDoService();

    public void mainMenu() {
        int input = 0;

        do {
            try {
                System.out.println("\n===== User & To-Do List 관리 프로그램 =====\n");
                System.out.println("1. 회원 가입(INSERT)");
                System.out.println("2. 로그인");
                System.out.println("3. 회원 정보 수정(UPDATE)");
                System.out.println("4. 회원 탈퇴(DELETE)");
                System.out.println("5. To-Do List 관리");
                System.out.println("0. 프로그램 종료");

                System.out.print("메뉴 선택 : ");
                input = sc.nextInt();
                sc.nextLine(); 

                switch(input) {
                    case 1: insertUser(); break;
                    case 2: login(); break;
                    case 3: updateName(); break;
                    case 4: deleteUser(); break;
                    case 5: toDoMenu(); break;
                    case 0: System.out.println("\n[프로그램 종료]\n"); break;
                    default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
                }

            } catch (InputMismatchException e) {
                System.out.println("\n*** 잘못 입력하셨습니다 ***\n");
                input = -1;
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("\n*** 오류 발생: " + e.getMessage() + " ***\n");
                e.printStackTrace();
            }

            System.out.println("\n-------------------------------------\n");

        } while(input != 0);
    }

    /** 회원 가입 */
    private void insertUser() throws Exception {
        System.out.println("\n===== 회원 가입 =====\n");

        String memId = null;
        boolean isUnique = false;

        // 아이디 중복 검사
        while(!isUnique) {
            System.out.print("아이디 : ");
            memId = sc.nextLine();

            int count = userService.idCheck(memId);

            if(count > 0) {
                System.out.println("\n*** 이미 사용 중인 아이디입니다 ***\n");
            } else {
                isUnique = true;
                System.out.println("\n[사용 가능한 아이디입니다]\n");
            }
        }

        System.out.print("비밀번호 : ");
        String memPw = sc.nextLine();

        System.out.print("이름 : ");
        String memName = sc.nextLine();

        Member member = new Member(memId, memPw, memName);

        int result = userService.insertUser(member);

        if (result > 0) {
            System.out.println("\n[회원 가입 성공]\n");
        } else {
            System.out.println("\n*** 회원 가입 실패 ***\n");
        }
    }

    /** 로그인 */
    private void login() throws Exception {
        System.out.println("\n===== 로그인 =====\n");

        System.out.print("아이디 : ");
        String memId = sc.nextLine();

        System.out.print("비밀번호 : ");
        String memPw = sc.nextLine();

        String memName = userService.selectUser(memId, memPw);

        if (memName != null) {
            System.out.println("\n" + memName + "님 환영합니다!\n");
            
            // 로그인 성공 시 해당 사용자의 ToDo 목록 조회
            List<ToDo> todoList = toDoService.selectUserToDo(memId);
            
            if(todoList != null && !todoList.isEmpty()) {
                System.out.println("\n===== " + memName + "님의 할 일 목록 =====\n");
                
                for(ToDo todo : todoList) {
                    System.out.println(todo.getTodoList() + 
                            " (완료 여부: " + (todo.isTodoCheck() ? "Y" : "N") + 
                            ", 마감일: " + todo.getTodoDday() + ")");
                }
            } else {
                System.out.println("\n[등록된 할 일이 없습니다]\n");
            }
            
        } else {
            System.out.println("\n*** 아이디 또는 비밀번호가 일치하지 않습니다 ***\n");
        }
    }

    /** 회원 정보 수정 */
    private void updateName() throws Exception {
        System.out.println("\n===== 회원 정보 수정 =====\n");

        System.out.print("아이디 : ");
        String memId = sc.nextLine();

        System.out.print("비밀번호 : ");
        String memPw = sc.nextLine();

        String memName = userService.selectUser(memId, memPw);

        if (memName != null) {
            System.out.println("\n현재 이름: " + memName + "\n");
            
            System.out.print("변경할 이름 : ");
            String newName = sc.nextLine();
            
            int result = userService.updateName(newName, memId);
            
            if(result > 0) {
                System.out.println("\n[이름이 변경되었습니다]\n");
            } else {
                System.out.println("\n*** 이름 변경 실패 ***\n");
            }
            
        } else {
            System.out.println("\n*** 아이디 또는 비밀번호가 일치하지 않습니다 ***\n");
        }
    }

    /** 회원 탈퇴 */
    private void deleteUser() throws Exception {
        System.out.println("\n===== 회원 탈퇴 =====\n");

        System.out.print("아이디 : ");
        String memId = sc.nextLine();

        System.out.print("비밀번호 : ");
        String memPw = sc.nextLine();

        String memName = userService.selectUser(memId, memPw);

        if (memName != null) {
            System.out.print("\n정말 탈퇴하시겠습니까? (Y/N) : ");
            String confirm = sc.nextLine();
            
            if(confirm.equalsIgnoreCase("Y")) {
                int result = userService.deleteUser(memId);
                
                if(result > 0) {
                    System.out.println("\n[회원 탈퇴 완료]\n");
                } else {
                    System.out.println("\n*** 회원 탈퇴 실패 ***\n");
                }
            } else {
                System.out.println("\n[회원 탈퇴가 취소되었습니다]\n");
            }
            
        } else {
            System.out.println("\n*** 아이디 또는 비밀번호가 일치하지 않습니다 ***\n");
        }
    }

    /** To-Do List 관리 메뉴 */
    private void toDoMenu() {
        int input = 0;

        do {
            try {
                System.out.println("\n===== To-Do List 관리 =====\n");
                System.out.println("1. 할 일 추가 (INSERT)");
                System.out.println("2. 나의 할 일 조회 (SELECT)");
                System.out.println("3. 모든 할 일 조회 (SELECT ALL)");
                System.out.println("4. 할 일 완료/미완료 수정 (UPDATE)");
                System.out.println("5. 할 일 삭제 (DELETE)");
                System.out.println("0. 이전 메뉴로");

                System.out.print("메뉴 선택 : ");
                input = sc.nextInt();
                sc.nextLine(); 

                switch(input) {
                    case 1: insertToDo(); break;
                    case 2: selectUserToDo(); break;
                    case 3: selectAllToDo(); break;
                    case 4: updateToDo(); break;
                    case 5: deleteToDo(); break;
                    case 0: System.out.println("\n[이전 메뉴로 돌아갑니다]\n"); break;
                    default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
                }

            } catch (InputMismatchException e) {
                System.out.println("\n*** 잘못 입력하셨습니다 ***\n");
                input = -1;
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("\n*** 오류 발생: " + e.getMessage() + " ***\n");
                e.printStackTrace();
            }

            System.out.println("\n-------------------------------------\n");

        } while(input != 0);
    }

    /** 할 일 추가 */
    private void insertToDo() throws Exception {
        System.out.println("\n===== 할 일 추가 =====\n");

        // 회원 확인
        System.out.print("사용자 ID : ");
        String memId = sc.nextLine();

        System.out.print("비밀번호 : ");
        String memPw = sc.nextLine();

        String memName = userService.selectUser(memId, memPw);

        if (memName == null) {
            System.out.println("\n*** 아이디 또는 비밀번호가 일치하지 않습니다 ***\n");
            return;
        }

        System.out.println("\n[" + memName + "님의 할 일을 추가합니다]\n");

        System.out.print("할 일 내용 : ");
        String todoList = sc.nextLine();

        System.out.print("완료 여부(Y/N) : ");
        String todoCheck = sc.nextLine();

        LocalDate todoDate = LocalDate.now();
        
        LocalDate todoDday = null;
        boolean validDate = false;
        
        while(!validDate) {
            try {
                System.out.print("마감일 (YYYY-MM-DD, 없으면 엔터) : ");
                String input = sc.nextLine();
                
                if(input == null || input.trim().isEmpty()) {
                    // 마감일을 입력하지 않았을 경우 오늘 날짜로 설정
                    todoDday = LocalDate.now();
                    validDate = true;
                } else {
                    todoDday = LocalDate.parse(input);
                    validDate = true;
                }
            } catch(DateTimeParseException e) {
                System.out.println("\n*** 날짜 형식이 잘못되었습니다. YYYY-MM-DD 형식으로 입력해주세요 ***\n");
            }
        }

        ToDo todo = new ToDo(memId, todoList, todoCheck.equalsIgnoreCase("Y"), todoDate, todoDday);

        int result = toDoService.insertToDo(todo);

        if (result > 0) {
            System.out.println("\n[할 일이 추가되었습니다]\n");
        } else {
            System.out.println("\n*** 할 일 추가 실패 ***\n");
        }
    }

    /** 특정 사용자의 할 일 조회 */
    private void selectUserToDo() throws Exception {
        System.out.println("\n===== 나의 할 일 조회 =====\n");

        System.out.print("사용자 ID : ");
        String memId = sc.nextLine();

        System.out.print("비밀번호 : ");
        String memPw = sc.nextLine();

        String memName = userService.selectUser(memId, memPw);

        if (memName == null) {
            System.out.println("\n*** 아이디 또는 비밀번호가 일치하지 않습니다 ***\n");
            return;
        }

        List<ToDo> todoList = toDoService.selectUserToDo(memId);

        if (todoList.isEmpty()) {
            System.out.println("\n*** 등록된 할 일이 없습니다 ***\n");
            return;
        }

        System.out.println("\n===== " + memName + "님의 할 일 목록 =====\n");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (ToDo todo : todoList) {
            System.out.println("[내용] " + todo.getTodoList());
            System.out.println("[상태] " + (todo.isTodoCheck() ? "완료" : "미완료"));
            System.out.println("[등록일] " + todo.getTodoDate().format(formatter));
            System.out.println("[마감일] " + todo.getTodoDday().format(formatter));
            System.out.println("--------------------------------");
        }
    }

    /** 모든 할 일 조회 */
    private void selectAllToDo() throws Exception {
        System.out.println("\n===== 모든 할 일 조회 =====\n");

        List<ToDo> todoList = toDoService.selectAllToDo();

        if (todoList.isEmpty()) {
            System.out.println("\n*** 등록된 할 일이 없습니다 ***\n");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (ToDo todo : todoList) {
            System.out.println("[사용자] " + todo.getMemId());
            System.out.println("[내용] " + todo.getTodoList());
            System.out.println("[상태] " + (todo.isTodoCheck() ? "완료" : "미완료"));
            System.out.println("[등록일] " + todo.getTodoDate().format(formatter));
            System.out.println("[마감일] " + todo.getTodoDday().format(formatter));
            System.out.println("--------------------------------");
        }
    }

    /** 할 일 수정 (완료/미완료) */
    private void updateToDo() throws Exception {
        System.out.println("\n===== 할 일 수정 =====\n");

        // 회원 확인
        System.out.print("사용자 ID : ");
        String memId = sc.nextLine();

        System.out.print("비밀번호 : ");
        String memPw = sc.nextLine();

        String memName = userService.selectUser(memId, memPw);

        if (memName == null) {
            System.out.println("\n*** 아이디 또는 비밀번호가 일치하지 않습니다 ***\n");
            return;
        }

        // 사용자의 할 일 목록 조회
        List<ToDo> todoList = toDoService.selectUserToDo(memId);

        if (todoList.isEmpty()) {
            System.out.println("\n*** 등록된 할 일이 없습니다 ***\n");
            return;
        }

        System.out.println("\n===== " + memName + "님의 할 일 목록 =====\n");
        
        for (int i = 0; i < todoList.size(); i++) {
            ToDo todo = todoList.get(i);
            System.out.println((i + 1) + ". " + todo.getTodoList() + 
                    " (완료 여부: " + (todo.isTodoCheck() ? "Y" : "N") + ")");
        }

        System.out.print("\n수정할 할 일 번호 선택 : ");
        int todoNum = sc.nextInt();
        sc.nextLine();

        // 유효한 번호 검사
        if (todoNum < 1 || todoNum > todoList.size()) {
            System.out.println("\n*** 유효하지 않은 번호입니다 ***\n");
            return;
        }

        ToDo selectedTodo = todoList.get(todoNum - 1);

        System.out.print("완료 여부 변경 (Y/N) : ");
        String todoCheck = sc.nextLine();

        int result = toDoService.updateToDo(memId, selectedTodo.getTodoList(), todoCheck);

        if (result > 0) {
            System.out.println("\n[할 일 상태가 수정되었습니다]\n");
        } else {
            System.out.println("\n*** 수정 실패 ***\n");
        }
    }

    /** 할 일 삭제 */
    private void deleteToDo() throws Exception {
        System.out.println("\n===== 할 일 삭제 =====\n");

        // 회원 확인
        System.out.print("사용자 ID : ");
        String memId = sc.nextLine();

        System.out.print("비밀번호 : ");
        String memPw = sc.nextLine();

        String memName = userService.selectUser(memId, memPw);

        if (memName == null) {
            System.out.println("\n*** 아이디 또는 비밀번호가 일치하지 않습니다 ***\n");
            return;
        }

        // 사용자의 할 일 목록 조회
        List<ToDo> todoList = toDoService.selectUserToDo(memId);

        if (todoList.isEmpty()) {
            System.out.println("\n*** 등록된 할 일이 없습니다 ***\n");
            return;
        }

        System.out.println("\n===== " + memName + "님의 할 일 목록 =====\n");
        
        for (int i = 0; i < todoList.size(); i++) {
            ToDo todo = todoList.get(i);
            System.out.println((i + 1) + ". " + todo.getTodoList());
        }

        System.out.print("\n삭제할 할 일 번호 선택 : ");
        int todoNum = sc.nextInt();
        sc.nextLine();

        // 유효한 번호 검사
        if (todoNum < 1 || todoNum > todoList.size()) {
            System.out.println("\n*** 유효하지 않은 번호입니다 ***\n");
            return;
        }

        ToDo selectedTodo = todoList.get(todoNum - 1);

        System.out.print("정말 삭제하시겠습니까? (Y/N) : ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("\n[삭제가 취소되었습니다]\n");
            return;
        }

        int result = toDoService.deleteToDo(memId, selectedTodo.getTodoList());

        if (result > 0) {
            System.out.println("\n[할 일이 삭제되었습니다]\n");
        } else {
            System.out.println("\n*** 삭제 실패 ***\n");
        }
    }
}