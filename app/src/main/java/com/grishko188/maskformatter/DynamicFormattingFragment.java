package com.grishko188.maskformatter;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.grishko188.library.MaskTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 19.05.2016.
 */
public class DynamicFormattingFragment extends Fragment {

    @BindView(R.id.mask_input)
    EditText mMaskInput;
    @BindView(R.id.text_input)
    EditText mTextInput;

    private MaskTextWatcher mTextWatcher;

    public static DynamicFormattingFragment getInstance() {
        return new DynamicFormattingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_formatting, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.confirm_mask)
    void onConfirmMask() {
        if (TextUtils.isEmpty(mMaskInput.getText())) {
            Toast.makeText(getActivity(), "Enter the mask!", Toast.LENGTH_SHORT).show();
        }
        mTextInput.setText(null);
        if (mTextWatcher != null)
            mTextInput.removeTextChangedListener(mTextWatcher);

        mTextInput.addTextChangedListener(mTextWatcher = new MaskTextWatcher(mMaskInput.getText().toString()));
    }

}
