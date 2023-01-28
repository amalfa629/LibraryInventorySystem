import java.io.*;
import java.util.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Inventory {
	private ArrayList<InventoryItem> inventory;
	private ArrayList<InventoryItem> locationUnknown;
	private HashMap<String, Person> persons;
	private ArrayList<Building> buildings;
	private String itemFile;
	private String locationUnknownFile;
	private String personFile;
	private String locationFile;
	//constructor
	public Inventory(String itemFile, String locationUnknownFile, String personFile, String locationFile) {
		this.inventory=new ArrayList<InventoryItem>();
		this.persons=new HashMap<String, Person>();
		this.buildings=new ArrayList<Building>();
		this.locationUnknown=new ArrayList<InventoryItem>();
		this.itemFile=itemFile;
		this.locationUnknownFile=locationUnknownFile;
		this.personFile=personFile;
		this.locationFile=locationFile;
		inputPersons();
		inputInventory();
		inputLocationUnknown();
		inputLocations();
	}
	//inputs
	private void inputInventory() {
		File file = new File(itemFile);
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while((line = br.readLine()) != null){
				int a=line.indexOf('/');
				String building=line.substring(0,a);
				line=line.substring(a+1);
				a=line.indexOf('/');
				String shelf=line.substring(0,a);
				line=line.substring(a+1);
				a=line.indexOf('/');
				String room="";
				Location location;
				if(a>0) {
					room=shelf;
					shelf=line.substring(0,a);
					line=line.substring(a+1);
					location=new Location(building, room, shelf);
				}
				else location=new Location(building, shelf);
				a=line.indexOf('|');
				String type=line.substring(0,a);
				line=line.substring(a+1);
				String series="";
				int number=0;
				boolean isSeries=false;
				if((type.equals("MOVIE"))||(type.equals("BOOK"))) {
					if((a=line.indexOf('{'))!=-1) {
						isSeries=true;
						series=line.substring(0,a);
						line=line.substring(a+1);
						a=line.indexOf('|');
						number=Integer.parseInt(line.substring(0,a));
						line=line.substring(a+1);
					}
				}
				a=line.indexOf('|');
				String title=line.substring(0,a);
				line=line.substring(a+1);
				String person="";
				a=line.indexOf('|');
				switch(type) {
					case "MOVIE":
						MovieFormat movieFormat=MovieFormat.valueOf(line.substring(0,a));
						line=line.substring(a+1);
						a=line.indexOf('|');
						if(a>0) {
							person=line.substring(0,a);
							if(isSeries) addItem(new Movie(title, series, number, movieFormat, location, persons.get(person)));
							else addItem(new Movie(title, movieFormat, location, persons.get(person)));
						}
						else {
							if(isSeries) addItem(new Movie(title, series, number, movieFormat, location));
							else addItem(new Movie(title, movieFormat, location));
						}
						break;
						case "MUSIC":
							String artist=line.substring(0,a);
							line=line.substring(a+1);
							a=line.indexOf('|');
							MusicFormat musicFormat=MusicFormat.valueOf(line.substring(0,a));
							line=line.substring(a+1);
							a=line.indexOf('|');
							if(a>0) {
								person=line.substring(0,a);
								addItem(new Music(title, artist, musicFormat, location, persons.get(person)));
							}
							else addItem(new Music(title, artist, musicFormat, location));
							break;
						case "BOOK":
							String author=line.substring(0,a);
							line=line.substring(a+1);
							a=line.indexOf('|');
							Cover cover=Cover.valueOf(line.substring(0,a));
							line=line.substring(a+1);
							a=line.indexOf('|');
							if(a>0) {
								person=line.substring(0,a);
								if(isSeries) addItem(new Book(title, author, series, number, cover, location, persons.get(person)));
								else addItem(new Book(title, author, cover, location, persons.get(person)));
							}
							else {
								if(isSeries) addItem(new Book(title, author, series, number, cover, location));
								else addItem(new Book(title, author, cover, location));
							}
							break;
				}
			}
			br.close();
		}
		catch(IOException e) {}
	}
	private void inputLocationUnknown() {
		File file = new File(locationUnknownFile);
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while((line = br.readLine()) != null){
				int a=line.indexOf('/');
				line=line.substring(a+1);
				a=line.indexOf('|');
				String type=line.substring(0,a);
				line=line.substring(a+1);
				String series="";
				int number=0;
				boolean isSeries=false;
				if((type.equals("MOVIE"))||(type.equals("BOOK"))) {
					if((a=line.indexOf('{'))!=-1) {
						isSeries=true;
						series=line.substring(0,a);
						line=line.substring(a+1);
						a=line.indexOf('|');
						number=Integer.parseInt(line.substring(0,a));
						line=line.substring(a+1);
					}
				}
				a=line.indexOf('|');
				String title=line.substring(0,a);
				line=line.substring(a+1);
				String person="";
				a=line.indexOf('|');
				switch(type) {
					case "MOVIE":
						MovieFormat movieFormat=MovieFormat.valueOf(line.substring(0,a));
						line=line.substring(a+1);
						a=line.indexOf('|');
						if(a>0) {
							person=line.substring(0,a);
							if(isSeries) addUnknown(new Movie(title, series, number, movieFormat, null, persons.get(person)));
							else addUnknown(new Movie(title, movieFormat, null, persons.get(person)));
						}
						else {
							if(isSeries) addUnknown(new Movie(title, series, number, movieFormat, null));
							else addUnknown(new Movie(title, movieFormat, null));
						}
						break;
						case "MUSIC":
							String artist=line.substring(0,a);
							line=line.substring(a+1);
							a=line.indexOf('|');
							MusicFormat musicFormat=MusicFormat.valueOf(line.substring(0,a));
							line=line.substring(a+1);
							a=line.indexOf('|');
							if(a>0) {
								person=line.substring(0,a);
								addUnknown(new Music(title, artist, musicFormat, null, persons.get(person)));
							}
							else addUnknown(new Music(title, artist, musicFormat, null));
							break;
						case "BOOK":
							String author=line.substring(0,a);
							line=line.substring(a+1);
							a=line.indexOf('|');
							Cover cover=Cover.valueOf(line.substring(0,a));
							line=line.substring(a+1);
							a=line.indexOf('|');
							if(a>0) {
								person=line.substring(0,a);
								if(isSeries) addUnknown(new Book(title, author, series, number, cover, null, persons.get(person)));
								else addUnknown(new Book(title, author, cover, null, persons.get(person)));
							}
							else {
								if(isSeries) addUnknown(new Book(title, author, series, number, cover, null));
								else addUnknown(new Book(title, author, cover, null));
							}
							break;
				}
			}
			br.close();
		}
		catch(IOException e) {}
	}
	private void inputPersons() {
		File file = new File(personFile);
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while((line = br.readLine()) != null){
				boolean admin=false;
				if(line.charAt(0)=='A') admin=true;
				line=line.substring(1);
				int a=line.indexOf('|');
				String name=line.substring(0,a);
				line=line.substring(a+1);
				a=line.indexOf('|');
				String encryptedPassword=line.substring(0,a);
				line=line.substring(a+1);
				a=line.indexOf('|');
				String salt = line.substring(0,a);
				String residence=line.substring(a+1);
				persons.put(name, new Person(name, encryptedPassword, salt, residence, admin));
			}
			br.close();
		}
		catch(IOException e) {}
	}
	private void inputLocations() {
		File file = new File(locationFile);
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line;
			Building building = new Building("temp");
			Room room = new Room("temp");
			while((line = br.readLine()) != null){
				char cmd = line.charAt(0);
				line=line.substring(1);
				switch(cmd) {
					case 'B':
						building = new Building(line);
						buildings.add(building);
						break;
					case 'R':
						room = new Room(line);
						building.addRoom(room);
						break;
					case 'S':
						if(!building.addShelf(line)) room.addShelf(line);
						break;
				}
			}
			br.close();
		}
		catch(IOException e) {}
	}
	//login and password
	public boolean verifyLogin(Person user, String password) {
		String salt = user.getSalt();
		String calculatedHash = getEncryptedPassword(password, salt);
		if (calculatedHash.equals(user.getEncryptedPassword())) return true;
		return false;
	}
	public boolean resetPassword(Person user, Person person) {
		if((!user.equals(person))&&(!person.isAdmin())) {
			persons.get(person.getName()).setEncryptedPassword("");
			persons.get(person.getName()).setSalt("");
			return true;
		}
		return false;
	}
	public void setPassword(Person user, String password) {
        String salt = getNewSalt();
		persons.get(user.getName()).setEncryptedPassword(getEncryptedPassword(password, salt));
		persons.get(user.getName()).setSalt(salt);
    }
	private String getEncryptedPassword(String password, String salt) {
		try {
			String algorithm = "PBKDF2WithHmacSHA1";
			int derivedKeyLength = 160; // for SHA1
			int iterations = 20000; // NIST specifies 10000
			byte[] saltBytes = Base64.getDecoder().decode(salt);
			KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
			SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
			byte[] encBytes = f.generateSecret(spec).getEncoded();
			return Base64.getEncoder().encodeToString(encBytes);
		}
		catch(Exception e) {}
		return "";
    }
	private String getNewSalt() {
		// Don't use Random!
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			// NIST recommends minimum 4 bytes. We use 8.
			byte[] salt = new byte[8];
			random.nextBytes(salt);
			return Base64.getEncoder().encodeToString(salt);
		}
		catch(Exception e) {}
		return "";
	}
	//check in and out
	public void checkOut(Person user, InventoryItem item) {
		inventory.get(inventory.indexOf(item)).checkOut(user);
	}
	public void checkIn(Person user, InventoryItem item) {
		inventory.get(inventory.indexOf(item)).checkIn(user);
	}
	//item
	public void addUnknown(InventoryItem item) {
		String title = item.getTitle();
		int n=0;
		while((n<locationUnknown.size())&&(locationUnknown.get(n).getTitle().compareTo(title)<0)) {
			n++;
		}
		locationUnknown.add(n, item);
	}
	public void setLocation(InventoryItem item, Location location) {
		deleteUnknown(item);
		item.setLocation(location);
		addItem(item);
	}
	public void deleteUnknown(InventoryItem item) {
		locationUnknown.remove(item);
	}
	public void addItem(InventoryItem item) {
		String order = item.getOrder().toUpperCase();
		int n=0;
		while((n<inventory.size())&&(inventory.get(n).getOrder().toUpperCase().compareTo(order)<0)) {
			n++;
		}
		inventory.add(n, item);
	}
	public void editLocation(InventoryItem item, Location location) {
		inventory.get(inventory.indexOf(item)).setLocation(location);
	}
	public void deleteItem(InventoryItem item) {
		inventory.remove(item);
	}
	public ArrayList<InventoryItem> searchItem(String line) {
		return searchItem(inventory, line);
	}
	public ArrayList<InventoryItem> searchItem(ArrayList<InventoryItem> items, String line) {
		ArrayList<String> terms = new ArrayList<String>();
		ArrayList<InventoryItem> output = new ArrayList<InventoryItem>(items);
		line=line.toUpperCase();
		while(line.indexOf(' ')!=-1) {
			int i = line.indexOf(' ');
			terms.add(line.substring(0,i));
			line=line.substring(i+1);
		}
		terms.add(line);
		for(String term: terms) {
			for(InventoryItem item: items) {
				if(item.getTitle().toUpperCase().contains(term));
				else if((item.getType()==Type.MUSIC)&&(((Music)item).getArtist().toUpperCase().contains(term))&&(!output.contains(term)));
				else if((item.getType()==Type.BOOK)&&(((Book)item).getAuthor().toUpperCase().contains(term))&&(!output.contains(term)));
				else if((item.getType()==Type.MOVIE)&&(((Movie)item).isSeries())&&(((Movie)item).getSeries().toUpperCase().contains(term))&&(!output.contains(term)));
				else if((item.getType()==Type.BOOK)&&(((Book)item).isSeries())&&(((Book)item).getSeries().toUpperCase().contains(term))&&(!output.contains(term)));
				else output.remove(item);
			}
		}
		return output;
	}
	//persons
	public ArrayList<Person> getPeople() {
		ArrayList<Person> output = new ArrayList<Person>();
		for (Map.Entry<String,Person> person : persons.entrySet()) {
			output.add(person.getValue());
		}
		return output;
	}
	public boolean addPerson(Person person) {
		if(persons.containsKey(person.getName())) return false;
		persons.put(person.getName(),person);
		return true;
	}
	public boolean deletePerson(Person person) {
		for(InventoryItem item: getPersonItems(person)) {
			checkIn(person, item);
		}
		if(persons.size()==1) return false;
		if(persons.remove(person.getName())!=null) return true;
		return false;
	}
	public void editResidence(Person person, String residence) {
		editResidence(person.getName(), residence);
	}
	public void editResidence(String personName, String residence) {
		persons.get(personName).setResidence(residence);
	}
	public boolean editAdmin(Person user, Person person) {
		if(!person.isAdmin()) persons.get(person.getName()).makeAdmin();
		else if(!user.equals(person)) persons.get(person.getName()).removeAdmin();
		else return false;
		return true;
	}
	//locations
		//building
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	public void renameBuilding(Building building, String buildingName) {
		for(InventoryItem item: inventory) {
			if(item.getLocation().getBuilding().equals(building.getName())) item.getLocation().setBuilding(buildingName);
		}
		buildings.get(buildings.indexOf(building)).setName(buildingName);
	}
	public boolean addBuilding(Building building) {
		if(buildings.contains(building)) return false;
		buildings.add(building);
		return true;
	}
	public boolean deleteBuilding(Building building) {
		if(buildings.size()>1) {
			buildings.remove(building);
			for(InventoryItem item: inventory) {
				if(item.getLocation().getBuilding().equals(building.getName())) {
					addUnknown(item);
				}
			}
			for(InventoryItem item: locationUnknown) {
				item.setLocation(null);
				deleteItem(item);
			}
			return true;
		}
		return false;
	}
		//rooms
	public ArrayList<Room> getRooms(Building building) {
		return buildings.get(buildings.indexOf(building)).getRooms();
	}
	public void renameRoom(Building building, Room room, String roomName) {
		for(InventoryItem item: inventory) {
			if((item.getLocation().getBuilding().equals(building.getName()))&&(item.getLocation().getRoom().equals(room.getName()))) item.getLocation().setRoom(roomName);
		}
		buildings.get(buildings.indexOf(building)).getRoom(room).setName(roomName);
	}
	public boolean addRoom(Building building, Room room) {
		if(buildings.get(buildings.indexOf(building)).getRooms().contains(room)) return false;
		buildings.get(buildings.indexOf(building)).addRoom(room);
		return true;
	}
	public boolean deleteRoom(Building building, Room room) {		
		if(building.getRooms().size()>1) {
			buildings.get(buildings.indexOf(building)).deleteRoom(room);
			for(InventoryItem item: inventory) {
				if((item.getLocation().getBuilding().equals(building.getName()))&&(item.getLocation().getRoom().equals(room.getName()))) {
					addUnknown(item);
				}
			}
			for(InventoryItem item: locationUnknown) {
				item.setLocation(null);
				deleteItem(item);
			}
			return true;
		}
		return false;
	}
		//shelves
	public ArrayList<String> getShelves(Building building) {
		return buildings.get(buildings.indexOf(building)).getShelves();
	}
	public ArrayList<String> getShelves(Building building, Room room) {
		return buildings.get(buildings.indexOf(building)).getRoom(room).getShelves();
	}
	public void renameShelf(Building building, String shelf, String shelfName) {
		for(InventoryItem item: inventory) {
			if((item.getLocation().getBuilding().equals(building.getName()))&&(item.getLocation().getShelf().equals(shelf))) item.getLocation().setShelf(shelfName);
		}
		buildings.get(buildings.indexOf(building)).renameShelf(shelf, shelfName);
	}
	public void renameShelf(Building building, Room room, String shelf, String shelfName) {
		for(InventoryItem item: inventory) {
			if((item.getLocation().getBuilding().equals(building.getName()))&&(item.getLocation().getRoom().equals(room.getName()))&&(item.getLocation().getShelf().equals(shelf))) item.getLocation().setShelf(shelfName);
		}
		buildings.get(buildings.indexOf(building)).getRoom(room).renameShelf(shelf, shelfName);
	}
	public boolean addShelf(Building building, String shelf) {
		if(buildings.get(buildings.indexOf(building)).getShelves().contains(shelf)) return false;
		buildings.get(buildings.indexOf(building)).addShelf(shelf);
		return true;
	}
	public boolean addShelf(Building building, Room room, String shelf) {
		if(buildings.get(buildings.indexOf(building)).getRoom(room).getShelves().contains(shelf)) return false;
		buildings.get(buildings.indexOf(building)).getRoom(room).addShelf(shelf);
		return true;
	}
	public boolean deleteShelf(Building building, String shelf) {
		if(building.getShelves().size()>1) {
			buildings.get(buildings.indexOf(building)).deleteShelf(shelf);
			for(InventoryItem item: inventory) {
				if((item.getLocation().getBuilding().equals(building.getName()))&&(item.getLocation().getShelf().equals(shelf))) {
					addUnknown(item);
				}
			}
			for(InventoryItem item: locationUnknown) {
				item.setLocation(null);
				deleteItem(item);
			}
			return true;
		}
		return false;
	}
	public boolean deleteShelf(Building building, Room room, String shelf) {
		if(building.getRoom(room).getShelves().size()>1) {
			buildings.get(buildings.indexOf(building)).getRoom(room).deleteShelf(shelf);
			for(InventoryItem item: inventory) {
				if((item.getLocation().getBuilding().equals(building.getName()))&&(item.getLocation().getRoom().equals(room.getName()))&&(item.getLocation().getShelf().equals(shelf))) {
					addUnknown(item);
				}
			}
			for(InventoryItem item: locationUnknown) {
				item.setLocation(null);
				deleteItem(item);
			}
			return true;
		}
		return false;
	}
	//general get functions
	public ArrayList<InventoryItem> getTypeList(Type type) {
		ArrayList<InventoryItem> movies = new ArrayList<InventoryItem>();
		for(InventoryItem item: inventory) {
			if(item.getType()==type) {
				movies.add(item);
			}
		}
		return movies;
	}
	public ArrayList<InventoryItem> getInventory() {
		return inventory;
	}
	public ArrayList<InventoryItem> getUnknowns() {
		return locationUnknown;
	}
	public ArrayList<InventoryItem> getFreeInventory() {
		ArrayList<InventoryItem> output = new ArrayList<InventoryItem>();
		for(InventoryItem item: inventory) {
			if(!item.checkedOut()) output.add(item);
		}
		return output;
	}
	public ArrayList<InventoryItem> getOutInventory() {
		ArrayList<InventoryItem> output = new ArrayList<InventoryItem>();
		for(InventoryItem item: inventory) {
			if(item.checkedOut()) output.add(item);
		}
		return output;
	}
	public ArrayList<InventoryItem> getPersonItems(Person user) {
		ArrayList<InventoryItem> output = new ArrayList<InventoryItem>();
		for(InventoryItem item: inventory) {
			if((item.getPerson()!=null)&&(item.getPerson().equals(user))) output.add(item);
		}
		return output;
	}
	//general boolean functions
	public boolean hasItems() {
		return inventory.size()>0;
	}
	public boolean hasLocations() {
		return buildings.size()>0;
	}
	//output
	public void outputToFile() {
		try {
			FileWriter output = new FileWriter(itemFile);
			for(InventoryItem item: inventory) {
				output.write(item.output()+"\n");
			}
			output.close();
			output = new FileWriter(locationUnknownFile);
			for(InventoryItem item: locationUnknown) {
				output.write(item.output()+"\n");
			}
			output.close();
			output = new FileWriter(locationFile);
			for(Building building: buildings) {
				output.write("B"+building.getName()+"\n");
				if(building.hasRooms()) {
					for(Room room: building.getRooms()) {
						output.write('R'+room.getName()+"\n");
						for(String shelf: room.getShelves()) {
							output.write('S'+shelf+"\n");
						}
					}
				}
				else {
					for(String shelf: building.getShelves()) {
						output.write('S'+shelf+"\n");
					}
				}
			}
			output.close();
			output = new FileWriter(personFile);
			for (Map.Entry<String,Person> person : persons.entrySet()) {
				output.write(person.getValue().output()+"\n");
			}
			output.close();
		} 
		catch(IOException e) {}
	}
}