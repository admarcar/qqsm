package extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.admarcar.qqsm.R;

import java.util.ArrayList;

/**
 * Created by Adrian on 18/02/2018.
 */

public class Adaptador extends ArrayAdapter<HighScore> {

    Context context;
    int resource;
    ArrayList<HighScore> data;

    public Adaptador(Context context, int resource, ArrayList<HighScore> data){
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    public View getView(final int posicion, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.scores_list, null);
        }
        HighScore d = data.get(posicion);
        TextView tv1 = (TextView) view.findViewById(R.id.textName);
        tv1.setText(d.getName());
        TextView tv2 = (TextView) view.findViewById(R.id.textScore);
        tv2.setText(d.getScoring() + " " + context.getResources().getString(R.string.currency));
        return view;
    }
}
