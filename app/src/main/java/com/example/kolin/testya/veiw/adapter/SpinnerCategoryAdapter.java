package com.example.kolin.testya.veiw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kolin.testya.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 13.04.2017.
 */

public class SpinnerCategoryAdapter extends ArrayAdapter<String> {

    private int[] icons;
    private List<String> stringList;
    private ViewHolder holder = null;

    public SpinnerCategoryAdapter(@NonNull Context context, List<String> spinnersText, int[] icons) {
        super(context, R.layout.spinner_item, spinnersText);
        this.icons = icons;
        stringList = new ArrayList<>(spinnersText);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getViewForSpinner(convertView, parent, position, true);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getViewForSpinner(convertView, parent, position, false);
    }

    private View getViewForSpinner(View convertView, @NonNull ViewGroup parent, int position, boolean showText) {

        int iconId = icons[position];
        String text = stringList.get(position);

        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);

        holder = new ViewHolder(convertView);


        holder.imageView.setImageDrawable(parent.getContext().getDrawable(iconId));
        if (showText) {
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setText(text);
        } else
            holder.textView.setVisibility(View.GONE);

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            imageView = (ImageView) itemView.findViewById(R.id.spinner_item_image);
            textView = (TextView) itemView.findViewById(R.id.spinner_item_text);
        }
    }

    public String getTextForPosition(int position){
        return position < stringList.size() && position >= 0 ? stringList.get(position) : null;
    }
}
