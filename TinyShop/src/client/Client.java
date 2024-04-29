package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket clientSocket = null; // Socket 변수 초기화
    BufferedReader in = null; // BufferedReader 변수 초기화
    PrintWriter out = null; // PrintWriter 변수 초기화
    Scanner sc = null;
    String token = "";
    public Client(Scanner sc) {
        this.sc = sc;
    }
    public void addUser() throws IOException { //회원 가입
        buffetOpen(3018);
        System.out.println("회원 가입을 진행합니다");

        System.out.println("id를 입력하세요");
        String id = sc.nextLine();
        System.out.println("pw를 입력하세요");
        String pw = sc.nextLine();
        System.out.println("username를 입력하세요");
        String username = sc.nextLine();

        token = String.format("userInsert,%s,%s,%s", id, pw,username);
        out.println(token); // 서버로 token 전송

        bufferClose();

    }
    public void removeUser() throws IOException {
        buffetOpen(3018);
        System.out.println("회원 탈퇴를 진행합니다");

        System.out.println("id를 입력하세요");
        String id = sc.nextLine();
        token = String.format("delete,%s", id);

        System.out.println("회원 탈퇴가 완료되었습니다");

        out.println(token); // 서버로 token 전송
        out.flush(); // 버퍼 비우기
        bufferClose();
    }
    public void login() throws IOException {
        buffetOpen(3018);

        System.out.println("로그인을 진행합니다");

        System.out.println("id를 입력하세요");
        String id = sc.nextLine();
        System.out.println("pw를 입력하세요");
        String pw = sc.nextLine();

        token = String.format("login,%s,%s", id, pw);
        out.println(token); // 서버로 token 전송
        out.flush(); // 버퍼 비우기

        String response = in.readLine(); // 서버로부터 응답 받기
        System.out.println(response); // 서버로부터 받은 응답 출력

        bufferClose();
    }

    public void viewFoods() throws IOException {
        buffetOpen(3019);

        System.out.println("음식 내용을 전반적으로 보여드립니다");
        token = "getAllFoods";
        System.out.println("완료되었습니다");
        out.println(token); // 서버로 token 전송
        bufferClose();
    }

    public void viewOrder() throws IOException {
        buffetOpen(3020);
        System.out.println("id를 입력하세요");
        String id = sc.nextLine();

        token = String.format("getOrders,%s",id);
        out.print(token);
        bufferClose();
    }
    public void changeOrder() throws IOException {
        buffetOpen(3020);

        System.out.println("주문번호를 적으시오");
        int orderId = Integer.parseInt(sc.nextLine());
        System.out.println("수량을 적으시오");
        int quantity = Integer.parseInt(sc.nextLine());
        token = String.format("updateOrder,%s,%s,%s", orderId,quantity);

        out.print(token);
        bufferClose();
    }
    public void deleteOrder() throws IOException {

        buffetOpen(3020);

        int orderID = Integer.parseInt(sc.nextLine());

        token = String.format("deleteOrder,%d",orderID);
        out.print(token);

        bufferClose();
    }
    public void insertOrder() throws IOException {
        buffetOpen(3020);
        System.out.println("음식이름을 적으시오");
        String foodName = sc.nextLine();
        System.out.println("id을 적으시오");
        String cusid = sc.nextLine();
        System.out.println("수량을 적으시오");
        int quantity = Integer.parseInt(sc.nextLine());

        token = String.format("placeOrder,%s,%s,%s", foodName, cusid,quantity);
        out.println(token);
        bufferClose();
    }

    private void buffetOpen(int port){
        try { // try 문 시작
            clientSocket = new Socket("localhost", port); // "localhost"의 포트 번호 5000으로 클라이언트 소켓 생성
            out = new PrintWriter(clientSocket.getOutputStream()); // 서버에 데이터를 보내기 위한 PrintWriter 생성
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // 서버로부터 데이터를 받기 위한 BufferedReader 생성

        } catch (IOException e) { // IOException 처리
            e.printStackTrace(); // 예외 출력
        }
    }
    private void bufferClose() throws IOException {

        try { // try 문 시작
            out.flush();
            token="";

            String msg = in.readLine(); // 서버로부터 메시지 읽기
            System.out.println(msg); // 서버로부터 받은 메시지 출력

        } catch (IOException e) { // IOException 처리
            e.printStackTrace(); // 예외 출력
        } finally { // finally 문 시작
            out.close(); // 출력 스트림 닫기
            clientSocket.close(); // 클라이언트 소켓 닫기
        }
    }
}
