package arpit.parekh.mvvmmodule.mvvm_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

import arpit.parekh.mvvmmodule.databinding.ActivityMyRetrofitBinding;
import arpit.parekh.mvvmmodule.mvvm_retrofit.data.Post;
import arpit.parekh.mvvmmodule.mvvm_retrofit.data.PostItem;

public class MyRetrofitActivity extends AppCompatActivity {
    private ActivityMyRetrofitBinding binding;
    private PostViewModel model;

    private List<PostItem> postItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyRetrofitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new ViewModelProvider(this).get(PostViewModel.class);

        model.makeApiCall();

        model.getMutablePostLiveData().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {

                for(int i=0;i<posts.size();i++){

                    Post post = posts.get(i);

                    postItems = post.getPost();

                    ArrayAdapter<PostItem> adapter = new ArrayAdapter<>(MyRetrofitActivity.this, android.R.layout.simple_list_item_1,postItems);

                    binding.listViewRetrofit.setAdapter(adapter);


                }

            }
        });

    }
}