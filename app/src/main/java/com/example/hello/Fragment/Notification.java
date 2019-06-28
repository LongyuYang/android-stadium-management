package com.example.hello.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.hello.R;
import java.util.List;
import Adapter.notifAdapter;
import util.Appoint;
import util.SqlHelper;
import util.State;

public class Notification extends Fragment {

    public Notification(){}

    public static Notification newInstance(){
        Notification notification = new Notification();
        return notification;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton Scan = getActivity().findViewById(R.id.Scan);
        Scan.bringToFront();
        List<Appoint> Appoints = SqlHelper.getInstance().getAppointsByUser(
                State.getState().getUser().userId,
                true
        );
        ListView listView = getActivity().findViewById(R.id.notifList);
        notifAdapter adapter = new notifAdapter(getActivity(), R.layout.notif_item, Appoints);
        listView.setAdapter(adapter);
    }
}
