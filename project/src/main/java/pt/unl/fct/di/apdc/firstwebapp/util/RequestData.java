package pt.unl.fct.di.apdc.firstwebapp.util;
import com.fasterxml.jackson.annotation.JsonProperty;
public class RequestData {
    @JsonProperty("operation")
    public int operation;

    @JsonProperty("input")
    public ChangePasswordData input;

    @JsonProperty("token")
    public AuthToken token;

    @JsonProperty("output")
    public String output;

    public RequestData() {
    }
}
