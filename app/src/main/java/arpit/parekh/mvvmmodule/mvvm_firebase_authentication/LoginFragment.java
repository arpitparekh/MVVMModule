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
import arpit.parekh.mvvmmodule.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel model;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.tvNewUser.setOnClickListener(view1 -> {

            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registerFragment);

        });

        model.getUserData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    Toast.makeText(getContext(),"Welcome",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_dashboardFragment);
                }
            }
        });

        binding.btnLogin.setOnClickListener(view1 -> {
            String userName = binding.edtNameFirebase.getText().toString();
            String password = binding.edtPasswordFirebase.getText().toString();

            if(!userName.isEmpty() && !password.isEmpty()){

                model.login(userName,password);
            }

        });
    }
}