package com.example.hello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class Record extends AppCompatActivity {

    private CardView cardView,cardView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
    }
    public void Backtome(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Me.class);
        startActivity(intent);
    }
}
