package iss.workshop.adproject.DataService;

import iss.workshop.adproject.Model.Blog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BlogDataService {

    @POST("/home/api/create")
    Call<ResponseBody> createBlog(@Body Blog blog);
}
