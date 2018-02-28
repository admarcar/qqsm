package com.example.admarcar.qqsm;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import extra.Adaptador;
import extra.HighScore;
import extra.HighScoreList;
import extra.SQLhelper;

public class ScoresActivity extends AppCompatActivity {

    ArrayList<HighScore> LocalList;
    ArrayList<HighScore> FriendList;

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

        LocalList = new ArrayList<HighScore>();
        getLocalScores();
        LocalAdaptador = new Adaptador(this, R.layout.scores_list, LocalList);
        ListView listView = findViewById(R.id.score_listview_local);
        listView.setAdapter(LocalAdaptador);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ScoresActivity.this);
                alert.setMessage(getString(R.string.scores_local_one_remove,LocalList.get(position).getScoring(),LocalList.get(position).getName()));
                alert.setTitle(R.string.scores_local_one_remove_header);
                alert.setNegativeButton(R.string.scores_local_one_remove_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nada
                    }
                });
                alert.setPositiveButton(R.string.scores_local_one_remove_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = LocalList.get(position).getName();
                        final String score = LocalList.get(position).getScoring();
                        LocalList.remove(position);
                        LocalAdaptador.notifyDataSetChanged();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SQLhelper.getInstance(ScoresActivity.this).removeScore(name, score);
                            }
                        }).start();
                    }
                });
                alert.create().show();
                return true;
            }
        });

        FriendList = new ArrayList<HighScore>();
        getFriendScores();
        FriendsAdaptador = new Adaptador(this, R.layout.scores_list, FriendList);
        ListView FriendlistView = findViewById(R.id.score_listview_friends);
        FriendlistView.setAdapter(FriendsAdaptador);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scores_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
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

    public void getFriendScores() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ScoresActivity.this);
        String my_name = prefs.getString("username", "");
        new AsyncTaskGetFriendsScores().execute(my_name);
    }

    void getLocalScores(){
        new AsyncTaskGetLocalScores().execute();
    }

    private class AsyncTaskGetFriendsScores extends AsyncTask<String, Void, List<HighScore>> {
        @Override
        protected List<HighScore> doInBackground(String... strings) {
            String my_name = strings[0];
            ArrayList<HighScore> list = new ArrayList<HighScore>();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            builder.authority("wwtbamandroid.appspot.com");
            builder.appendPath("rest");
            builder.appendPath("highscores");
            builder.appendQueryParameter("name", my_name);
            if (my_name.equals("")) return list;
            try {
                URL url = new URL(builder.build().toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Gson gson = new Gson();
                    HighScoreList hs_list = gson.fromJson(input, HighScoreList.class);
                    list = (ArrayList<HighScore>) hs_list.getScores();
                    input.close();
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Collections.sort(list);
            return list;
        }

        @Override
        protected void onPostExecute(List<HighScore> highScores) {
            ProgressBar pb = findViewById(R.id.scores_progress_bar_friends);
            pb.setVisibility(View.GONE);
            super.onPostExecute(highScores);
            FriendList.addAll(highScores);
            FriendsAdaptador.notifyDataSetChanged();
        }
    }

    private class AsyncTaskGetLocalScores extends AsyncTask<Void, Void, List<HighScore>>{

        @Override
        protected List<HighScore> doInBackground(Void... voids) {
            List<HighScore> list = SQLhelper.getInstance(ScoresActivity.this).getScores();
            Collections.sort(list);
            return list;
        }

        protected void onPostExecute(List<HighScore> highScores) {
            ProgressBar pb = findViewById(R.id.scores_progress_bar_local);
            pb.setVisibility(View.GONE);
            super.onPostExecute(highScores);
            LocalList.addAll(highScores);
            LocalAdaptador.notifyDataSetChanged();
        }
    }
}
