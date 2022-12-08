package com.carol8.syinfo.presenter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.syinfo.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public final LinearLayout linearLayout;
    public final TextView labelTextView;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.itemLinearLayout);
        labelTextView = itemView.findViewById(R.id.label);
    }
}
