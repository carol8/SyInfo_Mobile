package com.carol8.syinfo.presenter.result;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.syinfo.R;
import com.carol8.syinfo.presenter.ItemViewHolder;
import com.carol8.syinfo.model.Item;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class ResultAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private final List<Item> resultList = new ArrayList<>();

    public void addResults(List<Item> results){
        this.resultList.addAll(results);
    }

    public void clear(){
        this.resultList.clear();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(inflater.inflate(R.layout.item_result, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Item result = resultList.get(position);

        holder.labelTextView.setText(result.getLabel());

        TextView resultTextView = new TextView(holder.linearLayout.getContext());
        resultTextView.setText(result.getValues().get(0));
        resultTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        if(position == 0){
            holder.labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            holder.labelTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.title));
            float scale = holder.itemView.getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (5 * scale + 0.5f);
            resultTextView.setPadding(0, dpAsPixels, 0, dpAsPixels);
            resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            resultTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.title));
        }
        else if(position != resultList.size() - 1 && resultList.get(position + 1).getLabel().contains(result.getLabel())){
            holder.labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            holder.labelTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.table_header));
            float scale = holder.itemView.getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (5 * scale + 0.5f);
            resultTextView.setPadding(0, dpAsPixels, 0, dpAsPixels);
            resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            resultTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.table_header));
        }

        holder.linearLayout.addView(resultTextView);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }
}
