package com.jkpg.ruchu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jkpg.ruchu.R;

/**
 * Created by qindi on 2017/9/12.
 */

public class ColorStyleTextView extends TextView {
    public void setDefaultTextValue(String defaultTextValue) {
        DEFAULT_TEXT_VALUE = defaultTextValue;
        addStyle();
        invalidate();
    }

    private static String DEFAULT_TEXT_VALUE = null;
    private static int DEFAULT_COLOR = Color.parseColor("#ff5070");
    private static int LAST_COLOR = Color.parseColor("#767676");
    private static boolean HAS_UNDER_LINE = false;


    private ClickCallBack mClickCallBack;

    public ColorStyleTextView(Context context) {
        this(context, null);
    }

    public ColorStyleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorStyleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorStyleTextView, defStyleAttr, 0);
        DEFAULT_TEXT_VALUE = typedArray.getString(R.styleable.ColorStyleTextView_AutoLinkStyleTextView_text_value);
        DEFAULT_COLOR = typedArray.getColor(R.styleable.ColorStyleTextView_AutoLinkStyleTextView_default_color, DEFAULT_COLOR);
        HAS_UNDER_LINE = typedArray.getBoolean(R.styleable.ColorStyleTextView_AutoLinkStyleTextView_has_under_line, HAS_UNDER_LINE);
        addStyle();
        typedArray.recycle();
    }

    private void addStyle() {
        if (!TextUtils.isEmpty(DEFAULT_TEXT_VALUE) && DEFAULT_TEXT_VALUE.contains(",")) {
            final String[] values = DEFAULT_TEXT_VALUE.split(",");
            SpannableString spannableString = new SpannableString(getText().toString().trim());
            for (int i = 0; i < values.length - 1; i++) {
                final int position = i;
                if (i == 1){

                    spannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            if (mClickCallBack != null) mClickCallBack.onClick(position);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(DEFAULT_COLOR);
                            ds.setUnderlineText(HAS_UNDER_LINE);
                        }
                    }, getText().toString().trim().indexOf(values[i],values[0].length() + 3), getText().toString().trim().indexOf(values[i],values[0].length() + 3) + values[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (mClickCallBack != null) mClickCallBack.onClick(position);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(DEFAULT_COLOR);
                        ds.setUnderlineText(HAS_UNDER_LINE);
                    }
                }, getText().toString().trim().indexOf(values[i]), getText().toString().trim().indexOf(values[i]) + values[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (mClickCallBack != null) mClickCallBack.onClick(values.length - 1);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(LAST_COLOR);
                    ds.setUnderlineText(HAS_UNDER_LINE);
                }
            }, getText().toString().trim().lastIndexOf(values[values.length - 1]), getText().toString().trim().lastIndexOf(values[values.length - 1]) + values[values.length - 1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannableString);
            setMovementMethod(LinkMovementMethod.getInstance());

        }
    }


    public void setOnClickCallBack(ClickCallBack clickCallBack) {
        this.mClickCallBack = clickCallBack;
    }

    public interface ClickCallBack {
        void onClick(int position);
    }
}
