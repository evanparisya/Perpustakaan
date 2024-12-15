package backend;

import java.sql.*;
import java.util.ArrayList;

public class Peminjaman {
    private int idPeminjaman;
    private Anggota anggota;
    private Buku buku;
    private String tanggalPinjam;
    private String tanggalKembali;

    public Peminjaman() {
        this.anggota = new Anggota();
        this.buku = new Buku();
    }

    public Peminjaman(Anggota anggota, Buku buku, String tanggalPinjam, String tanggalKembali) {
        this.anggota = anggota;
        this.buku = buku;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
    }

    // Getters and Setters
    public int getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }

    public Anggota getAnggota() {
        return anggota;
    }

    public void setAnggota(Anggota anggota) {
        this.anggota = anggota;
    }

    public Buku getBuku() {
        return buku;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    public String getTanggalPinjam() {
        return tanggalPinjam;
    }

    public void setTanggalPinjam(String tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }

    public String getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(String tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    // Method to save or update data
    public void save() {
        if (getById(this.idPeminjaman) == null) {
            // Insert new record
            String sql = "INSERT INTO peminjaman (idanggota, idbuku, tanggalpinjam, tanggalkembali) VALUES (?, ?, ?, ?)";
            try (Connection conn = DBHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, this.anggota.getIdanggota());
                stmt.setInt(2, this.buku.getIdbuku());
                stmt.setString(3, this.tanggalPinjam);
                stmt.setString(4, this.tanggalKembali);
                stmt.executeUpdate();
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.idPeminjaman = generatedKeys.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Update existing record
            String sql = "UPDATE peminjaman SET idanggota = ?, idbuku = ?, tanggalpinjam = ?, tanggalkembali = ? WHERE idpeminjaman = ?";
            try (Connection conn = DBHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, this.anggota.getIdanggota());
                stmt.setInt(2, this.buku.getIdbuku());
                stmt.setString(3, this.tanggalPinjam);
                stmt.setString(4, this.tanggalKembali);
                stmt.setInt(5, this.idPeminjaman);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to delete data
    public void delete() {
        String sql = "DELETE FROM peminjaman WHERE idpeminjaman = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.idPeminjaman);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve data by ID
    public static Peminjaman getById(int id) {
        String sql = "SELECT * FROM peminjaman WHERE idpeminjaman = ?";
        Peminjaman peminjaman = null;
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    peminjaman = new Peminjaman();
                    peminjaman.setIdPeminjaman(rs.getInt("idpeminjaman"));
                    peminjaman.setAnggota(new Anggota().getById(rs.getInt("idanggota")));
                    peminjaman.setBuku(new Buku().getById(rs.getInt("idbuku")));
                    peminjaman.setTanggalPinjam(rs.getString("tanggalpinjam"));
                    peminjaman.setTanggalKembali(rs.getString("tanggalkembali"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return peminjaman;
    }

    // Method to retrieve all data
    public static ArrayList<Peminjaman> getAll() {
        ArrayList<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT * FROM peminjaman";
        try (Connection conn = DBHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Peminjaman peminjaman = new Peminjaman();
                peminjaman.setIdPeminjaman(rs.getInt("idpeminjaman"));
                peminjaman.setAnggota(new Anggota().getById(rs.getInt("idanggota")));
                peminjaman.setBuku(new Buku().getById(rs.getInt("idbuku")));
                peminjaman.setTanggalPinjam(rs.getString("tanggalpinjam"));
                peminjaman.setTanggalKembali(rs.getString("tanggalkembali"));
                list.add(peminjaman);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
