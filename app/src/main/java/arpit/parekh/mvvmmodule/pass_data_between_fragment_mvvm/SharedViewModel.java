package arpit.parekh.mvvmmodule.pass_data_between_fragment_mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    // pass data between fragments
    MutableLiveData<String> data = new MutableLiveData<>();

    public void setData(String input){
        data.setValue(input);
    }
    public MutableLiveData<String> getData(){
        return data;
    }
}
