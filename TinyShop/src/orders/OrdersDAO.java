package orders;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    private String url = "jdbc:mysql://localhost:3306/tinyshop";
    private String user = "jdbc";
    private String password = "jdbc";

    private Connection conn = null;

    public OrdersDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            createOrdersTable(); // 테이블 생성 메서드 호출
            System.out.println("Orders 테이블이 생성되었습니다.");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // orders 테이블 생성 메서드
    private void createOrdersTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS orders (" +
                "order_id INT AUTO_INCREMENT PRIMARY KEY," +
                "food_name VARCHAR(255)," +
                "cusid VARCHAR(255),"+
                "quantity INT" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public List<OrdersVO> getOrdersByCustomerId(String customerId) {
        List<OrdersVO> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE cusid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String foodName = rs.getString("food_name");
                int quantity = rs.getInt("quantity");
                OrdersVO order = new OrdersVO( foodName, customerId, quantity);
                orders.add(order);
            }
            closeResources(stmt, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean updateOrderQuantity(int orderId, int newQuantity) {
        String sql = "UPDATE orders SET quantity = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, orderId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertOrder(OrdersVO order) {
        String sql = "INSERT INTO orders (food_name, cusid, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getFoodName());
            stmt.setString(2, order.getCusid());
            stmt.setInt(3, order.getQuantity());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void closeResources(Statement stmt, ResultSet rs) {
        try {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Connection 닫는 메서드
    public void closeConnection() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
