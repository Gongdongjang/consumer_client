package com.example.consumer_client;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragPagerAdapter extends FragmentStateAdapter {
    private final int mSetItemCount = 2;
    public FragPagerAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int iViewIdx= getRealPosition(position);
        switch ( iViewIdx){
            case 0 : {return new Bannar1();}
            case 1 : {return new Bannar2();}
            default: {return new Bannar1();}
        }
    }

    private int getRealPosition(int position) {
        return position % mSetItemCount;
    }

    @Override
    public long getItemId(int position){
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
