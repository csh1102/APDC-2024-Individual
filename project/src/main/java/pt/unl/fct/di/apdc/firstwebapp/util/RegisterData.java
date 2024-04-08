package pt.unl.fct.di.apdc.firstwebapp.util;

import com.google.appengine.api.datastore.Email;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterData {
    @JsonProperty("username")
    public String username;

    @JsonProperty("password")
    public String password;

    @JsonProperty("name")
    public String name;

    @JsonProperty("email")
    public String email;

    @JsonProperty("job")
    public String job;

    @JsonProperty("nif")
    public String nif;

    @JsonProperty("postCode")
    public String postcode;

    @JsonProperty("telephone")
    public String telephone;

    @JsonProperty("workplace")
    public String workplace;

    @JsonProperty("privacy")
    public boolean privacy;

    @JsonProperty("state")
    public boolean state;

    public boolean validRegistration() {
        return username != null && !username.isEmpty() &&
                password != null && !password.isEmpty() &&
                email != null && !email.isEmpty();
    }
}
