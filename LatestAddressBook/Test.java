
import java.sql.*;

public class Test {

	

	public static void main(String[] args) throws Exception{
			Connection connect = null;
     Statement statement = null;
     ResultSet resultSet = null;
		Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://englishdatabase.db");
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select * from entries");
            while(resultSet.next())
            {
            	System.out.println(resultSet.getString("word"));
            }
		
	}	
}