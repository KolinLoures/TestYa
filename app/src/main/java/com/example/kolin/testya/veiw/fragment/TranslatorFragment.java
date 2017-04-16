package com.example.kolin.testya.veiw.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
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
import com.example.kolin.testya.veiw.ITranslatorView;
import com.example.kolin.testya.veiw.adapter.DictionaryAdapter;
import com.example.kolin.testya.veiw.adapter.SectionedDictionaryAdapter;
import com.example.kolin.testya.veiw.presenters.TranslatorPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class TranslatorFragment extends Fragment implements
        ITranslatorView,
        DataUpdatable<Pair<Boolean, InternalTranslation>>,
        LanguageDialogFragment.OnIteractionLanguageDialog {

    private static final String TAG = TranslatorFragment.class.getSimpleName();


    private TextView textTransResult;
    private TextView dictionaryTextHeader;
    private EditText editTextTranslate;
    private RecyclerView recyclerViewDictionary;
    private CheckBox btnAddFavorite;
    private ImageButton btnClear;
    private Button btnFrom;
    private Button btnTo;
    private ImageButton imBtmReverse;
    private TextView textDeterminedLanguage;
    private ProgressBar loadingProgress;
    private View translationCard;
    private View dictionaryCard;
    private TextView textViewError;

    private LanguageDialogFragment dialog;

    private View.OnClickListener onClickListener;

    private boolean blockTextWatcher;

    private DictionaryAdapter dictionaryAdapter;
    private SectionedDictionaryAdapter sectionedDictionaryAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translator, container, false);


        editTextTranslate = (EditText) view.findViewById(R.id.translator_edit_text);
        textTransResult = (TextView) view.findViewById(R.id.translation_card_text_result);
        recyclerViewDictionary = (RecyclerView) view.findViewById(R.id.dictionary_card_recycler_view);
        dictionaryTextHeader = (TextView) view.findViewById(R.id.dictionary_card_text_header);
        btnAddFavorite = (CheckBox) view.findViewById(R.id.translation_card_btn_favorite);
        btnClear = (ImageButton) view.findViewById(R.id.translation_clear_edit_btn);
        translationCard = view.findViewById(R.id.translation_card);
        dictionaryCard = view.findViewById(R.id.dictionary_card);
        btnFrom = (Button) view.findViewById(R.id.translation_btn_from);
        btnTo = (Button) view.findViewById(R.id.translation_btn_to);
        imBtmReverse = (ImageButton) view.findViewById(R.id.translation_img_btn_reverse);
        textDeterminedLanguage = (TextView) view.findViewById(R.id.translation_text_determined_lang);
        loadingProgress = (ProgressBar) view.findViewById(R.id.translation_progress_downloading);
        textViewError = (TextView) view.findViewById(R.id.translation_error_text);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dictionaryAdapter = new DictionaryAdapter();
        sectionedDictionaryAdapter = new SectionedDictionaryAdapter(getContext(), dictionaryAdapter);

        presenter.attacheView(this);

        if (savedInstanceState != null)
            presenter.restoreStateData(savedInstanceState);
        else
            presenter.loadSupportLanguages();


        setupEditTextChangeListener();
        setupRecyclerViewAdapter();
        initializeOnClickListener();

        btnClear.setOnClickListener(onClickListener);
        btnAddFavorite.setOnClickListener(onClickListener);
        btnTo.setOnClickListener(onClickListener);
        btnFrom.setOnClickListener(onClickListener);
        imBtmReverse.setOnClickListener(onClickListener);
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
            case R.id.translation_card_btn_favorite:
                CheckBox checkBox = (CheckBox) v;
                presenter.addRemoveTranslationDb(!checkBox.isChecked());
                break;
            case R.id.translation_clear_edit_btn:
                editTextTranslate.getText().clear();
                textTransResult.setText("");
                presenter.clear();
                presenter.clearDisposables();
                showDetermineLang(false);
                break;
            case R.id.translation_btn_from:
                List<String> listLanguages = presenter.getListLanguages();
                if (btnFrom.getText().toString().equals(getString(R.string.determine_language)))
                    listLanguages.remove(btnTo.getText().toString());
                dialog = LanguageDialogFragment.newInstance(listLanguages, true);
                dialog.show(getChildFragmentManager(), "language_dialog_fragment");
                break;
            case R.id.translation_btn_to:
                dialog = LanguageDialogFragment.newInstance(presenter.getListLanguages(), false);
                dialog.show(getChildFragmentManager(), "language_dialog_fragment");
                break;
            case R.id.translation_img_btn_reverse:
                if (reverseLanguages())
                    editTextTranslate.setText(textTransResult.getText());
                else
                    notifyUser(getString(R.string.chose_language_from));
                break;
        }
    }

    private boolean reverseLanguages() {
        String valueByKey = presenter.getCodeLang(btnFrom.getText().toString());
        if (valueByKey != null) {
            String temp = btnTo.getText().toString();
            setLanguagesToButtons(temp, btnFrom.getText().toString());
            return true;
        }
        return false;
    }

    private void setupRecyclerViewAdapter() {
        dictionaryAdapter.setOnClickListener(new DictionaryAdapter.OnClickDictionaryAdapter() {
            @Override
            public void onClickItem(int position) {

                if (!reverseLanguages()) {
                    String lang = textDeterminedLanguage.getText().toString().split(" ")[0];
                    btnTo.setText(lang);
                }

                editTextTranslate
                        .setText(
                                dictionaryAdapter.getDataAtPosition(
                                        sectionedDictionaryAdapter.sectionedPositionToPosition(position)
                                ).getText());
            }
        });

        recyclerViewDictionary.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDictionary.setAdapter(sectionedDictionaryAdapter);


    }

    private void setupEditTextChangeListener() {
        editTextTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!blockTextWatcher) {
                    String text = s.toString().trim();
                    if (text.isEmpty()) {
                        textTransResult.setText("");
                    }
                    setVisibleClearBtn(!text.isEmpty());
                    presenter.loadTranslation(text, btnFrom.getText().toString(), btnTo.getText().toString(), false);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.detachView();
        sectionedDictionaryAdapter = null;
        dictionaryAdapter = null;
        onClickListener = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();


    }

    @Override
    public void showTranslationResult(InternalTranslation translation) {
        textTransResult.setText(translation.getTextTo());
        btnAddFavorite.setChecked(translation.isFavorite());
    }


    //TODO refactor this moment and add transcription
    @Override
    public void showDictionary(List<Def> defList) {
        dictionaryAdapter.clearAdapter();
        dictionaryTextHeader.setText(defList.get(0).getText());
        List<SectionedDictionaryAdapter.Section> sections = new ArrayList<>();
        int position = 0;
        for (Def def : defList) {
            sections.add(new SectionedDictionaryAdapter.Section(position, def.getPos()));
            position += def.getTr().size();
        }
        for (Def def : defList)
            dictionaryAdapter.addDataList(def.getTr());
        sectionedDictionaryAdapter.setSections(sections);
    }

    @Override
    public void setDetermineLanguage(String langFrom) {
        textDeterminedLanguage.setText(String.format("%s (%s)",
                langFrom, getString(R.string.determined)));
    }

    @Override
    public void notifyUser(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTranslationCard(boolean show) {
        showAnimCard(translationCard, show);
    }

    @Override
    public void showDictionaryCard(boolean show) {
        showAnimCard(dictionaryCard, show);
    }

    @Override
    public void showLoadingProgress(boolean show) {
        showComponent(loadingProgress, show);
    }

    @Override
    public void showError(boolean show) {
        showComponent(textViewError, show);
    }

    @Override
    public void showDetermineLang(boolean show) {
        showComponent(textDeterminedLanguage, show);
    }

    @Override
    public void setLanguagesToButtons(String langFrom, String langTo) {
        if (langFrom == null)
            btnFrom.setText(getString(R.string.determine_language));
        else
            btnFrom.setText(langFrom);

        btnTo.setText(langTo);
    }

    private void showAnimCard(View card, boolean show) {
        if (card == null)
            return;

        if (show && card.getVisibility() == View.GONE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(card, View.ALPHA, 0.0f, 1.0f);
            animator.setDuration(500);
            animator.start();

            card.setVisibility(View.VISIBLE);
        }

        if (!show && card.getVisibility() == View.VISIBLE)
            card.setVisibility(View.GONE);


    }

    private void showComponent(View component, boolean show) {
        if (show && component.getVisibility() == View.GONE)
            component.setVisibility(View.VISIBLE);

        if (!show && component.getVisibility() == View.VISIBLE)
            component.setVisibility(View.GONE);
    }

    private void setVisibleClearBtn(boolean show) {
        showComponent(btnClear, show);
    }


    @Override
    public void update(Pair<Boolean, InternalTranslation> pair) {

        if (presenter == null)
            return;

        if (!pair.first && !presenter.equalsTranslationToCurrent(pair.second))
            return;

        if (!pair.first && presenter.equalsTranslationToCurrent(pair.second)) {
            btnAddFavorite.setChecked(pair.second.isFavorite());
            return;
        }

        String[] pairLang = pair.second.getLang().split("-");
        String langFrom = pairLang[0];
        String langTo = pairLang[1];

        if (langFrom.equals(langTo))
            btnFrom.setText(getString(R.string.determine_language));
        else
            btnFrom.setText(presenter.getNameLang(langFrom));

        btnTo.setText(presenter.getNameLang(langTo));

        blockTextWatcher = true;
        editTextTranslate.setText(pair.second.getTextFrom());
        blockTextWatcher = false;

        presenter.loadTranslation(
                editTextTranslate.getText().toString(),
                btnFrom.getText().toString(),
                btnTo.getText().toString(),
                true
        );
    }

    @Override
    public void clear() {
        //stub
    }

    @Override
    public void onChooseLanguageDialog(String lang, boolean isTextFrom) {
        dialog = null;
        if (isTextFrom) {
            if (lang.equals(btnTo.getText()))
                btnTo.setText(btnFrom.getText());

            btnFrom.setText(lang);
        } else {
            if (lang.equals(btnFrom.getText()))
                btnFrom.setText(btnTo.getText());

            btnTo.setText(lang);
        }


        presenter.loadTranslation(
                editTextTranslate.getText().toString(),
                btnFrom.getText().toString(),
                btnTo.getText().toString(),
                false
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        presenter.prepareForChangeState(outState);
    }
}
