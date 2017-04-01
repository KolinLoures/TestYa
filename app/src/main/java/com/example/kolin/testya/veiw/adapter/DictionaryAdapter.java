package com.example.kolin.testya.veiw.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.models.dictionary.Def;
import com.example.kolin.testya.data.models.dictionary.Dictionary;
import com.example.kolin.testya.data.models.dictionary.Mean;
import com.example.kolin.testya.data.models.dictionary.Tr;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {

    private List<Def> list;

    public DictionaryAdapter() {
        this.list = new LinkedList<>();
    }

    @Override
    public DictionaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_dictionary_rv, parent, false);
        return new DictionaryViewHolder(view);
    }


    /**
     * TODO Think about several values and process to build string text (add StringBuilder)
     *
     * currentItem.getTr().get(0)!
     * */
    @Override
    public void onBindViewHolder(DictionaryViewHolder holder, int position) {
        Def currentItem = list.get(position);
        Tr tr = currentItem.getTr().get(0);
        holder.primaryText.setText(tr.getText());

        String supportText = null;

        for (Mean m: tr.getMean()){
            supportText += m.getText();
        }

        holder.supportText.setText(supportText);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class DictionaryViewHolder extends RecyclerView.ViewHolder{

        private TextView  primaryText;
        private TextView  supportText;

        public DictionaryViewHolder(View itemView) {
            super(itemView);
        }
    }

}
