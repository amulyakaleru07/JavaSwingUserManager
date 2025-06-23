package sample2;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.sun.jdi.connect.spi.Connection;

public class SQLiteConnect {
    java.sql.Connection conn;

    public SQLiteConnect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:users.db");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (name TEXT, password TEXT, username TEXT PRIMARY KEY, role TEXT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertUser(String name, String password, String username, String role) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, username);
            ps.setString(4, role);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(String oldUsername, String name, String password, String username, String role) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET name=?, password=?, username=?, role=? WHERE username=?");
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, username);
            ps.setString(4, role);
            ps.setString(5, oldUsername);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String username) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE username=?");
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllUsers() {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM users");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
