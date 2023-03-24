package com.gdjang.consumer_client.md;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.gdjang.consumer_client.R;

public class MdPicture2 extends Fragment {
    String imageView;
    public MdPicture2(String imageView){
        this.imageView = imageView;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.md_pic2, container, false);

        setInit(view);
        return view;
    }
    public void setInit(View _view){

        ImageView img = _view.findViewById(R.id.md_pic2);
        Glide.with(this)
                .load(imageView)
                .into(img);
    }
}
