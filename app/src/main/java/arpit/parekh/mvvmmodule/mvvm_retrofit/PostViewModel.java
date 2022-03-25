package arpit.parekh.mvvmmodule.mvvm_retrofit;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import arpit.parekh.mvvmmodule.mvvm_retrofit.data.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel extends ViewModel {

    ApiService service;

    MutableLiveData<List<Post>> mutablePostLiveData;

    public PostViewModel(){
        mutablePostLiveData = new MutableLiveData<>();
        service = ApiServiceInstance.getService();
    }

    public MutableLiveData<List<Post>> getMutablePostLiveData() {
        return mutablePostLiveData;
    }

    public void makeApiCall(){
        Call<List<Post>> call = service.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                getMutablePostLiveData().postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.i("error",t.toString());
            }
        });
    }
}
