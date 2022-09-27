package com.example.admin_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.admin_app.databinding.ActivityAdminSignUpBinding;
import com.example.admin_app.databinding.ActivityMainBinding;
import com.example.admin_app.utilities.Constants;
import com.example.admin_app.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.zip.Inflater;

public class AdminSignUp extends AppCompatActivity {
    private ActivityAdminSignUpBinding binding;
    private PreferenceManager preferenceManager;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        fAuth = FirebaseAuth.getInstance();
        setListeners();
    }

    private void setListeners() {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails()){
                fireStoreSignUp();
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void fireStoreSignUp(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> admin = new HashMap<>();
        admin.put(Constants.KEY_NAME, binding.inputName.getText().toString());
        admin.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        admin.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        database.collection(Constants.KEY_COLLECTION_ADMINS)
                .add(admin)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), AdminSignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });
    }

    private Boolean isValidSignUpDetails(){
        if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Enter Name");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Enter Email");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Enter Valid Image");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm Your Password");
            return false;
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Passwords Must Be The Same");
            return false;
        } else {
            return true;
        }

    }

    private void loading(Boolean isLoading){
        if (isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }
}