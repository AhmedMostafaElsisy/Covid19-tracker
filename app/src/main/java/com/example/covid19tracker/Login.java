package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {
    TextInputEditText name, country;
    Button button;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.ET_Login_Name);
        country = findViewById(R.id.ET_Login_Country);
        button = findViewById(R.id.start_btn);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(country.getText().toString())) {
                    String userName= name.getText().toString();
                    String userCountry= country.getText().toString();
                    editor.putString("userName",userName);
                    editor.putString("userCountry",userCountry);
                    editor.apply();
                    sendToMain();
                } else {
                    name.setError("This filed is Empty");
                    country.setError("This filed is Empty");
                }

            }
        });
    }

    @Override
    protected void onStart() {
        if (sharedPreferences.getString("userName", null) != null && sharedPreferences.getString("userCountry", null) != null) {
            sendToMain();
        }
        super.onStart();


    }

    private void sendToMain() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}