package customers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerDAO {

    private String url = "jdbc:mysql://localhost:3306/tinyshop";
    private String user = "jdbc";
    private String password = "jdbc";
    private static CustomerDAO dao = new CustomerDAO();

    // 싱글톤 패턴을 사용하여 CustomerDAO 객체 생성
    private CustomerDAO() {
        // customer 테이블이 없으면 생성
        String sql = "CREATE TABLE IF NOT EXISTS customer (" +
                "cus_id VARCHAR(255) PRIMARY KEY NOT NULL," +
                "cus_pw VARCHAR(255) NOT NULL," +
                "cus_name VARCHAR(255) NOT NULL" +
                ")";
        System.out.println('.');
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("오류 발생 : " + e);
        }
    }

    // CustomerDAO 인스턴스를 반환하는 static 메서드
    public static CustomerDAO getInstance() {
        return dao;
    }

    // 데이터베이스 연결을 반환하는 메서드
    Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return conn;
    }

    // ResultSet, PreparedStatement, Connection을 닫는 메서드
    void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                System.out.println("오류 발생 : " + e);
            }
        }
        close(conn, ps);
    }

    // PreparedStatement, Connection을 닫는 메서드
    void close(Connection conn, PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception e) {
                System.out.println("오류 발생 : " + e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println("오류 발생 : " + e);
            }
        }
    }

    // 모든 사용자 목록을 반환하는 메서드
    public String[] userList() {
        ArrayList<String> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("select cus_id from customer order by cus_id desc");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("오류 발생 : " + e);
        } finally {
            close(conn, pstmt, rs);
        }
        return list.toArray(new String[0]);
    }

    // 이름으로 사용자를 검색하는 메서드
    public String[][] findByName(String name) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("select * from customer where cus_name like '%" + name + "%' order by cus_id desc;");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3)
                });
            }
        } catch (Exception e) {
            System.out.println("오류 발생 : " + e);
        } finally {
            close(conn, pstmt, rs);
        }
        String[][] arr = new String[list.size()][3];
        return list.toArray(arr);
    }

    // 새로운 사용자를 추가하는 메서드
    public void userInsert(CustomerVO user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("insert into customer(cus_id,cus_pw,cus_name) values(?,?,?)");
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPw());
            pstmt.setString(3, user.getName());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("오류 발생 : " + e);
        } finally {
            close(conn, pstmt);
        }
    }

    // 사용자의 존재 여부를 확인하는 메서드
    public boolean checkUserExists(String id, String pw) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("select * from customer where cus_id=? and cus_pw=?");
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("오류 발생 : " + e);
        } finally {
            close(conn, pstmt, rs);
        }
        return false;
    }

    // 사용자 정보를 업데이트하는 메서드
    public void updateUser(String id, String newPw, String newName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("update customer set cus_pw=?, name=? where cus_id=?");
            pstmt.setString(1, newPw);
            pstmt.setString(2, newName);
            pstmt.setString(3, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("오류 발생 : " + e);
        } finally {
            close(conn, pstmt);
        }
    }

    // 사용자를 삭제하는 메서드
    public void delete(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("delete from customer where cus_id=?");
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("오류 발생 : " + e);
        } finally {
            close(conn, pstmt);
        }
    }
}