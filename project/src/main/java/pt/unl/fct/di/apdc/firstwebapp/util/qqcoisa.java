package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class qqcoisa {
    @JsonProperty("operation")
    public int operation;

    @JsonProperty("input")
    public String input;

    @JsonProperty("token")
    public AuthToken token;

    @JsonProperty("output")
    public String output;
}
