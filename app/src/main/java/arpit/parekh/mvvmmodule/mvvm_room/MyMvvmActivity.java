package arpit.parekh.mvvmmodule.mvvm_room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import arpit.parekh.mvvmmodule.R;
import arpit.parekh.mvvmmodule.databinding.ActivityMyMvvmBinding;

public class MyMvvmActivity extends AppCompatActivity {
    private ActivityMyMvvmBinding binding;
    private List<Student> studentList;
    private StudentViewModel model;
    ArrayAdapter<Student> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyMvvmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(StudentViewModel.class);

        studentList = new ArrayList<>();

        // error == Cannot access database on the main thread since it may potentially lock the UI for a long period of time.
        // we cannot access database on main thread

        // to solve this we have 3 methods
        // 1 ->  add   .allowMainThreadQueries()
        // 2 ->  use liveData which automatically run query on thread
        // 3 ->  make future Callable in repo class


//        try {                                                    // for callback
//            studentList=model.getAllStudent();
//        }catch (Exception e){
//            Log.i("error",e.toString());
//        }

        model.getAllStudentLive().observe(MyMvvmActivity.this, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> studentList) {
                if(studentList!=null){

                    adapter = new ArrayAdapter<>(MyMvvmActivity.this, android.R.layout.simple_list_item_1,studentList);
                    binding.listView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_student,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.addStudent){

            startActivity(new Intent(this,AddStudentActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}