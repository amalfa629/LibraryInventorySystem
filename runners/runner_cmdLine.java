import java.io.*;
import java.util.*;

public class runner_cmdLine {
	public static Inventory inventory;
	public static Scanner scn;
	public static boolean incorrect;
	//start up sequence
	public static void users() {
		incorrect = false;
		while(true) {
			clear();
			ArrayList<Person> persons = inventory.getPeople();
			if(persons.size()>0) {
				int n=1;
				System.out.println("Login");
				for(Person person: persons) {
					System.out.println("["+n+"] "+person.getName());
					n++;
				}
				System.out.println("[Q] Quit Program");
				if(incorrect) System.out.println("Incorrect Input");
				incorrect=false;
				String inp=scn.nextLine().toUpperCase();
				if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),persons.size()))) {
					incorrect = true;
				}
				if(!incorrect) {
					if(inp.substring(0,1).equals("Q")) {
						return;
					}
					Person user = persons.get(Integer.parseInt(inp)-1);
					login(user);
				}
			}
			else {
				System.out.println("Add administrator");
				System.out.print("Name: ");
				String name = scn.nextLine();
				System.out.print("Address: ");
				String address = scn.nextLine();
				inventory.addPerson(new Person(name, address, true));
			}
		}
	}
	public static void login(Person user) {
		if(!user.hasPassword()) {
			setPassword(user);
		}
		else {
			clear();
			System.out.println(user.getName());
			System.out.print("Password: ");
			String password=scn.nextLine();
			if(inventory.verifyLogin(user, password)) menu(user);
			else {
				System.out.println("Incorrect Password");
				scn.nextLine();
			}
		}
	}
	public static void menu(Person user) {
		incorrect = false;
		int limit=3;
		if(user.isAdmin()) limit=6;
		while(true) {
			clear();
			boolean quitB=false;
			System.out.println(user.getName());
			System.out.println("[1] Check Out");
			System.out.println("[2] Check In");
			System.out.println("[3] User Settings");
			if(user.isAdmin()) {
				System.out.println("[4] Manage Items");
				System.out.println("[5] Manage People");
				System.out.println("[6] Manage Locations");
			}
			System.out.println("[P] Print");
			System.out.println("[Q] Log Out");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!inp.substring(0,1).equals("P"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),limit))) {
				incorrect = true;
			}
			if(!incorrect) {
				char cmd = inp.charAt(0);
				switch(cmd) {
					case 'Q':
						if(logout(user)) return;
						break;
					case '1':
						checkOut(user);
						break;
					case '2':
						checkIn(user);
						break;
					case '3':
						userSettings(user);
						break;
					case 'P':
						print();
						break;
				}
				if(user.isAdmin()) {
					switch(cmd) {
						case '4':
							items();
							break;
						case '5':
							persons(user);
							break;
						case '6':
							buildings();
							break;
					}
				}
			}
		}
	}
	public static boolean logout(Person user) {
		clear();
		System.out.println("Logging out "+user.getName());
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') return true;
		return false;
	}
	//standard user
	public static void checkOut(Person user) {
		incorrect = false;
		String search = "";
		while(true) {
			clear();
			System.out.println("Check Out");
			if(!inventory.hasItems()) {
				System.out.println("There are no items in the inventory.");
				scn.nextLine();
				return;
			}
			ArrayList<InventoryItem> items = inventory.searchItem(inventory.getFreeInventory(),search);
			if((items.size()==0)&&(search.equals(""))) {
				System.out.println("All items are checked out.");
				scn.nextLine();
				return;
			}
			else if(items.size()==0) {
				System.out.println("No search results found.");
				search="";
				scn.nextLine();
			}
			else {
				int n=1;
				for(InventoryItem item: items) {
					System.out.print("["+n+"] "+item.getTitle()+"|");
					switch(item.getType()) {
						case MOVIE:
							System.out.println(((Movie)item).getFormat());
							break;
						case MUSIC:
							System.out.println(((Music)item).getFormat());
							break;
						case BOOK:
							System.out.println(((Book)item).getCover());
							break;
					}
					n++;
				}
				System.out.println("[S] Search");
				System.out.println("[V] View Checked Out Materials");
				System.out.println("[Q] Quit");
				if(incorrect) System.out.println("Incorrect Input");
				incorrect=false;
				String inp=scn.nextLine().toUpperCase();
				if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!inp.substring(0,1).equals("S"))&&(!inp.substring(0,1).equals("V"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),items.size()))) {
					incorrect = true;
				}
				if(!incorrect) {
					if(inp.charAt(0)=='Q') return;
					else if(inp.charAt(0)=='S') {
						clear();
						System.out.print("Search: ");
						search=scn.nextLine();
					}
					else if(inp.charAt(0)=='V') {
						viewCheckedOut();
					}
					else {
						clear();
						InventoryItem item = items.get(Integer.parseInt(inp)-1);
						System.out.print("Checking out ");
						printItem(item);
						boolean confirm=false;
						System.out.println("Are You Sure? (Y/N)");
						if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.checkOut(user, item);
					}
				}
			}
		}
	}
	public static void viewCheckedOut() {
		incorrect = false;
		String search = "";
		while(true) {
			clear();
			ArrayList<InventoryItem> items = inventory.searchItem(inventory.getOutInventory(),search);
			if((items.size()==0)&&(search.equals(""))) {
				System.out.println("No items are checked out.");
				scn.nextLine();
				return;
			}
			else if(items.size()==0) {
				System.out.println("No search results found.");
				search="";
				scn.nextLine();
			}
			else {
				int n=1;
				for(InventoryItem item: items) {
					System.out.print("["+n+"] "+item.getTitle()+"|");
					switch(item.getType()) {
						case MOVIE:
							System.out.println(((Movie)item).getFormat());
							break;
						case MUSIC:
							System.out.println(((Music)item).getFormat());
							break;
						case BOOK:
							System.out.println(((Book)item).getCover());
							break;
					}
					n++;
				}
				System.out.println("[S] Search");
				System.out.println("[Q] Quit");
				if(incorrect) System.out.println("Incorrect Input");
				incorrect=false;
				String inp=scn.nextLine().toUpperCase();
				if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!inp.substring(0,1).equals("S"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),items.size()))) {
					incorrect = true;
				}
				if(!incorrect) {
					if(inp.charAt(0)=='Q') return;
					else if(inp.charAt(0)=='S') {
						clear();
						System.out.print("Search: ");
						search=scn.nextLine();
					}
					else {
						clear();
						InventoryItem item = items.get(Integer.parseInt(inp)-1);
						printItem(item);
						scn.nextLine();
					}
				}
			}
		}
	}
	public static void checkIn(Person user) {
		incorrect = false;
		while(true) {
			clear();
			System.out.println("Check In");
			ArrayList<InventoryItem> items = inventory.getPersonItems(user);
			if(items.size()==0) {
				System.out.println("You have no items checked out.");
				scn.nextLine();
				return;
			}
			int n=1;
			for(InventoryItem item: items) {
				System.out.print("["+n+"] "+item.getTitle()+"|");
				switch(item.getType()) {
					case MOVIE:
						System.out.println(((Movie)item).getFormat());
						break;
					case MUSIC:
						System.out.println(((Music)item).getFormat());
						break;
					case BOOK:
						System.out.println(((Book)item).getCover());
						break;
				}
				n++;
			}
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp=scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),items.size()))) {
				incorrect = true;
			}
			if(!incorrect) {
				if(inp.charAt(0)=='Q') return;
				clear();
				InventoryItem item = items.get(Integer.parseInt(inp)-1);
				System.out.print("Checking in ");
				printItem(item);
				boolean confirm=false;
				System.out.println("Are You Sure? (Y/N)");
				if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.checkIn(user, item);
			}
		}
	}
	public static void userSettings(Person user) {
		incorrect=false;
		while(true) {
			clear();
			System.out.println("User Settings");
			System.out.println("[1] Edit Address");
			System.out.println("[2] New Password");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),2))) {
				incorrect = true;
			}
			if(!incorrect) {
				char cmd = inp.charAt(0);
				switch(cmd) {
					case 'Q':
						return;
					case '1':
						editResidence(user);
						break;
					case '2':
						clear();
						System.out.println("Setting new password");
						System.out.print("Enter old password: ");
						String password = scn.nextLine();
						if(inventory.verifyLogin(user, password)) setPassword(user);
						else {
							System.out.println("Incorrect Password");
							scn.nextLine();
						}
						break;
				}
			}
		}
	}
	public static void setPassword(Person user) {
		while(true) {
			clear();
			System.out.println(user.getName());
			System.out.print("Set new password: ");
			String passA = scn.nextLine();
			System.out.print("Repeat new password: ");
			String passB = scn.nextLine();
			if(passA.equals(passB)) {
				inventory.setPassword(user, passA);
				System.out.println("Password set succesfully.");
				scn.nextLine();
				return;
			}
			else {
				System.out.println("Passwords do not match.");
				scn.nextLine();
			}
		}
	}
	//administrative user
		//items
	public static void items() {
		incorrect=false;
		String search = "";
		while(true) {
			clear();
			System.out.println("Items");
			int n=1;
			ArrayList<InventoryItem> items = inventory.searchItem(search);
			if(!inventory.hasItems()) {
				System.out.println("There are no items in the inventory.");
			}
			else if(items.size()==0) {
				System.out.println("No items found.");
			}
			for(InventoryItem item: items) {
				System.out.print("["+n+"] "+item.getTitle());
				switch(item.getType()) {
					case MOVIE:
						System.out.println("|"+((Movie)item).getFormat());
						break;
					case MUSIC:
						System.out.println(" by "+((Music)item).getArtist()+"|"+((Music)item).getFormat());
						break;
					case BOOK:
						System.out.println(" by "+((Book)item).getAuthor()+"|"+((Book)item).getCover());
						break;
				}
				n++;
			}
			System.out.println("[A] Add Items");
			System.out.println("[M] Manage Items With Unknown Locations");
			System.out.println("[S] Search");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("S"))&&(!inp.substring(0,1).equals("A"))&&(!inp.substring(0,1).equals("M"))&&(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),items.size()))) {
				incorrect = true;
			}
			if(!incorrect) {
				if(inp.charAt(0)=='Q') return;
				else if(inp.charAt(0)=='A') {
					if(inventory.getBuildings().size()>0) {
						addItems();
						search="";
					}
					else {
						clear();
						System.out.println("No locations available.");
						scn.nextLine();
					}
				}
				else if(inp.charAt(0)=='M') {
					unknowns();
				}
				else if(inp.charAt(0)=='S') {
						clear();
						System.out.print("Search: ");
						search=scn.nextLine();
					}
				else {
					InventoryItem item = items.get(Integer.parseInt(inp)-1);
					item(item);
				}
			}
		}
	}
	public static void addItems() {
		MovieFormat[] movieFormats = MovieFormat.values();
		MusicFormat[] musicFormats = MusicFormat.values();
		Cover[] covers = Cover.values();
		incorrect=false;
		while(true) {
			clear();
			System.out.println("[1] Movie");
			System.out.println("[2] Music");
			System.out.println("[3] Book");
			System.out.println("[Q] Quit Add");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),3))) {
				incorrect = true;
			}
			if(!incorrect) {
				char cmd = inp.charAt(0);
				if(cmd=='Q') {
					return;
				}
				clear();
				Location location;
				ArrayList<Building> buildings = inventory.getBuildings();
				System.out.println("Building?");
				for(int n=0; n<buildings.size(); n++) {
					System.out.println("["+(n+1)+"]"+" "+buildings.get(n).getName());
				}
				char ch = scn.nextLine().charAt(0);
				Building building = buildings.get(Integer.parseInt(String.valueOf(ch))-1);
				clear();
				if(building.hasRooms()) {
					ArrayList<Room> rooms = building.getRooms();
					System.out.println("Room?");
					for(int n=0; n<rooms.size(); n++) {
						System.out.println("["+(n+1)+"]"+" "+rooms.get(n).getName());
					}
					ch = scn.nextLine().charAt(0);
					Room room = rooms.get(Integer.parseInt(String.valueOf(ch))-1);
					ArrayList<String> shelves = room.getShelves();
					clear();
					System.out.println("Shelf?");
					for(int n=0; n<shelves.size(); n++) {
						System.out.println("["+(n+1)+"]"+" "+shelves.get(n));
					}
					ch = scn.nextLine().charAt(0);
					String shelf = shelves.get(Integer.parseInt(String.valueOf(ch))-1);
					location = new Location(building.getName(), room.getName(), shelf);
				}
				else {
					ArrayList<String> shelves = building.getShelves();
					System.out.println("Shelf?");
					for(int n=0; n<shelves.size(); n++) {
						System.out.println("["+(n+1)+"]"+" "+shelves.get(n));
					}
					ch = scn.nextLine().charAt(0);
					String shelf = shelves.get(Integer.parseInt(String.valueOf(ch))-1);
					location = new Location(building.getName(), shelf);
				}
				clear();
				System.out.println("Title?");
				String title = scn.nextLine();
				int a = 1;
				boolean isSeries=false;
				String series="";
				int number=0;
				switch(cmd) {
					case '1':
						clear();
						System.out.println("Is this part of a series?(Y/N)");
						if(scn.nextLine().toUpperCase().charAt(0)=='Y') {
							boolean quit=false;
							isSeries=true;
							while(!quit) {
								clear();
								if(!series.equals("")) System.out.println("Series? "+series);
								else {
									System.out.print("Series? ");
									series=scn.nextLine();
								}
								System.out.print("Number in series? ");
								inp = scn.nextLine();
								if(isInt(inp)) {
									number=Integer.parseInt(inp);
									quit=true;
								}
							}
						}
						clear();
						System.out.println("Format?");
						for(MovieFormat movieFormat: movieFormats) {
							System.out.println("["+a+"] "+movieFormat);
							a++;
						}
						MovieFormat movieFormat = movieFormats[Integer.parseInt(scn.nextLine().substring(0,1))-1];
						if(isSeries) inventory.addItem(new Movie(title, series, number, movieFormat, location));
						else inventory.addItem(new Movie(title, movieFormat, location));
						break;
					case '2':
						System.out.println("Artist?");
						String artist = scn.nextLine();
						clear();
						System.out.println("Format?");
						for(MusicFormat musicFormat: musicFormats) {
							System.out.println("["+a+"] "+musicFormat);
							a++;
						}
						MusicFormat musicFormat = musicFormats[Integer.parseInt(scn.nextLine().substring(0,1))-1];
						inventory.addItem(new Music(title, artist, musicFormat, location));
						break;
					case '3':
						System.out.println("Author?");
						String author = scn.nextLine();
						clear();
						System.out.println("Is this part of a series?(Y/N)");
						if(scn.nextLine().toUpperCase().charAt(0)=='Y') {
							clear();
							isSeries=true;
							System.out.print("Series? ");
							series=scn.nextLine();
							System.out.print("Number in series? ");
							number=Integer.parseInt(scn.nextLine());
						}
						clear();
						System.out.println("Format?");
						for(Cover cover: covers) {
							System.out.println("["+a+"] "+cover);
							a++;
						}
						Cover cover = covers[Integer.parseInt(scn.nextLine().substring(0,1))-1];
						if(isSeries) inventory.addItem(new Book(title, author, series, number, cover, location));
						else inventory.addItem(new Book(title, author, cover, location));
						break;
				}
			}
		}
	}
	public static void item(InventoryItem item) {
		incorrect = false;
		while(true) {
			clear();
			printItem(item);
			System.out.println("\t[1] Edit Location");
			System.out.println("\t[2] Delete Item");
			System.out.println("\t[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),2))) {
				incorrect = true;
			}
			if(!incorrect) {
				switch(inp.charAt(0)) {
					case 'Q':
						return;
					case '1':
						editLocation(item);
						break;
					case '2':
						deleteItem(item);
						return;
				}
			}
		}
	}
	public static void editLocation(InventoryItem item) {
		clear();
		Location location;
		ArrayList<Building> buildings = inventory.getBuildings();
		System.out.println("Building?");
		for(int n=0; n<buildings.size(); n++) {
			System.out.println("["+(n+1)+"]"+" "+buildings.get(n).getName());
		}
		char ch = scn.nextLine().charAt(0);
		Building building = buildings.get(Integer.parseInt(String.valueOf(ch))-1);
		clear();
		if(building.hasRooms()) {
			ArrayList<Room> rooms = building.getRooms();
			System.out.println("Room?");
			for(int n=0; n<rooms.size(); n++) {
				System.out.println("["+(n+1)+"]"+" "+rooms.get(n).getName());
			}
			ch = scn.nextLine().charAt(0);
			Room room = rooms.get(Integer.parseInt(String.valueOf(ch))-1);
			ArrayList<String> shelves = room.getShelves();
			clear();
			System.out.println("Shelf?");
			for(int n=0; n<shelves.size(); n++) {
				System.out.println("["+(n+1)+"]"+" "+shelves.get(n));
			}
			ch = scn.nextLine().charAt(0);
			String shelf = shelves.get(Integer.parseInt(String.valueOf(ch))-1);
			location = new Location(building.getName(), room.getName(), shelf);
		}
		else {
			ArrayList<String> shelves = building.getShelves();
			System.out.println("Shelf?");
			for(int n=0; n<shelves.size(); n++) {
				System.out.println("["+(n+1)+"]"+" "+shelves.get(n));
			}
			ch = scn.nextLine().charAt(0);
			String shelf = shelves.get(Integer.parseInt(String.valueOf(ch))-1);
			location = new Location(building.getName(), shelf);
		}
		inventory.editLocation(item, location);
	}
	public static void deleteItem(InventoryItem item) {
		clear();
		System.out.println("Deleting "+item.getTitle());
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.deleteItem(item);
	}
	public static void unknowns() {
		incorrect=false;
		while(true) {
			clear();
			int n=1;
			ArrayList<InventoryItem> unknowns = inventory.getUnknowns();
			if(unknowns.size()==0) {
				System.out.println("There are no items with unknown locations.");
				scn.nextLine();
				return;
			}
			for(InventoryItem item: unknowns) {
				System.out.println("["+n+"] "+item.getTitle());
				n++;
			}
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),unknowns.size()))) {
				incorrect = true;
			}
			if(!incorrect) {
				if(inp.charAt(0)=='Q') return;
				else if(inp.charAt(0)=='A') {
					addItems();
				}
				else {
					InventoryItem item = unknowns.get(Integer.parseInt(inp)-1);
					unknown(item);
				}
			}
		}
	}
	public static void unknown(InventoryItem item) {
		while(true) {
			clear();
			printItem(item);
			System.out.println("\t[1] Set Location");
			System.out.println("\t[2] Delete Item");
			System.out.println("\t[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),2))) {
				incorrect = true;
			}
			if(!incorrect) {
				switch(inp.charAt(0)) {
					case 'Q':
						return;
					case '1':
						setLocation(item);
						return;
					case '2':
						deleteUnknown(item);
						return;
				}
			}
		}
	}
	public static void setLocation(InventoryItem item) {
		clear();
		Location location;
		ArrayList<Building> buildings = inventory.getBuildings();
		System.out.println("Building?");
		for(int n=0; n<buildings.size(); n++) {
			System.out.println("["+(n+1)+"]"+" "+buildings.get(n).getName());
		}
		char ch = scn.nextLine().charAt(0);
		Building building = buildings.get(Integer.parseInt(String.valueOf(ch))-1);
		clear();
		if(building.hasRooms()) {
			ArrayList<Room> rooms = building.getRooms();
			System.out.println("Room?");
			for(int n=0; n<rooms.size(); n++) {
				System.out.println("["+(n+1)+"]"+" "+rooms.get(n).getName());
			}
			ch = scn.nextLine().charAt(0);
			Room room = rooms.get(Integer.parseInt(String.valueOf(ch))-1);
			ArrayList<String> shelves = room.getShelves();
			clear();
			System.out.println("Shelf?");
			for(int n=0; n<shelves.size(); n++) {
				System.out.println("["+(n+1)+"]"+" "+shelves.get(n));
			}
			ch = scn.nextLine().charAt(0);
			String shelf = shelves.get(Integer.parseInt(String.valueOf(ch))-1);
			location = new Location(building.getName(), room.getName(), shelf);
		}
		else {
			ArrayList<String> shelves = building.getShelves();
			System.out.println("Shelf?");
			for(int n=0; n<shelves.size(); n++) {
				System.out.println("["+(n+1)+"]"+" "+shelves.get(n));
			}
			ch = scn.nextLine().charAt(0);
			String shelf = shelves.get(Integer.parseInt(String.valueOf(ch))-1);
			location = new Location(building.getName(), shelf);
		}
		inventory.setLocation(item, location);
	}
	public static void deleteUnknown(InventoryItem item) {
		clear();
		System.out.println("Deleting "+item.getTitle());
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.deleteUnknown(item);
	}
		//persons
	public static void persons(Person user) {
		incorrect = false;
		while(true) {
			clear();
			System.out.println("People");
			int n=1;
			ArrayList<Person> persons = inventory.getPeople();
			for(Person person: persons) {
				System.out.print("["+n+"] ");
				printPerson(person);
				n++;
			}
			System.out.println("[A] Add Person");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("A"))&&(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),persons.size()))) {
				incorrect = true;
			}
			if(!incorrect) {
				if(inp.charAt(0)=='Q') return;
				else if(inp.charAt(0)=='A') {
					addPerson();
				}
				else {
					Person person = persons.get(Integer.parseInt(inp)-1);
					person(user, person);
				}
			}
		}
	}
	public static void addPerson() {
		clear();
		System.out.print("Name? ");
		String name=scn.nextLine();
		System.out.print("Address?: ");
		String address=scn.nextLine();
		if(inventory.addPerson(new Person(name,address))) System.out.println(name+" added successfully!");
		else System.out.println(name+" is already in the system.");
		scn.nextLine();
	}
	public static void person(Person user, Person person) {
		incorrect = false;
		while(true) {
			clear();
			System.out.println(person.getName());
			System.out.println("\t[1] Edit Address");
			System.out.println("\t[2] Edit Admin Status");
			System.out.println("\t[3] View Checked Out Items");
			System.out.println("\t[4] Reset Password");
			System.out.println("\t[5] Delete Person");
			System.out.println("\t[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),5))) {
				incorrect = true;
			}
			if(!incorrect) {
				switch(inp.charAt(0)) {
					case 'Q':
						return;
					case '1':
						editResidence(person);
						break;
					case '2':
						editAdmin(user, person);
						break;
					case '3':
						viewPersonCheckedOut(person);
						break;
					case '4':
						resetPassword(user, person);
						break;
					case '5':
						if(user!=person) {
							deletePerson(person);
							return;
						}
						else {
							clear();
							System.out.println(person.getName()+" unable to be deleted.");
							scn.nextLine();
						}
						break;
				}
			}
		}
	}
	public static void editResidence(Person person) {
		clear();
		System.out.println(person.getName());
		System.out.print("New residence? ");
		String residence = scn.nextLine();
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') {
			inventory.editResidence(person,residence);
		}
	}
	public static void resetPassword(Person user, Person person) {
		clear();
		System.out.println("Resetting password for "+person.getName());
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') {
			if(inventory.resetPassword(user, person)) System.out.println(person.getName()+" password succesfully reset.");
			else System.out.println(person.getName()+" password unable to be reset.");
			scn.nextLine();
		}
	}
	public static void editAdmin(Person user, Person person) {
		clear();
		if(person.isAdmin()) System.out.println("Removing administrator status from "+person.getName());
		else System.out.println("Making "+person.getName()+" an administrator");
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') {
			clear();
			if(inventory.editAdmin(user, person)) System.out.println(person.getName()+" administrator status changed.");
			else System.out.println("Unable to affect "+person.getName()+" administrator status.");
			scn.nextLine();
		}
	}
	public static void viewPersonCheckedOut(Person person) {
		incorrect=false;
		while(true) {
			clear();
			int n=1;
			ArrayList<InventoryItem> personItems = inventory.getPersonItems(person);
			if(personItems.size()==0) {
				System.out.println(person.getName()+" has no items checked out.");
				scn.nextLine();
				return;
			}
			for(InventoryItem item: personItems) {
				System.out.print("["+n+"] "+item.getTitle());
				switch(item.getType()) {
					case MOVIE:
						System.out.println("|"+((Movie)item).getFormat());
						break;
					case MUSIC:
						System.out.println(" by "+((Music)item).getArtist()+"|"+((Music)item).getFormat());
						break;
					case BOOK:
						System.out.println(" by "+((Book)item).getAuthor()+"|"+((Book)item).getCover());
						break;
				}
				n++;
			}
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),personItems.size()))) {
				incorrect = true;
			}
			if(!incorrect) {
				if(inp.charAt(0)=='Q') return;
				else {
					clear();
					InventoryItem item = personItems.get(Integer.parseInt(inp)-1);
					printItem(item);
					scn.nextLine();
				}
			}
		}
	}
	public static void deletePerson(Person person) {
		clear();
		System.out.println("Deleting "+person.getName());
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') {
			ArrayList<InventoryItem> items = inventory.getPersonItems(person);
			if(items.size()>0) {
				while(true) {
					clear();
					System.out.println(person.getName()+" has the following items checked out:");
					int n=1;
					for(InventoryItem item: items) {
						System.out.print("["+n+"]"+item.getTitle()+"|");
						switch(item.getType()) {
							case MOVIE:
								System.out.println(((Movie)item).getFormat());
								break;
							case MUSIC:
								System.out.println(((Music)item).getFormat());
								break;
							case BOOK:
								System.out.println(((Book)item).getCover());
								break;
						}
						n++;
					}
					System.out.println("They must be returned before deletion.");
					System.out.println("Have they all been returned? (Y/N)");
					if(incorrect) System.out.println("Incorrect Input");
					incorrect=false;
					String inp = scn.nextLine().toUpperCase();
					if((inp.length()<1)||(!inp.substring(0,1).equals("Y"))&&(!inp.substring(0,1).equals("N"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),items.size()))) {
						incorrect = true;
					}
					if(!incorrect) {
						if(inp.charAt(0)=='Y') {
							inventory.deletePerson(person);
							return;
						}
						else if(inp.charAt(0)=='N') return;
						else {
							clear();
							printItem(items.get(Integer.parseInt(inp)-1));
							scn.nextLine();
						}
					}
				}
			}
			else inventory.deletePerson(person);
		}
	}
		//locations
			//buildings
	public static void buildings() {
		incorrect=false;
		while(true) {
			clear();
			System.out.println("Buildings");
			if(!inventory.hasLocations()) {
				System.out.println("There are no locations in the inventory.");
			}
			int n=1;
			ArrayList<Building> buildings = inventory.getBuildings();
			for(Building building: buildings) {
				System.out.println("["+n+"] "+building.getName());
				n++;
			}
			System.out.println("[A] Add Building");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("A"))&&(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),buildings.size()))) {
				incorrect = true;
			}
			if(!incorrect) {
				if(inp.charAt(0)=='Q') return;
				else if(inp.charAt(0)=='A') {
					addBuilding();
				}
				else {
					Building building = buildings.get(Integer.parseInt(inp)-1);
					building(building);
				}
			}
		}
	}
	public static void addBuilding() {
		clear();
		Building building = null;
		System.out.println("Adding new building:");
		System.out.print("Building? ");
		String b = scn.nextLine();
		System.out.println("[1] Add Room");
		System.out. println("[2] Add Shelf");
		switch(scn.nextLine().charAt(0)) {
			case '1':
				System.out.println("Room? ");
				String room = scn.nextLine();
				System.out.println("Shelf? ");
				String shelf = scn.nextLine();
				Room r = new Room(room);
				r.addShelf(shelf);
				building = new Building(b, r);
				break;
			case '2':
				System.out.println("Shelf? ");
				String she = scn.nextLine();
				building = new Building(b, she);
		}
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.addBuilding(building);
	}
	public static void building(Building building) {
		incorrect = false;
		int limit = 0;
		while(true) {
			clear();
			System.out.println("Building: "+building.getName());
			if(building.hasRooms()) {
				System.out.println("[1] Rename Building");
				System.out.println("[2] View Rooms");
				System.out.println("[3] Delete Building");
				limit=3;
			}
			else {
				System.out.println("[1] Rename Building");
				System.out.println("[2] View Shelves");
				System.out.println("[3] Add Room");
				System.out.println("[4] Delete Building");
				limit=4;
			}
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),limit))) {
				incorrect = true;
			}
			if(!incorrect) {
				char cmd = inp.charAt(0);
				if(cmd=='Q') return;
				else if(building.hasRooms()) {
					switch(cmd) {
						case '1':
							renameBuilding(building);
							break;
						case '2':
							rooms(building);
							break;
						case '3':
							deleteBuilding(building);
							return;
					}
				}
				else {
					switch(cmd) {
						case '1':
							renameBuilding(building);
							break;
						case '2':
							shelves(building);
							break;
						case '3':
							addRoom(building);
							break;
						case '4':
							deleteBuilding(building);
							return;
					}
				}
			}
		}
	}
	public static void renameBuilding(Building building) {
		clear();
		System.out.println("Renaming "+building.getName());
		System.out.print("New name? ");
		String b = scn.nextLine();
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.renameBuilding(building, b);
	}
	public static void deleteBuilding(Building building) {
		clear();
		System.out.println("Deleting "+building.getName());
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.deleteBuilding(building);
	}
			//rooms
	public static void rooms(Building building) {
		incorrect=false;
		while(true) {
			clear();
			int n=1;
			ArrayList<Room> rooms = inventory.getRooms(building);
			System.out.println("Rooms");
			for(Room room: rooms) {
				System.out.println("["+n+"] "+room.getName());
				n++;
			}
			System.out.println("[A] Add Room");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("A"))&&(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),rooms.size()))) {
				incorrect = true;
			}
			if(!incorrect) {
				if(inp.charAt(0)=='Q') return;
				else if(inp.charAt(0)=='A') {
					addRoom(building);
				}
				else {
					Room room = rooms.get(Integer.parseInt(inp)-1);	
					room(building, room);
				}
			}
		}
	}
	public static void addRoom(Building building) {
		clear();
		System.out.println("Adding a room to "+building.getName());
		System.out.print("Room? ");
		String room = scn.nextLine();
		System.out.println("Shelf? ");
		String shelf = scn.nextLine();
		Room r = new Room(room);
		r.addShelf(shelf);
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.addRoom(building, r);
	}
	public static void room(Building building, Room room) {
		incorrect=false;
		while(true) {
			clear();
			System.out.println("Room: "+room.getName());
			System.out.println("[1] Rename Room");
			System.out.println("[2] View Shelves");
			System.out.println("[3] Delete Room");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),3))) {
				incorrect = true;
			}
			if(!incorrect) {
				char cmd = inp.charAt(0);
				if(cmd=='Q') return;
				switch(cmd) {
					case '1':
						renameRoom(building, room);
						break;
					case '2':
						shelves(building, room);
						break;
					case '3':
						deleteRoom(building, room);
						return;
				}
			}
		}
	}
	public static void renameRoom(Building building, Room room) {
		clear();
		System.out.println("Renaming "+room.getName());
		System.out.print("New name? ");
		String r = scn.nextLine();
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.renameRoom(building, room, r);
	}
	public static void deleteRoom(Building building, Room room) {
		clear();
		System.out.println("Deleting "+room.getName()+" in "+building.getName());
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.deleteRoom(building, room);
	}
			//shelves in building
	public static void shelves(Building building) {
		incorrect=false;
		while(true) {
			clear();
			int n=1;
			ArrayList<String> shelves = inventory.getShelves(building);
			System.out.println("Shelves");
			for(String shelf: shelves) {
				System.out.println("["+n+"] "+shelf);
				n++;
			}
			System.out.println("[A] Add Shelf");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("A"))&&(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),shelves.size()))) {
				incorrect = true;
			}
			if(!incorrect) {
				if(inp.charAt(0)=='Q') return;
				else if(inp.charAt(0)=='A') addShelf(building);
				else {
					String shelf = shelves.get(Integer.parseInt(inp)-1);
					shelf(building, shelf);
				}
			}
		}
	}
	public static void addShelf(Building building) {
		clear();
		System.out.println("Adding a shelf to "+building.getName());
		System.out.print("Shelf? ");
		String shelf = scn.nextLine();
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.addShelf(building, shelf);
	}
	public static void shelf(Building building, String shelf) {
		incorrect=false;
		while(true) {
			clear();
			System.out.println("Shelf: "+shelf);
			System.out.println("[1] Rename Shelf");
			System.out.println("[2] Delete Shelf");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),2))) {
				incorrect = true;
			}
			if(!incorrect) {
				char cmd = inp.charAt(0);
				if(cmd=='Q') return;
				switch(cmd) {
					case '1':
						renameShelf(building, shelf);
						break;
					case '2':
						deleteShelf(building, shelf);
						return;
				}
			}
		}
	}
	public static void renameShelf(Building building, String shelf) {
		clear();
		System.out.println("Renaming "+shelf);
		System.out.print("New name? ");
		String s = scn.nextLine();
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.renameShelf(building, shelf, s);
	}
	public static void deleteShelf(Building building, String shelf) {
		clear();
		System.out.println("Deleting "+shelf+" in "+building.getName());
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.deleteShelf(building, shelf);
	}
			//shelves in room
	public static void shelves(Building building, Room room) {
		incorrect=false;
		while(true) {
			clear();
			int n=1;
			ArrayList<String> shelves = inventory.getShelves(building, room);
			System.out.println("Shelves");
			for(String shelf: shelves) {
				System.out.println("["+n+"] "+shelf);
				n++;
			}
			System.out.println("[A] Add Shelf");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("A"))&&(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),shelves.size()))) {
				incorrect = true;
			}
			if(!incorrect) {
				if(inp.charAt(0)=='Q') return;
				else if(inp.charAt(0)=='A') addShelf(building, room);
				else {
					String shelf = shelves.get(Integer.parseInt(inp)-1);
					shelf(building, room, shelf);
				}
			}
		}
	}
	public static void addShelf(Building building, Room room) {
		clear();
		System.out.println("Adding a shelf to "+room.getName()+" in "+building.getName());
		System.out.print("Shelf? ");
		String shelf = scn.nextLine();
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.addShelf(building, room, shelf);
	}
	public static void shelf(Building building, Room room, String shelf) {
		incorrect=false;
		while(true) {
			clear();
			System.out.println("Shelf: "+shelf);
			System.out.println("[1] Rename Shelf");
			System.out.println("[2] Delete Shelf");
			System.out.println("[Q] Quit");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp = scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),2))) {
				incorrect = true;
			}
			if(!incorrect) {
				char cmd = inp.charAt(0);
				if(cmd=='Q') return;
				switch(cmd) {
					case '1':
						renameShelf(building, shelf);
						break;
					case '2':
						deleteShelf(building, shelf);
						return;
				}
			}
		}
	}
	public static void renameShelf(Building building, Room room, String shelf) {
		clear();
		System.out.println("Renaming "+shelf);
		System.out.print("New name? ");
		String s = scn.nextLine();
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.renameShelf(building, room, shelf, s);
	}
	public static void deleteShelf(Building building, Room room, String shelf) {
		clear();
		System.out.println("Deleting "+shelf+" in "+room.getName()+" in "+building.getName());
		boolean confirm=false;
		System.out.println("Are You Sure? (Y/N)");
		if(scn.nextLine().toUpperCase().charAt(0)=='Y') inventory.deleteShelf(building, room, shelf);
	}
	//print
	public static void print() {
		incorrect = false;
		while(true) {
			clear();
			System.out.println("Print");
			System.out.println("[1] Items");
			System.out.println("[2] People");
			System.out.println("[3] Locations");
			System.out.println("[Q] Quit Print");
			if(incorrect) System.out.println("Incorrect Input");
			incorrect=false;
			String inp=scn.nextLine().toUpperCase();
			if((inp.length()<1)||(!inp.substring(0,1).equals("Q"))&&(!isInt(inp)||outOfBounds(Integer.parseInt(inp),3))) {
				incorrect = true;
			}
			if(!incorrect) {
				switch(inp.charAt(0)) {
					case 'Q':
						return;
					case '1':
						clear();
						ArrayList<InventoryItem> items = inventory.getInventory();
						for(InventoryItem item: items) {
							printItem(item);
						}
						break;
					case '2':
						clear();
						ArrayList<Person> persons = inventory.getPeople();
						for(Person person: persons) {
							System.out.print("Name: ");
							printPerson(person);
						}
						break;
					case '3':
						clear();
						ArrayList<Building> buildings = inventory.getBuildings();
						for(Building b: buildings) {
							System.out.println("Building: "+b.getName());
							if(b.hasRooms()) {
								ArrayList<Room> rooms = b.getRooms();
								for(Room r: rooms) {
									System.out.println("\tRoom: "+r.getName());
									for(String shelf: r.getShelves()) {
										System.out.println("\t\tShelf: "+shelf);
									}
								}
							}
							else {
								for(String shelf: b.getShelves()) {
									System.out.println("\tShelf: "+shelf);
								}
							}
						}
				}
				scn.nextLine();
			}
		}
	}
	public static void printItem(InventoryItem item) {
		System.out.print(item.getTitle());
		if(item.checkedOut()) System.out.println(" | Checked out by "+item.getPerson().getName());
		else System.out.println();
		switch(item.getType()) {
			case MOVIE:
				Movie m = (Movie)item;
				if(m.isSeries()) System.out.println("\tSeries: "+m.getSeries()+" "+m.getNumber());
				System.out.println("\tFormat: "+m.getFormat());
				break;
			case MUSIC:
				System.out.println("\tArtist: "+((Music)item).getArtist());
				System.out.println("\tFormat: "+((Music)item).getFormat());
				break;
			case BOOK:
				Book b = (Book)item;
				if(b.isSeries()) System.out.println("\tSeries: "+b.getSeries()+" "+b.getNumber());
				System.out.println("\tAuthor: "+b.getAuthor());
				System.out.println("\tCover Type: "+b.getCover());
				break;
		}
		Location location = item.getLocation();
		if(location!=null) {
			System.out.print("\tLocation: "+location.getBuilding()+", ");
			if(location.hasRoom()) System.out.println(location.getRoom()+", "+location.getShelf());
			else System.out.println(location.getShelf());
		}
	}
	public static void printPerson(Person person) {
		System.out.println(person.getName());
		System.out.print("\tAccount Type: ");
		if(person.isAdmin()) System.out.println("Administrator");
		else System.out.println("Standard");
		System.out.println("\tAddress: "+person.getResidence());
	}
	//general methods
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e) {return false;}
	}
	public static boolean outOfBounds(int input, int lastList) {
		if(input<=0) return true;
		if(input>lastList) return true;
		return false;
	}
	public static void clear() {
		try {new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();}
		catch(Exception e) {}
	}
	/*public static void search() {
		
		clear();
		System.out.print("Search: ");
		String inp = scn.nextLine();
		ArrayList<InventoryItem> output = inventory.searchItem(inp);
		if(output.size()==0) {
			System.out.println("No data found.");
			scn.nextLine();
			return;
		}
		int n=1;
		for(InventoryItem i: output) {
			System.out.println("["+n+"] "+i.getTitle());
			n++;
		}
		scn.nextLine();
	}*/
	//main
	public static void main(String[]a) {
		scn = new Scanner(System.in);
		inventory = new Inventory("./Data/ITEMS.txt", "./Data/LOCATIONUNKNOWN.txt", "./Data/PERSONS.txt", "./Data/LOCATIONS.txt");
		users();
		inventory.outputToFile();
		scn.close();
	}
}
