package com.example.kolin.testya.domain;

import com.example.kolin.testya.domain.model.InternalTranslation;

import io.reactivex.Observable;

/**
 * Created by kolin on 09.07.2017.
 */

public class GetNetTranslation extends BaseObservableUseCase<InternalTranslation, Void> {

    @Override
    public Observable<InternalTranslation> createObservable(Void aVoid) {
        return null;
    }
}
