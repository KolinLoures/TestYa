package com.example.kolin.testya.veiw;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.adapter.ViewPagerAdapter;
import com.example.kolin.testya.veiw.fragment.DataUpdatable;
import com.example.kolin.testya.veiw.fragment.OnClickCommonFragment;
import com.example.kolin.testya.veiw.fragment.Updatable;
import com.example.kolin.testya.veiw.presenters.CommonPresenter;


public class HistoryFavoriteFragment extends Fragment
        implements ICommonView, Updatable {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;

    private CommonPresenter presenter;

    public HistoryFavoriteFragment() {
    }

    public static HistoryFavoriteFragment newInstance() {
        return new HistoryFavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new CommonPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history_favorite, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.history_favorite_view_pager);
        adapter = new ViewPagerAdapter(getFragmentManager(), true);

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
    public void showLoadedData(InternalTranslation translation) {
        if (translation.getType().equals(TypeSaveTranslation.HISTORY))
            ((DataUpdatable) adapter.getItem(0)).update(translation);
        else
            ((DataUpdatable) adapter.getItem(1)).update(translation);
    }

    @Override
    public void clearAdapter() {
        ((DataUpdatable) adapter.getItem(0)).remove();
        ((DataUpdatable) adapter.getItem(1)).remove();
    }

    @Override
    public void update() {
        presenter.loadTranslationDb();
    }

    @Override
    public void remove(InternalTranslation translation, boolean check) {
        presenter.addRemoveFavoriteTranslationDb(translation, check);
    }
}
