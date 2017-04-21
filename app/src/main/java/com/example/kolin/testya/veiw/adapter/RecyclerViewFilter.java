package com.example.kolin.testya.veiw.adapter;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 10.04.2017.
 *
 * Abstract class of Filter for Recycler view data.
 *
 */

public abstract class RecyclerViewFilter<T> extends Filter {


    private final List<T> data;
    private final List<T> filterData;

    public RecyclerViewFilter(List<T> data) {
        this.data = new ArrayList<>(data);
        this.filterData = new ArrayList<>(data);
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filterData.clear();
        final FilterResults filterResults = new FilterResults();
        if (constraint.length() == 0)
            filterData.addAll(data);
        else {
            final String search = constraint.toString().trim();

            for (T t: data){
                if (inWhatObjValueSearch(t).contains(search))
                    filterData.add(t);
            }
        }

        filterResults.values = filterData;
        filterResults.count = filterData.size();

        return filterResults;
    }

    public abstract String inWhatObjValueSearch(T obj);

    public abstract void publishFilterResult(List<T> filterData);

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        publishFilterResult((List<T>) results.values);
    }

    public void addNewData(List<T> data){
        this.data.clear();
        this.data.addAll(data);
    }
}
