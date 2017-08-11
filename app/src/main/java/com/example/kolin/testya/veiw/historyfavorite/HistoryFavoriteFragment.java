package com.example.kolin.testya.veiw.historyfavorite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.di.ProvideComponent;
import com.example.kolin.testya.di.components.ViewComponent;
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteAdapter;
import com.example.kolin.testya.veiw.fragment.ClearDialogFragment;

import java.util.List;

import javax.inject.Inject;

public class HistoryFavoriteFragment extends Fragment
        implements IHistoryFavoriteView, ClearDialogFragment.ClearDialogListener {

    private Toolbar toolbar;
    private ImageButton clearBtn;
    private TextView emptyTextView;
    private View mainContent;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private HistoryFavoriteAdapter adapter;


    @Inject
    HistoryFavoritePresenter presenter;




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

        return inflater.inflate(R.layout.fragment_history_favorite, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);


        presenter.attachView(this);

        if (savedInstanceState != null)
            presenter.restoreStateData(savedInstanceState);

        presenter.loadData();
    }

    @Override
    public void showLoadedFavorites(List<HistoryFavoriteModel> data) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        presenter.detachView();
        super.onDetach();
    }

    @Override
    public void onClickPositiveBtn() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.prepareForChangeState(outState);
    }
}
