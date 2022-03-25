package arpit.parekh.mvvmmodule.mvvm_retrofit.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Post{

	@SerializedName("post")
	private List<PostItem> post;

	public void setPost(List<PostItem> post){
		this.post = post;
	}

	public List<PostItem> getPost(){
		return post;
	}

	@Override
 	public String toString(){
		return 
			"Post{" + 
			"post = '" + post + '\'' + 
			"}";
		}
}