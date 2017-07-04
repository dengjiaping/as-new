package com.jkpg.ruchu.widget.flexbox.interfaces;

import java.util.List;

public interface OnFlexboxSubscribeListener<T> {

    /**
     * @param selectedItem 已选中的标签
     */
    void onSubscribe(List<T> selectedItem);
}
