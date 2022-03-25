package arpit.parekh.mvvmmodule.mvvm_firebase_authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import arpit.parekh.mvvmmodule.databinding.ActivityMyFirebaseBinding;

public class MyFirebaseActivity extends AppCompatActivity {
    private ActivityMyFirebaseBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyFirebaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}