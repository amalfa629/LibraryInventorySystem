public class Movie extends InventoryItem{
	private MovieFormat format;
	private String series;
	private int number;
	public Movie(String title, MovieFormat format, Location location) {
		this.title=title;
		this.order=title+" "+format;
		this.format=format;
		this.type=Type.MOVIE;
		this.location=location;
		this.person=null;
		this.number=-1;
	}
	public Movie(String title, MovieFormat format, Location location, Person person) {
		this.title=title;
		this.order=title+" "+format;
		this.format=format;
		this.type=Type.MOVIE;
		this.location=location;
		this.person=person;
		this.number=-1;
	}
	public Movie(String title, String series, int number, MovieFormat format, Location location) {
		this.title=title;
		this.series=series;
		this.number=number;
		this.order=series+" MOVIE "+number+" "+title+" "+format;
		this.format=format;
		this.type=Type.MOVIE;
		this.location=location;
		this.person=null;
	}
	public Movie(String title, String series, int number, MovieFormat format, Location location, Person person) {
		this.title=title;
		this.series=series;
		this.number=number;
		this.order=series+" MOVIE "+number+" "+title+" "+format;
		this.format=format;
		this.type=Type.MOVIE;
		this.location=location;
		this.person=person;
	}
	public MovieFormat getFormat() {
		return format;
	}
	public String getSeries() {
		return series;
	}
	public int getNumber() {
		return number;
	}
	public boolean isSeries() {
		if(number>0) return true;
		return false;
	}
	@Override
	public String output() {
		String l="null/";
		if(location!=null) l=location.output();
		String output=l+type+"|";
		if(isSeries()) output+=series+"{"+number+"|";
		output+=title+"|"+format+"|";
		if(checkedOut()) output+=person.getName()+"|";
		return output;
	}
}