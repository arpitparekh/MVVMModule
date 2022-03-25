package arpit.parekh.mvvmmodule.collapsingToolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import arpit.parekh.mvvmmodule.databinding.ActivityToolBinding;

public class ToolActivity extends AppCompatActivity {
    private ActivityToolBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityToolBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

    }
}