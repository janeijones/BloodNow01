package zetrixweb.com.bloodnow.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/fcm/send")
    Call<Message> sendMessage(@Body Message message);

}
