package arpit.parekh.mvvmmodule.mvvm_room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import arpit.parekh.mvvmmodule.databinding.ActivityAddStudentBinding;

public class AddStudentActivity extends AppCompatActivity {
    private ActivityAddStudentBinding binding;
    private StudentViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(StudentViewModel.class);

        binding.btnAddStudents.setOnClickListener(view -> {

            Student s = new Student();
            s.name = binding.edtName.getText().toString();
            s.email= binding.edtEmail.getText().toString();
            s.address = binding.edtAddress.getText().toString();

            model.insertStudent(s);

            startActivity(new Intent(this,MyMvvmActivity.class));


        });


    }
}