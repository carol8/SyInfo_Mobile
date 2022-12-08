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
import com.carol8.syinfo.presenter.syinfo.SyInfoAdapter;
import com.carol8.syinfo.model.information.CPUInformation;
import com.carol8.syinfo.model.information.ComponentInformation;
import com.carol8.syinfo.model.information.RAMInformation;
import com.carol8.syinfo.model.information.StorageInformation;
import com.carol8.syinfo.model.information.SystemInformation;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SyInfoFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int refreshDelay = 500;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_syinfo, container, false);
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
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<ComponentInformation>> informationFutures = List.of(
                executorService.submit(() -> new SystemInformation(getContext()))
                ,executorService.submit(CPUInformation::new)
                ,executorService.submit(() -> new RAMInformation(getContext()))
                ,executorService.submit(() -> new StorageInformation(getContext()))
        );
        for(Future<ComponentInformation> future : informationFutures){
            try {
                syInfoAdapter.add(future.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        syInfoAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
