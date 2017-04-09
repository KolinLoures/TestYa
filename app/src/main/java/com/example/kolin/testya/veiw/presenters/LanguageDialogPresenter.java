package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.example.kolin.testya.domain.GetLanguages;
import com.example.kolin.testya.veiw.fragment.LanguageDialogFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 09.04.2017.
 */

public class LanguageDialogPresenter extends BasePresenter<LanguageDialogFragment> {

    private static final String TAG = LanguageDialogPresenter.class.getSimpleName();

    private GetLanguages getLanguages;

    @Override
    public void attacheView(@NonNull LanguageDialogFragment view) {
        super.attacheView(view);

        getLanguages = new GetLanguages(view.getContext());
        loadSupportLanguages();
    }

    public void loadSupportLanguages(){
        getLanguages.execute(new LanguageObserver(),
                GetLanguages.GetLanguageParams.getParamsObj(true));
    }


    private void showLanguages(ArrayMap<String, String> languages){
        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        getAttachView().showLanguages(new ArrayList<>(languages.values()));
    }

    private final class LanguageObserver extends DisposableObserver<ArrayMap<String, String>> {

        @Override
        public void onNext(ArrayMap<String, String> stringStringArrayMap) {
            showLanguages(stringStringArrayMap);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "LanguageObserver" + e.toString());
        }

        @Override
        public void onComplete() {

        }
    }
}
