package iss.workshop.adproject.DataService;

import iss.workshop.adproject.Model.BlogUser;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import java.util.List;

import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserDataService {

    @GET("/account/api/blogusers")
    Call<List<BlogUser>> findAllActiveUser();

    @Headers("Content-Type: application/json")
    @POST("/account/api/bloguser")
    Call<ResponseBody> createUser(@Body BlogUser user);

    @PUT("/account/api/setting/{name}")
    Call<ResponseBody>updateUser(@Path("name")String name, @Body BlogUser inUser);

    @GET("/account/api/blogusers/{name}")
    Call<BlogUser> getBloguserByDisplayname(@Path("name")String name);
}
