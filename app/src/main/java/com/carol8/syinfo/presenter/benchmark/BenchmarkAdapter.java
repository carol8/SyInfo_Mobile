package com.carol8.syinfo.presenter.benchmark;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.syinfo.R;
import com.carol8.syinfo.model.benchmark.Benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BenchmarkAdapter extends RecyclerView.Adapter<BenchmarkViewHolder> {
    private final List<Benchmark> benchmarkList = new ArrayList<>();
    private final List<Benchmark> benchmarkSelected = new ArrayList<>();

    private boolean selectAll, deselectAll;

    @SuppressLint("NotifyDataSetChanged")
    public void addBenchmark(Benchmark benchmark) {
        benchmarkList.add(benchmark);
        Collections.sort(benchmarkList);
        notifyDataSetChanged();
    }

    public List<Benchmark> getBenchmarksSelected() {
        Collections.sort(benchmarkSelected);
        return this.benchmarkSelected;
    }

    public void selectAllCheckboxes(){
        selectAll = true;
        deselectAll = false;
        notifyDataSetChanged();
    }

    public void deselectAllCheckboxes(){
        deselectAll = true;
        selectAll = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BenchmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new BenchmarkViewHolder(inflater.inflate(R.layout.item_benchmark, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BenchmarkViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        holder.checkbox.setText(benchmarkList.get(position).getFullName());
        if(selectAll){
            holder.checkbox.setChecked(true);
            benchmarkSelected.add(benchmarkList.get(position));
        }
        else if(deselectAll){
            benchmarkSelected.remove(benchmarkList.get(position));
        }
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                benchmarkSelected.add(benchmarkList.get(position));
            } else {
                benchmarkSelected.remove(benchmarkList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return benchmarkList.size();
    }
}
