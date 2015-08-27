package com.example.slideViewpage;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewPageActivity extends FragmentActivity {
  private ViewPager mViewPager;
   private ArrayList<ListView> arrayList=new ArrayList<>();
    private int mWidth;
    private HorizontalScrollView mHsv;
    private LinearLayout tabsContainer;
    private int lastScrollX = 0;
    private int scrollOffset =20;
    String[] mTitle;
    private LinearLayout.LayoutParams defaultParams;
    private int tabPadding = 50;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mHsv=getViewById(R.id.hsv);
        tabsContainer=getViewById(R.id.tablinearlayout);
        mWidth=getWindowManager().getDefaultDisplay().getWidth();
        defaultParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        scrollOffset=mWidth/2;
        mTitle=new String[]{"TAB0","TAB1","TAB2","TAB3","TAB4","TAB5","TAB6","TAB7","TAB8"};

        tabPadding=(mWidth/3)/3;
       addTabTitle(mTitle);
        mHsv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mHsv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mHsv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                scrollToChild(mViewPager.getCurrentItem(),0);
            }
        });

        for(int i=0;i<mTitle.length;i++){
            View view= LayoutInflater.from(this).inflate(R.layout.item,null);
            ListView listView=(ListView)view.findViewById(R.id.listview1);
            arrayList.add(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    showToast("item=" + i);
                }
            });

        }

        mViewPager=getViewById(R.id.viewpager_listview);
        MyPagerAdapter myPagerAdapter=new MyPagerAdapter();
        mViewPager.setAdapter(myPagerAdapter);
        final int pageMargin=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4,getResources().getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);

        mViewPager.setCurrentItem(1);
        requestData(1);

        mViewPager.setOnPageChangeListener(new PageListener());

    }

    private void addTabTitle(String[] title){
        for(int i=0;i<title.length;i++){
            final int position=i;
            TextView tabtv=new TextView(mHsv.getContext());
            tabtv.setText(title[i]);
            tabtv.setGravity(Gravity.CENTER);
            tabtv.setFocusable(true);
            tabtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(position);
                }
            });
            tabtv.setPadding(tabPadding,0,tabPadding,0);
            tabsContainer.addView(tabtv,position,defaultParams);
        }
    }
    private void startAnimation(int position){
        ScaleAnimation scaleAnimation=new ScaleAnimation(0.0f,1.4f,0.0f,1.4f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        arrayList.get(position).setAnimation(scaleAnimation);
        arrayList.get(position).startAnimation(scaleAnimation);
    }
    private void setAdapterData(int position){
        int countTv=mTitle.length;
        arrayList.get(position).setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                Arrays.asList("ding"+position+"-1", "ding"+position+"-2", "ding"+position+"-3")));
        for(int i=0;i<countTv;i++){
            if(position==i){
                ((TextView)tabsContainer.getChildAt(i)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }else{
                ((TextView)tabsContainer.getChildAt(i)).setTextColor(getResources().getColor(android.R.color.white));
            }

        }
//        startAnimation(position);

    }
    private  <T extends View>T getViewById(int rId){
        return (T)findViewById(rId);
    }
    private int mCurrentPosition=0;
    private float currentPositionOffset = 0f;

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
             mCurrentPosition=position;
             currentPositionOffset=positionOffset;
             scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

        }

        @Override
        public void onPageSelected(int i) {
            requestData(i);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mViewPager.getCurrentItem(), 0);
            }
        }

    }
    private void requestData(int select){
        setAdapterData(select);

    }
    private void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private class MyPagerAdapter extends PagerAdapter {
        public MyPagerAdapter(){
            super();
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(arrayList.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(arrayList.get(position));
            return arrayList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view==o;
        }


    };

    private void scrollToChild(int position, int offset) {
        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            mHsv.scrollTo(newScrollX-mWidth/3, 0);
        }

    }
    private void scroll(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mHsv.smoothScrollBy();
            }
        },200);
    }
}
