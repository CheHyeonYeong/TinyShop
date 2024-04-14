package orders;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrdersDAO {

    // SQL 쿼리문 상수 정의

    //MySQL 쿼리문을 상수로 정의
    //주문
    private static final String INSERT_ORDER = "INSERT INTO orders (customer_name, food_name, quantity) VALUES (?, ?, ?)";
    //주문 조회문
    private static final String SELECT_ALL_ORDERS = "SELECT * FROM orders";
    //주문 수정문
    private static final String UPDATE_ORDER = "UPDATE orders SET quantity = ? WHERE id = ?";
    //주문 삭제문
    private static final String DELETE_ORDER = "DELETE FROM orders WHERE id = ?";

//

    // 데이터베이스 연결 메서드
    private Connection getConnection() throws SQLException {
        // 데이터베이스 연결 정보 설정  localhost:port/테이블명, "user","password"
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/orders", "jdbc", "jdbc");
    }



    // 주문 생성 메서드
    public void createOrder(OrdersVO ordersvo) {
        // 데이터베이스 연결 및 PreparedStatement 생성
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_ORDER)) {
            // 주문 정보를 PreparedStatement에 설정
            stmt.setString(2, ordersvo.getFoodName());
            stmt.setInt(3, ordersvo.getQuantity());
            // 쿼리 실행
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 모든 주문 조회 메서드
    public List<OrdersVO> getAllOrders() {
        // 데이터베이스 연결 및 Statement 생성
        List<OrdersVO> orders = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_ORDERS)) {
            // 결과 집합에서 Order 객체 생성 및 추가
            while (rs.next()) {
                OrdersVO ordersvo = new OrdersVO();
                ordersvo.setId(rs.getInt("id"));
                ordersvo.setFoodName(rs.getString("food_name"));
                ordersvo.setQuantity(rs.getInt("quantity"));
                orders.add(ordersvo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    // 특정 고객의 모든 주문 조회
    public List<OrdersVO> getOrdersByCustomerId(int customerId) {
        List<OrdersVO> orders = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE customer_id = ?")) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdersVO ordersvo = new OrdersVO();
                ordersvo.setId(rs.getInt("id"));
                ordersvo.setFoodName(rs.getString("food_name"));
                ordersvo.setQuantity(rs.getInt("quantity"));
                orders.add(ordersvo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // 주문 수정 메서드
    // 특정 고객의 주문 내역 수정
    public void updateOrder(int customerId, int orderId, OrdersVO updatedOrder) {
        // SQL 쿼리 준비
        String updateQuery = "UPDATE orders SET quantity = ? WHERE id = ? AND customer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            // 변경된 주문 정보 설정
            stmt.setInt(1, updatedOrder.getQuantity());
            stmt.setInt(2, orderId);
            stmt.setInt(3, customerId);
            // 쿼리 실행
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 주문 삭제 메서드
    public void deleteOrder(int orderId) {
        // 데이터베이스 연결 및 PreparedStatement 생성
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_ORDER)) {
            // 삭제할 주문 ID를 PreparedStatement에 설정
            stmt.setInt(1, orderId);
            // 쿼리 실행
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}