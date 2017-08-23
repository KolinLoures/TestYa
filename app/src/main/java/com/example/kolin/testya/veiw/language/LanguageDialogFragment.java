package com.example.kolin.testya.veiw.language;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.kolin.testya.R;
import com.example.kolin.testya.domain.model.Language;
import com.example.kolin.testya.veiw.adapter.LanguageAdapter;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * Dialog fragment to choose language
 */
public class LanguageDialogFragment extends DialogFragment implements ILanguageDialogView {

    //Argument keys
    private static final String KEY = "type";

    //Adapter
    private LanguageAdapter adapter;

    //Views
    private RecyclerView recyclerView;
    private Button btnCancel;
    private Button btnYes;
    private SearchView searchView;

    private View.OnClickListener onClickListener;

    //Presenter
    @Inject
    LanguageDialogPresenter presenter;

    public LanguageDialogFragment() {
        // Required empty public constructor
    }


    //Dialog fragment Callback interface
    public interface OnInteractionLanguageDialog {
        void onChooseLanguageDialog(Language language, int typeDialog);
    }


    public static LanguageDialogFragment newInstance(@LanguageDialogType.TypeLangDialog
                                                             int type) {
        LanguageDialogFragment dialog = new LanguageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, type);
        dialog.setArguments(bundle);
        return dialog;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidSupportInjection.inject(this);
        adapter = new LanguageAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language_dialog, container, false);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //set feature with no title (title visible in Lollipop device)
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.dialog_language_rv);
        btnCancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
        btnYes = (Button) view.findViewById(R.id.dialog_btn_yes);
        searchView = (SearchView) view.findViewById(R.id.dialog_language_search);

        presenter.attachView(this);

        setupRecyclerViewAdapter();
        initializeListener();
        setupSearchViewListener();

        btnCancel.setOnClickListener(onClickListener);
        btnYes.setOnClickListener(onClickListener);

        presenter.loadLanguages();
    }

    private void setupSearchViewListener() {
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


    private void setupRecyclerViewAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoadedLanguage(Language language) {
        adapter.add(language);
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

                Language choseLanguage = adapter.getChoseLanguage();

                if (choseLanguage != null) {
                    ((OnInteractionLanguageDialog) getParentFragment())
                            .onChooseLanguageDialog(choseLanguage, getArguments().getInt(KEY));

                    dismiss();
                }
                else
                    showNotificationToast();
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
        presenter.disposeAll();
        adapter = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
        adapter = null;
        onClickListener = null;
    }
}
