package com.example.kolin.testya.veiw;

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
import com.example.kolin.testya.veiw.fragment.Updatable;
import com.example.kolin.testya.veiw.presenters.HistoryFavoritePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HistoryFavoriteFragment extends Fragment implements NewView, Updatable, ClearDialogFragment.ClearDialogListener {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView textEmpty;
    private View mainContent;

    private Spinner spinner;
    private Toolbar toolbar;
    private ImageButton btnDelete;

    @Inject
    HistoryFavoritePresenter presenter;

    private SpinnerCategoryAdapter spinnerAdapter;
    private HistoryFavoriteAdapter adapter;

    private List<String> queriesHints = new ArrayList<>();

    public interface OnInteractionNewFragment {
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

        presenter.attacheView(this);

        if (savedInstanceState != null)
            presenter.restoreStateData(savedInstanceState);

        setupSearchView();
        setupRecyclerViewAdapter();
        initializeSpinnerAdapter();
        setListenerToClearButton();
    }

    private void setListenerToClearButton() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDialogFragment.newInstance(toolbar.getTitle().toString()).show(getChildFragmentManager(), null);
            }
        });
    }

    private void setupSearchView() {

        queriesHints.add(getString(R.string.search_in_history));
        queriesHints.add(getString(R.string.search_in_favorite));

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

    private void setQueryHintBySelection(int selection) {
        searchView.setQueryHint(queriesHints.get(selection));
    }

    private void setupRecyclerViewAdapter() {
        adapter = new HistoryFavoriteAdapter();
        adapter.setListener(new HistoryFavoriteAdapter.OnClickHistoryFavoriteListener() {
            @Override
            public void checkFavorite(InternalTranslation translation, boolean check) {

                presenter.addRemoveFavoriteDb(translation, check);

                translation.setFavorite(!check);

                if (getActivity() instanceof OnInteractionNewFragment)
                    ((OnInteractionNewFragment) getActivity()).onClickItem(translation, false);

            }

            @Override
            public void itemClick(InternalTranslation internalTranslation) {
                if (getActivity() instanceof OnInteractionNewFragment)
                    ((OnInteractionNewFragment) getActivity()).onClickItem(internalTranslation, true);
            }
        });


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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
            btnDelete.setEnabled(false);
            showComponent(mainContent, false);
            showComponent(textEmpty, true);
        } else {

            btnDelete.setEnabled(true);
            showComponent(mainContent, true);
            showComponent(textEmpty, false);

            adapter.clear();

            adapter.addAll(data);
            adapter.addNewDataToFilter(data);
        }
    }

    private void showComponent(View component, boolean show) {
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
    public void update() {
        presenter.loadTranslationFromDb(null, spinner.getSelectedItemPosition());
    }

    @Override
    public void onClickPositiveBtn() {
        presenter.deleteFromDb(spinner.getSelectedItemPosition());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        presenter.prepareForChangeState(outState);
    }
}
