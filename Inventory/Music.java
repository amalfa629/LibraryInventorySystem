public class Music extends InventoryItem{
	private String artist;
	private MusicFormat format;
	public Music(String title, String artist, MusicFormat format, Location location) {
		this.title=title;
		this.artist=artist;
		this.order=title;
		this.format=format;
		this.type=Type.MUSIC;
		this.location=location;
		this.person=null;
	}
	public Music(String title, String artist, MusicFormat format, Location location, Person person) {
		this.title=title;
		this.artist=artist;
		this.order=title+" "+format;
		this.format=format;
		this.type=Type.MUSIC;
		this.location=location;
		this.person=person;
	}
	public MusicFormat getFormat() {
		return format;
	}
	public String getArtist() {
		return artist;
	}
	@Override
	public String output() {
		String l="null/";
		if(location!=null) l=location.output();
		if(checkedOut()) return l+type+"|"+title+"|"+artist+"|"+format+"|"+person.getName()+"|";
		return l+type+"|"+title+"|"+artist+"|"+format+"|";
	}
}