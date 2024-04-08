package pt.unl.fct.di.apdc.firstwebapp.util;

import com.google.appengine.api.datastore.Email;

public class UserData {

	public String username;
	public String email;
	public String name;
	public String password, telephone, job, workplace, postcode;
	public String nif;
	public boolean privacy = false;
	public boolean state = false;
	public long role = 0;
	public UserData() {
	}
	public UserData(String username, String email, String name) {
		this.username = username;
        this.email = email;
        this.name = name;
	}

	public UserData(String user_name,String name, String user_email, String user_job, String user_pwd, String user_telephone, String user_workplace, String user_postcode, String user_nif, boolean user_privacy, boolean user_state, long user_role) {
		this.username = user_name;
        this.email = user_email;
        this.job = user_job;
        this.password = user_pwd;
        this.telephone = user_telephone;
        this.workplace = user_workplace;
        this.postcode = user_postcode;
        this.nif = user_nif;
        this.privacy = user_privacy;
        this.state = user_state;
        this.role = user_role;
	}
}
