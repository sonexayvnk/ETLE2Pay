package com.etl.money;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by Khem on 5/20/2017.
 */

public class NumberTextWatcher implements TextWatcher {


    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private EditText et;

    public NumberTextWatcher(EditText et) {
        df = new DecimalFormat("#,###");
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###");
        this.et = et;
        hasFractionalPart = false;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = df.parse(v);
            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                et.setText(df.format(n));
            } else {
                et.setText(dfnd.format(n));
            }
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        }

        et.addTextChangedListener(this);
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }


}

//    private final DecimalFormat df;
//    private final DecimalFormat dfnd;
//    private final EditText et;
//    private boolean hasFractionalPart;
//    private int trailingZeroCount;
//
//    public NumberTextWatcher(EditText editText, String pattern) {
//        df = new DecimalFormat(pattern);
//        df.setDecimalSeparatorAlwaysShown(true);
//        dfnd = new DecimalFormat("#,###.00");
//        //dfnd = new DecimalFormat("#,###");
//        this.et = editText;
//        hasFractionalPart = false;
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        et.removeTextChangedListener(this);
//
//        if (s != null && !s.toString().isEmpty()) {
//            try {
//                int inilen, endlen;
//                inilen = et.getText().length();
//                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("$","");
//                Number n = df.parse(v);
//                int cp = et.getSelectionStart();
//                if (hasFractionalPart) {
//                    StringBuilder trailingZeros = new StringBuilder();
//                    while (trailingZeroCount-- > 0)
//                        trailingZeros.append('0');
//                    et.setText(df.format(n) + trailingZeros.toString());
//                } else {
//                    et.setText(dfnd.format(n));
//                }
//               // et.setText("$".concat(et.getText().toString()));
//                et.setText("".concat(et.getText().toString()));
//                endlen = et.getText().length();
//                int sel = (cp + (endlen - inilen));
//                if (sel > 0 && sel < et.getText().length()) {
//                    et.setSelection(sel);
//                } else if (trailingZeroCount > -1) {
//
//                    et.setSelection(et.getText().length() - 3);
//
//                } else {
//                    et.setSelection(et.getText().length());
//                }
//            } catch (NumberFormatException | ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        et.addTextChangedListener((TextWatcher) this);
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        int index = s.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
//        trailingZeroCount = 0;
//        if (index > -1) {
//            for (index++; index < s.length(); index++) {
//                if (s.charAt(index) == '0')
//                    trailingZeroCount++;
//                else {
//                    trailingZeroCount = 0;
//                }
//            }
//            hasFractionalPart = true;
//        } else {
//            hasFractionalPart = false;
//        }
//    }
//}