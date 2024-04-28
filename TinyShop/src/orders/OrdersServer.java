package orders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class OrdersServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            // OrdersDAO 인스턴스 가져오기
            OrdersDAO ordersDAO = new OrdersDAO();

            serverSocket = new ServerSocket(3018);
            System.out.println("연결을 기다리고 있습니다");
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("클라이언트와 연결 성공");

            while (true) {
                String msg = in.readLine();
                if (msg == null) {
                    break;
                }

                String[] request = msg.split(",");
                String command = request[0];

                try {
                    switch (command) {
                        case "placeOrder":
                            if (request.length != 6) {
                                out.println("유효하지 않은 주문 정보입니다.");
                                break;
                            }
                            OrdersVO order = parseOrder(msg);
                            // 유효성 검사 로직 추가
                            boolean insertResult = ordersDAO.insertOrder(order);
                            if (insertResult) {
                                out.println("주문이 성공적으로 접수되었습니다.");
                            } else {
                                out.println("주문 접수에 실패하였습니다.");
                            }
                            break;
                        case "getOrders":
                            if (request.length != 2) {
                                out.println("유효하지 않은 요청입니다.");
                                break;
                            }
                            String cusid = request[1];
                            List<OrdersVO> orders = ordersDAO.getOrdersByCustomerId(cusid);
                            out.println("고객의 주문 내역:");
                            for (OrdersVO o : orders) {
                                out.println(o.toString());
                            }
                            break;
                        case "updateOrder":
                            if (request.length != 4) {
                                out.println("유효하지 않은 주문 정보입니다.");
                                break;
                            }
                            int updateOrderId = Integer.parseInt(request[1]);
                            String updateFoodName = request[2];
                            int updateQuantity = Integer.parseInt(request[3]);
                            boolean updateResult = ordersDAO.updateOrderQuantity(updateOrderId, updateQuantity);
                            if (updateResult) {
                                out.println("주문이 성공적으로 수정되었습니다.");
                            } else {
                                out.println("주문 수정에 실패하였습니다.");
                            }
                            break;
                        case "deleteOrder":
                            if (request.length != 2) {
                                out.println("유효하지 않은 요청입니다.");
                                break;
                            }
                            int deleteOrderId = Integer.parseInt(request[1]);
                            boolean deleteResult = ordersDAO.deleteOrder(deleteOrderId);
                            if (deleteResult) {
                                out.println("주문이 성공적으로 삭제되었습니다.");
                            } else {
                                out.println("주문 삭제에 실패하였습니다.");
                            }
                            break;
                        default:
                            out.println("유효하지 않은 명령입니다.");
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    out.println("유효하지 않은 요청 형식입니다.");
                } catch (NumberFormatException e) {
                    out.println("유효하지 않은 숫자 형식입니다.");
                }
            }

            out.close();
            clientSocket.close();
            serverSocket.close();
            ordersDAO.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static OrdersVO parseOrder(String msg) {
        String[] orderInfo = msg.split(",");
        int foodId = Integer.parseInt(orderInfo[1]);
        String foodName = orderInfo[2];
        String cusid = orderInfo[3];
        String cusname = orderInfo[4];
        int quantity = Integer.parseInt(orderInfo[5]);

        return new OrdersVO(foodId, foodName, cusid, cusname, quantity);
    }
}