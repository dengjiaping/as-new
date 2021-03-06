package com.jkpg.ruchu.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qindi on 2017/10/11.
 * TextView显示不同字体、不同颜色。
 *
 *
 * demo:
 * SpannableBuilder.create(this)
 .append("关联店铺", R.dimen.sp16, R.color.text_33)
 .append("(请添加您的所有店铺)", R.dimen.sp12, R.color.text_99)
 .build());

 */

public class SpannableBuilder {
    private Context context;
    private List<SpanWrapper> list;

    private SpannableBuilder(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public SpannableBuilder append(String text, int textSize, int textColor) {
        list.add(new SpanWrapper(text, textSize, textColor));
        return this;
    }

    public Spannable build() {
        SpannableStringBuilder textSpan = new SpannableStringBuilder();

        int start = 0;
        int end = 0;

        for (int i = 0; i < list.size(); i++) {
            SpanWrapper wrapper = list.get(i);
            String text = wrapper.getText();
            start = end;
            end = end + text.length();
            textSpan.append(text);

            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(getContext().getResources().getDimensionPixelSize(wrapper.getTextSize()));
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(UIUtils.getColor(wrapper.getTextColor()));

            textSpan.setSpan(sizeSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textSpan.setSpan(colorSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return textSpan;
    }

    public static SpannableBuilder create(Context context) {
        return new SpannableBuilder(context);
    }

    public Context getContext() {
        return context;
    }

    private class SpanWrapper {
        String text;
        int textSize;
        int textColor;

        public SpanWrapper(String text, int textSize, int textColor) {
            this.text = text;
            this.textSize = textSize;
            this.textColor = textColor;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }
    }

}
