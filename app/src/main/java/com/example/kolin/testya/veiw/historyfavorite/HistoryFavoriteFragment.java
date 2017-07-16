package com.example.kolin.testya.veiw.historyfavorite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.di.ProvideComponent;
import com.example.kolin.testya.di.components.ViewComponent;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteAdapter;
import com.example.kolin.testya.veiw.adapter.SpinnerCategoryAdapter;
import com.example.kolin.testya.veiw.fragment.ClearDialogFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HistoryFavoriteFragment extends Fragment
        implements HistoryFavoriteView, ClearDialogFragment.ClearDialogListener {

    //Views
    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView textEmpty;
    private View mainContent;
    private Spinner spinner;
    private Toolbar toolbar;
    private ImageButton btnDelete;

    @Inject
    HistoryFavoritePresenter presenter;

    //adapters
    private SpinnerCategoryAdapter spinnerAdapter;
    private HistoryFavoriteAdapter adapter;

    //queries list hints
    private List<String> queriesHints = new ArrayList<>();

    //Callback interface
    public interface OnInteractionHistoryFavoriteFragment {
        void onClickItem(InternalTranslation internalTranslation, boolean clicked);
    }


    public HistoryFavoriteFragment() {
        setRetainInstance(true);
    }

    public static HistoryFavoriteFragment newInstance() {
        return new HistoryFavoriteFragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ProvideComponent<ViewComponent>) getActivity()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_favorite, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        btnDelete = (ImageButton) view.findViewById(R.id.fragment_hf_delete);
        searchView = (SearchView) view.findViewById(R.id.fragment_hf_search);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_hf_rv);
        textEmpty = (TextView) view.findViewById(R.id.fragment_new_text_empty);
        mainContent = view.findViewById(R.id.fragment_common_main_content);

        spinner = (Spinner) view.findViewById(R.id.fragment_hf_spinner);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);

        if (savedInstanceState != null)
            presenter.restoreStateData(savedInstanceState);

        setupSearchView();
        setupRecyclerViewAdapter();
        initializeSpinnerAdapter();
        setListenerToClearButton();
    }

    private void setListenerToClearButton() {
        //set on click listener to button delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDialogFragment.newInstance(toolbar.getTitle().toString()).show(getChildFragmentManager(), null);
            }
        });
    }

    private void setupSearchView() {
        //add hints to list
        queriesHints.add(getString(R.string.search_in_history));
        queriesHints.add(getString(R.string.search_in_favorite));

        //set query text listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //get adapter filter and filter data
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void setQueryHintBySelection(int selection) {
        //set hints to search view in according with selection
        searchView.setQueryHint(queriesHints.get(selection));
    }

    private void setupRecyclerViewAdapter() {
        adapter = new HistoryFavoriteAdapter();
        //set listener to adapter
        adapter.setListener(new HistoryFavoriteAdapter.OnClickHistoryFavoriteListener() {
            @Override
            public void checkFavorite(InternalTranslation translation, boolean check) {

//                presenter.addRemoveFavoriteDb(translation, check);

                translation.setFavorite(!check);

                //update first fragment in view pager through OnInteractionHistoryFavoriteFragment
                if (getActivity() instanceof OnInteractionHistoryFavoriteFragment)
                    ((OnInteractionHistoryFavoriteFragment) getActivity()).onClickItem(translation, false);

            }

            @Override
            public void itemClick(InternalTranslation internalTranslation) {
                //update first fragment in view pager through OnInteractionHistoryFavoriteFragment
                if (getActivity() instanceof OnInteractionHistoryFavoriteFragment)
                    ((OnInteractionHistoryFavoriteFragment) getActivity()).onClickItem(internalTranslation, true);
            }
        });

        //other settings to recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void initializeSpinnerAdapter() {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.history));
        list.add(getString(R.string.favorite));
        spinnerAdapter = new SpinnerCategoryAdapter(getContext(), list,
                new int[]{
                        R.drawable.ic_access_time_black_24dp,
                        R.drawable.ic_bookmark_black_24dp}
        );

        spinner.setAdapter(spinnerAdapter);
        //set adapter spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //update data in fragment in according with spinner position
                presenter.showLoadedDataForPosition(position);
                toolbar.setTitle(spinnerAdapter.getTextForPosition(position));
                searchView.setQuery("", false);
                setQueryHintBySelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void showLoadedData(List<InternalTranslation> data) {
        if (data.isEmpty()) {
            //hide or show part of layout if data is empty
            btnDelete.setEnabled(false);
            showComponent(mainContent, false);
            showComponent(textEmpty, true);
        } else {
            //hide or show part of layout if data is empty
            btnDelete.setEnabled(true);
            showComponent(mainContent, true);
            showComponent(textEmpty, false);

            //clear adapter
            adapter.clear();

            //add data to adapter
            adapter.addAll(data);
            adapter.addNewDataToFilter(data);
        }
    }

    private void showComponent(View component, boolean show) {
        //set visibility to component
        if (show && component.getVisibility() == View.GONE)
            component.setVisibility(View.VISIBLE);

        if (!show && component.getVisibility() == View.VISIBLE)
            component.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.detachView();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        adapter.clear();
        adapter = null;
    }

    @Override
    public void onClickPositiveBtn() {
        presenter.deleteFromDb(spinner.getSelectedItemPosition());
        //Update Translator fragment (if current translation on it was checked as favorite)
        if (spinner.getSelectedItemPosition() == 1)
            if (getActivity() instanceof OnInteractionHistoryFavoriteFragment)
                ((OnInteractionHistoryFavoriteFragment) getActivity()).onClickItem(null, false);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        presenter.prepareForChangeState(outState);
    }
}
