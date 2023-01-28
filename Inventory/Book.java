public class Book extends InventoryItem{
	private String author;
	private Cover cover;
	private String series;
	private int number;
	public Book(String title, String author, Cover cover, Location location) {
		this.title=title;
		this.author=author;
		this.order=title+" "+cover;
		this.cover=cover;
		this.type=Type.BOOK;
		this.location=location;
		this.person=null;
	}
	public Book(String title, String author, Cover cover, Location location, Person person) {
		this.title=title;
		this.author=author;
		this.order=title+" "+cover;
		this.cover=cover;
		this.type=Type.BOOK;
		this.location=location;
		this.person=person;
	}
	public Book(String title, String author, String series, int number, Cover cover, Location location) {
		this.title=title;
		this.author=author;
		this.series=series;
		this.number=number;
		this.order=series+" BOOK "+number+" "+title+" "+cover;
		this.cover=cover;
		this.type=Type.BOOK;
		this.location=location;
		this.person=null;
	}
	public Book(String title, String author, String series, int number, Cover cover, Location location, Person person) {
		this.title=title;
		this.author=author;
		this.series=series;
		this.number=number;
		this.order=series+" BOOK "+number+" "+title+" "+cover;
		this.cover=cover;
		this.type=Type.BOOK;
		this.location=location;
		this.person=person;
	}
	public String getAuthor() {
		return author;
	}
	public Cover getCover() {
		return cover;
	}
	public String getSeries() {
		return series;
	}
	public int getNumber() {
		return number;
	}
	public boolean isSeries() {
		if(number!=0) return true;
		return false;
	}
	@Override
	public String output() {
		String l="null/";
		if(location!=null) l=location.output();
		String output=l+type+"|";
		if(isSeries()) output+=series+"{"+number+"|";
		output+=title+"|"+author+"|"+cover+"|";
		if(checkedOut()) output+=person.getName()+"|";
		return output;
	}
}
