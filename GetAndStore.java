import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


public class GetAndStore {

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;

        String dburl = "jdbc:postgresql://localhost/uniquelyphilly";
        String user = "uniquelyphilly";
        String password = "password";

        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        
        try {

        	url = new URL("https://hawttrends.appspot.com/api/terms/");
            is = url.openStream();  
            br = new BufferedReader(new InputStreamReader(is));
            line = br.readLine();
            //System.out.println(line);
            
            JSONObject obj = new JSONObject(line);
            JSONArray arr = obj.getJSONArray("1");
            
            //System.out.println(arr);
        	
            for (int i = 0; i < arr.length(); i++) {
            	String name = arr.getString(i);

                con = DriverManager.getConnection(dburl, user, password);

                String stm = "INSERT INTO woes(name) VALUES(?)";
                pst = con.prepareStatement(stm);
                pst.setString(1, name);                    
                pst.executeUpdate();
            }
            
            
            

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(GetAndStore.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(GetAndStore.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        
        
     
    }
}