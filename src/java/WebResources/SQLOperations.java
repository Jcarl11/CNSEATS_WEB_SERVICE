package WebResources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.*;

public class SQLOperations 
{
    private UserAccountsEntity data;
    private String connectionString = "jdbc:mysql://localhost:3306/softeng";
    private String dbUname = "root";
    private String dbPword = "joeycarlo11";
    private String classForname = "com.mysql.jdbc.Driver";
    private Connection con;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public SQLOperations(UserAccountsEntity data)
    {
        this.data = data;
    }
    
    public SQLOperations(){}
    
    public int InsertData() throws Exception
    {
        Class.forName(classForname);
        con = DriverManager.getConnection(connectionString,dbUname,dbPword);
        preparedStatement = con.prepareStatement("INSERT INTO ACCOUNTS(USERNAME, UPASSWORD,FNAME,LNAME,MINITIALS,EMAIL,ADDRESS) VALUES(?,?,?,?,?,?,?)");
        preparedStatement.setString(1, data.getUsername());
        preparedStatement.setString(2, data.getPassword());
        preparedStatement.setString(3, data.getFirstname());
        preparedStatement.setString(4, data.getLastname());
        preparedStatement.setString(5, data.getMiddleInitial());
        preparedStatement.setString(6, data.getEmail());
        preparedStatement.setString(7, data.getAddress());
        int result = preparedStatement.executeUpdate();
        return result;
    }
    
    public ArrayList<Object> LoginAccount(String username, String password) throws Exception
    {
        ArrayList<Object> data = new ArrayList<Object>();
        boolean doesAccountExist = false;
        Class.forName(classForname);
        con = DriverManager.getConnection(connectionString,dbUname,dbPword);
        preparedStatement = con.prepareStatement("SELECT USERNAME, UPASSWORD, USERNO FROM ACCOUNTS WHERE USERNAME = '"+username+"' AND UPASSWORD = '"+password+"' ");
        resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {
            doesAccountExist = true;
            data.add(doesAccountExist);
            data.add(resultSet.getString(3));
        }
        else
        {
            data.add(doesAccountExist);
        }
        
        return data;
    }
    
    public HashMap<String, JSONObject> getMovies() throws Exception
    {
        HashMap<String, JSONObject> data = new HashMap<>();
        Class.forName(classForname);
        con = DriverManager.getConnection(connectionString,dbUname,dbPword);
        preparedStatement = con.prepareStatement("SELECT * FROM MOVIES WHERE SHOWSTATUS = 'S'");
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next())
        {
            JSONObject json = new JSONObject();
            json.put("title", resultSet.getString(2).trim());
            json.put("price", resultSet.getString(4).trim());
            json.put("rating", resultSet.getString(3).trim());
            json.put("time", resultSet.getString(7).trim());
            data.put(resultSet.getString(1), json);
        }
        return data;
    }
    
    public ArrayList<String> getSeats(String moviename, String time) throws Exception
    {
        ArrayList<String> data = new ArrayList<>();
        Class.forName(classForname);
        con = DriverManager.getConnection(connectionString, dbUname, dbPword);
        preparedStatement = con.prepareStatement("SELECT SEATNUMBER FROM SEAT WHERE MOVIENAME = '"+moviename+"' AND PLAYTIME = '"+time+"'");
        resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {
            data.add(resultSet.getString(1));
            while(resultSet.next())
            {
                data.add(resultSet.getString(1));
            }
        }
        else
        {
            data.add("No Records");
        }
        return data;
    }
    
    public ArrayList<String> getMySeats(String moviename, String time, String username) throws Exception
    {
        ArrayList<String> data = new ArrayList<>();
        Class.forName(classForname);
        con = DriverManager.getConnection(connectionString, dbUname, dbPword);
        preparedStatement = con.prepareStatement("SELECT SEATNUMBER FROM SEAT WHERE MOVIENAME = '"+moviename+"' AND PLAYTIME = '"+time+"' AND USERNAME = '"+username+"'");
        resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {
            data.add(resultSet.getString(1));
            while(resultSet.next())
            {
                data.add(resultSet.getString(1));
            }
        }
        else
        {
            data.add("No Records");
        }
        return data;
    }
    
    public HashMap<String, Object> getAllReservations(String username) throws Exception
    {
        HashMap<String, Object> data = new HashMap<>();
        Class.forName(classForname);
        con = DriverManager.getConnection(connectionString, dbUname, dbPword);
        preparedStatement = con.prepareStatement("SELECT MOVIES.MOVIENO, SEAT.MOVIENAME,SEAT.PLAYTIME FROM MOVIES INNER JOIN SEAT ON MOVIES.TITLE = SEAT.MOVIENAME AND MOVIES.PLAYDATE = SEAT.PLAYTIME WHERE USERNAME = '"+username+"' GROUP BY PLAYTIME");
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next())
        {
            JSONObject obj = new JSONObject();
            obj.put(resultSet.getString(2), resultSet.getString(3));
            data.put(resultSet.getString(1), obj);
        }
        
        return data;
    }
    
    public int reserve(JSONObject userInfo) throws Exception
    {
        Class.forName(classForname);
        con = DriverManager.getConnection(connectionString,dbUname, dbPword);
        int result = 0;
        String user = (String)userInfo.get("uname");
        String movieName = (String)userInfo.get("moviename");
        String playTime = (String)userInfo.get("playtime");
        JSONArray ja = (JSONArray)userInfo.get("seats");
        Iterator<String>keyIterator = ja.iterator();
        while(keyIterator.hasNext())
        {
            preparedStatement = con.prepareStatement("INSERT INTO SEAT(SEATNUMBER, USERNAME, MOVIENAME, PLAYTIME) VALUES(?,?,?,?)");
            preparedStatement.setString(1, (String)keyIterator.next());
            preparedStatement.setString(2, user);
            preparedStatement.setString(3, movieName);
            preparedStatement.setString(4, playTime);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }
    
    public int deleteReservedSeats(String username, String movieName, String time) throws Exception
    {
        Class.forName(classForname);
        con = DriverManager.getConnection(connectionString,dbUname, dbPword);
        preparedStatement = con.prepareStatement("DELETE FROM SEAT WHERE USERNAME = '"+username+"' AND MOVIENAME = '"+movieName+"' AND PLAYTIME = '"+time+"'");
        int result = 0;
        result = preparedStatement.executeUpdate();
        return result;
    }
    
    public HashMap<String, JSONObject> getPastMovies() throws Exception
    {
        HashMap<String, JSONObject> data = new HashMap<>();
        Class.forName(classForname);
        con = DriverManager.getConnection(connectionString,dbUname, dbPword);
        preparedStatement = con.prepareStatement("SELECT * FROM MOVIES WHERE SHOWSTATUS = 'NS'");
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next())
        {
            JSONObject obj = new JSONObject();
            obj.put("title", resultSet.getString(2));
            obj.put("rating", resultSet.getString(3));
            obj.put("description", resultSet.getString(6));
            obj.put("playdate", resultSet.getString(7));
            data.put(resultSet.getString(1), obj);
        }
        return data;
    }
}
