import java.util.*;
public class Room{
	private String name;
	private ArrayList<String> shelves;
	public Room(String name) {
		this.name=name;
		shelves=new ArrayList<String>();
	}
	public Room(String name, ArrayList<String> shelves) {
		this.name=name;
		this.shelves=shelves;
	}
	public void addShelf(String shelf) {
		shelves.add(shelf);
	}
	public ArrayList<String> getShelves() {
		return shelves;
	}
	public void addShelves(ArrayList<String> shelves) {
		for(String shelf: shelves) {
			this.shelves.add(shelf);
		}
	}
	public void renameShelf(String oldShelf, String newShelf) {
		shelves.set(shelves.indexOf(oldShelf), newShelf);
	}
	public boolean deleteShelf(String shelf) {
		if(shelves.size()>1) {
			shelves.remove(shelf);
			return true;
		}
		return false;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	@Override
	public boolean equals(Object o) {
		Room room = (Room)o;
		if(this.name.equals(room.getName())) return true;
		return false;
	}
}