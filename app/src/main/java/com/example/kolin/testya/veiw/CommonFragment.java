package com.example.kolin.testya.veiw;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteAdapter;
import com.example.kolin.testya.veiw.fragment.DataUpdatable;


public class CommonFragment extends Fragment implements DataUpdatable<InternalTranslation> {

    private static final String TAG = CommonFragment.class.getSimpleName();


    private SearchView searchView;
    private RecyclerView recyclerView;

    private HistoryFavoriteAdapter adapter;


    public interface OnIteractionComminFragment{
        void onClickAddFavorite(InternalTranslation internalTranslation);

        void onClickAdapterItem(InternalTranslation internalTranslation);
    }

    public CommonFragment() {
    }

    public static CommonFragment newInstance() {
        return new CommonFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    private void setupRecyclerViewAdapter() {
        adapter = new HistoryFavoriteAdapter();

        adapter.setListener(new HistoryFavoriteAdapter.OnClickHistoryFavoriteListener() {
            @Override
            public void checkFavorite(InternalTranslation translation, boolean check) {
                Fragment parentFragment = getParentFragment();

                if (parentFragment != null && parentFragment instanceof ICommonView)
                    ((ICommonView) parentFragment).check(translation, check);

                if (getActivity() instanceof OnIteractionComminFragment) {
                    translation.setFavorite(!check);
                    ((OnIteractionComminFragment) getActivity()).onClickAddFavorite(translation);
                }
            }

            @Override
            public void itemClick(InternalTranslation internalTranslation) {
                if (getActivity() instanceof OnIteractionComminFragment) {
                    ((OnIteractionComminFragment) getActivity()).onClickAdapterItem(internalTranslation);
                }
            }
        });

        LinearLayoutManager rvLayout = new LinearLayoutManager(recyclerView.getContext());


        recyclerView.setLayoutManager(rvLayout);
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
    public void update(InternalTranslation newData) {
        adapter.add(newData);
    }

    @Override
    public void clear() {
        adapter.clear();
    }
}
