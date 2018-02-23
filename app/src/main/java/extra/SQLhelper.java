package extra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by admarcar on 19/02/2018.
 */

public class SQLhelper extends SQLiteOpenHelper{

    private static SQLhelper helper;

    public static SQLhelper getInstance(Context context){
        if(helper == null){
            helper = new SQLhelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE score_table (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, score INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private SQLhelper(Context context){
        super(context, "qqsm_database", null, 1);
    }

    public void putScore(String username, int score){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("name", username);
        content.put("score", score);
        db.insert("score_table", null, content);
        db.close();
    }

    public void removeScores(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("score_table", null, null);
        db.close();
    }

    public ArrayList<People_score> getScores(){
        ArrayList<People_score> list = new ArrayList<People_score>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT name, score FROM score_table", null);

        while(cursor1.moveToNext()){
            People_score ps = new People_score(cursor1.getString(0), cursor1.getString(1));
            list.add(ps);
        }
        cursor1.close();
        db.close();
        Collections.sort(list, new Comparator<People_score>() {
            @Override
            public int compare(People_score o1, People_score o2) {
                return Integer.parseInt(o2.getScore()) - Integer.parseInt(o1.getScore());
            }
        });
        return list;
    }
}
