package com.example.kolin.testya.veiw.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.kolin.testya.R;

public class ClearDialogFragment extends DialogFragment {

    private static final String KEY_TITLE = "title";

    private TextView textTitle;
    private TextView textContent;

    private Button btnCancel;
    private Button btnYes;

    private View.OnClickListener onClickListener;

    public ClearDialogFragment() {
        // Required empty public constructor
    }

    public static ClearDialogFragment newInstance(String title) {
        ClearDialogFragment fragment = new ClearDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public interface ClearDialogListener {
        void onClickPositiveBtn();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clear_dialog, container, false);

        textTitle = (TextView) view.findViewById(R.id.dialog_text_title);
        textContent = (TextView) view.findViewById(R.id.dialog_text_content);

        btnCancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
        btnYes = (Button) view.findViewById(R.id.dialog_btn_yes);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString(KEY_TITLE);

        textTitle.setText(title);
        textContent.setText(String.format("%s \"%s\" ?", getString(R.string.dialog_question), title));

        initializeListener();
        btnCancel.setOnClickListener(onClickListener);
        btnYes.setOnClickListener(onClickListener);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        return dialog;
    }

    private void initializeListener() {
        if (onClickListener == null)
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performClick(v);
                }
            };
    }

    private void performClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_cancel:
                dismiss();
                break;
            case R.id.dialog_btn_yes:
                Fragment parentFragment = getParentFragment();
                if (parentFragment != null && parentFragment instanceof ClearDialogListener){
                    ((ClearDialogListener) parentFragment).onClickPositiveBtn();
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onClickListener = null;
    }
}
