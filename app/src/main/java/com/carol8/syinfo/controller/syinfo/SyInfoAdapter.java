package com.carol8.syinfo.controller.syinfo;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.syinfo.R;
import com.carol8.syinfo.model.information.ComponentInformation;
import com.carol8.syinfo.model.Item;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class SyInfoAdapter extends RecyclerView.Adapter<SyInfoViewHolder> {
    private final List<Item> itemList = new ArrayList<>();

    public void add(ComponentInformation componentInformation) {
        itemList.add(componentInformation.getTag());
        itemList.addAll(componentInformation.getInformations());
    }

    public void clear() {
        itemList.clear();
    }

    @NonNull
    @Override
    public SyInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SyInfoViewHolder(inflater.inflate(R.layout.item_syinfo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SyInfoViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        boolean isTitle = true, isTableHeader = false;
        Item item = itemList.get(position);

        if(item.getLabel().contains("\\")){
            isTableHeader = true;
            holder.labelTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.table_header));
        }

        for (String s : item.getValues()) {
            holder.setIsRecyclable(false);
            isTitle = false;
            TextView value = new TextView(holder.linearLayout.getContext());
            float scale = holder.itemView.getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (5 * scale + 0.5f);
            value.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            value.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            value.setGravity(View.TEXT_ALIGNMENT_CENTER);
            value.setText(s);
            if(isTableHeader){
                value.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.table_header));
            }
            holder.linearLayout.addView(value);
        }

        holder.labelTextView.setText(item.getLabel());
        if (isTitle) {
            holder.labelTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            holder.labelTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.title));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
