package com.example.hello.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hello.Activity.Reservation;
import com.example.hello.R;

import java.util.ArrayList;
import java.util.List;

import util.SqlHelper;
import util.Stadium;
import Adapter.bookAdapter;
import util.State;

public class Book extends Fragment {

    private Button btn;
    public Book(){}
    public static Book newInstance(){
        Book book = new  Book();
        return book;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton Scan = getActivity().findViewById(R.id.Scan);
        Scan.bringToFront();

        ListView listView = (ListView) getActivity().findViewById(R.id.bookList);
        bookAdapter adapter = new bookAdapter(getActivity(),
                R.layout.book_item,
                State.getState().getStadiums());
        listView.setAdapter(adapter);
        //ListView item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                State.getState().setNowStadium((Stadium)adapterView.getItemAtPosition(i));
                Intent intent = new Intent(getActivity(), Reservation.class);
                startActivity(intent);
            }
        });
    }
}
