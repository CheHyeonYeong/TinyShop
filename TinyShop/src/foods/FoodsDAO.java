package foods;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodsDAO {
    private String url = "jdbc:mysql://localhost:3306/jdbc";
    private String user = "jdbc";
    private String password = "jdbc";

    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public void createFoodTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Food (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "category VARCHAR(255) NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "description TEXT," +
                "price BIGINT NOT NULL" +
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
            createFoodTable();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertFood(FoodsVO vo) {
        int result = 0;
        String sql = "INSERT INTO Food (category, name, description, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vo.getCategory());
            pstmt.setString(2, vo.getName());
            pstmt.setString(3, vo.getDescription());
            pstmt.setLong(4, vo.getPrice());
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
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getLong("price")
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
        String sql = "UPDATE Food SET category = ?, name = ?, description = ?, price = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vo.getCategory());
            pstmt.setString(2, vo.getName());
            pstmt.setString(3, vo.getDescription());
            pstmt.setLong(4, vo.getPrice());
            pstmt.setInt(5, vo.getId());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteFood(int id) {
        int result = 0;
        String sql = "DELETE FROM Food WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}