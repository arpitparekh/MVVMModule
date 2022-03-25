package arpit.parekh.mvvmmodule.pass_data_between_fragment_mvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import arpit.parekh.mvvmmodule.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.linear_home,new FirstFragment())
                .add(R.id.linear_home,new SecondFragment())
                .commit();

    }

}