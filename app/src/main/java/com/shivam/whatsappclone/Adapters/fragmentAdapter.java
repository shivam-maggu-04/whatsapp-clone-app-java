package com.shivam.whatsappclone.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shivam.whatsappclone.Fragments.CallFragment;
import com.shivam.whatsappclone.Fragments.ChartFragment;
import com.shivam.whatsappclone.Fragments.StatusFragment;

public class fragmentAdapter extends FragmentPagerAdapter {
    public fragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0: return new ChartFragment();
            case 1: return new StatusFragment();
            case 2: return new CallFragment();
            default:return new ChartFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title =null ;
        if(position==0)
            title = "CHATS";
        if(position==1)
            title = "STATUS";
        if(position==2)
            title = "CALLS";

        return title;
    }
}
