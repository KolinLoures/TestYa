package com.example.kolin.testya.veiw.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.TypeOfTranslation;
import com.example.kolin.testya.di.ProvideComponent;
import com.example.kolin.testya.di.components.ViewComponent;
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.veiw.CommonFragmentCallback;
import com.example.kolin.testya.veiw.Updatable;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteAdapter;
import com.example.kolin.testya.veiw.custom_views.CustomAppCompatEditText;

import java.util.List;

import javax.inject.Inject;


public class CommonFragment extends Fragment
        implements ICommonView, Updatable {


    private static final String KEY = "type";


    private String currentType;

    private TextView emptyTextView;
    private View mainContent;
    private CustomAppCompatEditText searchView;
    private RecyclerView recyclerView;

    private HistoryFavoriteAdapter recyclerAdapter;
    private RecyclerView.AdapterDataObserver recyclerAdapterDataObserver;

    @Inject
    CommonPresenter commonPresenter;

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

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ProvideComponent<ViewComponent>) getActivity()).getComponent().inject(this);
        recyclerAdapter = new HistoryFavoriteAdapter();
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
        searchView = (CustomAppCompatEditText) view.findViewById(R.id.fragment_common_search);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_common_rv);

        setupSearchView();
        setupRecyclerView();
        setupRecyclerAdapterCallBack();

        commonPresenter.attachView(this);
        commonPresenter.loadHistoryFavoriteFromDb(currentType);
    }

    private void setupRecyclerAdapterCallBack() {
        recyclerAdapter.setHistoryFavoriteCallback(new HistoryFavoriteAdapter.OnClickHistoryFavoriteCallback() {
            @Override
            public void checkFavorite(HistoryFavoriteModel model, boolean check) {
                commonPresenter.addRemoveFromFavorite(model.getId(), check);
                switch (currentType) {
                    case TypeOfTranslation.HISTORY:
                        if (!check)
                            ((CommonFragmentCallback) getParentFragment()).showTranslationInFavorite(model);
                        else
                            ((CommonFragmentCallback) getParentFragment()).removeTranslationInFavorite(model);
                        break;
                    case TypeOfTranslation.FAVORITE:
                        ((CommonFragmentCallback) getParentFragment()).updateTranslationInHistory(model);
                        break;

                }
            }

            @Override
            public void itemClick(HistoryFavoriteModel model) {
                switch (currentType) {
                    case TypeOfTranslation.HISTORY:
                    case TypeOfTranslation.FAVORITE:

                        break;
                }
            }

            @Override
            public void longItemClick(HistoryFavoriteModel model) {
                switch (currentType) {
                    case TypeOfTranslation.HISTORY:
                        commonPresenter.deleteHistoryFrom(model.getId());
                        break;
                    case TypeOfTranslation.FAVORITE:
                        if (model.isFavorite()) {
                            model.setFavorite(false);
                            ((CommonFragmentCallback) getParentFragment()).updateTranslationInHistory(model);
                        }
                        commonPresenter.addRemoveFromFavorite(model.getId(), true);
                        break;
                }
            }
        });

        recyclerAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                checkSizeRecyclerAdapter();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);

                checkSizeRecyclerAdapter();
            }
        };
        recyclerAdapter.registerAdapterDataObserver(recyclerAdapterDataObserver);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setupSearchView() {
        if (currentType != null && currentType.equals(TypeOfTranslation.HISTORY))
            searchView.setHint(getString(R.string.search_in_history));
        else if (currentType != null)
            searchView.setHint(getString(R.string.search_in_favorite));

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    return true;
                }
                return false;
            }
        });

        searchView.setOnKeyBoardHideListener(new CustomAppCompatEditText.onKeyboardHideListener() {
            @Override
            public void onKeyboardHidden() {
                searchView.clearFocus();
            }
        });
    }

    private void checkSizeRecyclerAdapter() {
        if (recyclerAdapter.getItemCount() == 0) {
            mainContent.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);

            ((CommonFragmentCallback) getParentFragment()).setVisibilityToDeleteButton(false);
        } else {
            mainContent.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.INVISIBLE);

            ((CommonFragmentCallback) getParentFragment()).setVisibilityToDeleteButton(true);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        commonPresenter.detachView();
        recyclerAdapter.setHistoryFavoriteCallback(null);
        recyclerAdapter.unregisterAdapterDataObserver(recyclerAdapterDataObserver);

        super.onDetach();
    }

    @Override
    public void showLoadedData(List<HistoryFavoriteModel> model) {
        recyclerAdapter.addAll(model);
    }

    @Override
    public void showLoadedEntity(HistoryFavoriteModel model) {
        recyclerAdapter.add(model);
    }

    @Override
    public void removeEntity(HistoryFavoriteModel model) {
        recyclerAdapter.removeEntity(model);
    }

    @Override
    public void updateCheckEntity(HistoryFavoriteModel model) {
        recyclerAdapter.updateEntityChecked(model.getId(), model.isFavorite());
    }

    @Override
    public void removeFavoritesFromFavorites() {
        recyclerAdapter.removeFavoritesFromHistory();
    }

    @Override
    public void showSnackBar() {
        Snackbar.make(getView(), "Success!", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void deleteData() {
        recyclerAdapter.clear();
        commonPresenter.deleteAllTranslation(currentType);
    }

    @Override
    public void update() {
        if (currentType.equals(TypeOfTranslation.FAVORITE))
            recyclerAdapter.removeNonFavoritesEntity();

        checkSizeRecyclerAdapter();
    }
}
