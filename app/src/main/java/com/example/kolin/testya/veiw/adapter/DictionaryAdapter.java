package com.example.kolin.testya.veiw.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.entity.dictionary.MeanSyn;
import com.example.kolin.testya.data.entity.dictionary.Tr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 *
 * Adapter for dictionary recycler view
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {

    private List<Tr> data;

    public DictionaryAdapter() {
        this.data = new ArrayList<>();
    }

    private OnClickDictionaryAdapter listener;

    public interface OnClickDictionaryAdapter{
        void onClickItem(int position);
    }

    @Override
    public DictionaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_dictionary_rv, parent, false);
        return new DictionaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DictionaryViewHolder holder, int position) {

        Tr currentItem = data.get(position);
        String supportText= null;
        holder.supportText.setVisibility(View.VISIBLE);
        holder.primaryText.setText(currentItem.getText());

        List<MeanSyn> mean = currentItem.getMean();
        if (mean != null) {
            supportText = TextUtils.join(", ", mean);
        }

        if (supportText != null && !supportText.isEmpty())
            holder.supportText.setText(supportText);
        else
            holder.supportText.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DictionaryViewHolder extends RecyclerView.ViewHolder {

        private TextView primaryText;
        private TextView supportText;

        DictionaryViewHolder(View itemView) {
            super(itemView);

            primaryText = (TextView) itemView.findViewById(R.id.sub_item_dic_primary_text);
            supportText = (TextView) itemView.findViewById(R.id.sub_item_dic_sub_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onClickItem((getAdapterPosition()));
                }
            });
        }
    }

    public Tr getDataAtPosition(int position){
        return data.get(position);
    }

    public void clearAdapter() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addDataList(List<Tr> trList) {
        this.data.addAll(trList);
        notifyDataSetChanged();
    }


    public void setOnClickListener(OnClickDictionaryAdapter listener){
        this.listener = listener;
    }


}
