package com.example.consumer_client;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gdjang.consumer_client.content.Bannar2;
import com.gdjang.consumer_client.md.MdPicture1;
import com.gdjang.consumer_client.md.MdPicture2;
import com.gdjang.consumer_client.md.MdPicture3;
import com.gdjang.consumer_client.md.MdPicture4;
import com.gdjang.consumer_client.md.MdPicture5;

public class ItemDetailPagerAdapter extends FragmentStateAdapter {
    private int mSetItemCount;
    String pic1, pic2, pic3, pic4, pic5;
    public ItemDetailPagerAdapter(int count, @NonNull FragmentActivity fragmentActivity, String pic1, String pic2, String pic3, String pic4, String pic5){
        super(fragmentActivity);
        this.mSetItemCount = count;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.pic4 = pic4;
        this.pic5 = pic5;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        switch (index){
            case 0 : return new MdPicture1(pic1);
            case 1 : return new MdPicture2(pic2);
            case 2 : return new MdPicture3(pic3);
            case 3 : return new MdPicture4(pic4);
            case 4 : return new MdPicture5(pic5);
            default: return new MdPicture1(pic1);
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
