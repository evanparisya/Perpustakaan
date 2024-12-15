/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

/**
 *
 * @author MSI GF
 */
import java.util.ArrayList;
import java.sql.*;

public class Buku {

    private int idbuku;
    private Kategori kategori;
    private String judul;
    private String penerbit;
    private String penulis;

    public Buku() {
        kategori = new Kategori();
    }

    public Buku(Kategori kategori, String judul, String penerbit, String penulis) {
        this.kategori = kategori;
        this.judul = judul;
        this.penerbit = penerbit;
        this.penulis = penulis;
    }

    public int getIdbuku() {
        return idbuku;
    }

    public void setIdbuku(int idbuku) {
        this.idbuku = idbuku;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public Buku getById(int id) {
        Buku buku = new Buku();
        String sql = "SELECT b.*, k.nama as kategori, k.keterangan "
                + "FROM buku b LEFT JOIN kategori k ON b.idkategori = k.idkategori "
                + "WHERE b.idbuku = '" + id + "'";

        ResultSet rs = DBHelper.selectQuery(sql);

        try {
            while (rs.next()) {
                buku.setIdbuku(rs.getInt("idbuku"));
                buku.getKategori().setIdkategori(rs.getInt("idkategori"));
                buku.getKategori().setNama(rs.getString("kategori"));
                buku.getKategori().setKeterangan(rs.getString("keterangan"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buku;
    }

    public ArrayList<Buku> getAll() {
        ArrayList<Buku> listBuku = new ArrayList();
        String sql = "SELECT b.*, k.nama as kategori, k.keterangan "
                + "FROM buku b LEFT JOIN kategori k ON b.idkategori = k.idkategori";
        ResultSet rs = DBHelper.selectQuery(sql);

        try {
            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdbuku(rs.getInt("idbuku"));
                buku.getKategori().setIdkategori(rs.getInt("idkategori"));
                buku.getKategori().setNama(rs.getString("kategori"));
                buku.getKategori().setKeterangan(rs.getString("keterangan"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));

                listBuku.add(buku);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listBuku;
    }

    public ArrayList<Buku> search(String keyword) {
        ArrayList<Buku> listBuku = new ArrayList();
        String sql = "SELECT b.*, k.nama as kategori, k.keterangan "
                + "FROM buku b LEFT JOIN kategori k ON b.idkategori = k.idkategori "
                + "WHERE b.judul LIKE '%" + keyword + "%' "
                + "OR b.penerbit LIKE '%" + keyword + "%' "
                + "OR b.penulis LIKE '%" + keyword + "%'";
        ResultSet rs = DBHelper.selectQuery(sql);

        try {
            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdbuku(rs.getInt("idbuku"));
                buku.getKategori().setIdkategori(rs.getInt("idkategori"));
                buku.getKategori().setNama(rs.getString("kategori"));
                buku.getKategori().setKeterangan(rs.getString("keterangan"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));

                listBuku.add(buku);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listBuku;
    }

    public void save() {
        if (getById(idbuku).getIdbuku() == 0) {
            String SQL = "INSERT INTO buku (idkategori, judul, penerbit, penulis) VALUES("
                    + this.getKategori().getIdkategori() + ", "
                    + "'" + this.judul + "', "
                    + "'" + this.penerbit + "', "
                    + "'" + this.penulis + "'"
                    + ")";
            this.idbuku = DBHelper.insertQueryGetId(SQL);
        } else {
            String SQL = "UPDATE buku SET "
                    + "idkategori = " + this.getKategori().getIdkategori() + ", "
                    + "judul = '" + this.judul + "', "
                    + "penerbit = '" + this.penerbit + "', "
                    + "penulis = '" + this.penulis + "' "
                    + "WHERE idbuku = '" + this.idbuku + "'";
            DBHelper.executeQuery(SQL);
        }
    }

    public void delete() {
        String SQL = "DELETE FROM buku WHERE idbuku = '" + this.idbuku + "'";
        DBHelper.executeQuery(SQL);
    }
}
