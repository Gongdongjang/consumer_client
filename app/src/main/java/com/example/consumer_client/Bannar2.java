package com.example.consumer_client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Bannar2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.banner_2, container, false);

        setInit(view);
        return view;
    }
    private void setInit(View _view){
        ImageView imageView= _view.findViewById(R.id.homeBanner2);
    }
}
