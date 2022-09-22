package com.example.chatapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.LocaleHelper;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    FirebaseAuth auth;
    Context context;
    Resources resources;
    int langSelected;
    String[] Language;
    TextView welcome, loginToContinue, createNewAccountText;
    Button show_lang_dialog, signInBtn, selectLanguageBtn;
    EditText inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

        show_lang_dialog = binding.showLangDialog;
        welcome = binding.welcomeText;
        loginToContinue = binding.logInToContinueText;
        inputEmail = binding.inputEmail;
        inputPassword = binding.inputPassword;
        signInBtn = binding.buttonSignIn;
        createNewAccountText = binding.textCreateNewAccount;
        langSelected = 0;

        //Change Language Button
        show_lang_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Language = new String[] {"English","Español"};
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignInActivity.this);
                dialogBuilder.setTitle("Select A Language");
                dialogBuilder.setIcon(R.drawable.lang_icon);
                dialogBuilder.setSingleChoiceItems(Language, langSelected,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                context = LocaleHelper.setLocale(SignInActivity.this, "en");
                                resources = context.getResources();
                                welcome.setText(resources.getString(R.string.welcome));
                                loginToContinue.setText(resources.getString(R.string.login_to_continue));
                                inputEmail.setHint(resources.getString(R.string.email));
                                inputPassword.setHint(resources.getString(R.string.password));
                                signInBtn.setText(resources.getString(R.string.sign_in));
                                createNewAccountText.setText(resources.getString(R.string.create_new_account));
                                langSelected = 0;
                                break;
                            case 1:
                                context = LocaleHelper.setLocale(SignInActivity.this, "es");
                                resources = context.getResources();
                                welcome.setText(resources.getString(R.string.welcome));
                                loginToContinue.setText(resources.getString(R.string.login_to_continue));
                                inputEmail.setHint(resources.getString(R.string.email));
                                inputPassword.setHint(resources.getString(R.string.password));
                                signInBtn.setText(resources.getString(R.string.sign_in));
                                createNewAccountText.setText(resources.getString(R.string.create_new_account));
                                langSelected = 1;
                                break;
                        }
                    }
                });
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


                dialogBuilder.show();
            }

        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }


    private void setListeners(){
        //Create New Account Button
        binding.textCreateNewAccount.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.buttonSignIn.setOnClickListener(view -> {
            if(isValidSignDetails()) {
                signInWithFirebase();
            }
        });
    }

    private void signInWithFirebase(){
        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        showToast("Please Verify Your Email Address");
                    }
                } else {
                    showToast(task.getException().getMessage());
                }
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignDetails(){
        if (binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Enter Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Enter Valid Email");
            return false;
        } else if (binding.inputPassword.getText().toString().isEmpty()){
            showToast("Enter Password");
            return false;
        } else {
            return true;
        }

    }


}