package com.example.kolin.testya.veiw.adapter;

import android.graphics.RadialGradient;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.kolin.testya.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 09.04.2017.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private List<String> data;

    public LanguageAdapter() {
        this.data = new ArrayList<>();
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chose_language, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        String name = data.get(position);
        holder.textNameLanguage.setText(name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    final static class LanguageViewHolder extends RecyclerView.ViewHolder{

        private RadioButton rdLanguage;
        private TextView textNameLanguage;

        public LanguageViewHolder(View itemView) {
            super(itemView);

            rdLanguage = (RadioButton) itemView.findViewById(R.id.item_chose_language_rdBtn);
            textNameLanguage = (TextView) itemView.findViewById(R.id.item_chose_language_text);
        }
    }

    public void addAll(List<String> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}
