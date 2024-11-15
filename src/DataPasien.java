/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
/**
 *
 * @author User
 */
public class DataPasien extends javax.swing.JPanel {
    
    private static DefaultTableModel modelPasien;
    
    /**
     * Creates new form DataPasien
     */
    public DataPasien() {
        initComponents();
     
        modelPasien = new DefaultTableModel();
        tbdatapasien.setModel(modelPasien);
        modelPasien.addColumn("ID Pasien");
        modelPasien.addColumn("Nama Pasien");
        modelPasien.addColumn("Tanggal Lahir");
        modelPasien.addColumn("Jenis Kelamin");
        modelPasien.addColumn("Alamat");
        modelPasien.addColumn("No. Telepon");
        modelPasien.addColumn("Keluhan");
        modelPasien.addColumn("Pemeriksaan");
        modelPasien.addColumn("Dokter");
        modelPasien.addColumn("Lokasi Periksa");
        modelPasien.addColumn("Tanggal Periksa");
        loadDataPasien();
        loadComboBoxPemeriksaan();
        loadComboBoxDokter();

        cbperiksa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cbperiksa.getSelectedIndex() > 0) {
                    updateDokterComboBox();
                } else {
                    cbdokter.removeAllItems();
                    cbdokter.addItem("---- Pilih Dokter ----");
                    tflokasip.setText("");
                }
            }
        });

        cbdokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cbdokter.getSelectedIndex() > 0) {
                    updateLokasiPeriksa();
                } else {
                    tflokasip.setText("");
                }
            }
        });

        tbdatapasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tbdatapasien.getSelectedRow();
                if (row != -1) {
                    bttambah.setEnabled(false);
                    tfidp.setText(String.valueOf(modelPasien.getValueAt(row, 0)));
                    tfnamap.setText((String) modelPasien.getValueAt(row, 1));

                    // konversi string ke Date 
                    String tanggalLahirStr = (String) modelPasien.getValueAt(row, 2);
                    java.util.Date tanggalLahir = java.sql.Date.valueOf(tanggalLahirStr);
                    jdtanggall.setDate(tanggalLahir);

                    cbjk.setSelectedItem(modelPasien.getValueAt(row, 3));
                    tfalamat.setText((String) modelPasien.getValueAt(row, 4));
                    tfnot.setText((String) modelPasien.getValueAt(row, 5));
                    tfkeluhan.setText((String) modelPasien.getValueAt(row, 6));
                    cbperiksa.setSelectedItem(modelPasien.getValueAt(row, 7));
                    cbdokter.setSelectedItem(modelPasien.getValueAt(row, 8));
                    tflokasip.setText((String) modelPasien.getValueAt(row, 9));

                    String tanggalPeriksaStr = (String) modelPasien.getValueAt(row, 10);
                    java.util.Date tanggalPeriksa = java.sql.Date.valueOf(tanggalPeriksaStr);
                    jdtanggalp.setDate(tanggalPeriksa);
                }
            }
        });
    }
    
   public static void loadDataPasien() {
        if (modelPasien == null) {
            JOptionPane.showMessageDialog(null, "Model pasien belum diinisialisasi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        modelPasien.setRowCount(0);
        String sql = "SELECT * FROM tb_data_pasien";
        try (Connection conn = Koneksi.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelPasien.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nama_pasien"),
                        rs.getString("tanggal_lahir"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("alamat"),
                        rs.getString("no_telepon"),
                        rs.getString("keluhan"),
                        rs.getString("pemeriksaan"),
                        rs.getString("dokter"),
                        rs.getString("lokasi_periksa"),
                        rs.getString("tanggal_periksa")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Load Data Pasien: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
   


    private void loadComboBoxPemeriksaan() {
        try {
            String sql = "SELECT DISTINCT spesialis FROM tb_data_dokter"; 
            try (Connection conn = Koneksi.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                cbperiksa.removeAllItems();
                cbperiksa.addItem("---- Pilih Pemeriksaan ----");
                while (rs.next()) {
                    cbperiksa.addItem(rs.getString("spesialis"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Load ComboBox Pemeriksaan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   private void loadComboBoxDokter() {
        try {
            String sql = "SELECT nama FROM tb_data_dokter";
            try (Connection conn = Koneksi.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                cbdokter.removeAllItems();
                cbdokter.addItem("---- Pilih Dokter ----");
                while (rs.next()) {
                    cbdokter.addItem(rs.getString("nama"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Load ComboBox Dokter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDokterComboBox() {
        try {
            String selectedSpesialis = (String) cbperiksa.getSelectedItem();
            String sql = "SELECT nama, lokasi_praktik FROM tb_data_dokter WHERE spesialis = ?";
            try (Connection conn = Koneksi.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, selectedSpesialis);
                try (ResultSet rs = ps.executeQuery()) {
                    cbdokter.removeAllItems();
                    cbdokter.addItem("---- Pilih Dokter ----"); 
                    while (rs.next()) {
                        cbdokter.addItem(rs.getString("nama")); 
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Update ComboBox Dokter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateLokasiPeriksa() {
        try {
            String selectedDokter = (String) cbdokter.getSelectedItem();
            String sql = "SELECT lokasi_praktik FROM tb_data_dokter WHERE nama = ?"; 
            try (Connection conn = Koneksi.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, selectedDokter);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        tflokasip.setText(rs.getString("lokasi_praktik")); 
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Update Lokasi Periksa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDataPasien() {
        if (tfnamap.getText().isEmpty() || jdtanggall.getDate() == null || tfalamat.getText().isEmpty() || tfnot.getText().isEmpty() || tfkeluhan.getText().isEmpty() || tflokasip.getText().isEmpty() || jdtanggalp.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Isi semua data pasien terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cbjk.getSelectedIndex() == 0 || cbdokter.getSelectedIndex() == 0 || cbperiksa.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih jenis kelamin, dokter, dan pemeriksaan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return; 
        }

        try {
            int idDokter = getIdDokter((String) cbdokter.getSelectedItem());

            String sql = "INSERT INTO tb_data_pasien (nama_pasien, tanggal_lahir, jenis_kelamin, alamat, no_telepon, keluhan, pemeriksaan, dokter, lokasi_periksa, tanggal_periksa, id_dokter) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = Koneksi.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tfnamap.getText());
                ps.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(jdtanggall.getDate()));
                ps.setString(3, (String) cbjk.getSelectedItem());
                ps.setString(4, tfalamat.getText());
                ps.setString(5, tfnot.getText());
                ps.setString(6, tfkeluhan.getText());
                ps.setString(7, (String) cbperiksa.getSelectedItem());
                ps.setString(8, (String) cbdokter.getSelectedItem());
                ps.setString(9, tflokasip.getText());
                ps.setString(10, new SimpleDateFormat("yyyy-MM-dd").format(jdtanggalp.getDate()));
                ps.setInt(11, idDokter);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data pasien berhasil ditambahkan", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDataPasien();
                reset();
                
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Add Data Pasien: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   private void updateDataPasien() {
        if (tfidp.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data pasien yang ingin diupdate", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cbjk.getSelectedIndex() == 0 || cbdokter.getSelectedIndex() == 0 || cbperiksa.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih jenis kelamin, dokter, dan pemeriksaan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return; 
        }

        try {
            int idDokter = getIdDokter((String) cbdokter.getSelectedItem());

            String sql = "UPDATE tb_data_pasien SET nama_pasien = ?, tanggal_lahir = ?, jenis_kelamin = ?, alamat = ?, no_telepon = ?, keluhan = ?, pemeriksaan= ?, dokter = ?, lokasi_periksa = ?, tanggal_periksa = ?, id_dokter = ? WHERE id = ?";
            try (Connection conn = Koneksi.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tfnamap.getText());
                ps.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(jdtanggall.getDate()));
                ps.setString(3, (String) cbjk.getSelectedItem());
                ps.setString(4, tfalamat.getText());
                ps.setString(5, tfnot.getText());
                ps.setString(6, tfkeluhan.getText());
                ps.setString(7, (String) cbperiksa.getSelectedItem());
                ps.setString(8, (String) cbdokter.getSelectedItem());
                ps.setString(9, tflokasip.getText());
                ps.setString(10, new SimpleDateFormat("yyyy-MM-dd").format(jdtanggalp.getDate()));
                ps.setInt(11, idDokter);
                ps.setInt(12, Integer.parseInt(tfidp.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data pasien berhasil diupdate", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDataPasien();
                reset();
                bttambah.setEnabled(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Update Data Pasien: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
   
    private int getIdDokter(String doctorName) {
        int doctorId = -1; 
        try {
            String sql = "SELECT id FROM tb_data_dokter WHERE nama = ?";
            try (Connection conn = Koneksi.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, doctorName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        doctorId = rs.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving doctor ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return doctorId;
    }

    private void deleteDataPasien() {
        if (tfidp.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data pasien yang ingin dihapus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM tb_data_pasien WHERE id = ?";
                try (Connection conn = Koneksi.getConnection(); 
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(tfidp.getText()));
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data pasien berhasil dihapus", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadDataPasien();                
                    reset();
                    bttambah.setEnabled(true);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error Delete Data Pasien: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void reset() {
            tfidp.setText("");
            tfnamap.setText("");
            jdtanggall.setDate(null); 
            cbjk.setSelectedIndex(0);
            tfalamat.setText("");
            tfnot.setText("");
            tfkeluhan.setText("");
            cbperiksa.setSelectedIndex(0);
            cbdokter.setSelectedIndex(0);
            tflokasip.setText("");
            jdtanggalp.setDate(null);       
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AdminHalut = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tfidp = new javax.swing.JTextField();
        tfnamap = new javax.swing.JTextField();
        tfalamat = new javax.swing.JTextField();
        tfnot = new javax.swing.JTextField();
        cbjk = new javax.swing.JComboBox<>();
        tflokasip = new javax.swing.JTextField();
        tfkeluhan = new javax.swing.JTextField();
        cbperiksa = new javax.swing.JComboBox<>();
        bttambah = new javax.swing.JButton();
        btupdate = new javax.swing.JButton();
        btreset = new javax.swing.JButton();
        bthapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbdatapasien = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cbdokter = new javax.swing.JComboBox<>();
        jdtanggall = new com.toedter.calendar.JDateChooser();
        jdtanggalp = new com.toedter.calendar.JDateChooser();

        AdminHalut.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setFont(new java.awt.Font("Sitka Text", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DATA PASIEN");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID Pasien :");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nama Pasien :");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tanggal Lahir :");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Jenis Kelamin :");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("No. Telepon :");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Alamat :");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Keluhan :");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Pemeriksaan :");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Lokasi Periksa :");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Dokter :");

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Tanggal Periksa :");

        tfidp.setEditable(false);

        cbjk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---- pilih jenis kelamin ----", "Perempuan", "Laki - Laki" }));

        tflokasip.setEditable(false);

        cbperiksa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "item 1", "item 2", "item 3" }));

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

        tbdatapasien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Pasien", "Nama Pasien", "Tanggal Lahir", "Jenis Kelamin", "Alamat", "No. Telepon", "Keluhan", "Pemeriksaan", "Dokter", "Lokasi Periksa", "Tanggal Periksa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbdatapasien);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/logo kesehatan.png"))); // NOI18N

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/logo kesehatan.png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");

        cbdokter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "item 1", "item 2", "item 3" }));

        jdtanggall.setDateFormatString("yyyy-MM-dd");

        jdtanggalp.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout AdminHalutLayout = new javax.swing.GroupLayout(AdminHalut);
        AdminHalut.setLayout(AdminHalutLayout);
        AdminHalutLayout.setHorizontalGroup(
            AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AdminHalutLayout.createSequentialGroup()
                .addContainerGap(104, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addContainerGap())
            .addGroup(AdminHalutLayout.createSequentialGroup()
                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AdminHalutLayout.createSequentialGroup()
                        .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(AdminHalutLayout.createSequentialGroup()
                                .addGap(171, 171, 171)
                                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(AdminHalutLayout.createSequentialGroup()
                                            .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel7))
                                            .addGap(18, 18, 18)
                                            .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(tfalamat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(tfidp, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(tfnamap, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(tfnot, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(AdminHalutLayout.createSequentialGroup()
                                            .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5)
                                                .addComponent(jLabel4))
                                            .addGap(18, 18, 18)
                                            .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(cbjk, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jdtanggall, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                    .addComponent(jLabel6)))
                            .addGroup(AdminHalutLayout.createSequentialGroup()
                                .addGap(430, 430, 430)
                                .addComponent(bttambah)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(AdminHalutLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jdtanggalp, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                            .addGroup(AdminHalutLayout.createSequentialGroup()
                                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8))
                                .addGap(22, 22, 22)
                                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfkeluhan)
                                    .addComponent(tflokasip)
                                    .addComponent(cbperiksa, 0, 200, Short.MAX_VALUE)
                                    .addComponent(cbdokter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(AdminHalutLayout.createSequentialGroup()
                                .addComponent(btupdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bthapus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btreset))))
                    .addGroup(AdminHalutLayout.createSequentialGroup()
                        .addGap(330, 330, 330)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 1294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 947, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        AdminHalutLayout.setVerticalGroup(
            AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminHalutLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(5, 5, 5)
                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8)
                    .addComponent(tfidp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfkeluhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addComponent(tfnamap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbperiksa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel11)
                        .addComponent(cbdokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jdtanggall, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbjk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(AdminHalutLayout.createSequentialGroup()
                        .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel10)
                            .addComponent(tflokasip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(AdminHalutLayout.createSequentialGroup()
                                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel12)
                                    .addComponent(tfalamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(tfnot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jdtanggalp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(AdminHalutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bttambah)
                            .addComponent(btupdate)
                            .addComponent(bthapus)
                            .addComponent(btreset))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1398, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(AdminHalut, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 826, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(AdminHalut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bttambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttambahActionPerformed
        addDataPasien();   
    }//GEN-LAST:event_bttambahActionPerformed

    private void btupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btupdateActionPerformed
        updateDataPasien();
    }//GEN-LAST:event_btupdateActionPerformed

    private void bthapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bthapusActionPerformed
        deleteDataPasien();
    }//GEN-LAST:event_bthapusActionPerformed

    private void btresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btresetActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin mereset halaman ini?", "Konfirmasi Reset", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            reset();
            bttambah.setEnabled(true);
        }
    }//GEN-LAST:event_btresetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AdminHalut;
    private javax.swing.JButton bthapus;
    private javax.swing.JButton btreset;
    private javax.swing.JButton bttambah;
    private javax.swing.JButton btupdate;
    private javax.swing.JComboBox<String> cbdokter;
    private javax.swing.JComboBox<String> cbjk;
    private javax.swing.JComboBox<String> cbperiksa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdtanggall;
    private com.toedter.calendar.JDateChooser jdtanggalp;
    private javax.swing.JTable tbdatapasien;
    private javax.swing.JTextField tfalamat;
    private javax.swing.JTextField tfidp;
    private javax.swing.JTextField tfkeluhan;
    private javax.swing.JTextField tflokasip;
    private javax.swing.JTextField tfnamap;
    private javax.swing.JTextField tfnot;
    // End of variables declaration//GEN-END:variables
}
