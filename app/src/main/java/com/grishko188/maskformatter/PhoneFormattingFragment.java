package com.grishko188.maskformatter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.grishko188.library.MaskFormatter;
import com.grishko188.library.MaskTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                .maskPrefix("+7 ")
                .ignoreInputPrefixes("7", "7 ", "+7", "+7 ") //this is best way to protect your input
                // from manually cleaning inside of the mask prefix, or copy/paste from buffer
                .mask("(###) ###-##-##");


        mTextInput.addTextChangedListener(new MaskTextWatcher(formatter));
        return view;
    }
}