public class Person {
	private String name;
	private String encryptedPassword;
	private String salt;
	private String residence;
	private boolean admin;
	public Person(String name, String residence) {
		this.name=name;
		this.encryptedPassword="";
		this.residence=residence;
		this.admin=false;
	}
	public Person(String name, String residence, boolean admin) {
		this.name=name;
		this.encryptedPassword="";
		this.residence=residence;
		this.admin=admin;
	}
	public Person(String name, String encryptedPassword, String salt, String residence) {
		this.name=name;
		this.encryptedPassword=encryptedPassword;
		this.salt=salt;
		this.residence=residence;
		this.admin=false;
	}
	public Person(String name, String encryptedPassword, String salt, String residence, boolean admin) {
		this.name=name;
		this.encryptedPassword=encryptedPassword;
		this.salt=salt;
		this.residence=residence;
		this.admin=admin;
	}
	public String getName() {
		return name;
	}
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	public String getSalt() {
		return salt;
	}
	public boolean hasPassword() {
		return encryptedPassword.length()>0;
	}
	public String getResidence() {
		return residence;
	}
	public void setResidence(String residence) {
		this.residence=residence;
	}
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword=encryptedPassword;
	}
	public void setSalt(String salt) {
		this.salt=salt;
	}
	public void makeAdmin() {this.admin=true;}
	public void removeAdmin() {this.admin=false;}
	public boolean isAdmin() {return admin;}
	public String output() {
		if(admin) return "A"+name+"|"+encryptedPassword+"|"+salt+"|"+residence;
		return "S"+name+"|"+encryptedPassword+"|"+salt+"|"+residence;
	}
}