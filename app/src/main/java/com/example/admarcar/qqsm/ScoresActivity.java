package com.example.admarcar.qqsm;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

import extra.Adaptador;
import extra.People_score;
import extra.SQLhelper;

public class ScoresActivity extends AppCompatActivity {

    ArrayList<People_score> LocalList;
    ArrayList<People_score> FriendList;

    Adaptador LocalAdaptador;
    Adaptador FriendsAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        TabHost host = (TabHost) findViewById(R.id.myTabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("TAB1");
        spec.setIndicator(getResources().getString(R.string.scores_local));
        spec.setContent(R.id.tab1);
        host.addTab(spec);

        spec = host.newTabSpec("TAB2");
        spec.setIndicator(getResources().getString(R.string.scores_friends));
        spec.setContent(R.id.tab2);
        host.addTab(spec);

        //LocalList = getLocalScores();
        LocalList = SQLhelper.getInstance(this).getScores();
        LocalAdaptador = new Adaptador(this, R.layout.scores_list, LocalList);
        ListView listView = findViewById(R.id.score_listview_local);
        listView.setAdapter(LocalAdaptador);

        FriendList = getFriendScores();
        FriendsAdaptador = new Adaptador(this, R.layout.scores_list, FriendList);
        ListView FriendlistView = findViewById(R.id.score_listview_friends);
        FriendlistView.setAdapter(FriendsAdaptador);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.scores_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(final MenuItem item){
        switch (item.getItemId()) {
            case R.id.score_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ScoresActivity.this);
                builder.setMessage(R.string.scores_message);
                builder.setPositiveButton(R.string.scores_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocalList.clear();
                        SQLhelper.getInstance(ScoresActivity.this).removeScores();
                        LocalAdaptador.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton(R.string.scores_no, null);
                builder.create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<People_score> getLocalScores(){
        ArrayList<People_score> list = new ArrayList<People_score>();
        list.add(new People_score("Adri","16000"));
        list.add(new People_score("Adri 2","200"));

        return list;
    }

    public ArrayList<People_score> getFriendScores(){
        ArrayList<People_score> list = new ArrayList<People_score>();
        list.add(new People_score("????","????"));
        list.add(new People_score("????","????"));

        return list;
    }
}
