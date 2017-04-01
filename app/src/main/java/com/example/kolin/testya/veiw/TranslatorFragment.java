package com.example.kolin.testya.veiw;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.models.dictionary.Def;
import com.example.kolin.testya.veiw.adapter.DictionaryAdapter;
import com.example.kolin.testya.veiw.adapter.SectionedDictionaryAdapter;
import com.example.kolin.testya.veiw.presenters.TranslatorPresenter;

import java.util.List;


public class TranslatorFragment extends Fragment implements ITranslatorView {


    private EditText editTextTranslate;
    private TextView textTransResult;
    private RecyclerView recyclerViewDictinary;

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
        recyclerViewDictinary = (RecyclerView) view.findViewById(R.id.dictionary_card_recycler_view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attacheView(this);

        setupEditTextChangeListener();
        setupRecyclerViewAdapter();
    }

    private void setupRecyclerViewAdapter() {
        dictionaryAdapter = new DictionaryAdapter();
        sectionedDictionaryAdapter = new SectionedDictionaryAdapter(getContext(), dictionaryAdapter);
    }

    private void setupEditTextChangeListener() {
        editTextTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.loadTranslation(s.toString(), "en-ru");
            }

            @Override
            public void afterTextChanged(Editable s) {
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
    public void showTranslationResult(String translation) {
        textTransResult.setText(translation);
    }

    @Override
    public void showTranslationOptions(List<Def> defList) {

    }
}
