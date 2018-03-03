package com.example.admarcar.qqsm;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import extra.Question;
import extra.SQLhelper;

public class PlayActivity extends AppCompatActivity {

    private int question_number;
    private int available_hints;
    private int prizes[] = {100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 1000000};
    List<Question> questions;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        question_number = prefs.getInt("question_number", 0);
        if(question_number == 0){//Partida nueva y se ha reducido el numero de ayudas disponible
            available_hints = prefs.getInt("hints_quantity_pos", 3);
        }
        else{//partida empezada, el numero de ayudas se reducira en la proxima
            available_hints = prefs.getInt("hints_quantity_remaining", 3);
        }
        questions = readQuestionList();
        fill_question();
    }

    protected void onPause(){
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("question_number", question_number);
        editor.putInt("hints_quantity_remaining",available_hints);
        editor.apply();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.play_menu, menu);
        if(available_hints == 0){
            MenuItem it;
            it = menu.findItem(R.id.play_menu_call);
            it.setEnabled(false);
            it = menu.findItem(R.id.play_menu_fifty);
            it.setEnabled(false);
            it = menu.findItem(R.id.play_menu_audience);
            it.setEnabled(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        boolean action = false;
        switch (item.getItemId()){
            case R.id.play_menu_call:
                Toast.makeText(this,getString(R.string.play_call_hint, question.getPhone()), Toast.LENGTH_LONG).show();
                change_button(question.getPhone());
                available_hints--;
                action = true;
                break;
            case R.id.play_menu_fifty:
                disable_buttons(question.getFifty1(), question.getFifty2());
                available_hints--;
                action = true;
                break;
            case R.id.play_menu_audience:
                Toast.makeText(this,getString(R.string.play_audience_hint, question.getAudience()), Toast.LENGTH_LONG).show();
                change_button(question.getAudience());
                available_hints--;
                action = true;
                break;
            case R.id.play_menu_surrender:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.play_surrender_title);
                final int money = get_earn_money();
                builder.setMessage(getString(R.string.play_surrender_message,Integer.toString(money)));
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        put_score(money);
                        new_game();
                        PlayActivity.this.finish();
                    }
                });
                builder.create().show();
                break;
        }
        if(action) {
            TextView hints = findViewById(R.id.play_hints);
            hints.setText(Integer.toString(available_hints));
        }
        if(action && available_hints == 0){
            invalidateOptionsMenu();
        }
        return action;
    }

    public void play_callback(View view){
        int id = view.getId();
        boolean right = false;
        switch(id){
            case R.id.play_option1:
                if(question.getRight().equals("1")){
                    right = true;
                }
                break;
            case R.id.play_option2:
                if(question.getRight().equals("2")){
                    right = true;
                }
                break;
            case R.id.play_option3:
                if(question.getRight().equals("3")){
                    right = true;
                }
                break;
            case R.id.play_option4:
                if(question.getRight().equals("4")){
                    right = true;
                }
                break;
        }
        if(right) {
            question_number++;
            if(question_number == 15){//No more questions
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.play_winningtitle);
                int money = prizes[14];
                put_score(money);
                builder.setMessage(R.string.play_winningmessage);
                builder.setNegativeButton(R.string.play_winningback, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new_game();
                        PlayActivity.this.finish();
                    }
                });
                builder.setPositiveButton(R.string.play_winningplayagain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new_game();
                        fill_question();
                        invalidateOptionsMenu();
                    }
                });
                builder.create().show();
            }
            else fill_question();
        }
        else{//Fallo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.play_losingtitle);
            int money = get_earn_money();
            put_score(money);
            builder.setMessage(getString(R.string.play_losingmessage, Integer.toString(question_number+1), Integer.toString(money)));
            builder.setNegativeButton(R.string.play_losingback, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new_game();
                    PlayActivity.this.finish();
                }
            });
            builder.setPositiveButton(R.string.play_losingplayagain, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new_game();
                    fill_question();
                    invalidateOptionsMenu();
                }
            });
            builder.create().show();
        }
    }

    public void fill_question(){
        TextView tv_prize = findViewById(R.id.play_money);
        tv_prize.setText(prizes[question_number] + "$");
        TextView tv_question = findViewById(R.id.play_questionnumber);
        tv_question.setText((question_number+1)+"");
        TextView tv_question_title = findViewById(R.id.play_question_title);
        tv_question_title.setText(questions.get(question_number).getText());
        TextView button1 = findViewById(R.id.play_option1);
        button1.setText(questions.get(question_number).getAnswer1());
        button1.setEnabled(true);
        //button1.setBackgroundColor(0);
        TextView button2 = findViewById(R.id.play_option2);
        button2.setText(questions.get(question_number).getAnswer2());
        button2.setEnabled(true);
        //button2.setBackgroundColor(0);
        TextView button3 = findViewById(R.id.play_option3);
        button3.setText(questions.get(question_number).getAnswer3());
        button3.setEnabled(true);
        //button3.setBackgroundColor(0);
        TextView button4 = findViewById(R.id.play_option4);
        button4.setText(questions.get(question_number).getAnswer4());
        button4.setEnabled(true);
        //button4.setBackgroundColor(0);
        question = questions.get(question_number);
        TextView hints = findViewById(R.id.play_hints);
        hints.setText(Integer.toString(available_hints));
    }

    public void change_button(String b){
        TextView button = null;
        switch (b){
            case "1": button = findViewById(R.id.play_option1); break;
            case "2": button = findViewById(R.id.play_option2); break;
            case "3": button = findViewById(R.id.play_option3); break;
            case "4": button = findViewById(R.id.play_option4); break;
        }
    }

    public void disable_buttons(String b1, String b2){
        TextView button1 = null;
        switch (b1){
            case "1": button1 = findViewById(R.id.play_option1); break;
            case "2": button1 = findViewById(R.id.play_option2); break;
            case "3": button1 = findViewById(R.id.play_option3); break;
            case "4": button1 = findViewById(R.id.play_option4); break;
        }
        TextView button2 = null;
        switch (b2){
            case "1": button2 = findViewById(R.id.play_option1); break;
            case "2": button2 = findViewById(R.id.play_option2); break;
            case "3": button2 = findViewById(R.id.play_option3); break;
            case "4": button2 = findViewById(R.id.play_option4); break;
        }
        button1.setEnabled(false);
        button2.setEnabled(false);
    }

    public List<Question> readQuestionList(){
        Log.d("hola", "adios");
        int question_number = 1;
        List<Question> list = new ArrayList<Question>();
        Question q;
        XmlPullParser parser;
        try{
            parser = getResources().getXml(R.xml.questions);
            int event = parser.getEventType();
            while(XmlPullParser.END_DOCUMENT != event){
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(!"question".equals(parser.getName())) break;
                        String answer1 = parser.getAttributeValue(0);
                        String answer2 = parser.getAttributeValue(1);
                        String answer3 = parser.getAttributeValue(2);
                        String answer4 = parser.getAttributeValue(3);
                        String audience = parser.getAttributeValue(4);
                        String fifty1 = parser.getAttributeValue(5);
                        String fifty2 = parser.getAttributeValue(6);
                        String number = parser.getAttributeValue(7);
                        String phone = parser.getAttributeValue(8);
                        String right = parser.getAttributeValue(9);
                        String text = parser.getAttributeValue(10);
                        q = new Question(Integer.toString(question_number++), text, answer1, answer2, answer3, answer4, right,
                                audience, phone, fifty1, fifty2);
                        list.add(q);

                        break;
                    case XmlPullParser.TEXT:
                        break;
                }
                parser.next();
                event = parser.getEventType();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    void put_score(final int money){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String my_name = prefs.getString("username","");
        //Lo almacenamos en la BD
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLhelper.getInstance(PlayActivity.this).putScore(my_name,money);
            }
        }).start();
        //Lo subimos al servidor
        final Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("wwtbamandroid.appspot.com");
        builder.appendPath("rest");
        builder.appendPath("highscores");
        if(my_name.equals("")) return;
        final String body = "name="+my_name + "&" + "score="+money;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(builder.build().toString());
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("PUT");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
                    output.write(body);
                    output.flush();
                    output.close();

                    BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    input.close();

                    connection.disconnect();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    int get_earn_money(){
        int money = 0;
        if(question_number <= 9 && question_number > 4) money = prizes[4];
        else if(question_number <= 15 && question_number > 9) money = prizes[9];
        return money;
    }

    void new_game(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        question_number = 0;
        available_hints = prefs.getInt("hints_quantity_pos", 3);
    }
}
