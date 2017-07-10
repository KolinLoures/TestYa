package com.example.kolin.testya.veiw.fragment;

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
import com.example.kolin.testya.veiw.DataUpdatable;
import com.example.kolin.testya.veiw.adapter.DictionaryAdapter;
import com.example.kolin.testya.veiw.adapter.SectionedDictionaryAdapter;
import com.example.kolin.testya.veiw.presenters.TranslatorPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class TranslatorFragment extends Fragment implements
        ITranslatorView,
        DataUpdatable<InternalTranslation>,
        LanguageDialogFragment.OnInteractionLanguageDialog {

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
    private ImageButton btnReverseLangs;
    private TextView textDeterminedLanguage;
    private ProgressBar loadingProgress;
    private View translationCard;
    private View dictionaryCard;
    private TextView textViewError;

    //Dialog
    private LanguageDialogFragment dialog;

    //Click listener
    private View.OnClickListener onClickListener;

    //Variable to block text watcher
    private boolean blockTextWatcher;

    //Adapters
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
        return inflater.inflate(R.layout.fragment_translator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextToTranslate = (EditText) view.findViewById(R.id.translator_edit_text);
        textTranslationResult = (TextView) view.findViewById(R.id.translation_card_text_result);
        recyclerViewDictionary = (RecyclerView) view.findViewById(R.id.dictionary_card_recycler_view);
        dictionaryTextHeader = (TextView) view.findViewById(R.id.dictionary_card_text_header);
        btnAddFavorite = (CheckBox) view.findViewById(R.id.translation_card_btn_favorite);
        btnClear = (ImageButton) view.findViewById(R.id.translation_clear_edit_btn);
        translationCard = view.findViewById(R.id.translation_card);
        dictionaryCard = view.findViewById(R.id.dictionary_card);
        btnFrom = (Button) view.findViewById(R.id.translation_btn_from);
        btnTo = (Button) view.findViewById(R.id.translation_btn_to);
        btnReverseLangs = (ImageButton) view.findViewById(R.id.translation_img_btn_reverse);
        textDeterminedLanguage = (TextView) view.findViewById(R.id.translation_text_determined_lang);
        loadingProgress = (ProgressBar) view.findViewById(R.id.translation_progress_downloading);
        textViewError = (TextView) view.findViewById(R.id.translation_error_text);


        dictionaryAdapter = new DictionaryAdapter();
        sectionedDictionaryAdapter = new SectionedDictionaryAdapter(getContext(), dictionaryAdapter);

        presenter.attachView(this);

//        if (savedInstanceState != null)
//            presenter.restoreStateData(savedInstanceState);
//        else
        presenter.loadSupportLanguages();


        setupEditTextChangeListener();
        setupRecyclerViewAdapter();
        initializeOnClickListener();

        //set click listeners
        btnClear.setOnClickListener(onClickListener);
        btnAddFavorite.setOnClickListener(onClickListener);
        btnTo.setOnClickListener(onClickListener);
        btnFrom.setOnClickListener(onClickListener);
        btnReverseLangs.setOnClickListener(onClickListener);
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
            //click check box favorite
            case R.id.translation_card_btn_favorite:
                CheckBox checkBox = (CheckBox) v;
                presenter.addRemoveTranslationDb(!checkBox.isChecked());
                break;
//            //click clear button
//            case R.id.translation_clear_edit_btn:
//                //clear all unnecessary texts
//                editTextToTranslate.getText().clear();
//                textTranslationResult.setText("");
//                //clear presenters data
//                presenter.clear();
//                presenter.clearDisposables();
//                //hide unnecessary part of layouts
//                showDetermineLang(false);
//                break;
//            //click button from choose language
//            case R.id.translation_btn_from:
//                //language from allow to determine lang
//                List<String> listLanguages = presenter.getListLanguages();
//                //do not allow to choose same languages
//                if (btnFrom.getText().toString().equals(getString(R.string.determine_language)))
//                    listLanguages.remove(btnTo.getText().toString());
//                dialog = LanguageDialogFragment.newInstance(listLanguages, true);
//                //show choose language dialog
//                dialog.show(getChildFragmentManager(), "language_dialog_fragment");
//                break;
//            //click button to choose language
//            case R.id.translation_btn_to:
//                dialog = LanguageDialogFragment.newInstance(presenter.getListLanguages(), false);
//                //show choose language dialog
//                dialog.show(getChildFragmentManager(), "language_dialog_fragment");
//                break;
//            //click reverse language button
//            case R.id.translation_img_btn_reverse:
//                //if reverse language between buttons success
//                if (reverseLanguages())
//                    //set to edit text Translated text
//                    editTextToTranslate.setText(textTranslationResult.getText());
//                //notify user that smth wrong
//                else
//                    notifyUser(getString(R.string.chose_language_from));
//                break;
        }
    }

    private boolean reverseLanguages() {
        //check code lang in presenter
//        String valueByKey = presenter.getCodeLang(btnFrom.getText().toString());
//        if (valueByKey != null) {
//            //reverse lang between buttons
//            String temp = btnTo.getText().toString();
//            setLanguagesToButtons(temp, btnFrom.getText().toString());
//            return true;
//        }
        return false;
    }

    private void setupRecyclerViewAdapter() {
        //set click listener to dictionary adapter
        dictionaryAdapter.setOnClickListener(new DictionaryAdapter.OnClickDictionaryAdapter() {
            @Override
            public void onClickItem(int position) {

                if (!reverseLanguages()) {
                    String lang = textDeterminedLanguage.getText().toString().split(" ")[0];
                    btnTo.setText(lang);
                }

                editTextToTranslate
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
        //set text watcher to edit text
        editTextToTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //check flag of blocking
                presenter.loadTranslation(s.toString().trim(), btnFrom.getText().toString(), btnTo.getText().toString(), false);

//                if (!blockTextWatcher) {
//                    String text = s.toString().trim();
//                    if (text.isEmpty()) {
//                        //set trans result if text is empty
//                        textTranslationResult.setText("");
//                    }
//                    //set visible clear button
//                    setVisibleClearBtn(!text.isEmpty());
//                    //execute translation in presenter
//                }
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
        textTranslationResult.setText(translation.getTextTo());
        btnAddFavorite.setChecked(translation.isFavorite());
    }


    //TODO refactor this moment and add transcription
    @Override
    public void showDictionary(List<Def> defList) {
        dictionaryAdapter.clearAdapter();
        dictionaryTextHeader.setText(defList.get(0).getText());
        List<SectionedDictionaryAdapter.Section> sections = new ArrayList<>();
        int position = 0;
        //add section to sectioned adapter (sections - part of speech)
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
        //set necessary lang to buttons
        if (langFrom == null)
            btnFrom.setText(getString(R.string.determine_language));
        else
            btnFrom.setText(langFrom);

        btnTo.setText(langTo);
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

    private void setVisibleClearBtn(boolean show) {
        showComponent(btnClear, show);
    }


    @Override
    public void update(boolean flag, InternalTranslation newData) {
        /*

        //flag - true was checked HistoryFavoriteFragment / false - was clicked HistoryFavoriteFragment

        //check presenter not null
        if (presenter == null) {
            return;
        }

        if (newData == null && btnAddFavorite.isChecked()) {
            btnAddFavorite.setChecked(false);
            return;
        }

        if (!flag && !presenter.equalsTranslationToCurrent(newData))
            return;

        //update current check box
        if (!flag && presenter.equalsTranslationToCurrent(newData)) {
            btnAddFavorite.setChecked(newData.isFavorite());
            return;
        }

        //set languages to butnLangs
        String[] pairLang = newData.getLang().split("-");
        String langFrom = pairLang[0];
        String langTo = pairLang[1];
        if (langFrom.equals(langTo))
            btnFrom.setText(getString(R.string.determine_language));
        else
            btnFrom.setText(presenter.getNameLang(langFrom));
        btnTo.setText(presenter.getNameLang(langTo));

        //set text edit
        blockTextWatcher = true;
        editTextToTranslate.setText(newData.getTextFrom());
        blockTextWatcher = false;

        //load translation with condition checking translation in data base
        presenter.loadTranslation(
                editTextToTranslate.getText().toString(),
                btnFrom.getText().toString(),
                btnTo.getText().toString(),
                true
        );

        */
    }

    @Override
    public void onChooseLanguageDialog(String lang, boolean isTextFrom) {
        //help gc
        dialog = null;
        //if dialog was invoke from btnFRom
        if (isTextFrom) {
            //reverse lang if they equals
            if (lang.equals(btnTo.getText()))
                btnTo.setText(btnFrom.getText());

            btnFrom.setText(lang);
        } else {
            //reverse lang if they equals
            if (lang.equals(btnFrom.getText()))
                btnFrom.setText(btnTo.getText());

            btnTo.setText(lang);
        }


        //load new translation with new langs
        presenter.loadTranslation(
                editTextToTranslate.getText().toString(),
                btnFrom.getText().toString(),
                btnTo.getText().toString(),
                false
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //invoke necessary in presenter
        presenter.prepareForChangeState(outState);
    }
}
