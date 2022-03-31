package com.example.consumer_client.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.consumer_client.Adapter.hamburger.FarmActivity;
import com.example.consumer_client.R;

public class TotalList extends Fragment {
    private View view;
    private TextView totalFarmTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_total_list, container, false);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_total_list, container, false);

        TextView totalFarmTextView = (TextView) view.findViewById(R.id.showTotalFarm);
        totalFarmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FarmActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}