public class InventoryItem {
	public String title;
	public Type type;
	public Location location;
	public Person person;
	public String order;
	public String getTitle() {
		return title;
	}
	public Type getType() {
		return type;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location=location;
	}
	public Person getPerson() {
		return person;
	}
	public String getOrder() {
		return order;
	}
	public void changeLocation(String building, String shelf) {
		location=new Location(building, shelf);
	}
	public void changeLocation(String building, String room, String shelf) {
		location=new Location(building, room, shelf);
	}
	public void checkOut(Person person) {
		this.person=person;
	}
	public boolean checkIn(Person person) {
		if(person.equals(person)) {
			this.person=null;
			return true;
		}
		return false;
	}
	public boolean checkedOut() {
		if((person==null)) {
			return false;
		}
		return true;
	}
	public String output() {
		return "";
	}
}