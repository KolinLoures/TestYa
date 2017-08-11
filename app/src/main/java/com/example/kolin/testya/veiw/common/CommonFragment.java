package com.example.kolin.testya.veiw.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteAdapter;
import com.example.kolin.testya.veiw.custom_views.CustomAppCompatEditText;

import java.util.List;


public class CommonFragment extends Fragment
        implements ICommonView {


    private static final String KEY = "type";



    private String currentType;

    private TextView emptyTextView;
    private View mainContent;
    private CustomAppCompatEditText searchView;
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
        searchView = (CustomAppCompatEditText) view.findViewById(R.id.fragment_common_search);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_common_rv);

        setupSearchView();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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
