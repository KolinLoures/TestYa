package com.example.kolin.testya.veiw.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;

import com.example.kolin.testya.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 09.04.2017.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>
implements Filterable{

    private RadioButton lastSelectedRadio;
    private List<String> data;
    private LanguageFilter filter;


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
        holder.rdLanguage.setText(name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<String> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public String getChose(){
        return lastSelectedRadio != null ? lastSelectedRadio.getText().toString() : null;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new LanguageFilter(data);
        return filter;
    }


    final class LanguageViewHolder extends RecyclerView.ViewHolder {

        private RadioButton rdLanguage;

        public LanguageViewHolder(View itemView) {
            super(itemView);

            rdLanguage = (RadioButton) itemView.findViewById(R.id.item_chose_language_rdBtn);

            rdLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelectedRadio != null)
                        lastSelectedRadio.setChecked(false);

                    if (lastSelectedRadio != v) {
                        lastSelectedRadio = (RadioButton) v;
                    } else
                        lastSelectedRadio = null;
                }
            });
        }
    }

    private final class LanguageFilter extends RecyclerViewFilter<String>{

        public LanguageFilter(List<String> data) {
            super(data);
        }

        @Override
        public String inWhatObjValueSearch(String obj) {
            return obj;
        }

        @Override
        public void publishFilterResult(List<String> filterData) {
            addAll(filterData);
        }
    }

}
