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
public class fmArbi extends javax.swing.JDialog {

    /**
     * Creates new form fmArbi
     */
    koneksi objKoneksi;
    int id_area = 0;
    public fmArbi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2 );
        objKoneksi = new koneksi();
        getKen();
        load();
    }
    
    private void getKen(){
        try{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "select nama_mobil from tb_kendaraan order by nama_mobil";
            ResultSet rs = st.executeQuery(sql);
                
                while(rs.next()){
                    jenis.addItem(rs.getString("nama_mobil"));
                }
            con.close();
            st.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void klir(){
        rad1.setSelected(false);
        rad2.setSelected(false);
        tarif.setText("");
        supir.setText("");
        bbm.setText("");
        jenis.setSelectedIndex(0);
    }
    
    private void load(){
        DefaultTableModel model = new DefaultTableModel(new String[]{"No.", "Kendaraan", "Area","Jam", "Tarif", "Sopir", "BBM"}, 0);
        try{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM tb_arbi";
            ResultSet rs = st.executeQuery(sql);
            int nom = 1;
            while(rs.next())
            {
                
                String jns = rs.getString("jenis_kendaraan");
                String jam = rs.getString("jam");
                String trf = rs.getString("tarif");
                String sup = rs.getString("sopir");
                String bbm = rs.getString("bbm");
                String are = rs.getString("area");
                model.addRow(new Object[]{nom, jns, are, jam, trf, sup, bbm});
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
        
        if(tarif.getText().equals("") || supir.getText().equals("") || bbm.getText().equals("") || jenis.getSelectedItem().toString().equals("") ){
            JOptionPane.showMessageDialog(rootPane, "Mohon lengkapi data entrian !");
        }else{
            
            try{
                int jam = 0;
                int ada1 = 0;
                int ada = 0;
                if(rad1.isSelected()==true){
                    jam = 12;
                }
                if(rad2.isSelected()==true){
                    jam = 24;
                }
                Connection con0 = objKoneksi.bukaKoneksi();
                Statement st0 = con0.createStatement();
                String cek = "SELECT COUNT(*) AS ada FROM tb_arbi WHERE jenis_kendaraan = '"+jenis.getSelectedItem()+"' AND area = '"+area.getSelectedItem()+"' AND sopir = '"+supir.getText()+"' AND bbm = '"+bbm.getText()+"'";
                ResultSet rs0 = st0.executeQuery(cek);
                if(rs0.next()){
                    ada1 = rs0.getInt("ada");
                }
                con0.close();
                st0.close();
                
                Connection con1 = objKoneksi.bukaKoneksi();
                Statement st1 = con1.createStatement();
                String cek1 = "SELECT COUNT(*) AS ada FROM tb_arbi WHERE id_area = '"+id_area+"'";
                ResultSet rs1 = st1.executeQuery(cek1);
                if(rs1.next()){
                    ada = rs1.getInt("ada");
                }
                con1.close();
                st1.close();
                                
                if(id_area==0 && ada1>0){
                    JOptionPane.showMessageDialog(rootPane, "Data mobil yang sama sudah ada !");
                }else{
                    int id = 0;
                    Connection con2 = objKoneksi.bukaKoneksi();
                    Statement st2 = con2.createStatement();
                    String cek2 = "SELECT id_area FROM tb_arbi WHERE jenis_kendaraan = '"+jenis.getSelectedItem()+"' AND area = '"+area.getSelectedItem()+"' AND sopir = '"+supir.getText()+"' AND bbm = '"+bbm.getText()+"' ";
                    ResultSet rs2 = st2.executeQuery(cek2);
                    if(rs2.next()){
                        id = rs2.getInt("id_area");
                    }
                    con2.close();
                    st2.close();
                
                    Connection con = objKoneksi.bukaKoneksi();
                    Statement st = con.createStatement();
                    String sql = null;
                    
                    if(ada==0){
                        sql = "INSERT INTO tb_arbi ( jenis_kendaraan, area, sopir, bbm, tarif, jam ) VALUES ( '"+jenis.getSelectedItem()+"', '"+area.getSelectedItem()+"', '"+supir.getText()+"', '"+bbm.getText()+"', '"+tarif.getText()+"', '"+jam+"' )";
                    }else{
                        sql = "UPDATE tb_arbi SET jenis_kendaraan = '"+jenis.getSelectedItem()+"', area = '"+area.getSelectedItem()+"', sopir = '"+supir.getText()+"', bbm = '"+bbm.getText()+"', tarif = '"+tarif.getText()+"', jam = '"+jam+"' WHERE id_area = '"+id_area+"'";
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
                    id_area = 0;
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
        String jn = jTable1.getModel().getValueAt(row, 1).toString();
        String are = jTable1.getModel().getValueAt(row, 2).toString();
        String jm = jTable1.getModel().getValueAt(row, 3).toString();
        String tr = jTable1.getModel().getValueAt(row, 4).toString();
        String sup = jTable1.getModel().getValueAt(row, 5).toString();
        String bb = jTable1.getModel().getValueAt(row, 6).toString();
        
        try{
            Connection con0 = objKoneksi.bukaKoneksi();
            Statement st0 = con0.createStatement();
            String cek = "SELECT id_area FROM tb_arbi WHERE jenis_kendaraan = '"+jn+"' AND sopir = '"+sup+"' AND bbm = '"+bb+"' AND area = '"+are+"'";
            ResultSet rs0 = st0.executeQuery(cek);
            if(rs0.next()){
                id_area = rs0.getInt("id_area");
            }
            con0.close();
            st0.close();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
                    
        tarif.setText(tr);
        supir.setText(sup);
        bbm.setText(bb);
        jenis.setSelectedItem(jn);
        area.setSelectedItem(are);
        if(jm.equals("12")){
            rad1.setSelected(true);
            rad2.setSelected(false);
        }else{
            rad1.setSelected(false);
            rad2.setSelected(true);
        }
        
    }
    
    private void hapus(){
        
        int row = jTable1.getSelectedRow();
        String jn = jTable1.getModel().getValueAt(row, 1).toString();
        String are = jTable1.getModel().getValueAt(row, 2).toString();
        String jm = jTable1.getModel().getValueAt(row, 3).toString();
        String tr = jTable1.getModel().getValueAt(row, 4).toString();
        String sup = jTable1.getModel().getValueAt(row, 5).toString();
        String bb = jTable1.getModel().getValueAt(row, 6).toString();
        try{
            Connection con0 = objKoneksi.bukaKoneksi();
            Statement st0 = con0.createStatement();
            String cek = "SELECT id_area FROM tb_arbi WHERE jenis_kendaraan = '"+jn+"' AND area = '"+are+"' AND sopir = '"+sup+"' AND bbm = '"+bb+"'";
            ResultSet rs0 = st0.executeQuery(cek);
            if(rs0.next()){
                id_area = rs0.getInt("id_area");
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
                String sql = "DELETE FROM tb_arbi WHERE id_area = '"+id_area+"'";
                int sukses = st.executeUpdate(sql);
                if(sukses>0){
                    JOptionPane.showMessageDialog(rootPane, "Data berhasil dihapus !");
                }else{
                    JOptionPane.showMessageDialog(rootPane, "Data gagal dihapus !");
                }
                con.close();
                st.close();
                load();
                id_area = 0;
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        supir = new javax.swing.JTextField();
        jenis = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        bbm = new javax.swing.JTextField();
        rad1 = new javax.swing.JRadioButton();
        rad2 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        tarif = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        area = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rentalapp/ic2.png"))); // NOI18N
        jLabel1.setText("icon.png");
        jLabel1.setMaximumSize(new java.awt.Dimension(153, 160));
        jLabel1.setMinimumSize(new java.awt.Dimension(153, 160));
        jLabel1.setPreferredSize(new java.awt.Dimension(153, 160));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setText("RentalApp");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Data Area dan Biaya");

        jLabel5.setText("Kendaraan");

        jLabel3.setText("Jam");

        jLabel4.setText("Sopir");

        jenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih mobil -" }));

        jLabel6.setText("BBM");

        buttonGroup1.add(rad1);
        rad1.setText("12");

        buttonGroup1.add(rad2);
        rad2.setText("24");

        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Jenis Mobil", "Area", "Jam", "Tarif", "Sopir", "BBM"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(2).setResizable(false);
        }

        jButton4.setText("Hapus");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel8.setText("Tarif");

        jButton5.setText("Ubah");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel10.setText("Area");

        area.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Area -", "Dalam Kota", "Luar Kota" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jenis, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rad1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rad2))
                            .addComponent(supir, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(bbm)
                            .addComponent(area, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tarif))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(bbm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rad1)
                    .addComponent(rad2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(tarif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(supir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton5)
                    .addComponent(jButton1)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        tambah();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        ubah();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        hapus();
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(fmArbi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fmArbi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fmArbi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fmArbi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                fmArbi dialog = new fmArbi(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> area;
    private javax.swing.JTextField bbm;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jenis;
    private javax.swing.JRadioButton rad1;
    private javax.swing.JRadioButton rad2;
    private javax.swing.JTextField supir;
    private javax.swing.JTextField tarif;
    // End of variables declaration//GEN-END:variables
}
