package com.example.kolin.testya.veiw.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.ITranslatorView;
import com.example.kolin.testya.veiw.adapter.DictionaryAdapter;
import com.example.kolin.testya.veiw.adapter.SectionedDictionaryAdapter;
import com.example.kolin.testya.veiw.presenters.TranslatorPresenter;

import java.util.ArrayList;
import java.util.List;


public class TranslatorFragment extends Fragment
        implements ITranslatorView, DataUpdatable<InternalTranslation> {

    private static final String TAG = TranslatorFragment.class.getSimpleName();


    private TextView textTransResult;
    private TextView dictionaryTextHeader;
    private EditText editTextTranslate;
    private RecyclerView recyclerViewDictionary;
    private CheckBox btnAddFavorite;
    private ImageButton btnClear;
    private View translationCard;
    private View dictionaryCard;

    private Animation animationFadeIn;
    private Animation animationFadeOut;

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

        animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attacheView(this);

        translationCard.setVisibility(View.INVISIBLE);
        dictionaryCard.setVisibility(View.INVISIBLE);

        setupEditTextChangeListener();
        setupRecyclerViewAdapter();
        initializeOnClickListener();
        setupAnimListener();

        btnClear.setOnClickListener(onClickListener);
        btnAddFavorite.setOnClickListener(onClickListener);

    }

    private void setupAnimListener() {
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
                translationCard.startAnimation(animationFadeOut);
                break;
        }
    }

    private void setupRecyclerViewAdapter() {
        dictionaryAdapter = new DictionaryAdapter();
        sectionedDictionaryAdapter = new SectionedDictionaryAdapter(getContext(), dictionaryAdapter);

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
                presenter.loadTranslation(s.toString().trim(), "en-ru");
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
    public void showTranslationCard(boolean show) {
        showCard(translationCard, show);
    }

    @Override
    public void showDictionaryCard(boolean show) {
//        dictionaryCard.clearAnimation();
//        showCard(dictionaryCard, show);
    }

    private void showCard(View card, boolean show){
        if (card == null)
            return;

        if (show) {
            if (card.getVisibility() == View.INVISIBLE) {
                card.startAnimation(animationFadeIn);
            }
            card.setVisibility(View.VISIBLE);
        } else {
            if (card.getVisibility() == View.VISIBLE) {
                card.startAnimation(animationFadeOut);
            }
            card.setVisibility(View.INVISIBLE);
        }

    }



    @Override
    public void update(InternalTranslation newData) {
        if (presenter.equalsTranslationToCurrent(newData)) {
            btnAddFavorite.setChecked(newData.isFavorite());
        } else {
            editTextTranslate.setText(newData.getTextFrom());
        }
    }

    @Override
    public void clear() {

    }
}
