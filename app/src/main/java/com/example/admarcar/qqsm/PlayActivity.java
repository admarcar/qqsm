package com.example.admarcar.qqsm;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import extra.Question;

public class PlayActivity extends AppCompatActivity {

    private int question_number = 0;
    private int prizes[] = {100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 1000000};
    List<Question> questions;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        questions = generateQuestionList();
        fill_question();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.play_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.play_menu_call:
                Toast.makeText(this,getString(R.string.play_call_hint, question.getPhone()), Toast.LENGTH_LONG).show();
                change_button(question.getPhone());
                return true;
            case R.id.play_menu_fifty:
                disable_buttons(question.getFifty1(), question.getFifty2());
                return true;
            case R.id.play_menu_audience:
                Toast.makeText(this,getString(R.string.play_audience_hint, question.getAudience()), Toast.LENGTH_LONG).show();
                change_button(question.getAudience());
                return true;
        }
        return false;
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
            fill_question();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.play_losingtitle);
            int money = 0;
            if(question_number <= 10 && question_number > 4) money = prizes[4];
            else if(question_number <= 15 && question_number > 10) money = prizes[9];
            builder.setMessage(getString(R.string.play_losingmessage, Integer.toString(question_number+1), Integer.toString(money)));
            builder.setNegativeButton(R.string.play_losingback, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PlayActivity.this.finish();
                }
            });
            builder.setPositiveButton(R.string.play_losingplayagain, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    question_number = 0;
                    fill_question();
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
        button1.setBackgroundColor(0);
        TextView button2 = findViewById(R.id.play_option2);
        button2.setText(questions.get(question_number).getAnswer2());
        button2.setEnabled(true);
        button2.setBackgroundColor(0);
        TextView button3 = findViewById(R.id.play_option3);
        button3.setText(questions.get(question_number).getAnswer3());
        button3.setEnabled(true);
        button3.setBackgroundColor(0);
        TextView button4 = findViewById(R.id.play_option4);
        button4.setText(questions.get(question_number).getAnswer4());
        button4.setEnabled(true);
        button4.setBackgroundColor(0);
        question = questions.get(question_number);
    }

    public void change_button(String b){
        TextView button = null;
        switch (b){
            case "1": button = findViewById(R.id.play_option1); break;
            case "2": button = findViewById(R.id.play_option2); break;
            case "3": button = findViewById(R.id.play_option3); break;
            case "4": button = findViewById(R.id.play_option4); break;
        }
        button.setBackgroundColor(23);
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

    public List<Question> generateQuestionList() {
        List<Question> list = new ArrayList<Question>();
        Question q = null;

        q = new Question(
                "1",
                "Which is the Sunshine State of the US?",
                "North Carolina",
                "Florida",
                "Texas",
                "Arizona",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "2",
                "Which of these is not a U.S. state?",
                "New Hampshire",
                "Washington",
                "Wyoming",
                "Manitoba",
                "4",
                "4",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "3",
                "What is Book 3 in the Pokemon book series?",
                "Charizard",
                "Island of the Giant Pokemon",
                "Attack of the Prehistoric Pokemon",
                "I Choose You!",
                "3",
                "2",
                "3",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "4",
                "Who was forced to sign the Magna Carta?",
                "King John",
                "King Henry VIII",
                "King Richard the Lion-Hearted",
                "King George III",
                "1",
                "3",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "5",
                "Which ship was sunk in 1912 on its first voyage, although people said it would never sink?",
                "Monitor",
                "Royal Caribean",
                "Queen Elizabeth",
                "Titanic",
                "4",
                "4",
                "4",
                "1",
                "2"
        );
        list.add(q);

        q = new Question(
                "6",
                "Who was the third James Bond actor in the MGM films? (Do not include &apos;Casino Royale&apos;.)",
                "Roger Moore",
                "Pierce Brosnan",
                "Timothy Dalton",
                "Sean Connery",
                "1",
                "3",
                "3",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "7",
                "Which is the largest toothed whale?",
                "Humpback Whale",
                "Blue Whale",
                "Killer Whale",
                "Sperm Whale",
                "4",
                "2",
                "2",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "8",
                "In what year was George Washington born?",
                "1728",
                "1732",
                "1713",
                "1776",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "9",
                "Which of these rooms is in the second floor of the White House?",
                "Red Room",
                "China Room",
                "State Dining Room",
                "East Room",
                "2",
                "2",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "10",
                "Which Pope began his reign in 963?",
                "Innocent III",
                "Leo VIII",
                "Gregory VII",
                "Gregory I",
                "2",
                "1",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "11",
                "What is the second longest river in South America?",
                "Parana River",
                "Xingu River",
                "Amazon River",
                "Rio Orinoco",
                "1",
                "1",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "12",
                "What Ford replaced the Model T?",
                "Model U",
                "Model A",
                "Edsel",
                "Mustang",
                "2",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "13",
                "When was the first picture taken?",
                "1860",
                "1793",
                "1912",
                "1826",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "14",
                "Where were the first Winter Olympics held?",
                "St. Moritz, Switzerland",
                "Stockholm, Sweden",
                "Oslo, Norway",
                "Chamonix, France",
                "4",
                "1",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "15",
                "Which of these is not the name of a New York tunnel?",
                "Brooklyn-Battery",
                "Lincoln",
                "Queens Midtown",
                "Manhattan",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        return list;
    }
}
