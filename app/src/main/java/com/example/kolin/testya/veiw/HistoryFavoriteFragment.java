package com.example.kolin.testya.veiw;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.adapter.ViewPagerAdapter;
import com.example.kolin.testya.veiw.fragment.ClearDialogFragment;
import com.example.kolin.testya.veiw.fragment.DataUpdatable;
import com.example.kolin.testya.veiw.fragment.Updatable;
import com.example.kolin.testya.veiw.presenters.HistoryFavoritePresenter;


public class HistoryFavoriteFragment extends Fragment
        implements ICommonView, Updatable, ClearDialogFragment.ClearDialogListener {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ImageButton deleteBtn;

    private HistoryFavoritePresenter presenter;

    public HistoryFavoriteFragment() {
    }


    public static HistoryFavoriteFragment newInstance() {
        return new HistoryFavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new HistoryFavoritePresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history_favorite, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.history_favorite_view_pager);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), true);
        deleteBtn = (ImageButton) view.findViewById(R.id.fragment_history_favorite_delete);

        adapter.addFragment(CommonFragment.newInstance(), getString(R.string.history));

        adapter.addFragment(CommonFragment.newInstance(), getString(R.string.favorite));

        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) view.findViewById(R.id.history_favorite_tab);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attacheView(this);

        setupClickDeleteBtn();
    }

    private void setupClickDeleteBtn() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager childFragmentManager = getChildFragmentManager();
                ClearDialogFragment clearDialogFragment =
                        ClearDialogFragment.newInstance(
                                (String) adapter.getPageTitle(viewPager.getCurrentItem()));

                clearDialogFragment.show(childFragmentManager, "clear_dialog_fragment");
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


    @SuppressWarnings("unchecked")
    @Override
    public void updateLoadedData(InternalTranslation translation) {
//        if (translation.getType().equals(TypeSaveTranslation.HISTORY))
//            ((DataUpdatable) adapter.getItem(0)).update(translation);
//        else
//            ((DataUpdatable) adapter.getItem(1)).update(translation);

        ((DataUpdatable) adapter
                .getItem(TypeSaveTranslation.getTypeId(translation.getType()))).update(translation);

    }

    @Override
    public void clearViewPagerFragment(@TypeSaveTranslation.TypeName String type) {
        if (type == null) {
            ((DataUpdatable) adapter.getItem(0)).clear();
            ((DataUpdatable) adapter.getItem(1)).clear();
        } else ((DataUpdatable) adapter
                .getItem(TypeSaveTranslation.getTypeId(type))).clear();


//            if (type.equals(TypeSaveTranslation.HISTORY))
//            ((DataUpdatable) adapter.getItem(0)).clear();
//        else
//            ((DataUpdatable) adapter.getItem(1)).clear();
    }

    @Override
    public void check(InternalTranslation translation, boolean check) {
        presenter.addRemoveFavoriteTranslationDb(translation, check);
    }

    @Override
    public void update() {
        presenter.loadTranslationDb(null);
    }

    @Override
    public void onClickPositiveBtn() {
        presenter.deleteTranslationsByCategory(TypeSaveTranslation.getTypeById(viewPager.getCurrentItem()));
    }
}
