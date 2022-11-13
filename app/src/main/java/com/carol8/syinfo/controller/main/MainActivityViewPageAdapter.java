package com.carol8.syinfo.controller.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.carol8.syinfo.view.BenchmarksView;
import com.carol8.syinfo.view.SyInfoView;

public class MainActivityViewPageAdapter extends FragmentStateAdapter {
    public MainActivityViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SyInfoView();
            default:
                return new BenchmarksView();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
