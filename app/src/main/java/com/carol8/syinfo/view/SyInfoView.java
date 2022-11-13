package com.carol8.syinfo.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.carol8.syinfo.R;
import com.carol8.syinfo.controller.syinfo.SyInfoAdapter;
import com.carol8.syinfo.model.information.CPUInformation;
import com.carol8.syinfo.model.information.RAMInformation;
import com.carol8.syinfo.model.information.StorageInformation;
import com.carol8.syinfo.model.information.SystemInformation;

public class SyInfoView extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int refreshDelay = 1000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_syinfo, container, false);
        SyInfoAdapter syInfoAdapter = new SyInfoAdapter();

        RecyclerView recyclerView = v.findViewById(R.id.syInfoRecyclerView);
        recyclerView.setAdapter(syInfoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        swipeRefreshLayout = v.findViewById(R.id.syInfoSwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> getAllInformation(syInfoAdapter));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllInformation(syInfoAdapter);
                handler.postDelayed(this, refreshDelay);
            }
        }, refreshDelay);

        getAllInformation(syInfoAdapter);
        return v;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllInformation(SyInfoAdapter syInfoAdapter){
        syInfoAdapter.clear();
        syInfoAdapter.add(new SystemInformation(getContext()));
        syInfoAdapter.add(new CPUInformation());
        syInfoAdapter.add(new RAMInformation(getContext()));
        syInfoAdapter.add(new StorageInformation(getContext()));
        syInfoAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
