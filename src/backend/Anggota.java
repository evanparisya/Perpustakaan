/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author WINDOWS 11
 */
public class Anggota {
    private int idanggota;
    private  String nama;
    private String alamat;
    private String telepon;

    public int getIdanggota() {
        return idanggota;
    }

    public void setIdanggota(int idanggota) {
        this.idanggota = idanggota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public Anggota() {
    }

    public Anggota(String nama, String alamat, String telepon) {
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
    }
    
    
    public Anggota getById(int id) {
        Anggota anggota01 = new Anggota();
        ResultSet rs = DBHelper.selectQuery("SELECT * FROM anggota WHERE idanggota = " + id + ";");
        try {
            while (rs.next()) {
                anggota01 = new Anggota();
                anggota01.setIdanggota(rs.getInt("idanggota"));
                anggota01.setNama(rs.getString("nama"));
                anggota01.setAlamat(rs.getString("alamat"));
                anggota01.setTelepon(rs.getString("telepon"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return anggota01;
    }
    
    public ArrayList<Anggota> getAll() {
        ArrayList<Anggota> listAnggota = new ArrayList<>();
        ResultSet rs = DBHelper.selectQuery("SELECT * FROM anggota");
        try {
            while (rs.next()) {
                Anggota anggota01 = new Anggota();
                anggota01.setIdanggota(rs.getInt("idanggota"));
                anggota01.setNama(rs.getString("nama"));
                anggota01.setAlamat(rs.getString("alamat"));
                anggota01.setTelepon(rs.getString("telepon"));
                listAnggota.add(anggota01);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAnggota;
    }

    public ArrayList<Anggota> search(String keyword) {
        ArrayList<Anggota> listAnggota = new ArrayList<>();
        String sql = "SELECT * FROM anggota WHERE " +
                     "nama LIKE '%" + keyword + "%' " +
                     "OR alamat LIKE '%" + keyword + "%'";
        ResultSet rs = DBHelper.selectQuery(sql);
        try {
            while (rs.next()) {
                Anggota anggota01 = new Anggota();
                anggota01.setIdanggota(rs.getInt("idanggota"));
                anggota01.setNama(rs.getString("nama"));
                anggota01.setAlamat(rs.getString("alamat"));
                anggota01.setTelepon(rs.getString("telepon"));
                listAnggota.add(anggota01);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAnggota;
    }

    public void save() {
        if (getById(idanggota).getIdanggota()== 0) {
            String SQL = "INSERT INTO anggota (nama, alamat, telepon) VALUES (" +
                         "'" + this.nama + "', " +
                         "'" + this.alamat + "', " +
                         "'" + this.telepon + "' )";
            this.idanggota = DBHelper.insertQueryGetId(SQL);
        } else {
            String SQL = "UPDATE anggota SET " +
             "nama = '" + this.nama + "', " +
             "alamat = '" + this.alamat + "', " +  
             "telepon = '" + this.telepon + "' " +
             "WHERE idanggota = " + this.idanggota;
            DBHelper.executeQuery(SQL);
        }
    }

    public void delete(){
        String SQL = "DELETE FROM anggota WHERE idanggota = '" + this.idanggota + "'";
        DBHelper.executeQuery(SQL);
    }
}