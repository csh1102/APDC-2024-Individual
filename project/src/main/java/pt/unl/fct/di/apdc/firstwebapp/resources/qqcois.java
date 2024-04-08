package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.unl.fct.di.apdc.firstwebapp.util.*;
public class qqcois {
    static class struct{
        public String username;
        public boolean status;
        public int role;
        public struct(){}
    }
    @JsonProperty("operation")
    public int operation;

    @JsonProperty("input")
    public struct input;

    @JsonProperty("token")
    public AuthToken token;

    @JsonProperty("output")
    public String output;
}

