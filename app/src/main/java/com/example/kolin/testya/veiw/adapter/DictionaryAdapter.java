package com.example.kolin.testya.veiw.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.entity.dictionary.Mean;
import com.example.kolin.testya.data.entity.dictionary.Tr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {

    private List<Tr> data;

    public DictionaryAdapter() {
        this.data = new ArrayList<>();
    }

    @Override
    public DictionaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_dictionary_rv, parent, false);
        return new DictionaryViewHolder(view);
    }


    /**
     * TODO Think about several values and process to build string text (add StringBuilder)
     * <p>
     * currentItem.getTr().get(0)!
     */
    @Override
    public void onBindViewHolder(DictionaryViewHolder holder, int position) {
        Tr currentItem = data.get(position);
        holder.primaryText.setText(currentItem.getText());

        String supportText = "";

        List<Mean> mean = currentItem.getMean();
        if (mean != null)
            for (Mean m : mean)
                supportText += m.getText();

        if (!supportText.equals(""))
            holder.supportText.setText(supportText);
        else
            holder.supportText.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class DictionaryViewHolder extends RecyclerView.ViewHolder {

        private TextView primaryText;
        private TextView supportText;

        public DictionaryViewHolder(View itemView) {
            super(itemView);

            primaryText = (TextView) itemView.findViewById(R.id.sub_item_dic_primary_text);
            supportText = (TextView) itemView.findViewById(R.id.sub_item_dic_sub_text);

        }
    }

    public void clearAdapter() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addDataList(List<Tr> trList) {
        this.data.addAll(trList);
        notifyDataSetChanged();
    }

}
