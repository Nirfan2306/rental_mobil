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
public class fmUser extends javax.swing.JDialog {

    /**
     * Creates new form fmUser
     */
    koneksi objKoneksi;
    int id_user = 0;
    public fmUser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2 );
        objKoneksi = new koneksi();
        load();
    } 
    
    private void klir(){
        nm_lengkap.setText("");
        alamat.setText("");
        uname.setText("");
        pass.setText("");
        role.setSelectedIndex(0);
    }
    
    private void load(){
        DefaultTableModel model = new DefaultTableModel(new String[]{"No.", "Nama Lengkap", "Username", "Alamat", "Role"}, 0);
        try{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM tb_user";
            ResultSet rs = st.executeQuery(sql);
            int nom = 1;
            while(rs.next())
            {
                
                String nam = rs.getString("nama_lengkap");
                String unm = rs.getString("username");
                String almt = rs.getString("alamat");
                String rol = rs.getString("role");
                int id = rs.getInt("id_user");
                model.addRow(new Object[]{nom, nam, unm, almt, rol, id});
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
        String filter = null;
        
        if(nm_lengkap.getText().equals("") || uname.getText().equals("") || role.getSelectedIndex()==0 ){
            JOptionPane.showMessageDialog(rootPane, "Mohon lengkapi data entrian !");
        }else{
            
            try{
                int ada1 = 0;
                int ada = 0;
                Connection con0 = objKoneksi.bukaKoneksi();
                Statement st0 = con0.createStatement();
                String cek = "SELECT COUNT(*) AS ada FROM tb_user WHERE nama_lengkap = '"+nm_lengkap.getText()+"' AND username = '"+uname.getText()+"'";
                ResultSet rs0 = st0.executeQuery(cek);
                if(rs0.next()){
                    ada1 = rs0.getInt("ada");
                }
                con0.close();
                st0.close();
                
                Connection con1 = objKoneksi.bukaKoneksi();
                Statement st1 = con1.createStatement();
                String cek1 = "SELECT COUNT(*) AS ada FROM tb_user WHERE id_user = '"+id_user+"'";
                ResultSet rs1 = st1.executeQuery(cek1);
                if(rs1.next()){
                    ada = rs1.getInt("ada");
                }
                con1.close();
                st1.close();
                                
                if(id_user==0 && ada1>0){
                    JOptionPane.showMessageDialog(rootPane, "Data yang sama sudah ada !");
                }else{
                    String pwd = "";
                    String passText = new String(pass.getPassword());
                    if(passText.equals("")){
                        pwd = "";
                    }else{
                        pwd = ", password = md5('"+passText+"')";
                    }
                    Connection con = objKoneksi.bukaKoneksi();
                    Statement st = con.createStatement();
                    String sql = null;
                    
                    if(ada==0){
                        sql = "INSERT INTO tb_user ( nama_lengkap, alamat, username, password, role ) VALUES ( '"+nm_lengkap.getText()+"', '"+alamat.getText()+"', '"+uname.getText()+"', md5('"+passText+"'), '"+role.getSelectedItem()+"' )";
                    }else{
                        sql = "UPDATE tb_user SET nama_lengkap = '"+nm_lengkap.getText()+"', alamat = '"+alamat.getText()+"', username = '"+uname.getText()+"', role = '"+role.getSelectedItem()+"' "+pwd+" WHERE id_user = '"+id_user+"'";
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
                    id_user = 0;
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
        String un = jTable1.getModel().getValueAt(row, 2).toString();
        String al = jTable1.getModel().getValueAt(row, 3).toString();
        String rol = jTable1.getModel().getValueAt(row, 4).toString();
        
        try{
            Connection con0 = objKoneksi.bukaKoneksi();
            Statement st0 = con0.createStatement();
            String cek = "SELECT id_user FROM tb_user WHERE nama_lengkap = '"+nm+"' AND username = '"+un+"'";
            ResultSet rs0 = st0.executeQuery(cek);
            if(rs0.next()){
                id_user = rs0.getInt("id_user");
            }
            con0.close();
            st0.close();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
                    
        uname.setText(un);
//        pass.setText(ps);
        alamat.setText(al);
        nm_lengkap.setText(nm);
        role.setSelectedItem(rol);

    }
    
    private void hapus(){
        
        int row = jTable1.getSelectedRow();
        String nm = jTable1.getModel().getValueAt(row, 1).toString();
        String un = jTable1.getModel().getValueAt(row, 2).toString();
        try{
            Connection con0 = objKoneksi.bukaKoneksi();
            Statement st0 = con0.createStatement();
            String cek = "SELECT id_user FROM tb_user WHERE nama_lengkap = '"+nm+"' AND username = '"+un+"'";
            ResultSet rs0 = st0.executeQuery(cek);
            if(rs0.next()){
                id_user = rs0.getInt("id_user");
            }
            con0.close();
            st0.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        int des = JOptionPane.showConfirmDialog(null, "Anda yakin akan menghapus data ini ?","Hapus Pengguna", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        
        if(des==0){
            try{
                Connection con = objKoneksi.bukaKoneksi();
                Statement st = con.createStatement();
                String sql = "DELETE FROM tb_user WHERE id_user = '"+id_user+"'";
                int sukses = st.executeUpdate(sql);
                if(sukses>0){
                    JOptionPane.showMessageDialog(rootPane, "Data berhasil dihapus !");
                }else{
                    JOptionPane.showMessageDialog(rootPane, "Data gagal dihapus !");
                }
                con.close();
                st.close();
                load();
                id_user = 0;
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
        nm_lengkap = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        uname = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        alamat = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        role = new javax.swing.JComboBox<>();
        pass = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rentalapp/ic2.png"))); // NOI18N
        jLabel1.setText("icon.png");
        jLabel1.setMaximumSize(new java.awt.Dimension(153, 160));
        jLabel1.setMinimumSize(new java.awt.Dimension(153, 160));
        jLabel1.setPreferredSize(new java.awt.Dimension(153, 160));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setText("RentalApp");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Data Pengguna");

        jLabel5.setText("Password");

        nm_lengkap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nm_lengkapActionPerformed(evt);
            }
        });

        jLabel3.setText("Nama Lengkap");

        jLabel4.setText("Username");

        jButton3.setText("Ubah");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("Hapus");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel8.setText("Alamat");

        jLabel6.setText("Role");

        role.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih role -", "Admin", "User" }));

        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(alamat, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(role, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pass)
                    .addComponent(uname)
                    .addComponent(nm_lengkap))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nm_lengkap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(uname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4)
                    .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(role, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Nama Lengkap", "Username", "Alamat", "Role"
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
                        .addGap(50, 50, 50)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nm_lengkapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nm_lengkapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nm_lengkapActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        ubah();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        tambah();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        hapus();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passActionPerformed

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
            java.util.logging.Logger.getLogger(fmUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fmUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fmUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fmUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                fmUser dialog = new fmUser(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField alamat;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField nm_lengkap;
    private javax.swing.JPasswordField pass;
    private javax.swing.JComboBox<String> role;
    private javax.swing.JTextField uname;
    // End of variables declaration//GEN-END:variables
}
