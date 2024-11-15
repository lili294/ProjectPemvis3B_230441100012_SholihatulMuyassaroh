/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author User
 */
public class DataDokter extends javax.swing.JPanel {
    
    private DefaultTableModel modelDokter;

    /**
     * Creates new form DataDokter
     */
    public DataDokter() {
        initComponents(); 
        
        modelDokter = new DefaultTableModel();
        tbDokter.setModel(modelDokter);
        modelDokter.addColumn("ID Dokter");
        modelDokter.addColumn("Nama Dokter");
        modelDokter.addColumn("NIP");
        modelDokter.addColumn("Lokasi Praktik");
        modelDokter.addColumn("Spesialis");
        loadDataDokter();
        
        tbDokter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tbDokter.getSelectedRow();
                if (row != -1) {
                    tfidd.setText(String.valueOf(modelDokter.getValueAt(row, 0)));
                    tfnamad.setText((String) modelDokter.getValueAt(row, 1));
                    tfnip.setText((String) modelDokter.getValueAt(row, 2));
                    tflokasiprak.setText((String) modelDokter.getValueAt(row, 3));
                    tfspesialis.setText((String) modelDokter.getValueAt(row, 4));
                }
            }
        });
        
    }

    
    private void loadDataDokter() {
        modelDokter.setRowCount(0);
        String sql = "SELECT * FROM tb_data_dokter"; 
        try (Connection conn = Koneksi.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelDokter.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("nip"),
                        rs.getString("lokasi_praktik"),
                        rs.getString("spesialis")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Load Data Dokter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean NipAda(String nip) {
        String sql = "SELECT COUNT(*) FROM tb_data_dokter WHERE nip = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nip);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error memeriksa NIP: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private boolean LokasiPraktikAda(String lokasiPraktik) {
        String sql = "SELECT COUNT(*) FROM tb_data_dokter WHERE lokasi_praktik = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lokasiPraktik);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error memeriksa lokasi praktik: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private void addDataDokter() {
        if (tfnamad.getText().isEmpty() || tfnip.getText().isEmpty() || tflokasiprak.getText().isEmpty() || tfspesialis.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi semua data dokter terlebih dahulu", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (NipAda(tfnip.getText())) {
            JOptionPane.showMessageDialog(this, "NIP sudah terdaftar", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (LokasiPraktikAda(tflokasiprak.getText())) {
            JOptionPane.showMessageDialog(this, "Lokasi praktik sudah terpakai", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO tb_data_dokter (nama, nip, lokasi_praktik, spesialis) VALUES (?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tfnamad.getText());
            ps.setString(2, tfnip.getText());
            ps.setString(3, tflokasiprak.getText());
            ps.setString(4, tfspesialis.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data dokter berhasil ditambahkan");
            loadDataDokter();
            tfidd.setText("");
            tfnamad.setText("");
            tfnip.setText("");
            tflokasiprak.setText("");
            tfspesialis.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Add Data Dokter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDataDokter() {
        if (tfidd.getText().isEmpty() || tfnamad.getText().isEmpty() || tfnip.getText().isEmpty() || tflokasiprak.getText().isEmpty() || tfspesialis.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data dokter yang akan diupdate", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE tb_data_dokter SET nama = ?, nip = ?, lokasi_praktik = ?, spesialis = ? WHERE id = ?";
        try (Connection conn = Koneksi.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tfnamad.getText());
            ps.setString(2, tfnip.getText());
            ps.setString(3, tflokasiprak.getText());
            ps.setString(4, tfspesialis.getText());
            ps.setInt(5, Integer.parseInt(tfidd.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data dokter berhasil diperbarui");
            loadDataDokter(); 
            tfidd.setText("");
            tfnamad.setText("");
            tfnip.setText("");
            tflokasiprak.setText("");
            tfspesialis.setText("");
            DataPasien.loadDataPasien(); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Update Data Dokter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteDataDokter() {
        if (tfidd.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data dokter yang akan dihapus", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tb_data_dokter WHERE id = ?";
            try (Connection conn = Koneksi.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, Integer.parseInt(tfidd.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data dokter berhasil dihapus");
                loadDataDokter();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error Delete Data Dokter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void reset() {
        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin mereset halaman ini?", "Konfirmasi Reset", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tfidd.setText("");
            tfnamad.setText("");
            tfnip.setText("");
            tflokasiprak.setText("");
            tfspesialis.setText("");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AdminHalut3 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        tfidd = new javax.swing.JTextField();
        tfnamad = new javax.swing.JTextField();
        tfspesialis = new javax.swing.JTextField();
        bttambah = new javax.swing.JButton();
        btupdate = new javax.swing.JButton();
        btreset = new javax.swing.JButton();
        bthapus = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbDokter = new javax.swing.JTable();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        tflokasiprak = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        tfnip = new javax.swing.JTextField();

        AdminHalut3.setBackground(new java.awt.Color(0, 153, 153));

        jLabel33.setFont(new java.awt.Font("Sitka Text", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("DATA DOKTER");

        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("ID Dokter :");

        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Nama Dokter :");

        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Lokasi Praktik :");

        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Spesialis :");

        tfidd.setEditable(false);

        bttambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/simpan.png"))); // NOI18N
        bttambah.setText("Tambah");
        bttambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttambahActionPerformed(evt);
            }
        });

        btupdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/edit.png"))); // NOI18N
        btupdate.setText("Update");
        btupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btupdateActionPerformed(evt);
            }
        });

        btreset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/reset.png"))); // NOI18N
        btreset.setText("Reset");
        btreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btresetActionPerformed(evt);
            }
        });

        bthapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/hapus.png"))); // NOI18N
        bthapus.setText("Hapus");
        bthapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bthapusActionPerformed(evt);
            }
        });

        tbDokter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Dokter", "Nama Dokter", "NIP", "Lokasi Praktik", "Spesialis"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbDokter);

        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/logo kesehatan.png"))); // NOI18N

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/logo kesehatan.png"))); // NOI18N

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");

        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");

        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("NIP :");

        javax.swing.GroupLayout AdminHalut3Layout = new javax.swing.GroupLayout(AdminHalut3);
        AdminHalut3.setLayout(AdminHalut3Layout);
        AdminHalut3Layout.setHorizontalGroup(
            AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminHalut3Layout.createSequentialGroup()
                .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AdminHalut3Layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(jLabel34)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(AdminHalut3Layout.createSequentialGroup()
                                .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfidd, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfnamad, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(37, 37, 37)
                                .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(AdminHalut3Layout.createSequentialGroup()
                                        .addComponent(jLabel41)
                                        .addGap(28, 28, 28)
                                        .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(tfspesialis, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tflokasiprak, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(tfnip, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(AdminHalut3Layout.createSequentialGroup()
                        .addGap(330, 330, 330)
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel33)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AdminHalut3Layout.createSequentialGroup()
                        .addGap(338, 338, 338)
                        .addComponent(bttambah)
                        .addGap(31, 31, 31)
                        .addComponent(btupdate)
                        .addGap(33, 33, 33)
                        .addComponent(bthapus)
                        .addGap(32, 32, 32)
                        .addComponent(btreset)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(AdminHalut3Layout.createSequentialGroup()
                .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 1294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1011, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );
        AdminHalut3Layout.setVerticalGroup(
            AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminHalut3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addGap(45, 45, 45)
                .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel34)
                    .addComponent(tfidd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tflokasiprak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(tfnamad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42)
                    .addComponent(tfspesialis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(tfnip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(AdminHalut3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bttambah)
                    .addComponent(btupdate)
                    .addComponent(bthapus)
                    .addComponent(btreset))
                .addGap(22, 22, 22)
                .addComponent(jLabel48)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1306, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(AdminHalut3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 6, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 854, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(AdminHalut3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 27, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bttambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttambahActionPerformed
        addDataDokter();
    }//GEN-LAST:event_bttambahActionPerformed

    private void btupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btupdateActionPerformed
        updateDataDokter();
    }//GEN-LAST:event_btupdateActionPerformed

    private void bthapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bthapusActionPerformed
        deleteDataDokter();
        tfidd.setText("");
        tfnamad.setText("");
        tfnip.setText("");
        tflokasiprak.setText("");
        tfspesialis.setText("");
    }//GEN-LAST:event_bthapusActionPerformed

    private void btresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btresetActionPerformed
        reset();
    }//GEN-LAST:event_btresetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AdminHalut3;
    private javax.swing.JButton bthapus;
    private javax.swing.JButton btreset;
    private javax.swing.JButton bttambah;
    private javax.swing.JButton btupdate;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tbDokter;
    private javax.swing.JTextField tfidd;
    private javax.swing.JTextField tflokasiprak;
    private javax.swing.JTextField tfnamad;
    private javax.swing.JTextField tfnip;
    private javax.swing.JTextField tfspesialis;
    // End of variables declaration//GEN-END:variables
}
