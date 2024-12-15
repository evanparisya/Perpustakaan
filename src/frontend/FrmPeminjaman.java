package frontend;

import backend.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmPeminjaman extends javax.swing.JFrame {

    public FrmPeminjaman() {
        initComponents();
        tampilkanDataPeminjaman();
        kosongkanForm();
    }

    private void kosongkanForm() {
        txtIdPeminjaman.setText(String.valueOf(getNextId())); // Set the next ID
        txtIdAnggota.setText("");
        txtIdBuku.setText("");
        txtTanggalPinjam.setText("");
        txtTanggalKembali.setText(""); // Clear the Tanggal Kembali field
        lblNamaAnggota.setText("Nama Anggota");
        lblJudulBuku.setText("Judul Buku");
        setFieldsEnabled(true); // Enable all fields initially
        txtTanggalKembali.setEnabled(false); // Disable Tanggal Kembali initially
    }

    private int getNextId() {
        // Fetch the last ID from the database and return the next ID
        ArrayList<Peminjaman> list = new Peminjaman().getAll();
        if (list.isEmpty()) {
            return 1; // Start from 1 if no records exist
        } else {
            int lastId = list.get(list.size() - 1).getIdPeminjaman(); // Assuming getIdPeminjaman() returns the ID
            return lastId + 1; // Increment the last ID
        }
    }

    private void setFieldsEnabled(boolean enabled) {
        txtIdAnggota.setEnabled(enabled);
        txtIdBuku.setEnabled(enabled);
        txtTanggalPinjam.setEnabled(enabled);
        txtTanggalKembali.setEnabled(!enabled); // Tanggal Kembali will be disabled when creating a new entry
        txtIdPeminjaman.setEnabled(false); // ID Peminjaman should remain disabled
    }

  private void tampilkanDataPeminjaman() {
    String[] kolom = {"ID", "ID Anggota", "Nama Anggota", "ID Buku", "Judul Buku", "Tanggal Pinjam", "Tanggal Kembali"};
    ArrayList<Peminjaman> list = new Peminjaman().getAll();
    Object rowData[] = new Object[7];

    tblPeminjaman.setModel(new DefaultTableModel(new Object[][]{}, kolom));

    for (Peminjaman p : list) {
        rowData[0] = p.getIdPeminjaman(); // ID Peminjaman
        rowData[1] = p.getAnggota().getIdanggota(); // ID Anggota
        rowData[2] = p.getAnggota().getNama(); // Nama Anggota
        rowData[3] = p.getBuku().getIdbuku(); // ID Buku
        rowData[4] = p.getBuku().getJudul(); // Judul Buku
        rowData[5] = p.getTanggalPinjam(); // Tanggal Pinjam
        rowData[6] = p.getTanggalKembali(); // Tanggal Kembali

        ((DefaultTableModel) tblPeminjaman.getModel()).addRow(rowData);
    }
}




    private void cariAnggota() {
        String idAnggota = txtIdAnggota.getText().trim(); // Trim whitespace
        if (!isNumeric(idAnggota)) {
            JOptionPane.showMessageDialog(null, "ID Anggota harus berupa angka.");
            return;
        }
        Anggota anggota = new Anggota().getById(Integer.parseInt(idAnggota));
        if (anggota != null) {
            lblNamaAnggota.setText("Nama Anggota: " + anggota.getNama());
        } else {
            lblNamaAnggota.setText("Anggota tidak ditemukan.");
        }
    }

    private void cariBuku() {
        String idBuku = txtIdBuku.getText().trim(); // Trim whitespace
        if (!isNumeric(idBuku)) {
            JOptionPane.showMessageDialog(null, "ID Buku harus berupa angka.");
            return;
        }
        Buku buku = new Buku().getById(Integer.parseInt(idBuku));
        if (buku != null) {
            lblJudulBuku.setText("Judul Buku: " + buku.getJudul());
        } else {
            lblJudulBuku.setText("Buku tidak ditemukan.");
        }
    }


   private void simpanPeminjaman() {
    String idAnggota = txtIdAnggota.getText().trim(); // Trim whitespace
    String idBuku = txtIdBuku.getText().trim(); // Trim whitespace
    String tanggalPinjam = txtTanggalPinjam.getText().trim(); // Trim whitespace
    String tanggalKembali = txtTanggalKembali.getText().trim(); // Trim whitespace

   // Validate ID Anggota
    if (idAnggota.isEmpty() || !isNumeric(idAnggota)) {
        JOptionPane.showMessageDialog(null, "ID Anggota harus berupa angka.");
        return;
    }

    // Validate ID Buku
    if (idBuku.isEmpty() || !isNumeric(idBuku)) {
        JOptionPane.showMessageDialog(null, "ID Buku harus berupa angka.");
        return;
    }

    Anggota anggota = new Anggota().getById(Integer.parseInt(idAnggota));
    Buku buku = new Buku().getById(Integer.parseInt(idBuku));

    if (anggota != null && buku != null) {
        if (tanggalKembali.isEmpty()) {
            tanggalKembali = null; // Allow empty
        }

        Peminjaman peminjaman = new Peminjaman(anggota, buku, tanggalPinjam, tanggalKembali);
        
        // Check if we are updating or creating a new entry
        if (txtIdPeminjaman.getText().isEmpty() || txtIdPeminjaman.getText().equals("0")) {
            // New entry
            peminjaman.save();
            JOptionPane.showMessageDialog(null, "Peminjaman berhasil disimpan.");
        } else {
            // Existing entry, set the ID for update
            peminjaman.setIdPeminjaman(Integer.parseInt(txtIdPeminjaman.getText()));
            peminjaman.save(); // Update existing record
            JOptionPane.showMessageDialog(null, "Peminjaman berhasil diupdate.");
        }

        tampilkanDataPeminjaman();
        kosongkanForm();
    } else {
        JOptionPane.showMessageDialog(null, "Anggota atau Buku tidak ditemukan.");
    }
}



    // Helper method to check if a string is numeric
   private boolean isNumeric(String str) {
    if (str == null || str.trim().isEmpty()) {
        return false; // Handle empty strings
    }
    str = str.trim(); // Trim whitespace
    if (str.length() > 1 && str.startsWith("0")) {
        return false; // Reject leading zeros
    }
    try {
        Integer.parseInt(str);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}


    private void hapusPeminjaman() {
        String idPeminjaman = txtIdPeminjaman.getText();
        Peminjaman peminjaman = new Peminjaman();
        peminjaman.setIdPeminjaman(Integer.parseInt(idPeminjaman));
        peminjaman.delete();
        JOptionPane.showMessageDialog(null, "Peminjaman berhasil dihapus.");
        tampilkanDataPeminjaman();
        kosongkanForm();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtIdPeminjaman = new javax.swing.JTextField();
        txtIdAnggota = new javax.swing.JTextField();
        txtIdBuku = new javax.swing.JTextField();
        txtTanggalPinjam = new javax.swing.JTextField();
        txtTanggalKembali = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPeminjaman = new javax.swing.JTable();
        lblNamaAnggota = new javax.swing.JLabel();
        lblJudulBuku = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String[] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("ID");

        jLabel2.setText("ID Anggota");

        jLabel3.setText("ID Buku");

        jLabel4.setText("Tanggal Pinjam");

        jLabel5.setText("Tanggal Kembali");

        txtIdPeminjaman.setEditable(false);

        // Button actions
        jButton1.setText("Cari Anggota");
        jButton1.addActionListener(evt -> cariAnggota());

        jButton2.setText("Cari Buku");
        jButton2.addActionListener(evt -> cariBuku());

        jButton3.setText("Simpan");
        jButton3.addActionListener(evt -> simpanPeminjaman());

        jButton4.setText("Tambah Baru");
        jButton4.addActionListener(evt -> kosongkanForm());

        jButton5.setText("Hapus");
        jButton5.addActionListener(evt -> hapusPeminjaman());

        tblPeminjaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String[] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblPeminjaman);

        // Add mouse listener to the table
     tblPeminjaman.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent evt) {
        int row = tblPeminjaman.getSelectedRow();
        if (row != -1) {
            // Get the selected data and populate the fields
            Object idPeminjamanValue = tblPeminjaman.getValueAt(row, 0);
            Object idAnggotaValue = tblPeminjaman.getValueAt(row, 1); // ID Anggota
            Object idBukuValue = tblPeminjaman.getValueAt(row, 3); // ID Buku
            Object tanggalPinjamValue = tblPeminjaman.getValueAt(row, 5);
            Object tanggalKembaliValue = tblPeminjaman.getValueAt(row, 6);

            // Set the text fields with the correct ID values
            txtIdPeminjaman.setText(idPeminjamanValue != null ? idPeminjamanValue.toString() : "");
            txtIdAnggota.setText(idAnggotaValue != null ? idAnggotaValue.toString() : ""); // Set ID Anggota
            txtIdBuku.setText(idBukuValue != null ? idBukuValue.toString() : ""); // Set ID Buku
            txtTanggalPinjam.setText(tanggalPinjamValue != null ? tanggalPinjamValue.toString() : "");
            txtTanggalKembali.setText(tanggalKembaliValue != null ? tanggalKembaliValue.toString() : "");

            // Disable all fields except for Tanggal Kembali
            setFieldsEnabled(false);
            txtTanggalKembali.setEnabled(true); // Enable Tanggal Kembali for editing
        }
    }
});




        lblNamaAnggota.setText("Nama Anggota");

        lblJudulBuku.setText("Judul Buku");

        jLabel8.setText("Format : YYYY/MM/DD");

        jLabel9.setText("Format : YYYY/MM/DD");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jButton4))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtIdPeminjaman)
                                    .addComponent(txtIdAnggota)
                                    .addComponent(txtIdBuku)
                                    .addComponent(txtTanggalPinjam)
                                    .addComponent(txtTanggalKembali, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton1)
                                            .addComponent(jButton2))
                                        .addGap(26, 26, 26)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblJudulBuku)
                                            .addComponent(lblNamaAnggota)))
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))))))
                .addContainerGap(56, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIdPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(lblNamaAnggota))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtIdBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(lblJudulBuku))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTanggalPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTanggalKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addGap(43, 43, 43)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FrmPeminjaman().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblJudulBuku;
    private javax.swing.JLabel lblNamaAnggota;
    private javax.swing.JTable tblPeminjaman;
    private javax.swing.JTextField txtIdAnggota;
    private javax.swing.JTextField txtIdBuku;
    private javax.swing.JTextField txtIdPeminjaman;
    private javax.swing.JTextField txtTanggalKembali;
    private javax.swing.JTextField txtTanggalPinjam;
    // End of variables declaration                   
}