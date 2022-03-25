package arpit.parekh.mvvmmodule.mvvm_retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceInstance {

    public static String BASE_URL = "https://jsonplaceholder.typicode.com/";

    public static ApiService service;

    public static ApiService getService(){

        if(service==null){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(ApiService.class);
        }
        return service;
    }

}
