package com.example.kolin.testya.veiw.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kolin.testya.R;
import com.example.kolin.testya.veiw.adapter.LanguageAdapter;

import java.util.ArrayList;
import java.util.List;

public class LanguageDialogFragment extends DialogFragment {

    private static final String KEY_LANG = "list_languages";
    private static final String KEY_SHOW = "show_determine_lang";

    private LanguageAdapter adapter;

    private RecyclerView recyclerView;
    private Button btnCancel;
    private Button btnYes;

    private View.OnClickListener onClickListener;

    public LanguageDialogFragment() {
        // Required empty public constructor
    }

    public interface OnIteractionLanguageDialog {
        void onChooseLanguageDialog(String lang, boolean isTextFrom);
    }


    public static LanguageDialogFragment newInstance(List<String> languages,
                                                     boolean showDetermineLang) {
        LanguageDialogFragment fragment = new LanguageDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(KEY_LANG, (ArrayList<String>) languages);
        args.putBoolean(KEY_SHOW, showDetermineLang);
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
        View view = inflater.inflate(R.layout.fragment_language_dialog, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.dialog_language_rv);
        btnCancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
        btnYes = (Button) view.findViewById(R.id.dialog_btn_yes);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerViewAdapter();
        initializeListener();

        setAdapterData();

        btnCancel.setOnClickListener(onClickListener);
        btnYes.setOnClickListener(onClickListener);
    }


    private void setupRecyclerViewAdapter() {
        adapter = new LanguageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setAdapterData() {
        ArrayList<String> list = getArguments().getStringArrayList(KEY_LANG);
        if (list != null && adapter != null) {
            if (!getArguments().getBoolean(KEY_SHOW))
                list.remove(0);
            adapter.addAll(list);
        }
    }


    private void initializeListener() {
        if (onClickListener == null)
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performClick(v);
                }
            };
    }

    private void performClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_cancel:
                dismiss();
                break;
            case R.id.dialog_btn_yes:
                Fragment parentFragment = getParentFragment();
                if (parentFragment != null && parentFragment instanceof LanguageDialogFragment.OnIteractionLanguageDialog) {
                    String chose = adapter.getChose();
                    if (chose != null) {
                        ((LanguageDialogFragment.OnIteractionLanguageDialog) parentFragment)
                                .onChooseLanguageDialog(chose, getArguments().getBoolean(KEY_SHOW));
                        dismiss();
                    }
                    else {
                        showNotificationToast();
                    }
                }
                break;
        }
    }

    private void showNotificationToast() {
        Toast.makeText(getContext(), getString(R.string.chose_language), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter = null;
    }
}
