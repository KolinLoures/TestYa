package com.example.kolin.testya.veiw.adapter;

import android.widget.Filter;

import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 07.04.2017.
 */

public class HistoryFavoriteFilter extends Filter {

    private final HistoryFavoriteAdapter adapter;

    private final List<InternalTranslation> data;
    private final List<InternalTranslation> filterData;

    public HistoryFavoriteFilter(HistoryFavoriteAdapter adapter,
                                 List<InternalTranslation> data) {
        this.adapter = adapter;
        this.data = new ArrayList<>(data);
        this.filterData = new ArrayList<>();
    }



    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filterData.clear();
        final FilterResults filterResults = new FilterResults();

        if (constraint.length() == 0){
            filterData.addAll(data);
        } else {
            final String search = constraint.toString().trim();

            for (InternalTranslation t: data){
                if (t.getTextFrom().contains(search))
                    filterData.add(t);
            }
        }

        filterResults.values = filterData;
        filterResults.count = filterData.size();

        return filterResults;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.addAll((List<InternalTranslation>) results.values);
    }
}
