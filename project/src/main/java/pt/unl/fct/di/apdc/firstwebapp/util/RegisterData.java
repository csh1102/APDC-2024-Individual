package pt.unl.fct.di.apdc.firstwebapp.util;

import com.google.appengine.api.datastore.Email;

public class RegisterData{
    public String username;
    public String name;
    public String password;
    public String email;
    public String telephone;
    public RegisterData(){}
    public RegisterData(String username,String name, String password, String email,String telephone){
        this.username = username;
        this.name=name;
        this.password = password;
        this.email = email;
        this.telephone = telephone;
    }
    public boolean validRegistration(){
        return !username.trim().equals("")
                &&!name.trim().equalsIgnoreCase("")
                &&!password.trim().equalsIgnoreCase("")
                &&!telephone.trim().equalsIgnoreCase("");
    }
}
