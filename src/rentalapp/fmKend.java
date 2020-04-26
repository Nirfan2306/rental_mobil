/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalapp;

import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author windows
 */
public class fmKend extends javax.swing.JDialog {

    /**
     * Creates new form fmKend
     */
    koneksi objKoneksi;
    int id_ken = 0;
    public fmKend(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2 );
        objKoneksi = new koneksi();
        load();
    }
    
    private void klir(){
//        autoNumber();
//        cbspare.setSelectedIndex(0);
        nm_ken.setText("");
        nopol.setText("");
        tahun.setText("");
        jenis.setSelectedIndex(0);
    }
    
    private void load(){
        DefaultTableModel model = new DefaultTableModel(new String[]{"No.", "Nama Mobil", "No. Polisi", "Tahun", "Jenis"}, 0);
        try{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM tb_kendaraan";
            ResultSet rs = st.executeQuery(sql);
            int nom = 1;
            while(rs.next())
            {
                
                String nam = rs.getString("nama_mobil");
                String nop = rs.getString("no_polisi");
                String th = rs.getString("th_buat");
                String jn = rs.getString("jenis_mobil");
                model.addRow(new Object[]{nom, nam, nop, th, jn});
                nom++;
            }
            jTable1.setModel(model);
            con.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private void tambah(){
        
        if(nm_ken.getText().equals("") || nopol.getText().equals("") || tahun.getText().equals("") || jenis.getSelectedItem().toString().equals("") ){
            JOptionPane.showMessageDialog(rootPane, "Mohon lengkapi data entrian !");
        }else{
            
            try{
                int ada1 = 0;
                int ada = 0;
                Connection con0 = objKoneksi.bukaKoneksi();
                Statement st0 = con0.createStatement();
                String cek = "SELECT COUNT(*) AS ada FROM tb_kendaraan WHERE nama_mobil = '"+nm_ken.getText()+"' AND no_polisi = '"+nopol.getText()+"' AND th_buat = '"+tahun.getText()+"'";
                ResultSet rs0 = st0.executeQuery(cek);
                if(rs0.next()){
                    ada1 = rs0.getInt("ada");
                }
                con0.close();
                st0.close();
                
                Connection con1 = objKoneksi.bukaKoneksi();
                Statement st1 = con1.createStatement();
                String cek1 = "SELECT COUNT(*) AS ada FROM tb_kendaraan WHERE id_kendaraan = '"+id_ken+"'";
                ResultSet rs1 = st1.executeQuery(cek1);
                if(rs1.next()){
                    ada = rs1.getInt("ada");
                }
                con1.close();
                st1.close();
                                
                if(id_ken==0 && ada1>0){
                    JOptionPane.showMessageDialog(rootPane, "Data mobil yang sama sudah ada !");
                }else{
                    int id = 0;
                    Connection con2 = objKoneksi.bukaKoneksi();
                    Statement st2 = con2.createStatement();
                    String cek2 = "SELECT id_kendaraan FROM tb_kendaraan WHERE nama_mobil = '"+nm_ken.getText()+"' AND no_polisi = '"+nopol.getText()+"' AND th_buat = '"+tahun.getText()+"' ";
                    ResultSet rs2 = st2.executeQuery(cek2);
                    if(rs2.next()){
                        id = rs2.getInt("id_kendaraan");
                    }
                    con2.close();
                    st2.close();
                
                    Connection con = objKoneksi.bukaKoneksi();
                    Statement st = con.createStatement();
                    String sql = null;
                    
                    if(ada==0){
                        sql = "INSERT INTO tb_kendaraan ( nama_mobil, no_polisi, th_buat, jenis_mobil ) VALUES ( '"+nm_ken.getText()+"', '"+nopol.getText()+"', '"+tahun.getText()+"', '"+jenis.getSelectedItem()+"' )";
                    }else{
                        sql = "UPDATE tb_kendaraan SET nama_mobil = '"+nm_ken.getText()+"', no_polisi = '"+nopol.getText()+"', th_buat = '"+tahun.getText()+"', jenis_mobil = '"+jenis.getSelectedItem()+"' WHERE id_kendaraan = '"+id_ken+"'";
                    }

                    int sukses = st.executeUpdate(sql);
                    if(sukses>0){
                        JOptionPane.showMessageDialog(rootPane, "Data berhasil disimpan !");
                    }else{
                        JOptionPane.showMessageDialog(rootPane, "Data gagal disimpan !");
                    }
                    con.close();
                    st.close();
                    load();
                    klir();
                    id_ken = 0;
                }
                
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    private void ubah(){
        int id = 0;
        int col = 0;
        int row = jTable1.getSelectedRow();
        String nm = jTable1.getModel().getValueAt(row, 1).toString();
        String nop = jTable1.getModel().getValueAt(row, 2).toString();
        String th = jTable1.getModel().getValueAt(row, 3).toString();
        String mob = jTable1.getModel().getValueAt(row, 4).toString();
//        id_ken = Integer.parseInt(jTable1.getModel().getValueAt(row, 4).toString());
        
        try{
            Connection con0 = objKoneksi.bukaKoneksi();
            Statement st0 = con0.createStatement();
            String cek = "SELECT id_kendaraan FROM tb_kendaraan WHERE nama_mobil = '"+nm+"' AND no_polisi = '"+nop+"' AND th_buat = '"+th+"'";
            ResultSet rs0 = st0.executeQuery(cek);
            if(rs0.next()){
                id_ken = rs0.getInt("id_kendaraan");
            }
            con0.close();
            st0.close();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
                    
        nm_ken.setText(nm);
        nopol.setText(nop);
        tahun.setText(th);
        jenis.setSelectedItem(mob);
        
    }
    
    private void hapus(){
        
        int row = jTable1.getSelectedRow();
        String nm = jTable1.getModel().getValueAt(row, 1).toString();
        String nop = jTable1.getModel().getValueAt(row, 2).toString();
        String th = jTable1.getModel().getValueAt(row, 3).toString();
        try{
            Connection con0 = objKoneksi.bukaKoneksi();
            Statement st0 = con0.createStatement();
            String cek = "SELECT id_kendaraan FROM tb_kendaraan WHERE nama_mobil = '"+nm+"' AND no_polisi = '"+nop+"' AND th_buat = '"+th+"'";
            ResultSet rs0 = st0.executeQuery(cek);
            if(rs0.next()){
                id_ken = rs0.getInt("id_kendaraan");
            }
            con0.close();
            st0.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        int des = JOptionPane.showConfirmDialog(null, "Anda yakin akan menghapus data ini ?","Hapus data", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        
        if(des==0){
            try{
                Connection con = objKoneksi.bukaKoneksi();
                Statement st = con.createStatement();
                String sql = "DELETE FROM tb_kendaraan WHERE id_kendaraan = '"+id_ken+"'";
                int sukses = st.executeUpdate(sql);
                if(sukses>0){
                    JOptionPane.showMessageDialog(rootPane, "Data berhasil dihapus !");
                }else{
                    JOptionPane.showMessageDialog(rootPane, "Data gagal dihapus !");
                }
                con.close();
                st.close();
                load();
                id_ken = 0;
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }else{
            
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        nm_ken = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nopol = new javax.swing.JTextField();
        jenis = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        tahun = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rentalapp/ic2.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setMaximumSize(new java.awt.Dimension(153, 160));
        jLabel1.setMinimumSize(new java.awt.Dimension(153, 160));
        jLabel1.setPreferredSize(new java.awt.Dimension(153, 160));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setText("RentalApp");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Data Kendaraan");

        jLabel5.setText("Jenis Kendaraan");

        nm_ken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nm_kenActionPerformed(evt);
            }
        });

        jLabel3.setText("Nama Mobil");

        jLabel4.setText("Nomor Polisi");

        jenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih jenis -", "4 Seat", "6 Seat" }));

        jLabel6.setText("Tahun");

        jButton3.setText("Ubah");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Hapus");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Simpan");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Batal");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nm_ken)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nopol, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jenis, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tahun, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addGap(8, 8, 8)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(nm_ken, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(nopol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(tahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Nama Mobil", "Nomor Polisi", "Tahun", "Jenis"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nm_kenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nm_kenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nm_kenActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        hapus();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        ubah();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        tambah();
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(fmKend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fmKend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fmKend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fmKend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                fmKend dialog = new fmKend(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jenis;
    private javax.swing.JTextField nm_ken;
    private javax.swing.JTextField nopol;
    private javax.swing.JTextField tahun;
    // End of variables declaration//GEN-END:variables
}
