package arpit.parekh.mvvmmodule.mvvm_room;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StudentRepo {

    // handles data operations and business logic

    // we can decide from which source  we will fetch data like , local storage , web services , APIs etc..

    // this class separate out data source from rest of the application

    // lets bring everything here ...

    private StudentDatabase database;


    private Executor executor= Executors.newSingleThreadExecutor();  // returns single thread

    public StudentRepo(Context context){

        database = StudentDatabase.getDatabase(context);
    }

    public void insertData(Student student){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.getDao().insertStudent(student);     // run query on separate thread
            }
        });

    }

    public void updateData(Student student){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.getDao().updateStudent(student);     // run query on separate thread
            }
        });

    }

    public void deleteData(Student student){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.getDao().deleteStudent(student);     // run query on separate thread
            }
        });

    }

    public List<Student> getAllStudentsFuture() throws Exception {      // callback


        Callable<List<Student>> callable = new Callable<List<Student>>() {
            @Override
            public List<Student> call() throws Exception {

                return database.getDao().getStudentListFuture();

            }
        };

        Future<List<Student>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }


    public LiveData<List<Student>> getAllStudentsLive(){    // view model

                return database.getDao().getStudentListLive();

    }





}
