package com.carol8.syinfo.presenter.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.carol8.syinfo.view.BenchmarksFragment;
import com.carol8.syinfo.view.SyInfoFragment;

public class MainActivityViewPageAdapter extends FragmentStateAdapter {
    public MainActivityViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SyInfoFragment();
            default:
                return new BenchmarksFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
