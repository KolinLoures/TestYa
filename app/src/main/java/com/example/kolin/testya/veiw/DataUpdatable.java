package com.example.kolin.testya.veiw;

/**
 * Created by kolin on 07.04.2017.
 *
 * Interface for update data fragment from activity.
 *
 * @param <T> Type of data.
 */
public interface DataUpdatable<T> {


    /**
     * Updates data
     *
     * @param flag boolean flag
     * @param newData data
     */
    void update(boolean flag, T newData);
}
