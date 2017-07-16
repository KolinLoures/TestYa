package com.example.kolin.testya.veiw.translator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.di.ProvideComponent;
import com.example.kolin.testya.di.components.ViewComponent;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.domain.model.Language;
import com.example.kolin.testya.veiw.Updatable;
import com.example.kolin.testya.veiw.adapter.DictionaryAdapter;
import com.example.kolin.testya.veiw.language.LanguageDialogFragment;
import com.example.kolin.testya.veiw.language.LanguageDialogType;

import java.util.List;

import javax.inject.Inject;


public class TranslatorFragment extends Fragment implements
        ITranslatorView,
        LanguageDialogFragment.OnInteractionLanguageDialog,
        Updatable{

    //TAG fo logging
    private static final String TAG = TranslatorFragment.class.getSimpleName();

    //Views
    private TextView textTranslationResult;
    private TextView dictionaryTextHeader;
    private EditText editTextToTranslate;
    private RecyclerView recyclerViewDictionary;
    private CheckBox btnAddFavorite;
    private ImageButton btnClear;
    private Button btnFrom;
    private Button btnTo;
    private ImageButton btnReverseLang;
    private ProgressBar loadingProgress;
    private View translationCard;
    private View dictionaryCard;
    private View translationDictionaryCard;
    private TextView textViewError;

    //Dialog
    private LanguageDialogFragment dialog;

    //Click listener
    private View.OnClickListener onClickListener;

    //Variable to block text watcher
    private boolean blockTextWatcher = false;

    //Adapters
    private DictionaryAdapter dictionaryAdapter;

    @Inject
    TranslatorPresenter presenter;

    public TranslatorFragment() {
        setRetainInstance(true);
    }


    public static TranslatorFragment newInstance() {
        return new TranslatorFragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ProvideComponent<ViewComponent>) getActivity()).getComponent().inject(this);
        dictionaryAdapter = new DictionaryAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextToTranslate = (EditText) view.findViewById(R.id.translator_edit_text);
        textTranslationResult = (TextView) view.findViewById(R.id.translation_card_text_result);
        recyclerViewDictionary = (RecyclerView) view.findViewById(R.id.dictionary_recycler_view);
        dictionaryTextHeader = (TextView) view.findViewById(R.id.dictionary_card_text_header);
        btnAddFavorite = (CheckBox) view.findViewById(R.id.translation_card_btn_favorite);
        btnClear = (ImageButton) view.findViewById(R.id.translation_clear_edit_btn);
        translationCard = view.findViewById(R.id.translation_card);
        dictionaryCard = view.findViewById(R.id.dictionary_card);
        translationDictionaryCard = view.findViewById(R.id.translation_dictionary_card);
        btnFrom = (Button) view.findViewById(R.id.translation_btn_from);
        btnTo = (Button) view.findViewById(R.id.translation_btn_to);
        btnReverseLang = (ImageButton) view.findViewById(R.id.translation_img_btn_reverse);
        loadingProgress = (ProgressBar) view.findViewById(R.id.translation_progress_downloading);
        textViewError = (TextView) view.findViewById(R.id.translation_error_text);


        presenter.attachView(this);
        presenter.loadLangFromPreferences();

        if (savedInstanceState != null)
            presenter.restoreStateData(savedInstanceState);

        setupEditTextChangeListener();
        setupRecyclerViewAdapter();
        initializeOnClickListener();

        //set click listeners
        btnClear.setOnClickListener(onClickListener);
        btnAddFavorite.setOnClickListener(onClickListener);
        btnTo.setOnClickListener(onClickListener);
        btnFrom.setOnClickListener(onClickListener);
        btnReverseLang.setOnClickListener(onClickListener);
    }

    private void initializeOnClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClick(v);
            }
        };
    }

    private void performClick(View v) {
        switch (v.getId()) {
            case R.id.translation_btn_from:
                dialog = LanguageDialogFragment.newInstance(LanguageDialogType.TYPE_FROM);
                dialog.show(getChildFragmentManager(), null);
                break;
            case R.id.translation_btn_to:
                dialog = LanguageDialogFragment.newInstance(LanguageDialogType.TYPE_TO);
                dialog.show(getChildFragmentManager(), null);
                break;
            case R.id.translation_card_btn_favorite:
                presenter.addFavorite(!((CheckBox) v).isChecked());
                break;
            case R.id.translation_img_btn_reverse:
                presenter.reverseLanguages();
                break;
            case R.id.translation_clear_edit_btn:
                editTextToTranslate.setText("");
                break;
        }
    }

    private void setupRecyclerViewAdapter() {
        recyclerViewDictionary.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDictionary.setAdapter(dictionaryAdapter);
    }

    private void setupEditTextChangeListener() {
        //set text watcher to edit text
        editTextToTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().trim().isEmpty())
                    btnClear.setVisibility(View.VISIBLE);
                else
                    btnClear.setVisibility(View.INVISIBLE);

                if (!blockTextWatcher)
                    presenter.loadTranslation(s.toString().trim());
                else
                    blockTextWatcher = false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStop() {
        presenter.saveLangToPreferences();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        presenter.disposeAll();
        dictionaryAdapter = null;
        onClickListener = null;

        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setLangFrom(String lang) {
        btnFrom.setText(lang);
    }

    @Override
    public void setLangTo(String lang) {
        btnTo.setText(lang);
    }

    @Override
    public void setFavoriteCheckBox(boolean check) {
        btnAddFavorite.setChecked(check);
    }

    @Override
    public void setTranslatableText(String text, boolean blockTextWatcher) {
        this.blockTextWatcher = blockTextWatcher;
        editTextToTranslate.setText(text);
    }

    @Override
    public void showTranslationResult(InternalTranslation translation) {
        showTranslationCard(true);
        showDictionaryCard(!translation.getDef().isEmpty());

        textTranslationResult.setText(translation.getTextTo());
        btnAddFavorite.setChecked(translation.isFavorite());
        if (translation.getDef() != null && !translation.getDef().isEmpty()) {
            showDictionary(translation.getDef());
        }

        showTranslationDictionaryCard(true);
    }


    private void showDictionary(List<Def> defList) {
        dictionaryAdapter.clearAdapter();
        dictionaryAdapter.addData(defList);
    }

    @Override
    public void notifyUser(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTranslationDictionaryCard(boolean show) {
        showAnimCard(translationDictionaryCard, show);
    }

    @Override
    public void showTranslationCard(boolean show) {
        showComponent(translationCard, show);
    }

    @Override
    public void showDictionaryCard(boolean show) {
        showComponent(dictionaryCard, show);
    }

    @Override
    public void showLoadingProgress(boolean show) {
        showComponent(loadingProgress, show);
    }

    @Override
    public void showError(boolean show) {
        showComponent(textViewError, show);
    }

    private void showAnimCard(View card, boolean show) {
        if (card == null)
            return;


        //animate view
        if (show && card.getVisibility() == View.GONE) {
            //initialize animator
            ObjectAnimator animator = ObjectAnimator.ofFloat(card, View.ALPHA, 0.0f, 1.0f);
            animator.setDuration(500);
            animator.start();

            card.setVisibility(View.VISIBLE);
            card.clearAnimation();
        }

        //hide view
        if (!show && card.getVisibility() == View.VISIBLE)
            card.setVisibility(View.GONE);


    }

    private void showComponent(View component, boolean show) {
        if (show && component.getVisibility() == View.GONE)
            component.setVisibility(View.VISIBLE);

        if (!show && component.getVisibility() == View.VISIBLE)
            component.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //invoke necessary in presenter
        presenter.prepareForChangeState(outState);
    }


    @Override
    public void onChooseLanguageDialog(Language language,
                                       @LanguageDialogType.TypeLangDialog int typeDialog) {
        switch (typeDialog) {
            case LanguageDialogType.TYPE_FROM:
                presenter.setLangBtnFrom(language);
                break;
            case LanguageDialogType.TYPE_TO:
                presenter.setLangBtnTo(language);
                break;
        }
        presenter.loadTranslation(editTextToTranslate.getText().toString().trim());
    }

    @Override
    public void update() {
        presenter.checkFavoriteIs();
    }
}
