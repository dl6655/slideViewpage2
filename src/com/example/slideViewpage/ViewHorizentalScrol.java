package com.example.slideViewpage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by dingli on 2015-8-25.
 */
public class ViewHorizentalScrol extends HorizontalScrollView {
    public ViewHorizentalScrol(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        initView(context);
    }

    private void initView(Context cx) {

    }

    public ViewHorizentalScrol(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    private ViewPager mViewPager;

    public void setViewPager(ViewPager vp){
        mViewPager=vp;
    }

}
