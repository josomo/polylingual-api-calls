import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RetrieveAndStore
{
  public static void main(String[] args)
  throws Exception
  {
    new RetrieveAndStore();
  }
  public RetrieveAndStore()
  {
    try
    {
      String myUrl = "https://hawttrends.appspot.com/api/terms/";
 
      String results = doHttpUrlConnectionAction(myUrl);
      
      Connection con = null;
      PreparedStatement pst = null;
  
      String url = "jdbc:postgresql://localhost/uniquelyphilly";        
      String user = "uniquelyphilly";
      String password = "password";

      String name = "java name2";
      con = DriverManager.getConnection(url, user, password);       
      String stm = "INSERT INTO woes(name) VALUES(?)";
      pst = con.prepareStatement(stm);
      pst.setString(1, name);
      pst.executeUpdate();

      JSONObject obj = new JSONObject (results);
      List<String> list = new ArrayList<String>();
      JSONArray array = obj.getJSONArray("1");

      for(int i = 0 ; i < array.length() ; i++){
        System.out.println(array.getJSONObject(i));
      }

      System.out.println(results);
    }
    catch (Exception e) { }
  }

  private String doHttpUrlConnectionAction(String desiredUrl)
  throws Exception
  {
    URL url = null;
    BufferedReader reader = null;
    StringBuilder stringBuilder;
 
    try
    {
      url = new URL(desiredUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod("GET");
       
      connection.setReadTimeout(5*1000);
      connection.connect();
 
      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      stringBuilder = new StringBuilder();
 
      String line = null;
      while ((line = reader.readLine()) != null)
      {
        stringBuilder.append(line + "\n");
      }
      return stringBuilder.toString();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {
      if (reader != null)
      {
        try
        {
          reader.close();
        }
        catch (IOException ioe)
        {
          ioe.printStackTrace();
        }
      }
    }
  }
}