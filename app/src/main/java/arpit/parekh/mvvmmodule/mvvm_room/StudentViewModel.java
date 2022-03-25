package arpit.parekh.mvvmmodule.mvvm_room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StudentViewModel extends AndroidViewModel {

    private StudentRepo repo;


    public StudentViewModel(@NonNull Application application) {
        super(application);

        repo = new StudentRepo(application);
    }

    // link the view with model

    // view model is responsible to update the view through  liveData , dataBinding or RXJava

    // also holds the last updated snapshot of data to update view

    //use Android View model for context

    //use ViewModel for without context

    public void insertStudent(Student student){
        repo.insertData(student);
    }

    public void updateStudent(Student student){
        repo.insertData(student);
    }

    public void deleteStudent(Student student){
        repo.insertData(student);
    }

    // u can either use callback or viewModel

    public List<Student> getAllStudentFuture() throws Exception{

        return repo.getAllStudentsFuture();
    }

    public LiveData<List<Student>> getAllStudentLive(){

        return repo.getAllStudentsLive();
    }





}
