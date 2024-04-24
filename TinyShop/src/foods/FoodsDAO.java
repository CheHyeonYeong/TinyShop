package foods;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodsDAO {
    private String url = "jdbc:mysql://localhost:3306/tinyshop";
    private String user = "jdbc";
    private String password = "jdbc";

    private Connection conn = null;

    public void FoodTable() {
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

    public FoodsDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            FoodTable();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertFood(FoodsVO vo) {
        int result = 0;
        String sql = "INSERT INTO Food (food_category, food_name, food_description, food_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vo.getFoodCategory());
            pstmt.setString(2, vo.getFoodName());
            pstmt.setString(3, vo.getFoodDescription());
            pstmt.setLong(4, vo.getFoodPrice());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<FoodsVO> getAllFoods() {
        List<FoodsVO> foods = new ArrayList<>();
        String sql = "SELECT * FROM Food";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                FoodsVO vo = new FoodsVO(
                        rs.getInt("food_id"),
                        rs.getString("food_category"),
                        rs.getString("food_name"),
                        rs.getString("food_description"),
                        rs.getLong("food_price")
                );
                foods.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    public int updateFood(FoodsVO vo) {
        int result = 0;
        String sql = "UPDATE Food SET food_category = ?, food_name = ?, food_description = ?, food_price = ? WHERE food_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vo.getFoodCategory());
            pstmt.setString(2, vo.getFoodName());
            pstmt.setString(3, vo.getFoodDescription());
            pstmt.setLong(4, vo.getFoodPrice());
            pstmt.setInt(5, vo.getFoodId());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteFood(int food_id) {
        int result = 0;
        String sql = "DELETE FROM Food WHERE food_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, food_id);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}