package com.example.kolin.testya.veiw;

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

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteAdapter;
import com.example.kolin.testya.veiw.fragment.Updatable;
import com.example.kolin.testya.veiw.presenters.CommonPresenter;


public class CommonFragment extends Fragment implements ICommonView, Updatable {

    private static final String TAG = CommonFragment.class.getSimpleName();
    private static final String KEY_TYPE = "history_favorite";


    private SearchView searchView;
    private RecyclerView recyclerView;

    private CommonPresenter presenter;

    private HistoryFavoriteAdapter adapter;

    public CommonFragment() {
    }


    public static CommonFragment newInstance(@TypeSaveTranslation.TypeName String type) {
        CommonFragment fragment = new CommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new CommonPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_common, container, false);
        searchView = (SearchView) view.findViewById(R.id.fragment_common_search);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_common_rv);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSearchView();
        setupRecyclerViewAdapter();

        presenter.attacheView(this);
        presenter.loadTranslationDb(getArguments().getString(KEY_TYPE));
    }

    private void setupRecyclerViewAdapter() {
        adapter = new HistoryFavoriteAdapter();

        adapter.setListener(new HistoryFavoriteAdapter.OnClickHistoryFavoriteListener() {
            @Override
            public void checkFavorite(InternalTranslation translation, boolean check) {
                presenter.addRemoveFavoriteTranslationDb(translation, check);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setSearchView() {
        searchView.setQueryHint(getString(R.string.search_in_history));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
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
    public void showLoadedData(InternalTranslation translation) {
        adapter.add(translation);
    }

    @Override
    public void clearAdapter() {
        adapter.clear();
    }

    @Override
    public void update() {
        presenter.loadTranslationDb(getArguments().getString(KEY_TYPE));
    }
}
