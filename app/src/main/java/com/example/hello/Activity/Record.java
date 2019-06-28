package com.example.hello.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ListView;

import com.example.hello.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.recordAdapter;
import util.Appoint;
import util.SqlHelper;
import util.State;

public class Record extends AppCompatActivity {

    private CardView cardView,cardView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        List<Appoint> Appoints = SqlHelper.getInstance().getAppointsByUser(
                State.getState().getUser().userId,
                false
        );
        ListView listView = findViewById(R.id.recordList);
        recordAdapter adapter = new recordAdapter(Record.this, R.layout.record_item, Appoints);
        listView.setAdapter(adapter);
    }
    public void Backtome(View view) {
        Intent intent = new Intent(this, First.class);
        startActivity(intent);
    }
}
