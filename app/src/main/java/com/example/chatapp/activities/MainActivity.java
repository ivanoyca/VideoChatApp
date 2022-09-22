package com.example.chatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.LocaleHelper;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Context context;
    Resources resources;
    TextView helloWorldText, helloWorldTextTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        helloWorldText = binding.helloWorldText;
        helloWorldTextTwo = binding.helloWorldTextTwo;

        if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("es")){
            context = LocaleHelper.setLocale(MainActivity.this,"es");
            resources = context.getResources();

            helloWorldText.setText(resources.getString(R.string.hello_world));
            helloWorldTextTwo.setText(resources.getString(R.string.hello_world));

        } else if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("en")){
            context = LocaleHelper.setLocale(MainActivity.this,"en");
            resources =context.getResources();
            helloWorldText.setText(resources.getString(R.string.hello_world));
            helloWorldTextTwo.setText(resources.getString(R.string.hello_world));
        }


    }
}