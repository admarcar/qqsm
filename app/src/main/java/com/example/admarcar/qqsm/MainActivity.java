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
        switch (view.getId()){
            case R.id.main_play_button:
                intent = new Intent(this,PlayActivity.class);
                break;
            case R.id.main_score_button:
                intent = new Intent(this,ScoresActivity.class);
                break;
            case R.id.main_settings_button:
                intent = new Intent(this,SettingsActivity.class);
                break;
            case R.id.main_credits_button:
                intent = new Intent(this,CreditsActivity.class);
                break;
        }

        startActivity(intent);
    }
}
