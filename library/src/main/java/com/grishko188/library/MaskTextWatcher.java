package com.grishko188.library;

import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * Watches a {@link android.widget.TextView} and format input with the given mask and replacement char.
 * Formatting and clearing  presented by {@link MaskFormatter#format(String)} and {@link MaskFormatter#clear(String)}
 * <br/><br/> Usage: {@code mEditTex.addTextChangedListener(new MaskTextWatcher("№ ### ### ####"));}
 * <br/><br/>
 * Are allowed to enter only:
 * <ul>
 * <li>Letters</li>
 * <li>Digits</li>
 * </ul>
 * Known issues:
 * <ul>
 * <li>Do not use the mask type ####z ( any letter or number after replacement sequence without space after last replacement char)</li>
 * <li>Do not use the mask type #### #### #### z ( any characters after replacement sequence in the end of mask) with EditText formatting(you will have troubles with removing), but works fine for static formatting</li>
 * </ul>
 *
 * @author Grishko Nikita
 * on 12.05.2016.
 */
public class MaskTextWatcher implements TextWatcher {

    private MaskFormatter mFormatter;

    private boolean mSelfChange;

    private MaskFormatter.PrefixConfig mPrefixConfig;

    private static final Handler HANDLER = new Handler();

    private static final MaskInputFilter ALLOWED_CHARS_FILTER = new MaskInputFilter();

    public MaskTextWatcher(MaskFormatter formatter) {
        this.mFormatter = formatter;
        initPrefixConfiguration();
    }

    public MaskTextWatcher(String mask) {
        initFormatter(mask, MaskFormatter.EMPTY);
        initPrefixConfiguration();
    }

    public MaskTextWatcher(String mask, char symbol) {
        initFormatter(mask, symbol);
        initPrefixConfiguration();
    }

    private void initFormatter(String mask, char symbol) {
        mFormatter = MaskFormatter.get()
                .symbol(symbol)
                .mask(mask);
    }

    private void initPrefixConfiguration() {
        mPrefixConfig = mFormatter.getPrefixConfiguration();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (mSelfChange) {
            return;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mSelfChange) {
            return;
        }
    }

    @Override
    public synchronized void afterTextChanged(final Editable s) {
        if (mSelfChange) {
            if (!TextUtils.isEmpty(mPrefixConfig.getPrefix()) && mPrefixConfig.isNecessarily()) {
                if (s.toString().equalsIgnoreCase(mPrefixConfig.getPrefix())) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            Selection.setSelection(s, mPrefixConfig.getPrefix().length());
                        }
                    });
                }
            }
            s.setFilters(new InputFilter[]{ALLOWED_CHARS_FILTER});
            mSelfChange = false;
            return;
        }
        String cleanText = mFormatter.clear(s.toString());
        mSelfChange = true;
        String formattedText = mFormatter.format(cleanText);
        s.setFilters(new InputFilter[]{});
        s.replace(0, s.length(), formattedText);
    }

    private static class MaskInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (end != 0 && !Character.isLetterOrDigit(source.charAt(end - 1))) {
                return source.subSequence(start, end - 1);
            }
            return null;
        }
    }
}
