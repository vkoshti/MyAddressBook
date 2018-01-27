package addressbook.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import addressbook.person.Person;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class PersonDatabase
{
	private PreparedStatement insertPerson;
	private PreparedStatement deletePerson;
	private PreparedStatement searchbyfirstname;
	private PreparedStatement searchbylastname;
	private PreparedStatement searchbyemailid;
	private Connection conn = null;
	private Statement statement;
	private ResultSet rs;
	private ObservableList<Person> data;

	public PersonDatabase()
	{
		try
		{
			conn = DriverManager.getConnection("jdbc:sqlite:addressbook/person/Person.db");
			statement = conn.createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS PERSON(FIRSTNAME TEXT, LASTNAME TEXT, EMAIL VARCHAR);");
			insertPerson = conn.prepareStatement("INSERT INTO PERSON(FIRSTNAME , LASTNAME , EMAIL)VALUES(?,?,?)");
			deletePerson = conn.prepareStatement("DELETE FROM PERSON WHERE FIRSTNAME=? AND LASTNAME=? AND EMAIL=?");
			searchbyfirstname = conn.prepareStatement("SELECT * FROM PERSON WHERE FIRSTNAME LIKE ?");
			searchbylastname = conn.prepareStatement("SELECT * FROM PERSON WHERE LASTNAME LIKE ?");
			searchbyemailid = conn.prepareStatement("SELECT * FROM PERSON WHERE EMAIL LIKE ?");


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean insertperson(Person p)
	{
		try
		{
			insertPerson.setString(1,p.getFirstName());
			insertPerson.setString(2,p.getLastName());
			insertPerson.setString(3,p.getEmail());
			insertPerson.executeUpdate();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}

	}

	public ObservableList<Person> fetchData()
	{
		data = FXCollections.observableArrayList();
		try
		{

			String query = "SELECT * FROM PERSON;";
			rs = statement.executeQuery(query);
			while(rs.next())
			{
				String fname = rs.getString("FIRSTNAME");
				String lname = rs.getString("LASTNAME");
				String email_id = rs.getString("EMAIL");
				data.add(new Person(fname,lname,email_id)); 

			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
		return data;

	}

	public boolean deleteperson(Person p)
	{
		try
		{
			deletePerson.setString(1,p.getFirstName());
			deletePerson.setString(2,p.getLastName());
			deletePerson.setString(3,p.getEmail());
			deletePerson.executeUpdate();

		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public ObservableList<Person> searchByFname(String fname)
	{
		try
		{
			data.clear();
			searchbyfirstname.setString(1,fname+"%");
			rs = searchbyfirstname.executeQuery();
			while(rs.next())
			{
				data.add(new Person(rs.getString("FIRSTNAME"),rs.getString("LASTNAME"),rs.getString("EMAIL")));

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return data;


	}

	public ObservableList<Person> searchByLname(String lname)
	{
		try
		{
			data.clear();
			searchbylastname.setString(1,lname+"%");
			rs = searchbylastname.executeQuery();
			while(rs.next())
			{
				data.add(new Person(rs.getString("FIRSTNAME"),rs.getString("LASTNAME"),rs.getString("EMAIL")));

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}

	public ObservableList<Person> searchByEmail(String email)
	{
		try
		{
			data.clear();
			searchbyemailid.setString(1,email+"%");
			rs = searchbyemailid.executeQuery();
			while(rs.next())
			{
				data.add(new Person(rs.getString("FIRSTNAME"),rs.getString("LASTNAME"),rs.getString("EMAIL")));

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}
}