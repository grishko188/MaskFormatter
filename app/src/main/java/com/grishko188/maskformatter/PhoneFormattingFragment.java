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

import com.grishko188.library.MaskFormatter;
import com.grishko188.library.MaskTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by UnrealMojo on 06.11.17.
 *
 * @author Grishko Nikita
 */

public class PhoneFormattingFragment extends Fragment {

    @BindView(R.id.text_input)
    EditText mTextInput;

    public static PhoneFormattingFragment getInstance() {
        return new PhoneFormattingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_formatting, null);
        ButterKnife.bind(this, view);
        MaskFormatter formatter = MaskFormatter.get()
                .mask("+7 (###) ###-##-##")
                .ignorePrefix("+7", "7", "8");

        mTextInput.addTextChangedListener(new MaskTextWatcher(formatter));
        return view;
    }
}