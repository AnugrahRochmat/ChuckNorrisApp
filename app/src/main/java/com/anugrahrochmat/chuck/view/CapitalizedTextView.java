package com.anugrahrochmat.chuck.view;

import android.content.Context;
import android.util.AttributeSet;

public class CapitalizedTextView extends android.support.v7.widget.AppCompatTextView {
    public CapitalizedTextView(Context context) {
        super(context);
    }

    public CapitalizedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CapitalizedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text.length() > 0) {
            text = String.valueOf(text.charAt(0)).toUpperCase() + text.subSequence(1, text.length());
        }
        super.setText(text, type);
    }
}
