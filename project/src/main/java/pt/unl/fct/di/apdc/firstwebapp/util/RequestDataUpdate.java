package pt.unl.fct.di.apdc.firstwebapp.util;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestDataUpdate {
    @JsonProperty("operation")
    public int operation;

    @JsonProperty("input")
    public RequestUpdate input;

    @JsonProperty("token")
    public AuthToken token;

    @JsonProperty("output")
    public String output;

    public RequestDataUpdate() {
    }
}
