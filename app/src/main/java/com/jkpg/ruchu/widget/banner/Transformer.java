package com.jkpg.ruchu.widget.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.jkpg.ruchu.widget.banner.transformer.AccordionTransformer;
import com.jkpg.ruchu.widget.banner.transformer.BackgroundToForegroundTransformer;
import com.jkpg.ruchu.widget.banner.transformer.CubeInTransformer;
import com.jkpg.ruchu.widget.banner.transformer.CubeOutTransformer;
import com.jkpg.ruchu.widget.banner.transformer.DefaultTransformer;
import com.jkpg.ruchu.widget.banner.transformer.DepthPageTransformer;
import com.jkpg.ruchu.widget.banner.transformer.FlipHorizontalTransformer;
import com.jkpg.ruchu.widget.banner.transformer.FlipVerticalTransformer;
import com.jkpg.ruchu.widget.banner.transformer.ForegroundToBackgroundTransformer;
import com.jkpg.ruchu.widget.banner.transformer.RotateDownTransformer;
import com.jkpg.ruchu.widget.banner.transformer.RotateUpTransformer;
import com.jkpg.ruchu.widget.banner.transformer.ScaleInOutTransformer;
import com.jkpg.ruchu.widget.banner.transformer.StackTransformer;
import com.jkpg.ruchu.widget.banner.transformer.TabletTransformer;
import com.jkpg.ruchu.widget.banner.transformer.ZoomInTransformer;
import com.jkpg.ruchu.widget.banner.transformer.ZoomOutSlideTransformer;
import com.jkpg.ruchu.widget.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
