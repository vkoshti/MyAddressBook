package addressbook.person;

public class Person 
{
	private String firstName;
	private String lastName;
	private String email;

	public Person(String firstName,String lastName , String email)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public String getEmail()
	{
		return this.email;
	}

	public boolean equals(Object obj)
	{
		return obj instanceof Person && (((Person)obj).firstName.equals(firstName)) && (((Person)obj).lastName.equals(lastName))  && (((Person)obj).email.equals(email));
	}

	public String toString()
	{
		return this.firstName+" "+this.lastName+" "+this.email;
	}
}