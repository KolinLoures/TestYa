package com.example.kolin.testya.veiw.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.properties.LanguageProperties;
import com.example.kolin.testya.veiw.LanguageDialogView;
import com.example.kolin.testya.veiw.adapter.LanguageAdapter;
import com.example.kolin.testya.veiw.presenters.LanguageDialogPresenter;

import java.util.List;

public class LanguageDialogFragment extends DialogFragment implements LanguageDialogView{

    private LanguageAdapter adapter;

    private RecyclerView recyclerView;
    private Button btnCancel;
    private Button btnYes;

    private LanguageDialogPresenter presenter;

    public LanguageDialogFragment() {
        // Required empty public constructor
    }


    public static LanguageDialogFragment newInstance() {
        return new LanguageDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LanguageDialogPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_language_dialog, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.dialog_language_rv);
        btnCancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
        btnYes =(Button) view.findViewById(R.id.dialog_btn_yes);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attacheView(this);

        setupRecyclerViewAdapter();
    }

    private void setupRecyclerViewAdapter() {
        adapter = new LanguageAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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
    public void showLanguages(List<String> languages) {
        adapter.addAll(languages);
    }
}
