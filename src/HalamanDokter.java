
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.*; 
import java.awt.event.*; 
import java.sql.*; 
import javax.swing.JOptionPane; 
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author User
 */
public class HalamanDokter extends javax.swing.JFrame {
    Connection conn;
    private int selectedConsultationId = -1; 
    private String namadokter;
    

    /**
     * Creates new form HalamanDokter
     */
    public HalamanDokter(String doctorName, String specialization) {
        initComponents();
        this.namadokter = doctorName;
        conn = Koneksi.getConnection();
        tfdokter.setText(doctorName); 
        tfspesialis.setText(specialization); 
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        loadPatientData();
        tfdiagnosis.requestFocus();
        
        Timer timer = new Timer(5000, e -> loadPatientData()); 
        timer.start();
        
        tbhalamandok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tbhalamandok.getSelectedRow();
                if (row != -1) {
                    tfnamap.setText((String) tbhalamandok.getValueAt(row, 1)); 
                    tfnamadok.setText((String) tbhalamandok.getValueAt(row, 2)); 
                    tftanggalp.setText((String) tbhalamandok.getValueAt(row, 3)); 
                    tfkeluhan.setText((String) tbhalamandok.getValueAt(row, 4)); 
                    tfdiagnosis.setText((String) tbhalamandok.getValueAt(row, 5));
                    tfresepobat.setText((String) tbhalamandok.getValueAt(row, 6));
                }
            }
        });
        
        tbhalamandok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tbhalamandok.getSelectedRow();
                if (row != -1) {
                    selectedConsultationId = (int) tbhalamandok.getValueAt(row, 0); 
                }
            }
        });
    }

    private HalamanDokter() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    public void setPatientDetails(String patientName, String examinationDate, String complaint, String doctorName) {
        tfnamap.setText(patientName);
        tftanggalp.setText(examinationDate);
        tfkeluhan.setText(complaint);
        tfdokter.setText(doctorName);
    }
    
    private void loadPatientData() {
        DefaultTableModel model = (DefaultTableModel) tbhalamandok.getModel();
    model.setRowCount(0); 

    try {
        String sql = "SELECT kd.id, dp.nama_pasien, dp.dokter, dp.tanggal_periksa, dp.keluhan, kd.diagnosis, kd.resep_obat " +
                     "FROM tb_data_pasien dp " +
                     "JOIN tb_konsul_dokter kd ON dp.id = kd.id_pasien " +
                     "WHERE kd.dokter = ?"; 
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, namadokter); 
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id"), 
                rs.getString("nama_pasien"),
                rs.getString("dokter"),
                rs.getString("tanggal_periksa"),
                rs.getString("keluhan"),
                rs.getString("diagnosis"),
                rs.getString("resep_obat")
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading patient data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbhalamandok = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tfdokter = new javax.swing.JTextField();
        tfspesialis = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tfnamap = new javax.swing.JTextField();
        tftanggalp = new javax.swing.JTextField();
        tfkeluhan = new javax.swing.JTextField();
        tfnamadok = new javax.swing.JTextField();
        tfdiagnosis = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btupdate = new javax.swing.JButton();
        btreset = new javax.swing.JButton();
        tfresepobat = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        btkeluar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 219, 204));

        jLabel2.setFont(new java.awt.Font("Sylfaen", 1, 36)); // NOI18N
        jLabel2.setText("HALAMAN DOKTER");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/dr_cowk-removebg-preview.png"))); // NOI18N

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/dr_cewk-removebg-preview.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(289, 289, 289)
                .addComponent(jLabel1)
                .addGap(52, 52, 52)
                .addComponent(jLabel2)
                .addGap(44, 44, 44)
                .addComponent(jLabel3)
                .addContainerGap(382, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1307, 100));

        jPanel2.setBackground(new java.awt.Color(0, 168, 157));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/dokter2 (2).png"))); // NOI18N

        tbhalamandok.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "id konsul", "Nama Pasien", "Nama Dokter", "Tanggal Periksa", "Keluhan", "Diagnonis", "Resep Obat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbhalamandok);

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Dokter :");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Spesialis :");

        tfdokter.setEditable(false);

        tfspesialis.setEditable(false);

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("______________________________________________________________________________________________________________________________________________________________________________________________________________");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Nama Pasien :");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Tanggal Periksa :");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Keluhan :");

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Nama Dokter :");

        tfnamap.setEditable(false);

        tftanggalp.setEditable(false);

        tfkeluhan.setEditable(false);

        tfnamadok.setEditable(false);

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Diagnosis :");

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

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Resep Obat :");

        btkeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/keluar.png"))); // NOI18N
        btkeluar.setText("KELUAR");
        btkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btkeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(414, 414, 414)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btkeluar)
                                .addGap(18, 18, 18)
                                .addComponent(btupdate)
                                .addGap(18, 18, 18)
                                .addComponent(btreset)
                                .addGap(87, 87, 87))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tfdokter, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(139, 139, 139)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfspesialis, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tftanggalp, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfnamap, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfkeluhan, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(111, 111, 111)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfnamadok, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfdiagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfresepobat, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(174, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(90, 90, 90))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(tfdokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfspesialis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jLabel7)
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(tfnamap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfnamadok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfdiagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(tftanggalp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfresepobat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(tfkeluhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btupdate)
                            .addComponent(btreset)
                            .addComponent(btkeluar))
                        .addGap(17, 17, 17)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 740));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btupdateActionPerformed
        String diagnosis = tfdiagnosis.getText();
        String prescription = tfresepobat.getText();

        if (selectedConsultationId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data pasien yang ingin diupdate", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (diagnosis.isEmpty() || prescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "isi diagnosis dan resep obat", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "UPDATE tb_konsul_dokter SET diagnosis = ?, resep_obat = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, diagnosis);
            ps.setString(2, prescription);
            ps.setInt(3, selectedConsultationId); 
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPatientData();
            } else {
                JOptionPane.showMessageDialog(this, "No data was updated. Please check the consultation ID.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btupdateActionPerformed

    private void btresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btresetActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin mereset halaman ini?", "Konfirmasi Reset", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tfnamap.setText("");
            tftanggalp.setText("");
            tfkeluhan.setText("");
            tfnamadok.setText("");
            tfdiagnosis.setText("");
            tfresepobat.setText("");
        }
    }//GEN-LAST:event_btresetActionPerformed

    private void btkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btkeluarActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin akan keluar dari halaman ini?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); 
        }
    }//GEN-LAST:event_btkeluarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
         try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(HalamanDokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        if (!LoginDokter.isLoggedIn) {
//            JOptionPane.showMessageDialog(null, "Anda harus login terlebih dahulu!.", "peringatan", JOptionPane.WARNING_MESSAGE);
            new LoginDokter().setVisible(true); 
        } else {
            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HalamanDokter().setVisible(true);
            }
        });
        }
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HalamanDokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HalamanDokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HalamanDokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HalamanDokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btkeluar;
    private javax.swing.JButton btreset;
    private javax.swing.JButton btupdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbhalamandok;
    private javax.swing.JTextField tfdiagnosis;
    private javax.swing.JTextField tfdokter;
    private javax.swing.JTextField tfkeluhan;
    private javax.swing.JTextField tfnamadok;
    private javax.swing.JTextField tfnamap;
    private javax.swing.JTextField tfresepobat;
    private javax.swing.JTextField tfspesialis;
    private javax.swing.JTextField tftanggalp;
    // End of variables declaration//GEN-END:variables
}
