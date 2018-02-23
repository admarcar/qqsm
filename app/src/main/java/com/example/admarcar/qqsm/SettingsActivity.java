package com.example.admarcar.qqsm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        EditText edit_name = findViewById(R.id.settings_name_edit_text);
        Spinner spinner = findViewById(R.id.settings_hints_spinner);
        edit_name.setText(prefs.getString("username", ""));
        spinner.setSelection(prefs.getInt("hints_quantity_pos", 3));
    }

    public void onPause(){
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        EditText edit_name = findViewById(R.id.settings_name_edit_text);
        editor.putString("username", edit_name.getText().toString());
        Spinner spinner = findViewById(R.id.settings_hints_spinner);
        editor.putInt("hints_quantity_pos", spinner.getSelectedItemPosition());
        editor.apply();
    }
}
