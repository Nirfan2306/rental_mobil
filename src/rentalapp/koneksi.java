/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalapp;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;

/**
 *
 * @author windows
 */
public class koneksi {

    static java.sql.Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public Connection bukaKoneksi(){
        
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/dbrental","root","");
            System.out.println("Koneksi sukses !");
            return con;
        }catch (Exception e){
            System.out.println("Koneksi gagal ! -> "+e.getMessage());
            return con = null;
        }
    }
}
