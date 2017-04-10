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
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteAdapter;
import com.example.kolin.testya.veiw.fragment.DataUpdatable;


public class CommonFragment extends Fragment implements DataUpdatable<InternalTranslation> {

    private static final String TAG = CommonFragment.class.getSimpleName();

    private static final String KEY_TEXT = "what_fragment";

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView textEmpty;
    private View mainContent;

    private HistoryFavoriteAdapter adapter;


    public interface OnInteractionCommonFragment {
        void onClickItemInCommonFragment(InternalTranslation internalTranslation, boolean clicked);
    }

    public CommonFragment() {
    }

    public static CommonFragment newInstance(String textToSearchView) {

        CommonFragment fragment = new CommonFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, textToSearchView);
        fragment.setArguments(args);
        return fragment;
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
        textEmpty = (TextView) view.findViewById(R.id.fragment_common_text_empty);
        mainContent = view.findViewById(R.id.fragment_common_main_content);

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

                if (getActivity() instanceof OnInteractionCommonFragment) {
                    translation.setFavorite(!check);
                    ((OnInteractionCommonFragment) getActivity())
                            .onClickItemInCommonFragment(translation, false);
                }
            }

            @Override
            public void itemClick(InternalTranslation internalTranslation) {
                if (getActivity() instanceof OnInteractionCommonFragment) {
                    ((OnInteractionCommonFragment) getActivity())
                            .onClickItemInCommonFragment(internalTranslation, true);
                }
            }
        });

        LinearLayoutManager rvLayout = new LinearLayoutManager(recyclerView.getContext());


        recyclerView.setLayoutManager(rvLayout);
        recyclerView.setAdapter(adapter);
    }

    private void setSearchView() {
        String string = getArguments().getString(KEY_TEXT);
        searchView.setQueryHint(string);
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
        showComponent(mainContent, true);
        showComponent(textEmpty, false);
    }

    @Override
    public void clear() {
        adapter.clear();
        showComponent(mainContent, false);
        showComponent(textEmpty, true);
    }

    private void showComponent(View component, boolean show) {
        if (show && component.getVisibility() == View.INVISIBLE)
            component.setVisibility(View.VISIBLE);

        if (!show && component.getVisibility() == View.VISIBLE)
            component.setVisibility(View.INVISIBLE);
    }
}
