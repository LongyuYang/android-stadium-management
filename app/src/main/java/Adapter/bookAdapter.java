package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hello.R;

import java.util.List;

import util.Stadium;


public class bookAdapter extends ArrayAdapter {

    public bookAdapter(Context context, int resource, List<Stadium> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Stadium stadium = (Stadium) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.book_item, null);
        TextView name = (TextView)view.findViewById(R.id.gymname);
        TextView start = (TextView)view.findViewById(R.id.startTime);
        TextView end = (TextView)view.findViewById(R.id.endTime);
        TextView address = (TextView)view.findViewById(R.id.address);
        name.setText(stadium.StadiumName);
        start.setText(stadium.startTime.substring(0, 5));
        end.setText(stadium.endTime.substring(0, 5));
        address.setText(stadium.address);
        return view;
    }
}
