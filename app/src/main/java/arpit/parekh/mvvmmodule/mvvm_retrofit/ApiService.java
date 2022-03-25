package arpit.parekh.mvvmmodule.mvvm_retrofit;

import java.util.List;

import arpit.parekh.mvvmmodule.mvvm_retrofit.data.Post;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("posts")
    Call<List<Post>> getPosts();
}
