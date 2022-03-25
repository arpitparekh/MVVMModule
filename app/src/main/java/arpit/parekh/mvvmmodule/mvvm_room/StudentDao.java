package arpit.parekh.mvvmmodule.mvvm_room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {     // mvvm is the best

    // return int if u want id of data that u inserted , updated or deleted

    @Insert(onConflict = OnConflictStrategy.REPLACE)   // replace existing data if key is same
    void insertStudent(Student student);

    @Update
    void updateStudent(Student student);

    @Delete
    void deleteStudent(Student student);

    @Delete
    void deleteListOfStudent(List<Student> studentList);  // delete the whole list

    @Update
    void updateListOfStudent(List<Student> studentList);  // update the whole list

    @Insert
    void insertListOfStudent(List<Student> studentList);  // insert the whole list

    @Query("SELECT * FROM  studentData")
    LiveData<List<Student>> getStudentListLive();   // use this for ViewModel method

    @Query("SELECT * FROM  studentData")
    List<Student> getStudentListFuture();    // use this for in callback method

    @Query("SELECT * FROM studentData WHERE id=:id")   // get single student data
    Student getParticularStudent(int id);



}
