package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.google.gson.Gson;
import pt.unl.fct.di.apdc.firstwebapp.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("/ops")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Operation {
    private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
    private final Gson g = new Gson();
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    public Operation() {
    }
    @POST
    @Path("/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeStatus(qqcois data) {
        if (data.token.expirationDate < System.currentTimeMillis()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token expired.").build();
        }
        qqcois.struct input = data.input;
        AuthToken token = data.token;
        long role = token.role;
        LOG.fine("Attempt to list users");
        Key key = datastore.newKeyFactory().setKind("User").newKey(input.username);
        Entity user = datastore.get(key);//be changed
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).entity("User does not exist.").build();
        }
        if(role == 4 ||user.getLong("user_level") < role){
            user = Entity.newBuilder(user).set("user_state",input.status).build();
            datastore.update(user);
            return Response.ok(g.toJson(user)).build();
        }else {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to change this user's status.").build();
        }
    }
    @POST
    @Path("/role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeRole(qqcois data) {
        if (data.token.expirationDate < System.currentTimeMillis()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token expired.").build();
        }
        qqcois.struct input = data.input;
        AuthToken token = data.token;
        long role = token.role;
        Key key = datastore.newKeyFactory().setKind("User").newKey(input.username);
        Entity user = datastore.get(key);//be changed
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).entity("User does not exist.").build();
        }
        if(role == 4 ||(role == 3 && user.getLong("user_level") < 3)){
            user = Entity.newBuilder(user).set("user_level",input.role).build();
            datastore.update(user);
            return Response.ok().build();
        }else {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to change this user's status.").build();
        }
    }
    @POST
    @Path("/List")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ListUser(RequestData data) {
        if (data.token.expirationDate < System.currentTimeMillis()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token expired.").build();
        }
        AuthToken token = data.token;
        LOG.fine("Attempt to list users");
        long role = token.role;
        Query<Entity> query = Query.newEntityQueryBuilder().setKind("User").build();
        QueryResults<Entity> results = datastore.run(query);
        List<UserData> users = new java.util.ArrayList();
        if(role == 1){
            results.forEachRemaining(entity -> {
                long ur = entity.getLong("user_level");
                boolean state = entity.getBoolean("user_state");
                boolean privacy = entity.getBoolean("user_privacy");
                if(ur == 1 && state&& privacy){
                    users.add(new UserData(entity.getString("user_name"),
                            entity.getString("user_email"),
                            entity.getString("user_job")));
                }
            });
        }else if(role == 2){
            extracted(results, users,1);

        }else if(role == 3){
            extracted(results, users,3);

        }else if(role == 4){
            extracted(results, users,4);
        }
        return Response.ok(g.toJson(users)).build();
    }

    private void extracted(QueryResults<Entity> results, List<UserData> users,long r) {
        results.forEachRemaining(entity -> {
            long ur = entity.getLong("user_level");
            if(ur <= r){
                users.add(new UserData(entity.getKey().getName(),
                        entity.getString("user_name"),
                        entity.getString("user_email"),
                        entity.getString("user_job"),
                        entity.getString("user_pwd"),
                        entity.getString("user_telephone"),
                        entity.getString("user_workplace"),
                        entity.getString("user_postcode"),
                        entity.getString("user_nif"),
                        entity.getBoolean("user_privacy"),
                        entity.getBoolean("user_state"),
                        entity.getLong("user_level")));
            }
        });
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doLogout(RequestData data) {
        LOG.fine("Logout attempt by user: " + data.token.name);
        if (data.token.expirationDate < System.currentTimeMillis()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token expired.").build();
        }
        Key authKey = datastore.newKeyFactory().setKind("AuthToken").newKey(data.token.tokenID);
        datastore.delete(authKey);
        return Response.ok().build();
    }
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(qqcoisa rsdata) {
        LOG.fine("Attempt to delete user: " + rsdata.token.name);
        if (rsdata.token.expirationDate < System.currentTimeMillis()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token expired.").build();
        }
        Transaction txn = datastore.newTransaction();
        try {
            long rrole = rsdata.token.role;
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(rsdata.input);
            Entity user = datastore.get(userKey);
            if(user == null){
                return Response.status(Response.Status.NOT_FOUND).entity("User does not exist.").build();
            }
            long role = user.getLong("user_level");
            if(userKey.getName().equals(rsdata.token.name)||(rrole == 3 && role < 3) || rrole == 4){
                txn.delete(userKey);
                LOG.info("User deleted " + rsdata.input);
                txn.commit();
                return Response.ok().build();
            }else {
                return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to delete this user.").build();
            }
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }
    @POST
    @Path("/changepassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doChangePassword(RequestData rsdata) {
        ChangePasswordData data = rsdata.input;
        LOG.fine("Attempt to change password for user: " + rsdata.token.name);
        if (rsdata.token.expirationDate < System.currentTimeMillis()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token expired.").build();
        }
        Transaction txn = datastore.newTransaction();
        try {
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(rsdata.token.name);
            Entity user = datastore.get(userKey);
            if (user.getString("user_pwd").equals(DigestUtils.sha512Hex(data.oldPassword))) {
                user = Entity.newBuilder(user)
                        .set("user_pwd", DigestUtils.sha512Hex(data.newPassword))
                        .build();//?
                txn.update(user);
                LOG.info("User updated " + rsdata.token.name);
                txn.commit();
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity("Wrong Password.").build();
            }
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doSelfUpdate(RequestDataUpdate rsdata) {
        RequestUpdate data = rsdata.input;
//        if(datastore.get(datastore.newKeyFactory().setKind("AuthToken").newKey(rsdata.token.tokenID))!=null)
//            return Response.status(Response.Status.BAD_REQUEST).entity("AuthToken invalid.").build();
        LOG.fine("Attempt to update user: " + rsdata.token.name);
        if (rsdata.token.expirationDate < System.currentTimeMillis()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token expired.").build();
        }
        Transaction txn = datastore.newTransaction();
        try {
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(rsdata.token.name);
            Entity user = datastore.get(userKey);
            user = Entity.newBuilder(user)
                    .set("user_job", data.job.equals("") ? user.getString("user_job") : data.job)
                    .set("user_nif", data.nif.equals("")? user.getString("user_nif") : data.nif)
                    .set("user_postcode", data.postCode.equals("")? user.getString("user_postcode") : data.postCode)
                    .set("user_privacy", data.privacy == user.getBoolean("user_privacy"))
                    .set("user_state", data.state == user.getBoolean("user_state"))
                    .set("user_telephone", data.telephone.equals("")? user.getString("user_telephone") : data.telephone)
                    .set("user_workplace", data.workplace.equals("")? user.getString("user_workplace") : data.workplace)
                    .build();
            txn.update(user);
            LOG.info("User updated " + rsdata.token.name);
            txn.commit();
            return Response.ok().build();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doRegistrationV1(RegisterData data) {
        LOG.fine("Attempt to register user: " + data.username);
        Transaction txn = datastore.newTransaction();
        try {
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
            Entity user = txn.get(userKey);
            if (user != null) {
                txn.rollback();
                return Response.status(Response.Status.BAD_REQUEST).entity("User already exist.").build();
            } else {
                user = Entity.newBuilder(userKey)
                        .set("user_pwd", DigestUtils.sha512Hex(data.password))
                        .set("user_name", data.name)
                        .set("user_email", data.email)
                        .set("user_job", data.job)
                        .set("user_level", 1)
                        .set("user_nif", data.nif)
                        .set("user_postcode", data.postcode)
                        .set("user_privacy", data.privacy)
                        .set("user_state", false)
                        .set("user_telephone", data.telephone)
                        .set("user_workplace", data.workplace)
                        .set("user_creation_time", Timestamp.now())
                        .build();
                txn.add(user);
                LOG.info("User registered " + data.username);
                txn.commit();
                return Response.ok("{}").build();
            }
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }


}
