package addressbook.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import addressbook.person.Person;
import javafx.event.ActionEvent;
import addressbook.database.PersonDatabase;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ComboBox;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.application.Platform;
import javafx.stage.Popup;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import javafx.concurrent.ScheduledService;
import javafx.event.EventHandler;

public class UIController implements Initializable
{
	@FXML private TextField firstNameField;
	@FXML private TextField lastNameField;
	@FXML private TextField emailField;
	@FXML private Label responce;
	@FXML private TableView<Person> tableview;
	@FXML private ComboBox<String> searchby;
	@FXML private TextField search;
	@FXML private GridPane gridpane;
	private static PersonDatabase persondatabase;
	

	public UIController()
	{
		if(persondatabase==null)
			persondatabase = new PersonDatabase();


	}

	@FXML private void addPerson(ActionEvent ae)
	{
		
		addPersontoAddressbook.reset();
		addPersontoAddressbook.start();		

		addPersontoAddressbook.setOnSucceeded(new EventHandler<WorkerStateEvent> ()
		{
			public void handle(WorkerStateEvent wse)
			{
				String result = addPersontoAddressbook.getValue();
				switch(result)
				{
					case "D":
						Platform.runLater(new Runnable()
						{
							public void run()
							{
								responce.setText("Duplicate Entries");
								clearStat();
								clearstatus();
							}
						});break;

					case "R":
						Platform.runLater(new Runnable()
						{
							public void run()
							{
								responce.setText("Failed");
								clearstatus();
								clearStat();
							}
						});break;

					case "S":
						Platform.runLater(new Runnable()
						{
							public void run()
							{
								responce.setText("Successful");
								clearstatus();
								clearStat();
							}
						});break;

					case "I":
						Platform.runLater(new Runnable()
						{
							public void run()
							{
								responce.setText("Invalid entries");
								clearStat();
								clearstatus();
							}
						});break;
				}

			}
		});


		
	}

	@FXML private void deletePerson(ActionEvent ae)
	{
		if (!delete.isRunning())
		{
			delete.reset();
			delete.start();
		}
		
			delete.setOnSucceeded(new EventHandler<WorkerStateEvent> ()
			{
				public void handle(WorkerStateEvent wse)
				{
					switch(delete.getValue())
					{
						case "E":	Platform.runLater(new Runnable()
							{
								public void run()
								{
									responce.setText("Error occured");
									clearstatus();
								}
							});break;

						case "S":Platform.runLater(new Runnable()
							{
								public void run()
								{
									responce.setText("Successfully Deleted");
									clearstatus();
								}
							});break;

						case "N":Platform.runLater(new Runnable()
							{
								public void run()
								{
									responce.setText("No Item Selected");
									clearstatus();
								}
							});break;
					}
				}
			});
	}

	@FXML public void searchPerson(ActionEvent ae)
	{
		
			
		String selecteditem = searchby.getSelectionModel().getSelectedItem();
		String sr = search.getText().trim();
		ObservableList<Person> data;
		if (sr.equals(""))
		{
			responce.setText("Enter Any Query");
			clearstatus();	
			return;
		}

		if (selecteditem.equals("By First Name"))
		{
		
			data = persondatabase.searchByFname(sr);
		}
		else if (selecteditem.equals("By Last Name"))
		{
			
			data = persondatabase.searchByLname(sr);
		}
		else if (selecteditem.equals("By Email Id"))
		{
			
			data = persondatabase.searchByEmail(sr);
		}
		else
		{
			responce.setText("Select :  Any Search By  Method");
			clearstatus();	
			return;
		}

		if (data.size()==0)
		{
			responce.setText("No Records found");
			clearstatus();
			return;
		}
		responce.setText(data.size()+" records found");
		clearstatus();
		tableview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		for (Person p:data)
		{
			tableview.requestFocus();
			tableview.getSelectionModel().select(p);
			tableview.getFocusModel().focus(tableview.getItems().indexOf(p));
		}
		search.clear();
	}

	public boolean checkFname(String name)
	{
		Pattern pattern = Pattern.compile("([a-z]|[A-Z])+");
		Matcher matcher = pattern.matcher(name);
		if (matcher.matches())
			return true;
		return false;
	}

	public boolean checkLname(String lname)
	{
		Pattern pattern = Pattern.compile("([a-z]|[A-Z])+");
		Matcher matcher = pattern.matcher(lname);
		if (matcher.matches())
			return true;
		return false;
	}

	public boolean checkEmail(String email)
	{
		Pattern pattern = Pattern.compile("([a-z]|[A-Z])+([.]?|[0-9])+([a-z]|[A-Z]|[0-9])+[@]([a-z]+|[A-Z]+)[.][a-z]+");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches())
			return true;
		return false;
	}

	public void initialize(URL url, ResourceBundle rb)
	{
		responce.setText("Loading Contacts");
		clearstatus();
		if (!initializedata.isRunning())
		{	initializedata.reset();
			initializedata.start();
		}
		initializedata.setOnSucceeded(new EventHandler<WorkerStateEvent> ()
			{
				public void handle(WorkerStateEvent wse)
				{
					responce.setText("Loading Contacts Successful");
					clearstatus();
				}
			});
	}

	Service<String> initializedata = new Service<String>()
	{
		public Task<String> createTask()
		{
			return new Task<String>()
			{
				protected String call()
				{
					ObservableList<Person> list = persondatabase.fetchData();
					tableview.getItems().addAll(list);
					return "S";
				}
			};
		}
	};

	Service<String> addPersontoAddressbook = new Service<String>()
	{
		public Task<String> createTask()
		{
			return new Task<String>()
			{
				protected String call()
				{	

					if (!checkFname(firstNameField.getText())|| !checkLname(lastNameField.getText())|| !checkEmail(emailField.getText()))
					{
						return "I";
					}

					ObservableList<Person> data = tableview.getItems();
					Person p = new Person(firstNameField.getText(),lastNameField.getText(),emailField.getText());
					if (data.contains(p))
					{	
						return "D";
					}
					if (persondatabase.insertperson(p))
					{
						data.add(p);
					}
					else
					{
						return "R";
					}
					return "S";
				}
			};
		}
	}; 

	Service<Void> clearStates = new Service<Void>()
	{
		public Task<Void> createTask()
		{
			return new Task<Void>()
			{
				public Void call()
				{
					Platform.runLater(new Runnable()
						{
							public void run()
							{
								firstNameField.clear();
								lastNameField.clear();
								emailField.clear();
							}
						});
					return null;
				}
				protected void cancelled()
				{
					
					clearStates.restart();
				}

				protected void failed()
				{
					
					clearStates.restart();
				}
			};
		}
	};

	Service<Void> clearStatus = new Service<Void>()
	{
		public Task<Void> createTask()
		{
			return new Task<Void>()
			{
				public Void call()
				{

					
					try
					{

						Thread.sleep(3000);
					}
					catch(Exception e)
					{
					}
					
						Platform.runLater(new Runnable()
						{
							public void run()
							{
									responce.setText("");	
							}
						});
			
					return null;
				}

				protected void cancelled()
				{
					
					clearStatus.restart();
				}

				protected void failed()
				{
					
					clearStatus.restart();
				}

			};

		}
	};

	Service<String> delete = new Service<String>(){
		String selectedmenuitem;
		public void setTextItem(String selectedmenuitem)
		{
			this.selectedmenuitem = selectedmenuitem;
		}

		public Task<String> createTask()
		{
			return new Task<String>()
			{
				public String call()
				{
					tableview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
					Person p = (Person) tableview.getSelectionModel().getSelectedItem();
					if(p==null)
					{
						
						return "N";
					}
					tableview.getItems().remove(p);
					if (persondatabase.deleteperson(p))
					{
						return "S";
					}
					else
					{
						return "E";
					}
						
			
				}
			};
		}
	};

	public void clearStat()
	{
		if (!clearStates.isRunning())
		{
			clearStates.reset();
			clearStates.start();
		}
	}

	public void clearstatus()
	{
		if(!clearStatus.isRunning())
		{
			clearStatus.reset();
			clearStatus.start();
		}
	}

}