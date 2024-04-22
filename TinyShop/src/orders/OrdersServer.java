// OrdersServer.java
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
            OrdersDAO ordersDAO = OrdersDAO.getInstance();

            serverSocket = new ServerSocket(3018);
            System.out.println("연결을 기다리고 있습니다");
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("클라이언트와 연결 성공");

            // food 테이블과 customer 테이블 정보 출력
            ordersDAO.getFoodInfo();
            ordersDAO.getCustomerInfo();

            while (true) {
                String msg = in.readLine();
                if (msg == null) {
                    break;
                }

                // 토큰 생성
                String token = generateToken();

                String[] request = msg.split(",");
                String command = request[0];

                try {
                    switch (command) {
                        case "placeOrder"://placeOrder,1(foode_id),2(food_name),3(cusid),4(cusname),5(quantity)
                            if (request.length != 6) {
                                out.println("유효하지 않은 주문 정보입니다.");
                                break;
                            }
                            OrdersVO order = parseOrder(msg);
                            if (ordersDAO.isValid(order)) {
                                ordersDAO.placeOrder(order);
                                out.println("주문이 성공적으로 접수되었습니다.");
                            } else {
                                out.println("유효하지 않은 주문 정보입니다.");
                            }
                            break;
                        case "getOrders": //getOrders,1(cusid)
                            if (request.length != 2) {
                                out.println("유효하지 않은 요청입니다.");
                                break;
                            }
                            String cusid = request[1];
                            if (ordersDAO.isValidCustomerId(cusid)) {
                                List<OrdersVO> orders = ordersDAO.getOrdersByCusid(cusid);
                                out.println("고객의 주문 내역:");
                                for (OrdersVO o : orders) {
                                    out.println(o.toString());
                                }
                            } else {
                                out.println("유효하지 않은 고객 ID입니다.");
                            }
                            break;
                        //updateOrder,1(order_id),2(food_name),3(quantity)
                        case "updateOrder":
                            if (request.length != 4) {
                                out.println("유효하지 않은 주문 정보입니다.");
                                break;
                            }
                            int updateOrderId = Integer.parseInt(request[1]);
                            String updateFoodName = request[2];
                            int updateQuantity = Integer.parseInt(request[3]);
                            boolean updateResult = ordersDAO.updateOrder(updateOrderId, updateFoodName, updateQuantity);
                            if (updateResult) {
                                out.println("주문이 성공적으로 수정되었습니다.");
                            } else {
                                out.println("주문 수정에 실패하였습니다.");
                            }
                            break;
                            //deleteOrder,1(order_id)
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
                } catch (SQLException e) {
                    out.println("데이터베이스 오류가 발생했습니다: " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    out.println("유효하지 않은 요청 형식입니다.");
                } catch (NumberFormatException e) {
                    out.println("유효하지 않은 숫자 형식입니다.");
                }
            }

            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    // 토큰 생성 메서드
    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    // 주문 정보 파싱 메서드
    private static OrdersVO parseOrder(String msg) {
        String[] orderInfo = msg.split(",");
        int foodId = Integer.parseInt(orderInfo[1]);
        String foodName = orderInfo[2];
        String cusid = orderInfo[3];
        String cusname = orderInfo[4];
        int quantity = Integer.parseInt(orderInfo[5]);

        OrdersVO order = new OrdersVO();
        order.setFoodId(foodId);
        order.setFoodName(foodName);
        order.setCusid(cusid);
        order.setCusname(cusname);
        order.setQuantity(quantity);

        return order;
    }
}