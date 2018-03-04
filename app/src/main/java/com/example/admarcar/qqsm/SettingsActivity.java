package com.example.admarcar.qqsm;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import extra.InternetAvailable;

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

    public void add_friend(View view){
        if(!InternetAvailable.isNetworkAvailable(this)){
            Toast.makeText(this, R.string.settings_no_internet_add_friend, Toast.LENGTH_LONG).show();
        }
        else {
            EditText edit_friend = findViewById(R.id.settings_friend_name);
            EditText edit_name = findViewById(R.id.settings_name_edit_text);
            String friend_name = edit_friend.getText().toString();
            String my_name = edit_name.getText().toString();
            if(my_name.equals("")){
                Toast.makeText(this, R.string.settings_no_my_name, Toast.LENGTH_LONG).show();
            }
            if(friend_name.equals("")){
                Toast.makeText(this, R.string.settings_no_friends_name, Toast.LENGTH_LONG).show();
            }
            else{
                final Uri.Builder builder = new Uri.Builder();
                builder.scheme("https");
                builder.authority(getString(R.string.api_url));
                builder.appendPath("rest");
                builder.appendPath("friends");
                final String body = "name="+my_name + "&" + "friend_name="+friend_name;
                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Toast.makeText(SettingsActivity.this, R.string.settings_add_friend_no_server, Toast.LENGTH_LONG).show();
                    }
                };

                final Handler correct = new Handler() {
                    public void handleMessage(Message msg){
                        if(msg.what == 0){
                            Toast.makeText(SettingsActivity.this, R.string.settings_add_friend_correct, Toast.LENGTH_LONG).show();
                        }
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    try{
                        URL url = new URL(builder.build().toString());
                        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
                        output.write(body);
                        output.flush();
                        output.close();

                        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        input.close();

                        connection.disconnect();
                        correct.sendEmptyMessage(0);
                    }
                    catch(Exception e){
                        handler.sendEmptyMessage(0);
                    }
                    }
                }).start();
            }
        }
    }
}
