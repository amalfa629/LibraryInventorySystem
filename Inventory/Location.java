public class Location {
	private String building;
	private String room;
	private String shelf;
	public Location(String building, String room, String shelf) {
		this.building=building;
		this.room=room;
		this.shelf=shelf;
	}
	public Location(String building, String shelf) {
		this.building=building;
		this.room="";
		this.shelf=shelf;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building=building;
	}
	public boolean hasRoom() {
		if(room.length()>0) {
			return true;
		}
		return false;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room=room;
	}
	public String getShelf() {
		return shelf;
	}
	public void setShelf(String shelf) {
		this.shelf=shelf;
	}
	public String output() {
		if(hasRoom()) return building+"/"+room+"/"+shelf+"/";
		return building+"/"+shelf+"/";
	}
}