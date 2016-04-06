package antbuddy.htk.com.antbuddy2016.api;

import java.util.HashMap;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.model.Organization;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.Token;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.util.RequestKey;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by thanhnguyen on 01/04/2016.
 */
public interface API {


    @FormUrlEncoded
    @POST("/users/session/")
    Call<Token> GETLogin(@Field(RequestKey.email)String  email, @Field(RequestKey.password)String  password);

    @GET("/api/organizations/")
    Call<List<Organization>> GETOrganizations(@Header("authorization") String token);

    /* ----Request API with domain --- */

    @GET("/api/users/me/")
    Call<UserMe> GETUserProfile(@Header("authorization") String token);

    @GET("/api/users/")
    Call<List<User>> GETUsers(@Header("authorization") String token);

    @GET("/api/rooms/")
    Call<List<Room>> GETRooms(@Header("authorization") String token);

    @POST("/api/messages/")
    Call<ChatMessage> newMessageToHistory(@Header("authorization") String token, @Body HashMap<String, Object> body);

    @GET("/api/messages?limit=50")
    Call<List<ChatMessage>> GETMessages(@Header("authorization") String token, @Query("before") String time, @Query("chatRoom") String chatRoomId,@Query("type") String typeChat);

}
