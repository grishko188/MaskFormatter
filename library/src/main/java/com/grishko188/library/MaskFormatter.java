package com.grishko188.library;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formatter class that provides the ability to format the text for any arbitrarily given mask,
 * and to clean the filter.
 * <br/><br/>You can use only letters and numbers in the source string
 * <br/>Add mask as string, for example № ###-###-### (##), where '#' replacement char
 * <br/>If replacement char is not initialized manually, the formatter will find in your mask most frequently used symbol, and will consider it as a symbol to replace
 * *
 *
 * @author Grishko Nikita
 *         on 12.05.2016.
 */
public class MaskFormatter {

    public static final char DEFAULT_MASK_CHAR = '#';

    private static final String BRACKET_OPEN = "[";
    private static final String BRACKET_CLOSE = "]*";
    private static final String REG_EX_PART = "(\\w*\\d*)";
    public static final char EMPTY = '\u0000';

    private String mMask;
    private char mReplacementChar;
    private Pattern mRegex;
    private String[] mIgnorePrefix = null;
    private String mMaskPrefix;
    private boolean mIsMaskStrict = true;
    private int mMaskAvailableCharsCount;

    private static final MaskFormatter instance = new MaskFormatter();

    private MaskFormatter() {
    }

    /**
     * Create new instance of {@link MaskFormatter}
     */
    public static MaskFormatter get() {
        return new MaskFormatter();
    }

    /**
     * Returns single instance of {@link MaskFormatter}
     * Use only if you sure that formatting params would not change
     */
    public static MaskFormatter single() {
        return instance;
    }

    /**
     * Set the given string as format mask
     */
    public MaskFormatter mask(String mask) {
        this.mMask = mask;
        return this;
    }


    /**
     * Set the given string as mask prefix, to build complicated mask. Output will be look like this scheme
     * <br/> (mask_prefix)+(result of formatting)
     */
    public MaskFormatter maskPrefix(String prefix) {
        this.mMaskPrefix = prefix;
        return this;
    }

    /**
     * Change mask strict mode. If strict set to false and input string length is bigger then available replacement chars in mask, formatting will not be applied
     */
    public MaskFormatter strictMask(boolean isStrict) {
        this.mIsMaskStrict = isStrict;
        return this;
    }

    public String getMask() {
        return this.mMask;
    }

    public char getReplacementChar() {
        return mReplacementChar;
    }

    /**
     * Set an array of possible prefixes, which should be ignored while formatting. Generally created for phone formatter, to ignore country code or etc.
     * <pre>
     * {@code
     * MaskFormatter formatter = MaskFormatter.get().mask("+7 (###) ###-##-##")
     *                                          .ignoreInputPrefixes("+7", "7","8");
     *     assertEquals("+7 (930) 792-00-00", formatter.format(79307920000));
     * }
     * </pre>
     */

    public MaskFormatter ignoreInputPrefixes(String... prefix) {
        this.mIgnorePrefix = prefix;
        return this;
    }

    /**
     * Set the given char as replacement char.
     * <br/>If replacement char is not initialized , the formatter will find in your mask most frequently used symbol, and will consider it as a symbol to replace
     */
    public MaskFormatter symbol(char replacementChar) {
        this.mReplacementChar = replacementChar;
        return this;
    }

    /**
     * Returns clear string without formatting.
     * <br/>Based on RegExp which is built on the basis of the entered mask
     * <br/>
     * Known issue:
     * <ul>
     * <li>Do not use the mask type ####z ( any letter or number after replacement sequence without space after last replacement char)
     * <br/> If you need to format and clear such mask, use {@link MaskFormatter#clearStatic(String)} instead, but only for formatting static text (not allowed for user input formatting).</li>
     * </ul>
     */
    public String clear(String source) {

        if (TextUtils.isEmpty(mMask))
            return source;

        if (mReplacementChar == EMPTY)
            mReplacementChar = findMostPopularChar();

        if (TextUtils.isEmpty(source))
            return source;

        if (mMaskPrefix != null) {
            source = clearPrefixIfExist(source, mMaskPrefix);
        }

        Matcher matcher = getRegexPattern().matcher(source);

        if (!matcher.matches()) {
            return source;
        }
        StringBuilder cleanTextBuilder = new StringBuilder();

        for (int i = 1; i <= matcher.groupCount(); i++) {
            cleanTextBuilder.append(matcher.group(i));
        }

        return cleanTextBuilder.toString();
    }

    /**
     * Returns clear string without formatting.
     * <br/>
     * Known issue:
     * <ul>
     * <li>Do not use for user input formatting, only static text
     * </ul>
     */
    public String clearStatic(String source) {

        if (TextUtils.isEmpty(mMask))
            return source;

        if (mReplacementChar == EMPTY)
            mReplacementChar = findMostPopularChar();

        if (TextUtils.isEmpty(source))
            return source;

        if (mMaskPrefix != null) {
            source = clearPrefixIfExist(source, mMaskPrefix);
        }

        Matcher matcher = getRegexPattern().matcher(source);

        if (!matcher.matches()) {
            return source;
        }

        StringBuilder cleanTextBuilder = new StringBuilder();
        char[] mask = mMask.toCharArray();
        char[] sourceChars = source.toCharArray();

        int length = mask.length > sourceChars.length ? sourceChars.length : mask.length;
        for (int i = 0; i < length; i++) {
            if (mask[i] == mReplacementChar) {
                cleanTextBuilder.append(sourceChars[i]);
            }
        }
        return cleanTextBuilder.toString();
    }

    /**
     * Format source string with given mask.
     * <br/>All not letters and digits chars will be escaped
     * * Known issue:
     * <ul>
     * <li>Do not use the mask type #### #### #### z ( any characters after replacement sequence in the end of mask) with EditText formatting(you will have troubles with removing), but works fine for static formatting</li>
     * <br/>
     * </ul>
     */
    public String format(String source) {

        if (TextUtils.isEmpty(mMask))
            return source;

        if (mReplacementChar == EMPTY)
            mReplacementChar = findMostPopularChar();

        if (TextUtils.isEmpty(source))
            return source;

        if (source.length() > mMaskAvailableCharsCount && !mIsMaskStrict)
            return source;

        if (mIgnorePrefix != null && mIgnorePrefix.length > 0) {
            for (String prefixToCheck : mIgnorePrefix) {
                if (source.startsWith(prefixToCheck)) {
                    source = clearPrefix(source, prefixToCheck);
                    break;
                }
            }
        }

        String fixedSource = escapeIncorrectSymbols(source);
        int indexInSource = 0;

        char[] mask = mMask.toCharArray();
        char[] charsOfSource = fixedSource.toCharArray();

        int resultLength = mask.length;
        char[] result = new char[resultLength];

        for (int i = 0; i < resultLength; i++) {
            if (indexInSource >= charsOfSource.length) {

                if (countNotEmptyLength(result) < mask.length) {

                    boolean hasMoreReplacementChars = false;
                    for (int j = i; j < result.length; j++) {
                        if (mask[j] == mReplacementChar) {
                            hasMoreReplacementChars = true;
                            break;
                        }
                    }
                    //situation when chars in source finished, but mask still have some chars to append
                    if (!hasMoreReplacementChars) {
                        for (int j = i; j < result.length; j++) {
                            result[j] = mask[j];
                        }
                    }
                }
                break;
            }
            if (mask[i] == mReplacementChar) {
                result[i] = charsOfSource[indexInSource];
                indexInSource++;
            } else {
                result[i] = mask[i];
            }
        }
        String resultString = String.valueOf(trimEmptyElements(result));

        if (mMaskPrefix != null && !resultString.startsWith(mMaskPrefix) && !mMask.startsWith(mMaskPrefix)) {
            resultString = mMaskPrefix + resultString;
        }

        return resultString;
    }

    private String clearPrefix(@NonNull String source, @NonNull String prefix) {
        int indexOf = source.indexOf(prefix);
        if (indexOf != -1) {
            source = source.substring(indexOf + prefix.length(), source.length());
        }
        return source;
    }

    private String clearPrefixIfExist(@NonNull String source, @NonNull String prefix) {

        if (source.length() < prefix.length() && prefix.startsWith(source))
            return "";

        if (source.startsWith(prefix)) {
            source = clearPrefix(source, prefix);
        }
        return source;
    }

    /**
     * Build RegExp based on mask
     */
    private Pattern getRegexPattern() {
        if (mRegex == null) {
            StringBuilder regEx = new StringBuilder();
            regEx.append('^');
            char[] maskChars = mMask.toCharArray();
            boolean isReplacementSequence = false;
            for (char symbol : maskChars) {
                if (symbol != mReplacementChar) {
                    if (isReplacementSequence) {
                        regEx.append(REG_EX_PART);
                        isReplacementSequence = false;
                    }
                    regEx.append(BRACKET_OPEN);
                    regEx.append(symbol);
                    regEx.append(BRACKET_CLOSE);
                } else {
                    isReplacementSequence = true;
                }
            }
            if (isReplacementSequence) {
                regEx.append(REG_EX_PART);
            }
            regEx.append('$');
            mRegex = Pattern.compile(regEx.toString());
        }
        return mRegex;
    }


    private char[] trimEmptyElements(char[] chars) {
        int notEmptyCharsCount = 0;
        for (char element : chars) {
            if (element == EMPTY)
                break;
            notEmptyCharsCount++;
        }
        return Arrays.copyOfRange(chars, 0, notEmptyCharsCount);
    }

    private int countNotEmptyLength(char[] array) {
        int counter = 0;
        for (char element : array) {
            if (element != EMPTY)
                counter++;
        }
        return counter;
    }

    private String escapeIncorrectSymbols(String source) {
        StringBuilder correctString = new StringBuilder();
        for (char symbol : source.toCharArray()) {
            if (Character.isLetterOrDigit(symbol)) {
                correctString.append(symbol);
            }
        }
        return correctString.toString();
    }

    private char findMostPopularChar() {
        Map<Character, AtomicInteger> characters2CountMap = new HashMap<>();

        for (char element : mMask.toCharArray()) {
            if (characters2CountMap.containsKey(element)) {
                characters2CountMap.get(element).incrementAndGet();
            } else {
                AtomicInteger counter = new AtomicInteger();
                counter.set(1);
                characters2CountMap.put(element, counter);
            }
        }

        char mostPopularChar = DEFAULT_MASK_CHAR;
        int biggestCounter = 0;

        for (Map.Entry<Character, AtomicInteger> entry : characters2CountMap.entrySet()) {
            if (entry.getValue().get() > biggestCounter) {
                biggestCounter = entry.getValue().get();
                mostPopularChar = entry.getKey();
                mMaskAvailableCharsCount = biggestCounter;
            }
        }
        return mostPopularChar;
    }

}