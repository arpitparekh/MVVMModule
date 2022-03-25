package arpit.parekh.mvvmmodule.mvvm_firebase_authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepo repo;
    private MutableLiveData<FirebaseUser> userData;

    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repo = new AuthRepo(application);
        userData = repo.getFirebaseUserMutableLiveData();
    }

    public void register(String userName,String password){
        repo.register(userName,password);
    }
    public void login(String email,String password){
        repo.login(email, password);
    }
    public void logOut(){
        repo.logout();
    }
}
