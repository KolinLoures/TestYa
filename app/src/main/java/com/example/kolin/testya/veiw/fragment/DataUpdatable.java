package com.example.kolin.testya.veiw.fragment;

/**
 * Created by kolin on 07.04.2017.
 */

public interface DataUpdatable<T> {
    void update(T newData);

    void clear();
}
