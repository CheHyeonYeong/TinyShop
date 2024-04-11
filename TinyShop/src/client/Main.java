package client; // 클라이언트 패키지 선언

import java.io.BufferedReader; // BufferedReader 클래스 import
import java.io.IOException; // IOException 클래스 import
import java.io.InputStreamReader; // InputStreamReader 클래스 import
import java.io.PrintWriter; // PrintWriter 클래스 import
import java.net.Socket; // Socket 클래스 import
import java.util.Scanner; // Scanner 클래스 import

public class Main { // Main 클래스 시작

    public static void main(String[] args) throws IOException { // main 메서드 시작
        Socket clientSocket = null; // Socket 변수 초기화
        BufferedReader in = null; // BufferedReader 변수 초기화
        PrintWriter out = null; // PrintWriter 변수 초기화
        final Scanner sc = new Scanner(System.in); // Scanner 변수 초기화
        try { // try 문 시작

            clientSocket = new Socket("localhost", 5000); // "localhost"의 포트 번호 5000으로 클라이언트 소켓 생성
            out = new PrintWriter(clientSocket.getOutputStream()); // 서버에 데이터를 보내기 위한 PrintWriter 생성
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // 서버로부터 데이터를 받기 위한 BufferedReader 생성
            String msg; // 문자열 변수 선언

            while (true) { // 무한 루프 시작



                System.out.print("서버로 보낼 문자열을 입력하고 엔터키를 치세요: "); // 서버로 보낼 문자열 입력 안내 메시지 출력
                msg = sc.nextLine(); // 사용자로부터 문자열 입력 받음

                if (msg.equalsIgnoreCase("quit")) { // 만약 메시지가 "quit"이면
                    out.println(msg); // 메시지를 서버에 전송
                    out.flush(); // 버퍼 비우기
                    break; // 무한 루프 탈출
                }


                out.println(msg); // 메시지를 서버에 전송
                System.out.println(msg);

                out.flush(); // 버퍼 비우기
                msg = in.readLine(); // 서버로부터 메시지 읽기
                System.out.println("서버로부터 온 메시지: " + msg); // 서버로부터 받은 메시지 출력


            }
        } catch (IOException e) { // IOException 처리
            e.printStackTrace(); // 예외 출력
        } finally { // finally 문 시작
            out.close(); // 출력 스트림 닫기
            clientSocket.close(); // 클라이언트 소켓 닫기
        }
    }
}
