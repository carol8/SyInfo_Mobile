package com.carol8.syinfo.presenter.benchmark;

import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.syinfo.R;

public class BenchmarkViewHolder extends RecyclerView.ViewHolder {
    public final CheckBox checkbox;

    public BenchmarkViewHolder(@NonNull View itemView) {
        super(itemView);
        checkbox = itemView.findViewById(R.id.benchmarkOptionCheckBox);
    }
}
