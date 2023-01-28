import java.util.*;
public class Building {
	private String name;
	private ArrayList<Room> rooms;
	private ArrayList<String> shelves;
	private boolean hasRooms;
	public Building(String name) {
		this.name=name;
		this.hasRooms=false;
		this.shelves=new ArrayList<String>();
	}
	public Building(String name, Room room) {
		this.name=name;
		this.rooms=new ArrayList<Room>();
		this.rooms.add(room);
		this.hasRooms=true;
	}
	public Building(String name, String shelf) {
		this.name=name;
		this.shelves=new ArrayList<String>();
		this.shelves.add(shelf);
		this.hasRooms=false;
	}
	public void addRoom(Room room) {
		if(!hasRooms) makeRooms();
		rooms.add(room);
	}
	private void makeRooms() {
		hasRooms=true;
		this.rooms=new ArrayList<Room>();
		if(shelves.size()>0) {
			Room room = new Room("DEFAULT_ROOM");
			room.addShelves(shelves);
			rooms.add(room);
			shelves.clear();
		}
	}
	public boolean addShelf(String shelf) {
		if(hasRooms) return false;
		shelves.add(shelf);
		return true;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	public Room getRoom(Room room) {
		return rooms.get(rooms.indexOf(room));
	}
	public boolean deleteRoom(Room room) {
		if(rooms.size()>1) {
			rooms.remove(room);
			return true;
		}
		return false;
	}
	public boolean hasRooms() {
		return hasRooms;
	}
	public ArrayList<String> getShelves() {
		return shelves;
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
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Building)) return false;
		Building building = (Building)o;
		if(this.name.equals(building.getName())) return true;
		return false;
	}
}