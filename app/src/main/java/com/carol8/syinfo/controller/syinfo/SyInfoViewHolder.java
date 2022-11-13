package com.carol8.syinfo.controller.syinfo;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.syinfo.R;

public class SyInfoViewHolder extends RecyclerView.ViewHolder {
    public final LinearLayout linearLayout;
    public final TextView labelTextView;

    public SyInfoViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.itemLinearLayout);
        labelTextView = itemView.findViewById(R.id.label);
    }
}
