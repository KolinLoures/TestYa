package com.example.kolin.testya.veiw.custom_views;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by kolin on 11.08.2017.
 */

public class CustomAppCompatEditText extends AppCompatEditText {

    private onKeyboardHideListener onKeyboardHideListener;

    public interface onKeyboardHideListener {
        void onKeyboardHidden();
    }

    public CustomAppCompatEditText(Context context) {
        super(context);
    }

    public CustomAppCompatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAppCompatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (onKeyboardHideListener != null)
                onKeyboardHideListener.onKeyboardHidden();
            this.clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setOnKeyBoardHideListener(onKeyboardHideListener onKeyboardHideListener) {
        this.onKeyboardHideListener = onKeyboardHideListener;
    }

    public void clearOnKeyBoardHideListener() {
        if (this.onKeyboardHideListener != null)
            this.onKeyboardHideListener = null;
    }
}
