package com.example.admarcar.qqsm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void callback_main(View view){
        Intent intent = null;
        intent = new Intent(this,PlayActivity.class);
        startActivity(intent);
    }
}
