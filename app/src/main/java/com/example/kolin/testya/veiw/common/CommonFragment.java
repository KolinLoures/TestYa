package com.example.kolin.testya.veiw.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.TypeOfTranslation;
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteAdapter;

import java.util.List;


public class CommonFragment extends Fragment
        implements ICommonView {


    private static final String KEY = "type";



    private String currentType;

    private TextView emptyTextView;
    private View mainContent;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private HistoryFavoriteAdapter adapter;


    public CommonFragment() {
        // Required empty public constructor
    }


    public static CommonFragment newInstance(@TypeOfTranslation.TypeName String type) {
        CommonFragment commonFragment = new CommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, type);
        commonFragment.setArguments(bundle);
        return commonFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new HistoryFavoriteAdapter();
        currentType = getArguments().getString(KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_common, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        emptyTextView = (TextView) view.findViewById(R.id.fragment_common_text_empty);
        mainContent = view.findViewById(R.id.fragment_common_main_content);
        searchView = (SearchView) view.findViewById(R.id.fragment_common_search);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_common_rv);

        setupSearchViewQueryHint();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchViewQueryHint() {
        if (currentType != null && currentType.equals(TypeOfTranslation.HISTORY))
            searchView.setQueryHint(getString(R.string.search_in_history));
        else if (currentType != null)
            searchView.setQueryHint(getString(R.string.search_in_favorite));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showLoadedData(List<HistoryFavoriteModel> model) {
//        adapter.addAll(model);
    }

    @Override
    public void deleteData() {
        adapter.clear();
    }
}
