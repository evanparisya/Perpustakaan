package backend;

import java.sql.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author WINDOWS 11
 */
public class DBHelper {
    private static Connection koneksi;

    public static void bukaKoneksi() {
        if (koneksi == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/dbperpus";
                String user = "root";
                String password = "";

                // Menggunakan Driver MySQL yang terbaru
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                koneksi = DriverManager.getConnection(url, user, password);
            } catch (SQLException t) {
                t.printStackTrace(); // Menampilkan log error
            }
        }
    }

    public static int insertQueryGetId(String query) {
        bukaKoneksi();
        int result = -1;
        try (Statement stmt = koneksi.createStatement()) {
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    result = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean executeQuery(String query) {
        bukaKoneksi();
        boolean result = false;
        try (Statement stmt = koneksi.createStatement()) {
            stmt.executeUpdate(query);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ResultSet selectQuery(String query) {
        bukaKoneksi();
        try {
            Statement stmt = koneksi.createStatement();
            return stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}