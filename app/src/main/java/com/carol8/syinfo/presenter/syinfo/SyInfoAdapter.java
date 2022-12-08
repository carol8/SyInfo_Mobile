package com.carol8.syinfo.presenter.syinfo;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.syinfo.R;
import com.carol8.syinfo.presenter.ItemViewHolder;
import com.carol8.syinfo.model.information.ComponentInformation;
import com.carol8.syinfo.model.Item;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class SyInfoAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private final List<Item> propertyList = new ArrayList<>();

    public void add(ComponentInformation componentInformation) {
        propertyList.add(componentInformation.getTag());
        propertyList.addAll(componentInformation.getInformations());
    }

    public void clear() {
        propertyList.clear();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(inflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        boolean isTitle = true, isTableHeader = false;
        Item property = propertyList.get(position);

        if(property.getLabel().contains("\\")){
            isTableHeader = true;
            holder.labelTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.table_header));
        }

        for (String s : property.getValues()) {
            holder.setIsRecyclable(false);
            isTitle = false;
            TextView value = new TextView(holder.linearLayout.getContext());
            float scale = holder.itemView.getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (5 * scale + 0.5f);
            value.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            value.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            value.setGravity(Gravity.CENTER_VERTICAL);
            value.setText(s);
            if(isTableHeader){
                value.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.table_header));
            }
            holder.linearLayout.addView(value);
        }

        holder.labelTextView.setText(property.getLabel());
        if (isTitle) {
            holder.labelTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            holder.labelTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.title));
        }
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }
}
