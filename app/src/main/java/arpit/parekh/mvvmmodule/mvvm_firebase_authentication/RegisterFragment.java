package arpit.parekh.mvvmmodule.mvvm_firebase_authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import arpit.parekh.mvvmmodule.R;
import arpit.parekh.mvvmmodule.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthViewModel model;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentRegisterBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnRegister.setOnClickListener(view1 -> {

            String userName = binding.edtNameFirebase.getText().toString();
            String password = binding.edtPasswordFirebase.getText().toString();

            if(!userName.isEmpty() && !password.isEmpty()){
                model.register(userName,password);
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }

        });
    }
}