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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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


public class TranslatorFragment extends Fragment implements ITranslatorView {

    private static final String TAG = TranslatorFragment.class.getSimpleName();


    private TextView textTransResult;
    private TextView dictionaryTextHeader;
    private EditText editTextTranslate;
    private RecyclerView recyclerViewDictionary;
    private CheckBox btnAddFavorite;

    private int animationDuration;

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

        animationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attacheView(this);

        setupEditTextChangeListener();
        setupRecyclerViewAdapter();
        setupOnClickFavoriteBtn();

    }

    private void setupOnClickFavoriteBtn() {
        btnAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if (checkBox.isChecked()) {
                    Log.i(TAG, "presenter.addFavorite()");
                    presenter.addRemoveTranslationDb(false);
                }
                else {
                    Log.i(TAG, "presenter.removeFromFavorite()");
                    presenter.addRemoveTranslationDb(true);
                }
            }
        });
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
    }

    @Override
    public void showTranslationResult(InternalTranslation translation) {
//        crossFade();
        textTransResult.setText(translation.getTextTo());
        btnAddFavorite.setChecked(translation.isFavorite());
    }


    //TODO refactor this moment and add transcription
    @Override
    public void showTranslationOptions(List<Def> defList) {
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

    //use to animate view

//    private void crossFade(){
//        translationCard.setAlpha(0f);
//        translationCard.setVisibility(View.VISIBLE);
//
//        translationCard.animate()
//                .alpha(1f)
//                .setDuration(animationDuration)
//                .setListener(null);
//    }
}
