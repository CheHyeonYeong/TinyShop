// OrdersDAO.java
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
        String sql = "CREATE TABLE IF NOT EXISTS Food (" +
                "food_id INT AUTO_INCREMENT PRIMARY KEY," +
                "food_category VARCHAR(255) NOT NULL," +
                "food_name VARCHAR(255) NOT NULL," +
                "food_description TEXT," +
                "food_price BIGINT NOT NULL" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Food 테이블이 생성되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static OrdersDAO getInstance() {
        if (instance == null) {
            instance = new OrdersDAO();
        }
        return instance;
    }

    // orders 테이블 생성 메서드
    private void createOrdersTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS orders (" +
                "order_id INT AUTO_INCREMENT PRIMARY KEY," +
                "food_id INT," +
                "food_name VARCHAR(255)," +
                "cusid VARCHAR(255)," +
                "cusname VARCHAR(255)," +
                "quantity INT" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    // food 테이블의 food_id와 food_name 조회 메서드
    public void getFoodInfo() throws SQLException {
        String sql = "SELECT food_id, food_name FROM food";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int foodId = rs.getInt("food_id");
                String foodName = rs.getString("food_name");
                System.out.println("Food ID: " + foodId + ", Food Name: " + foodName);
            }
        }
    }

    // customer 테이블의 cusid와 cusname 조회 메서드
    public void getCustomerInfo() throws SQLException {
        String sql = "SELECT cusid, cusname FROM customer";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String cusid = rs.getString("cusid");
                String cusname = rs.getString("cusname");
                System.out.println("Customer ID: " + cusid + ", Customer Name: " + cusname);
            }
        }
    }

    // 주문을 받는 메서드
    public void placeOrder(OrdersVO order) throws SQLException {
        String sql = "INSERT INTO orders (food_id, food_name, cusid, cusname, quantity) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, order.getFoodId());
            pstmt.setString(2, order.getFoodName());
            pstmt.setString(3, order.getCusid());
            pstmt.setString(4, order.getCusname());
            pstmt.setInt(5, order.getQuantity());
            pstmt.executeUpdate();
        }
    }

    // 고객의 주문 내역을 조회하는 메서드
    public List<OrdersVO> getOrdersByCusid(String cusid) throws SQLException {
        List<OrdersVO> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders"
                +" WHERE cusid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cusid);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrdersVO order = new OrdersVO();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setFoodId(rs.getInt("food_id"));
                    order.setFoodName(rs.getString("food_name"));
                    order.setCusid(rs.getString("cusid"));
                    order.setCusname(rs.getString("cusname"));
                    order.setQuantity(rs.getInt("quantity"));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    // 주문 내역 수정 메서드
    public boolean updateOrder(int orderId, String foodName, int quantity) throws SQLException {
        String sql = "UPDATE orders "
                +"SET food_name = ?, quantity = ? "
                + "WHERE order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, foodName);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, orderId);
            int updatedRows = pstmt.executeUpdate();
            return updatedRows > 0;
        }
    }

    // 주문 삭제 메서드
    public boolean deleteOrder(int orderId) throws SQLException {
        String sql = "DELETE FROM orders"
                +" WHERE order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            int deletedRows = pstmt.executeUpdate();
            return deletedRows > 0;
        }
    }

    // 주문 정보 유효성 검사 메서드
    public boolean isValid(OrdersVO order) throws SQLException {
        // cusid와 cusname 검증
        String customerSql = "SELECT COUNT(*) FROM customer WHERE cusid = ? AND cusname = ?";
        try (PreparedStatement customerStmt = conn.prepareStatement(customerSql)) {
            customerStmt.setString(1, order.getCusid());
            customerStmt.setString(2, order.getCusname());
            try (ResultSet customerRs = customerStmt.executeQuery()) {
                customerRs.next();
                int customerCount = customerRs.getInt(1);
                if (customerCount == 0) {
                    return false;
                }
            }
        }

        // foodid와 foodname 검증
        String foodSql = "SELECT COUNT(*) FROM food WHERE food_id = ? AND food_name = ?";
        try (PreparedStatement foodStmt = conn.prepareStatement(foodSql)) {
            foodStmt.setInt(1, order.getFoodId());
            foodStmt.setString(2, order.getFoodName());
            try (ResultSet foodRs = foodStmt.executeQuery()) {
                foodRs.next();
                int foodCount = foodRs.getInt(1);
                if (foodCount == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    // 유효한 고객 ID인지 확인하는 메서드
    public boolean isValidCustomerId(String cusid) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customer WHERE cusid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cusid);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);
                return count > 0;
            }
        }
    }
}