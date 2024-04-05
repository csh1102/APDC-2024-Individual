package pt.unl.fct.di.apdc.firstwebapp.util;

import com.google.appengine.api.datastore.Email;

public class UserData {

	public String username;
	public Email email;
	public String name;
	public String password, telephone, job, workplace, postcode;
	long nif;
	boolean privacy = false;
	boolean state = false;
	int level = 0;
	public UserData() {
		
	}
	
	public UserData(String username, String name, String email, String password,
					String telephone,boolean state,int level, boolean privacy, String job,long nif,
					String workplace,String postcode) {
		this.username = username;
		this.name = name;
		this.email = new Email(email);
		this.password = password;
		this.telephone = telephone;
		this.state = state;
		this.level=level;
		this.privacy = privacy;
		this.job = job;
		this.nif = nif;
		this.workplace = workplace;
		this.postcode = postcode;
	}
	
}
