package com.example.kolin.testya.veiw.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.data.entity.dictionary.MeanSyn;
import com.example.kolin.testya.data.entity.dictionary.Tr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 * <p>
 * Adapter for dictionary recycler view
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {

    private List<Tr> data;

    public DictionaryAdapter() {
        this.data = new ArrayList<>();
    }

    private OnClickDictionaryAdapter listener;

    public interface OnClickDictionaryAdapter {
        void onClickItem(int position);
    }

    @Override
    public DictionaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dictionary, parent, false);
        return new DictionaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DictionaryViewHolder holder, int position) {
        Tr currentItem = this.data.get(position);
        List<MeanSyn> synList = new ArrayList<>();
        List<MeanSyn> meanList = new ArrayList<>();

        if (position == 0 || (position > 0 && !this.data.get(position - 1).getPos().equals(currentItem.getPos()))) {
            holder.partOfSpeechText.setVisibility(View.VISIBLE);
            holder.partOfSpeechText.setText(currentItem.getPos());
        }

        synList.add(0, new MeanSyn(currentItem.getText()));

        if (currentItem.getSyn() != null){
            synList.addAll(1, currentItem.getSyn());
        }

        holder.synText.setText(TextUtils.join(", ", synList));

        if (currentItem.getMean() != null) {
            meanList.addAll(currentItem.getMean());
            holder.meanText.setText(TextUtils.join(", ", meanList));
        }
        else {
            holder.meanText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DictionaryViewHolder extends RecyclerView.ViewHolder {

        private TextView partOfSpeechText;
        private TextView synText;
        private TextView meanText;

        DictionaryViewHolder(View itemView) {
            super(itemView);

            partOfSpeechText = (TextView) itemView.findViewById(R.id.dictionary_part_of_speech_text);
            synText = (TextView) itemView.findViewById(R.id.dictionary_syn_text);
            meanText = (TextView) itemView.findViewById(R.id.dictionary_mean_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onClickItem((getAdapterPosition()));
                }
            });
        }
    }


    public void clearAdapter() {
        int size = this.data.size();
        this.data.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addData(List<Def> data) {
        this.data.addAll(getAllTrFromDef(data));
        notifyItemRangeInserted(0, this.data.size());
    }

    private List<Tr> getAllTrFromDef(List<Def> data) {
        List<Tr> temp = new LinkedList<>();
        for (Def d : data) {
            temp.addAll(d.getTr());
        }
        return temp;
    }


    public void setOnClickListener(OnClickDictionaryAdapter listener) {
        this.listener = listener;
    }


}
