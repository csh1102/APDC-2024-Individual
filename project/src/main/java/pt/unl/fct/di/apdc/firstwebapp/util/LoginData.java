package pt.unl.fct.di.apdc.firstwebapp.util;
import com.google.cloud.datastore.*;
public class LoginData {
	
	public String username;
	public String password;
	
	public LoginData() {
		
	}
	
	public LoginData(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
}
