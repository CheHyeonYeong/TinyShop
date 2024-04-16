package customers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerDAO {

    private String url = "jdbc:mysql://localhost:3306/jdbctest";
    private String user = "jdbc";
    private String password = "jdbc";
    private static CustomerDAO dao = new CustomerDAO();

    /**
     * 기본 생성자
     * user 테이블이 없는 경우 생성
     */
    private CustomerDAO() {
        String sql = "CREATE TABLE IF NOT EXISTS tinyshop (" +
                "id VARCHAR(255) PRIMARY KEY NOT NULL," +
                "pw VARCHAR(255) NOT NULL," +
                "name VARCHAR(255) NOT NULL" +
                ")";
        System.out.println('.');
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("오류 발생 : " + e);
        }
    }

    public static CustomerDAO getInstance() {
        return dao;
    }

    /**
     * 데이터베이스 연결을 위한 Connection 객체 반환
     *
     * @return Connection 객체
     */
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

    /**
     * ResultSet, PreparedStatement, Connection 객체 Close

     */
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

    /**
     * PreparedStatement, Connection 객체 Close

     */
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

    /**
     * 모든 회원 정보 조회
     *
     * @return 회원 정보 2차원 배열
     */
    public String[][] userList() {
        ArrayList<String[]> list = new ArrayList<String[]>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("select * from tinyshop order by id desc;");
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

    /**
     * 이름으로 회원 정보 검색
     */
    public String[][] findByName(String name) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("select * from tinyshop where name like '%" + name + "%' order by id desc;");
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

    /**
     * 회원 정보 삽입
     */
    public void userInsert(CustomerVO user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("insert into tinyshop(id,pw,name) values(?,?,?)");
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

    /**
     * 회원 정보 수정
     *
     */
    public boolean checkUserExists(String id, String pw) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("select * from tinyshop where id=? and pw=?");
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
    public void updateUser(String id, String newPw, String newName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("update tinyshop set pw=?, name=? where id=?");
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

    /**
     * 회원 정보 삭제
     *
     */
    public void delete(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("delete from tinyshop where id=?");
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("오류 발생 : " + e);
        } finally {
            close(conn, pstmt);
        }
    }
}