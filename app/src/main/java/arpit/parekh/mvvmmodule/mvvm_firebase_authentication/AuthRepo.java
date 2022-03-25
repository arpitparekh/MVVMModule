package arpit.parekh.mvvmmodule.mvvm_firebase_authentication;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepo {

    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private FirebaseAuth auth;

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }
    public AuthRepo(Application application){
        this.application=application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null){
            firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
        }
    }
    public void register(String userName , String password){

        auth.createUserWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(application, "Register SuccessFul", Toast.LENGTH_SHORT).show();
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
                }else{

                    Log.i("error",task.getException().getMessage());
                }
            }
        });
    }
    public void login(String userName,String password){

        auth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
                    Toast.makeText(application, "Login SuccessFul", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void logout(){
        auth.signOut();

    }

}
