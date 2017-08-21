package com.example.kolin.testya.veiw.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by kolin on 06.04.2017.
 * <p>
 * Adapter for history and favorite recycler view
 */

public class HistoryFavoriteAdapter
        extends RecyclerView.Adapter<HistoryFavoriteAdapter.ViewHolder>
        implements Filterable {

    private List<HistoryFavoriteModel> data;
    private HistoryFavoriteFilter filter;

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new HistoryFavoriteFilter(data);
        return filter;
    }


    public interface OnClickHistoryFavoriteCallback {
        void checkFavorite(HistoryFavoriteModel model, boolean check);

        void itemClick(HistoryFavoriteModel model);

        void longItemClick(HistoryFavoriteModel model);
    }

    private OnClickHistoryFavoriteCallback listener;

    public HistoryFavoriteAdapter() {
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_favorite_rv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryFavoriteModel translation = data.get(position);
        holder.chkBoxFavorite.setChecked(translation.isFavorite());
        holder.primaryText.setText(translation.getTextFrom());
        holder.secondaryText.setText(translation.getTextTo());
        holder.langText.setText(translation.getLang());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void clear() {
        int oldSize = this.data.size();
        this.data.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public void clearFilter() {
        filter = null;
    }

    public void add(HistoryFavoriteModel translation) {
        this.data.add(0, translation);
        notifyItemInserted(0);
    }

    public void addAll(List<HistoryFavoriteModel> translations) {
        this.data.addAll(translations);
        notifyItemRangeInserted(0, translations.size());
    }

    public void removeEntityById(HistoryFavoriteModel model) {
        int index = -1;
        for (HistoryFavoriteModel h : data) {
            if (h.getId() == model.getId()) {
                index = data.indexOf(h);
                break;
            }
        }

        if (index != -1) {
            this.data.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void removeNonFavoritesEntity() {
        Iterator<HistoryFavoriteModel> iterator = this.data.iterator();
        while (iterator.hasNext()) {
            HistoryFavoriteModel model = iterator.next();
            if (!model.isFavorite()) {
                int index = this.data.indexOf(model);
                iterator.remove();
                notifyItemRemoved(index);
            }
        }
    }

    public void updateEntityChecked(int id, boolean check) {
        int i = -1;
        for (int k = 0; k < this.data.size(); k++) {
            if (this.data.get(k).getId() == id)
                i = k;
        }

        if (i != -1) {
            this.data.get(i).setFavorite(check);
            notifyItemChanged(i);
        }
    }

    public void removeFavoritesFromHistory() {
        for (int i = 0; i < this.data.size(); i++) {
            this.data.get(i).setFavorite(false);
            notifyItemChanged(i);
        }
    }

    public void addNewDataToFilter(List<HistoryFavoriteModel> translations) {
        if (filter == null)
            getFilter();

        filter.addNewData(translations);
    }

    public List<HistoryFavoriteModel> getAdapterData() {
        return data;
    }

    public void setHistoryFavoriteCallback(OnClickHistoryFavoriteCallback listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox chkBoxFavorite;
        private TextView primaryText;
        private TextView secondaryText;
        private TextView langText;

        public ViewHolder(View itemView) {
            super(itemView);

            chkBoxFavorite = (CheckBox) itemView.findViewById(R.id.item_check_box_favorite);
            primaryText = (TextView) itemView.findViewById(R.id.item_primary_text);
            secondaryText = (TextView) itemView.findViewById(R.id.item_secondary_text);
            langText = (TextView) itemView.findViewById(R.id.item_lang_text);

            chkBoxFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    if (listener != null) {
                        HistoryFavoriteModel model = data.get(getAdapterPosition());
                        boolean check = !checkBox.isChecked();

                        model.setFavorite(!check);

                        listener.checkFavorite(model, check);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.itemClick(data.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.longItemClick(data.get(getAdapterPosition()));
                        data.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }


    }

    private class HistoryFavoriteFilter extends RecyclerViewFilter<HistoryFavoriteModel> {

        HistoryFavoriteFilter(List<HistoryFavoriteModel> data) {
            super(data);
        }

        @Override
        public String inWhatObjValueSearch(HistoryFavoriteModel obj) {
            return obj.getTextFrom();
        }

        @Override
        public void publishFilterResult(List<HistoryFavoriteModel> filterData) {
            clear();
            addAll(filterData);
        }
    }
}
