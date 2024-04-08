package pt.unl.fct.di.apdc.firstwebapp.util;

import java.util.UUID;

public class AuthToken {
    public static  final long EXPIATION_TIME = 1000*60*60*2;
    public String name;
    public String tokenID;
    public long role;
    public long createDate;
    public long expirationDate;
    public AuthToken(){}
    public AuthToken(String userName,long role){
        this.name = userName;
        this.tokenID= UUID.randomUUID().toString();
        this.role = role;
        this.createDate = System.currentTimeMillis();
        this.expirationDate = createDate + AuthToken.EXPIATION_TIME;
    }
}
