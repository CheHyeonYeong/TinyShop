package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        final Scanner sc = new Scanner(System.in);
        try {
            clientSocket = new Socket("localhost", 3018);
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                System.out.println("메뉴를 선택하세요:");
                System.out.println("1. 회원 가입");
                System.out.println("2. 로그인");
                System.out.println("3. 회원 탈퇴");
                System.out.println("4. 주문내역 조회");
                System.out.println("5. 주문내역 수정");
                System.out.println("6. 주문내역 삭제");
                System.out.println("7. 주문하기");
                System.out.println("8. Food 내역 확인");
                System.out.println("9. 종료");

                String choice = sc.nextLine();
                out.println(choice);
                out.flush();

                switch (choice) {
                    case "1":
                        // 회원 가입 로직 구현
                        break;
                    case "2":
                        // 로그인 로직 구현
                        break;
                    case "3":
                        // 회원 탈퇴 로직 구현
                        break;
                    case "4":
                        // 주문내역 조회 로직 구현
                        break;
                    case "5":
                        // 주문내역 수정 로직 구현
                        break;
                    case "6":
                        // 주문내역 삭제 로직 구현
                        break;
                    case "7":
                        // 주문하기 로직 구현
                        break;
                    case "8":
                        // Food 내역 확인 로직 구현
                        break;
                    case "9":
                        out.println("quit");
                        out.flush();
                        break;
                    default:
                        System.out.println("잘못된 선택입니다.");
                        break;
                }

                if (choice.equals("9")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            clientSocket.close();
        }
    }
}