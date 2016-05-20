package com.grishko188.maskformatter;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.grishko188.library.MaskFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 19.05.2016.
 */
public class StaticFormattingFragment extends Fragment {

    @BindView(R.id.mask_input)
    EditText mMaskInput;
    @BindView(R.id.text_input)
    EditText mTextInput;

    @BindView(R.id.formatted_text)
    TextView mFormattedText;
    @BindView(R.id.clean_text)
    TextView mCleanText;

    private MaskFormatter mFormatter = MaskFormatter.single();

    public static StaticFormattingFragment getInstance() {
        return new StaticFormattingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_static_formatting, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.confirm_mask)
    void onConfirmMask() {
        if (TextUtils.isEmpty(mMaskInput.getText())) {
            Toast.makeText(getActivity(), "Enter the mask!", Toast.LENGTH_SHORT).show();
        }
        mTextInput.setText(null);
        mFormatter.symbol(MaskFormatter.EMPTY)
                .mask(mMaskInput.getText().toString());
    }

    @OnClick(R.id.format)
    void onFormat() {
        if (TextUtils.isEmpty(mFormatter.getMask())) {
            Toast.makeText(getActivity(), "Enter the mask!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mTextInput.getText())) {
            Toast.makeText(getActivity(), "Enter the text", Toast.LENGTH_SHORT).show();
            return;
        }
        String formattedText = mFormatter.format(mTextInput.getText().toString());
        mFormattedText.setText(getString(R.string.formatted_label, formattedText));
        mCleanText.setText(getString(R.string.clean_label, mFormatter.clearStatic(formattedText)));
    }
}
