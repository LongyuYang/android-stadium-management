package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hello.R;

import java.util.List;

import util.Appoint;
import util.Utility;


public class recordAdapter extends ArrayAdapter {

    public recordAdapter(Context context, int resource, List<Appoint> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Appoint appoint = (Appoint) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.record_item, null);

        TextView stadiumName = view.findViewById(R.id.recordStadium);
        TextView date = view.findViewById(R.id.recordDate);
        TextView start = view.findViewById(R.id.recordStart);
        TextView end = view.findViewById(R.id.recordEnd);
        TextView address = view.findViewById(R.id.recordAdd);
        TextView submit = view.findViewById(R.id.submitTime);
        TextView mode = view.findViewById(R.id.recordMode);

        stadiumName.setText(appoint.stadiumName);
        date.setText(appoint.bookDate);
        start.setText(appoint.startTime.substring(0, 5));
        end.setText(appoint.endTime.substring(0, 5));
        address.setText(appoint.stadiumAdd);
        submit.setText(appoint.submitDate);

        if (appoint.mode >= 2 && appoint.mode <= 3){
            mode.setTextColor(Color.GREEN);
        }
        else if (appoint.mode == 4){
            mode.setTextColor(Color.RED);
        }
        mode.setText(Utility.getBookMode(appoint.mode));
        return view;
    }
}