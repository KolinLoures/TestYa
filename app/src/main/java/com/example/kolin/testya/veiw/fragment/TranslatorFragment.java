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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.ITranslatorView;
import com.example.kolin.testya.veiw.adapter.DictionaryAdapter;
import com.example.kolin.testya.veiw.adapter.SectionedDictionaryAdapter;
import com.example.kolin.testya.veiw.presenters.TranslatorPresenter;

import java.util.ArrayList;
import java.util.List;


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


    private DictionaryAdapter dictionaryAdapter;
    private SectionedDictionaryAdapter sectionedDictionaryAdapter;

    private TranslatorPresenter presenter;

    public TranslatorFragment() {
        // Required empty public constructor
    }


    public static TranslatorFragment newInstance() {
        return new TranslatorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new TranslatorPresenter();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attacheView(this);

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
                presenter.clearDisposables();
                editTextTranslate.getText().clear();
                showDetermineLang(false);
                break;
            case R.id.translation_btn_from:
                dialog = LanguageDialogFragment.newInstance(presenter.getListLanguages(), true);
                dialog.show(getChildFragmentManager(), "language_dialog_fragment");
                break;
            case R.id.translation_btn_to:
                dialog = LanguageDialogFragment.newInstance(presenter.getListLanguages(), false);
                dialog.show(getChildFragmentManager(), "language_dialog_fragment");
                break;
            case R.id.translation_img_btn_reverse:
                if (reverseLanguages())
                    editTextTranslate.setText(textTransResult.getText());
                break;
        }
    }

    private boolean reverseLanguages() {
        String valuerByKey = presenter.getCodeLang(btnFrom.getText().toString());
        if (valuerByKey != null) {
            String temp = btnTo.getText().toString();
            btnTo.setText(btnFrom.getText());
            btnFrom.setText(temp);
            return true;
        } else
            notifyUser(getString(R.string.chose_language_from));
        return false;
    }

    private void setupRecyclerViewAdapter() {
        dictionaryAdapter = new DictionaryAdapter();
        sectionedDictionaryAdapter = new SectionedDictionaryAdapter(getContext(), dictionaryAdapter);

        dictionaryAdapter.setOnClickListener(new DictionaryAdapter.OnClickDictionaryAdapter() {
            @Override
            public void onClickItem(int position) {
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
                String text = s.toString().trim();
                setVisibleClearBtn(!text.isEmpty());
                presenter.loadTranslation(text, btnFrom.getText().toString(), btnTo.getText().toString());
            }
        });

        editTextTranslate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    editTextTranslate.clearFocus();

                    InputMethodManager imm =
                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextTranslate.getWindowToken(), 0);
                }
                return false;
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        sectionedDictionaryAdapter = null;
        dictionaryAdapter = null;
        onClickListener = null;
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
                langFrom, getString(R.string.determine)));
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

    private void showAnimCard(View card, boolean show) {
        if (card == null)
            return;

        if (show && card.getVisibility() == View.INVISIBLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(card, View.ALPHA, 0.0f, 1.0f);
            animator.setDuration(500);
            animator.start();

            card.setVisibility(View.VISIBLE);
        }

        if (!show && card.getVisibility() == View.VISIBLE)
            card.setVisibility(View.INVISIBLE);


    }

    private void showComponent(View component, boolean show) {
        if (show && component.getVisibility() == View.INVISIBLE)
            component.setVisibility(View.VISIBLE);

        if (!show && component.getVisibility() == View.VISIBLE)
            component.setVisibility(View.INVISIBLE);
    }

    private void setVisibleClearBtn(boolean show) {
        if (show) {
            btnClear.setVisibility(View.VISIBLE);
        } else {
            btnClear.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void update(Pair<Boolean, InternalTranslation> pair) {
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
            btnFrom.setText(getString(R.string.determine));
        else
            btnFrom.setText(presenter.getNameLang(langFrom));

        btnTo.setText(presenter.getNameLang(langTo));
        editTextTranslate.setText(pair.second.getTextFrom());
    }

    @Override
    public void clear() {
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
                btnTo.getText().toString()
        );
    }
}
