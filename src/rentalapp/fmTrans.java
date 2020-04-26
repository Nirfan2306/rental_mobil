/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalapp;

import static com.sun.javafx.util.Utils.split;
import java.awt.Toolkit;
import java.math.BigDecimal;
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
public class fmTrans extends javax.swing.JDialog {

    /**
     * Creates new form fmTrans
     */
    koneksi objKoneksi;
    String kd_sp;
    public fmTrans(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2 );
        objKoneksi = new koneksi();
        autoNumber();
        getSpare();
        getKen();
        load();
    }
    
    private void klir(){
        autoNumber();
        cbPlgn.setSelectedIndex(0);
        cbArea.setSelectedIndex(0);
        cbKen.setSelectedIndex(0);
        txtharga.setText("");
//        txtjumlah.setText("");
        cbdisc.setSelectedIndex(0);
    }
    
    private void load(){
        DefaultTableModel model = new DefaultTableModel(new String[]{"No. Order", "Pelanggan", "Area", "Kendaraan", "Biaya", "Disc", "Total"}, 0);
        try{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM transaksi";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                
                String nom = rs.getString("notransaksi");
                String plg = rs.getString("pelanggan");
                String are = rs.getString("area");
                String mbl = rs.getString("mobil");
                String bea = rs.getString("biaya");
                String dsk = rs.getInt("diskon")+" %";
                String ttl = rs.getString("total");
                model.addRow(new Object[]{nom, plg, are, mbl, bea, dsk, ttl});
            }
            jTable1.setModel(model);
            con.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private void autoNumber(){
//        String no_transaksi = "TRX";
        int i = 0;
        String nomer = null;
        try{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "SELECT IFNULL(MAX(notransaksi), 0) AS nom FROM transaksi";
            ResultSet rs = st.executeQuery(sql);
                while(rs.next()){
                    nomer = rs.getString("nom");
                }
            nomer = nomer.substring(3);
            i = Integer.parseInt(nomer)+1;
//            notransaksi = "00"+i;
            String a = String.format("%05d", i);
            nomer = "TRX"+a;
            txtnotransaksi.setText(nomer);
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }                                  

    private void getSpare(){
        try{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "SELECT nama_klien FROM tb_klien ORDER BY nama_klien";
            ResultSet rs = st.executeQuery(sql);
                cbPlgn.addItem("- Pilih Pelanggan -");
                while(rs.next()){
                    cbPlgn.addItem(rs.getString("nama_klien"));
                }
            con.close();
            st.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }     
    
    private void getKen(){
        try{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "SELECT nama_mobil FROM tb_kendaraan ORDER BY nama_mobil";
            ResultSet rs = st.executeQuery(sql);
                cbKen.addItem("- Pilih Mobil -");
                while(rs.next()){
                    cbKen.addItem(rs.getString("nama_mobil"));
                }
            con.close();
            st.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    private void getArea(){
        try{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "SELECT nama_mobil FROM tb_kendaraan ORDER BY nama_mobil";
            ResultSet rs = st.executeQuery(sql);
                cbKen.addItem("- Pilih Mobil -");
                while(rs.next()){
                    cbKen.addItem(rs.getString("nama_mobil"));
                }
            con.close();
            st.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }                        

    private void dataSpare(){
        try{
            if(cbArea.getSelectedItem()!="- Pilih Area -" && cbKen.getSelectedItem()!="- Pilih Mobil -"){
                
//            }else{
            Connection con = objKoneksi.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "select * from tb_arbi where area = '"+cbArea.getSelectedItem()+"' AND jenis_kendaraan = '"+cbKen.getSelectedItem()+"'";
            ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    String trf = rs.getString("tarif");
                    String[] trfe = split(trf, ".");
                    System.out.println(trfe[0]);

                    txtharga.setText(trfe[0]);
                    kd_sp = rs.getString("id_area");
                }else{
                    JOptionPane.showMessageDialog(rootPane, "Data area dan kendaraan belum ada !");
                    txtharga.setText("0");
                }
                if(txtharga.getText().equals("0")){
                    txtharga.setEditable(true);
                }else{
                    txtharga.setEditable(false);
                }
            con.close();
            st.close();
            System.out.println(""+kd_sp);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    private void tambah(){
        double disc = 0;
        double jml_byr = 0;
        
        String dsc = cbdisc.getSelectedItem().toString();
        
        if(txtharga.getText().equals("") || cbArea.getSelectedItem().equals("") || cbKen.getSelectedItem().equals("") || txtnotransaksi.getText().equals("") || cbPlgn.getSelectedItem().toString().equals("") ){
            JOptionPane.showMessageDialog(rootPane, "Mohon lengkapi data entrian !");
        }else{
            if(dsc=="5"){
                disc = 0.05;
            } else if(dsc=="10") {
                disc = 0.10;
            } else if(dsc=="15") {
                disc = 0.15;
            } else {
                dsc = "0";
            }

            jml_byr = Integer.parseInt(txtharga.getText());
            double total = jml_byr - ( jml_byr * disc);
//            System.out.println(disc);
            try{
                int ada = 0;
                Connection con0 = objKoneksi.bukaKoneksi();
                Statement st0 = con0.createStatement();
                String cek = "SELECT COUNT(*) AS ada FROM transaksi WHERE notransaksi = '"+txtnotransaksi.getText()+"'";
                ResultSet rs0 = st0.executeQuery(cek);
                if(rs0.next()){
                    ada = rs0.getInt("ada");
                }
                con0.close();
                st0.close();
                
                Connection con = objKoneksi.bukaKoneksi();
                Statement st = con.createStatement();
                String sql = null;
                
                if(ada==0){
                    sql = "INSERT INTO transaksi ( notransaksi, pelanggan, area, mobil, biaya, diskon, total ) VALUES ( '"+txtnotransaksi.getText()+"', '"+cbPlgn.getSelectedItem()+"', '"+cbArea.getSelectedItem()+"', '"+cbKen.getSelectedItem()+"', '"+txtharga.getText()+"', '"+dsc+"', '"+total+"' )";
                }else{
                    sql = "UPDATE transaksi SET pelanggan = '"+cbPlgn.getSelectedItem()+"', area = '"+cbArea.getSelectedItem()+"', mobil = '"+cbKen.getSelectedItem()+"', biaya = '"+txtharga.getText()+"', diskon = '"+dsc+"', total = '"+total+"' WHERE notransaksi = '"+txtnotransaksi.getText()+"'";
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
                
            }catch(SQLException e){
                JOptionPane.showMessageDialog(rootPane, e.getMessage()+" !");
//                System.out.println(e.getMessage());
            }
        }
    }
    
    private void ubah(){
        int col = 0;
        int row = jTable1.getSelectedRow();
        String notransaksi = jTable1.getModel().getValueAt(row, 0).toString();
        String pel = jTable1.getModel().getValueAt(row, 1).toString();
        String are = jTable1.getModel().getValueAt(row, 2).toString();
        String mob = jTable1.getModel().getValueAt(row, 3).toString();
        String bea = jTable1.getModel().getValueAt(row, 4).toString();
        String dsc = jTable1.getModel().getValueAt(row, 5).toString();
        String ttl = jTable1.getModel().getValueAt(row, 6).toString();
        
        txtnotransaksi.setText(notransaksi);
        cbPlgn.setSelectedItem(pel);
        cbArea.setSelectedItem(are);
        cbKen.setSelectedItem(mob);
        txtharga.setText(bea);
        cbdisc.setSelectedItem(dsc);
        
    }
    
    private void hapus(){
        
        int col = 0;
        int row = jTable1.getSelectedRow();
        String notransaksi = jTable1.getModel().getValueAt(row, 0).toString();
        
        int des = JOptionPane.showConfirmDialog(null, "Anda yakin akan menghapus data ini ?","Hapus data", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        
        if(des==0){
            try{
                Connection con = objKoneksi.bukaKoneksi();
                Statement st = con.createStatement();
                String sql = "DELETE FROM transaksi WHERE notransaksi = '"+notransaksi+"'";
                int sukses = st.executeUpdate(sql);
                if(sukses>0){
                    JOptionPane.showMessageDialog(rootPane, "Data berhasil dihapus !");
                }else{
                    JOptionPane.showMessageDialog(rootPane, "Data gagal dihapus !");
                }
                con.close();
                st.close();
                load();
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

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        save = new javax.swing.JButton();
        txtnotransaksi = new javax.swing.JTextField();
        cbPlgn = new javax.swing.JComboBox<>();
        txtharga = new javax.swing.JTextField();
        cbdisc = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        delete = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        cbKen = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cbArea = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel5.setText("Biaya");

        jLabel7.setText("Diskon");

        save.setText("Simpan");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        cbPlgn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPlgnActionPerformed(evt);
            }
        });

        cbdisc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih diskon -", "5", "10", "15" }));

        jLabel3.setText("No. Order");

        jLabel4.setText("Pelanggan");

        delete.setText("Hapus");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        edit.setText("Ubah");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        jLabel9.setText("Mobil");

        cbKen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKenActionPerformed(evt);
            }
        });

        jLabel10.setText("Area");

        cbArea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Area -", "Dalam Kota", "Luar Kota" }));
        cbArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAreaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbArea, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtnotransaksi)
                            .addComponent(txtharga))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbPlgn, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbKen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbdisc, 0, 118, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnotransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(cbPlgn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbKen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(cbArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(cbdisc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save)
                    .addComponent(edit)
                    .addComponent(delete))
                .addGap(6, 6, 6))
        );

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setText("RentalApp");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rentalapp/ic2.png"))); // NOI18N
        jLabel1.setText("icon.png");
        jLabel1.setMaximumSize(new java.awt.Dimension(153, 160));
        jLabel1.setMinimumSize(new java.awt.Dimension(153, 160));
        jLabel1.setPreferredSize(new java.awt.Dimension(153, 160));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Transaksi");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Order", "Pelanggan", "Area", "Kendaraan", "Biaya", "Diskon", "Total"
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(7, 7, 7))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        // TODO add your handling code here:
        tambah();
    }//GEN-LAST:event_saveActionPerformed

    private void cbPlgnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPlgnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPlgnActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
        hapus();
    }//GEN-LAST:event_deleteActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        // TODO add your handling code here:
        ubah();
    }//GEN-LAST:event_editActionPerformed

    private void cbKenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKenActionPerformed
        // TODO add your handling code here:
        dataSpare();
    }//GEN-LAST:event_cbKenActionPerformed

    private void cbAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAreaActionPerformed
        // TODO add your handling code here:
        dataSpare();
    }//GEN-LAST:event_cbAreaActionPerformed

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
            java.util.logging.Logger.getLogger(fmTrans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fmTrans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fmTrans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fmTrans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                fmTrans dialog = new fmTrans(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> cbArea;
    private javax.swing.JComboBox<String> cbKen;
    private javax.swing.JComboBox<String> cbPlgn;
    private javax.swing.JComboBox<String> cbdisc;
    private javax.swing.JButton delete;
    private javax.swing.JButton edit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton save;
    private javax.swing.JTextField txtharga;
    private javax.swing.JTextField txtnotransaksi;
    // End of variables declaration//GEN-END:variables
}
