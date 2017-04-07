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
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 06.04.2017.
 */

public class HistoryFavoriteAdapter
        extends RecyclerView.Adapter<HistoryFavoriteAdapter.ViewHolder>
        implements Filterable {

    private List<InternalTranslation> data;
    private HistoryFavoriteFilter filter;

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new HistoryFavoriteFilter(this, data);
        return filter;
    }


    public interface OnClickHistoryFavoriteListener {
        void checkFavorite(InternalTranslation translation, boolean check);
    }

    private OnClickHistoryFavoriteListener listener;

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
        InternalTranslation translation = data.get(position);
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
        data.clear();
        notifyDataSetChanged();
    }

    public void add(InternalTranslation translation) {
        this.data.add(translation);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<InternalTranslation> translations){
        data.clear();
        data.addAll(translations);
        notifyDataSetChanged();
    }

    public void setListener(OnClickHistoryFavoriteListener listener) {
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
                        listener.checkFavorite(data.get(getAdapterPosition()), !checkBox.isChecked());
                    }
                }
            });
        }


    }
}
