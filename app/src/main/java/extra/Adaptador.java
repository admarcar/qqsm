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

public class Adaptador extends ArrayAdapter<People_score> {

    Context context;
    int resource;
    ArrayList<People_score> data;

    public Adaptador(Context context, int resource, ArrayList<People_score> data){
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
        People_score d = data.get(posicion);
        TextView tv1 = (TextView) view.findViewById(R.id.textName);
        tv1.setText(d.getName());
        TextView tv2 = (TextView) view.findViewById(R.id.textScore);
        tv2.setText(d.getScore());
        return view;
    }
}
