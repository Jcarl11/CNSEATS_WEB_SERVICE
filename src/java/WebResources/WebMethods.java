package WebResources;

import java.util.HashMap;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

@Path("generic")
public class WebMethods 
{

    @Context
    private UriInfo context;

    public WebMethods() {}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public JSONObject credentials(JSONObject data) throws Exception
    {
        JSONObject output = new JSONObject();
        UserAccountsEntity user = new UserAccountsEntity();
        user.setFirstname((String)data.get("fname"));
        user.setLastname((String)data.get("lname"));
        user.setMiddleInitial((String)data.get("middleInitial"));
        user.setEmail((String)data.get("emailAdd"));
        user.setUsername((String)data.get("uname"));
        user.setPassword((String)data.get("pword"));
        user.setAddress((String)data.get("address"));
        
        SQLOperations sQLOperation = new SQLOperations(user);
        int result = sQLOperation.InsertData();
        if(result > 0)
        {
            output.put("output", "Data Registered");
        }
        else
        {
            output.put("output", "Error");
        }
        
        return output;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public JSONObject loginUser(JSONObject data) throws Exception
    {
        JSONObject output = new JSONObject();
        SQLOperations sQLOperation = new SQLOperations();
        if((boolean)sQLOperation.LoginAccount((String)data.get("USERNAME"), (String)data.get("PASSWORD")).get(0) == true)
        {
            output.put("output",true);
            output.put("userid",sQLOperation.LoginAccount((String)data.get("USERNAME"), (String)data.get("PASSWORD")).get(1));
        }
        else
        {
            output.put("output",false);
        }
        return output;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getNowShowing")
    public JSONObject getMovies() throws Exception
    {
        SQLOperations sqlOperation = new SQLOperations();
        JSONObject output = new JSONObject(sqlOperation.getMovies());
        return output;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getSeats")
    public JSONObject getSeat(JSONObject movienameNtime) throws Exception
    {
        JSONObject output = new JSONObject();
        JSONArray arr = new JSONArray();
        SQLOperations sqlOperations = new SQLOperations();
        for(String list : sqlOperations.getSeats((String)movienameNtime.get("movieName"), (String)movienameNtime.get("playTime")))
        {
            arr.add(list);
        }
        output.put("seatnumber", arr);
        return output;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getMySeats")
    public JSONObject getMySeat(JSONObject movienameNtime) throws Exception
    {
        JSONObject output = new JSONObject();
        JSONArray arr = new JSONArray();
        SQLOperations sqlOperations = new SQLOperations();
        for(String list : sqlOperations.getMySeats((String)movienameNtime.get("movieName"), (String)movienameNtime.get("playTime"), (String)movienameNtime.get("username")))
        {
            arr.add(list);
        }
        output.put("seatnumber", arr);
        return output;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reservation")
    public JSONObject reserveUser(JSONObject data) throws Exception
    {
        JSONObject output = new JSONObject();
        JSONParser parser = new JSONParser();
        Object ob = parser.parse(data.toJSONString());
        JSONObject jso = (JSONObject)ob;
        SQLOperations sql = new SQLOperations();
        if(sql.reserve(jso) > 0)
        {
            output.put("output", "Reserved");
        }
        else
        {
            output.put("output", "Error");
        }
        return output;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllreservation")
    public JSONObject getALLRes(JSONObject username) throws Exception
    {
        SQLOperations sql = new SQLOperations();
        JSONObject output = new JSONObject(sql.getAllReservations((String)username.get("username")));
        return output;
    }
    
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/sayHello")
    public String sayHello()
    {
        /*HashMap<String, String>data = new HashMap<String, String>();
        data.put("say", "Hello World");*/
        
        org.json.JSONObject out = new org.json.JSONObject();
        out.put("Data", "Hello World");
        return out.toString();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/deleteReservation")
    public JSONObject delete(JSONObject data) throws Exception
    {
        JSONObject output = new JSONObject();
        SQLOperations sql = new SQLOperations();
        if(sql.deleteReservedSeats((String)data.get("username"), (String)data.get("moviename"), (String)data.get("time")) > 0)
        {
            output.put("output", "Record Deleted");
        }
        else
        {
            output.put("output", "Something went wrong");
        }
        return output;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getPastFilms")
    public JSONObject getPast() throws Exception
    {
        SQLOperations sql = new SQLOperations();
        JSONObject output = new JSONObject(sql.getPastMovies());
        return output;
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {throw new UnsupportedOperationException();}

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {}
}
