package com.example.kolin.testya.veiw.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;

import com.example.kolin.testya.R;
import com.example.kolin.testya.domain.model.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 09.04.2017.
 * <p>
 * Adapter for chose language recycler view.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>
        implements Filterable {

    private Language selectedLanguage;
    private RadioButton lastSelectedRadio;
    private List<Language> data;
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
        Language currentItem = data.get(position);
        holder.rdLanguage.setText(currentItem.getDef());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(Language language) {
        this.data.add(language);
        notifyItemInserted(this.data.size() - 1);
    }

    private void clearAdapter(){
        int oldSize = this.data.size();
        this.data.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public void addAll(List<Language> languages){
        clearAdapter();
        this.data.addAll(languages);
        notifyItemRangeInserted(0, languages.size());
    }

    public Language getChoseLanguage() {
        return selectedLanguage != null ? selectedLanguage : null;
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelectedRadio != null) {
                        lastSelectedRadio.setChecked(false);
                    }

                    if (lastSelectedRadio != v) {
                        lastSelectedRadio = (RadioButton) v;
                        selectedLanguage = data.get(getAdapterPosition());
                    } else {
                        lastSelectedRadio = null;
                        selectedLanguage = null;
                    }
                }
            });
        }
    }

    private final class LanguageFilter extends RecyclerViewFilter<Language> {

        public LanguageFilter(List<Language> data) {
            super(data);
        }

        @Override
        public String inWhatObjValueSearch(Language obj) {
            return obj.getDef();
        }

        @Override
        public void publishFilterResult(List<Language> filterData) {
            addAll(filterData);
        }
    }

}
