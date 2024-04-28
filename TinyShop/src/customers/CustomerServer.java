package customers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class CustomerServer {

    public static void main(String[] args) { // main 메서드 시작
        ServerSocket severSocket = null; // ServerSocket 변수 초기화
        Socket clientSocket = null; // Socket 변수 초기화
        BufferedReader in = null; // BufferedReader 변수 초기화
        PrintWriter out = null; // PrintWriter 변수 초기화
        CustomerDAO dao = CustomerDAO.getInstance();

        try{ // try 문 시작
            severSocket = new ServerSocket(3018); // 포트 번호 5000으로 ServerSocket 생성
            System.out.println("연결을 기다리고 있습니다"); // 연결 대기 메시지 출력
            clientSocket = severSocket.accept(); // 클라이언트의 연결 수락
            out = new PrintWriter(clientSocket.getOutputStream()); // 클라이언트에게 데이터를 보내기 위한 PrintWriter 생성
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // 클라이언트로부터 데이터를 받기 위한 BufferedReader 생성
            System.out.println("클라이언트와 연결 성공"); // 클라이언트와의 연결 성공 메시지 출력

            while (true){ // 무한 루프 시작
                String msg = in.readLine(); // 클라이언트로부터 문자열을 읽어옴
                System.out.println(msg);

                if(msg.equalsIgnoreCase("quit")){ // 만약 메시지가 "quit"이면
                    System.out.println("클라이언트에서 연결을 종료하였습니다"); // 클라이언트에서 연결 종료 메시지 출력
                    break; // 무한 루프 탈출
                }


                //main에서 동작할 것들을 넣어줘야 함 아래는 예시
//                String omsg = sc.nextLine(); // 사용자로부터 문자열 입력 받음
//                out.write(omsg+"\n"); // 클라이언트로 문자열 전송
                String[] tokens = msg.split(",");
                String command = tokens[0];

                switch (command) {
                    //customer list 확인
                    case "userList":
                        String[] userList = dao.userList();
                        out.println(String.join(",", userList));
                        break;
                    //customer 이름으로 찾기    
                    case "findByName":
                        String name = tokens[1];
                        String[][] foundUsers = dao.findByName(name);
                        out.println(arrayToString(foundUsers));
                        System.out.println("회원 정보가 검색되었습니다.");
                        break;
                    //customer 추가    
                    case "userInsert":
                        String id = tokens[1];
                        String pw = tokens[2];
                        String username = tokens[3];
                        System.out.println(id+ pw+ username);
                        CustomerVO user = new CustomerVO(id, pw, username);
                        System.out.println(user.toString());
                        dao.userInsert(user);
                        System.out.println("클라이언트에서 연결 되었습니다");
                        out.println("회원 정보가 추가되었습니다.");
                        out.flush();
                        break;
                    //customer 수정    
                    case "update":
                        String updateId = tokens[1];
                        String currentPw = tokens[2];
                        boolean exists = dao.checkUserExists(updateId, currentPw);
                        if (exists) {
                            out.println("해당 회원 정보가 존재합니다. 수정하시겠습니까? (y/n)");
                            out.flush();
                            String choice = in.readLine();
                            if (choice.equalsIgnoreCase("y")) {
                                out.println("새로운 비밀번호를 입력하세요:");
                                out.flush();
                                String newPw = in.readLine();
                                out.println("새로운 이름을 입력하세요:");
                                out.flush();
                                String newName = in.readLine();
                                dao.updateUser(updateId, newPw, newName);
                                out.println("회원 정보가 수정되었습니다.");
                            } else {
                                out.println("회원 정보 수정이 취소되었습니다.");
                            }
                        } else {
                            out.println("해당 회원 정보가 존재하지 않습니다.");
                        }
                        break;
                    //customer 삭제
                    case "delete":
                        String deleteId = tokens[1];
                        dao.delete(deleteId);
                        out.println("회원 정보가 삭제되었습니다.");
                        break;
                    default:
                        out.println("잘못된 명령입니다.");
                        break;
                }

                out.flush(); // 버퍼 비우기
            }
            out.close(); // 출력 스트림 닫기
            clientSocket.close(); // 클라이언트 소켓 닫기
            severSocket.close(); // 서버 소켓 닫기
        }catch (IOException e){ // IOException 처리
            e.printStackTrace(); // 예외 출력
        }
    }

    // 2차원 배열을 문자열로 변환하는 메서드
    private static String arrayToString(String[][] array) {
        StringBuilder sb = new StringBuilder();
        for (String[] row : array) {
            for (String cell : row) {
                sb.append(cell).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
        }
        return sb.toString();
    }
}