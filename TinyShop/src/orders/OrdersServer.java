package orders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class OrdersServer {
    public static void main(String[] args) {
        // ServerSocket 생성 및 연결 대기
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        Scanner sc = new Scanner(System.in);

        // OrdersDAO 객체 생성
        OrdersDAO ordersDAO = new OrdersDAO();

        try {
            // 서버 소켓 생성 및 포트 바인딩
            serverSocket = new ServerSocket(3306);
            System.out.println("연결을 기다리고 있습니다");
            clientSocket = serverSocket.accept(); // 클라이언트 연결 수락
            out = new PrintWriter(clientSocket.getOutputStream()); // 클라이언트로 데이터 전송
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // 클라이언트로부터 데이터 수신
            System.out.println("클라이언트와 연결 성공");

            // 무한 루프로 클라이언트의 요청 처리
            while (true) {
                String msg = in.readLine(); // 클라이언트로부터 명령 수신
                if (msg.equalsIgnoreCase("quit")) { // "quit" 명령 수신 시 종료
                    System.out.println("클라이언트에서 연결을 종료하였습니다");
                    break;
                }

                // 주문 관리 기능 수행
                switch (msg) {
                    case "create":
                        // 주문 생성
                        OrdersVO newOrder = createOrderFromClient(in);
                        ordersDAO.createOrder(newOrder);
                        out.println("주문에 성공했습니다");
                        break;
                    case "list":
                        // 모든 주문 조회
                        List<OrdersVO> orders = ordersDAO.getAllOrders();
                        sendOrdersToClient(out, orders);
                        break;
                    case "update":
                        // 주문 수정
                        int customerId = Integer.parseInt(in.readLine()); // 고객 ID 읽기
                        OrdersVO updatedOrder = updateOrderFromClient(in);
                        ordersDAO.updateOrder(customerId, updatedOrder.getId(), updatedOrder);
                        out.println("주문내역 수정에 성공했습니다.");
                        break;
                    case "delete":
                        // 주문 삭제
                        int orderId = Integer.parseInt(in.readLine());
                        ordersDAO.deleteOrder(orderId);
                        out.println("주문을 삭제하였습니다");
                        break;
                    default:
                        out.println("잘못된 입력입니다. 다시 시도해 주세요");
                }
                out.flush(); // 출력 버퍼 비우기
            }
            // 리소스 정리
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 클라이언트로부터 주문 정보 생성
    private static OrdersVO createOrderFromClient(BufferedReader in) throws IOException {
        OrdersVO order = new OrdersVO();
        order.setFoodName(in.readLine());
        order.setQuantity(Integer.parseInt(in.readLine()));
        return order;
    }

    // 클라이언트로부터 주문 정보 수정
    private static OrdersVO updateOrderFromClient(BufferedReader in) throws IOException {
        OrdersVO order = new OrdersVO();
        order.setId(Integer.parseInt(in.readLine()));
        order.setQuantity(Integer.parseInt(in.readLine()));
        return order;
    }

    // 주문 목록을 클라이언트에게 전송
    private static void sendOrdersToClient(PrintWriter out, List<OrdersVO> orders) {
        for (OrdersVO order : orders) {
            out.println(order.getId());
            out.println(order.getFoodName());
            out.println(order.getQuantity());
            out.println();
        }
        out.println("주문을 종료합니다.");
    }
}