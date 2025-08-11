package hwdroid.widget.indicator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.droid.R;

public class HWTabPageIndicator extends HorizontalScrollView implements PageIndicator {
	/** Title text used when no title is provided by the adapter. */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    public interface OnPageChangeListener2 {
    	void onPageScrollStateChanged(int state);
    	void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
    	void onPageSelected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView)view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            try{
            	mViewPager.setCurrentItem(newSelected);
            } catch (Exception e) { // fix bug : 98930
            	e.printStackTrace();
            	return;
            }
            if (oldSelected != newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    private final IcsLinearLayout mTabLayout;
    private final UnderLine mUnderlinePageIndicator;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;

    //styleables
    private boolean mUseEvenTabWidth = false;
    private int mUnderlineHeight = 10; //dp
    private int mOverScrollMode = OVER_SCROLL_NEVER;
    private int mBackgroundColor = Color.TRANSPARENT;
    private boolean mIndicatorOnTab = false;
    private ViewGroup mTabsWrapper;

    private OnTabReselectedListener mTabReselectedListener;

    private OnPageChangeListener2 mOnPageChangeLinstener2;

    public HWTabPageIndicator(Context context) {
        this(context, null);
    }

    public IcsLinearLayout getTabLayout () {
    	return mTabLayout;
    }

    public HWTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

        String layoutHeight = null;
        try {
            if(null != attrs) layoutHeight = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        } catch (Exception e) {
            Log.d("HWTabPageIndicator", "does not exist attribute layout_height, " + e.getMessage());
        }


        setHorizontalScrollBarEnabled(false);
        this.setBackgroundColor(mBackgroundColor);  //also public to customize
        this.setOverScrollMode(mOverScrollMode);
        int actionBarHeight = Math.round(context.getResources().getDimension(R.dimen.hw_action_bar_height));
        if(!mIndicatorOnTab) {
            mTabsLayout = new FrameLayout(context);
            mTextView = new TextView(context);
            mTextView.setGravity(Gravity.CENTER);
            mTabsWrapper = new LinearLayout(context);
            ((LinearLayout)mTabsWrapper).setOrientation(LinearLayout.VERTICAL);
            mUnderlinePageIndicator = new UnderLine(context);
            mTabLayout = new IcsLinearLayout(context, R.attr.hwTabPageIndicatorStyle);
            if(mUseEvenTabWidth){
                mTabsWrapper.addView(mTabLayout, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1));
            } else {
                mTabsWrapper.addView(mTabLayout, new LinearLayout.LayoutParams(WRAP_CONTENT, 0, 1));
            }
            mTabsWrapper.addView(mUnderlinePageIndicator, new LinearLayout.LayoutParams(MATCH_PARENT, mUnderlineHeight));
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(MATCH_PARENT, actionBarHeight / 2);
            layoutParams.setMargins(0, actionBarHeight / 2, 0, 0);
            mTabsLayout.addView(mTabsWrapper, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            mTabsLayout.addView(mTextView, layoutParams);
            addView(mTabsLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            mTextView.setAlpha(0.0f);
        } else {
            int height;
            if(null ==layoutHeight || layoutHeight.equals("-2")) {//WRAP_CONTENT
                height = actionBarHeight;
            } else {
                height = -2;
            }

            mTabsLayout = new FrameLayout(context);
            mTextView = new TextView(context);
            mTextView.setGravity(Gravity.CENTER);

            mTabsWrapper = new RelativeLayout(context);

            mUnderlinePageIndicator = new UnderLine(context);
            mUnderlinePageIndicator.setId(R.id.hw_underline_indicator);
            mTabLayout = new IcsLinearLayout(context, R.attr.hwTabPageIndicatorStyle);
            mTabLayout.setId(R.id.hw_tab_container);

            if(mUseEvenTabWidth){
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(MATCH_PARENT, height);
                mTabsWrapper.addView(mTabLayout, lp1);
            } else {
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(WRAP_CONTENT, height);
                mTabsWrapper.addView(mTabLayout, lp1);
            }

            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(MATCH_PARENT, mUnderlineHeight);
            lp2.addRule(RelativeLayout.ALIGN_LEFT, R.id.hw_tab_container);
            lp2.addRule(RelativeLayout.ALIGN_RIGHT, R.id.hw_tab_container);
            lp2.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hw_tab_container);

            mTabsWrapper.addView(mUnderlinePageIndicator, lp2);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(MATCH_PARENT, actionBarHeight / 2);
            layoutParams.setMargins(0, actionBarHeight / 2, 0, 0);
            mTabsLayout.addView(mTabsWrapper, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            mTabsLayout.addView(mTextView, layoutParams);
            addView(mTabsLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            mTextView.setAlpha(0.0f);
        }
        mTextView.setVisibility(View.GONE);
        mTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTabsShrink)
                    toggleExpandTabs();
            }
        });

    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.TabPageIndicator, R.attr.hwTabPageIndicatorStyle, 0);

        mUseEvenTabWidth = a.getBoolean(R.styleable.TabPageIndicator_hw_useEvenTabWidth, mUseEvenTabWidth);
        mUnderlineHeight = a.getDimensionPixelSize(R.styleable.TabPageIndicator_hw_underlineHeight, 10);
        mIndicatorOnTab = a.getBoolean(R.styleable.TabPageIndicator_hw_underlineOnTab, false);

        a.recycle();
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    public void setOnPageChangeListener2(OnPageChangeListener2 listener) {
    	mOnPageChangeLinstener2 = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                if(mUseEvenTabWidth)
                    mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) / childCount);
                else
                    mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        final TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setOnClickListener(mTabClickListener);
        tabView.setText(text);

        if (iconResId != 0) {
            tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        }

        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    	mUnderlinePageIndicator.onPageScrollStateChanged(arg0);
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }

        if(mOnPageChangeLinstener2 != null) {
        	mOnPageChangeLinstener2.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    	mUnderlinePageIndicator.onPageScrolled(arg0, arg1, arg2);
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }

        if(mOnPageChangeLinstener2 != null) {
        	mOnPageChangeLinstener2.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        if(isTabsShrink) toggleExpandTabs();

    	mUnderlinePageIndicator.onPageSelected(arg0);
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }

        if(mOnPageChangeLinstener2 != null) {
        	mOnPageChangeLinstener2.onPageSelected(arg0);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter)adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            Log.e("HWTabPageIndicator", "ViewPager has not been bound.");
            return ;
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.hwTabPageIndicatorStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Re-measure if we went beyond our maximum size.
            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }

        public int getIndex() {
            return mIndex;
        }
    }

    /** tabs shrink animation start **/
    private long mTabShrinkAnimationTime = 200;
    private boolean isTabsShrink = false;
    private boolean isAnimationWorking = false;
    private FrameLayout mTabsLayout;
    private TextView mTextView;

    public void setTabShrinkAnimationTime(long timeMillis) {
        this.mTabShrinkAnimationTime = timeMillis;
    }

    public long getTabShrinkAnimationTime() {
        return mTabShrinkAnimationTime;
    }

    /**
     * trigger indicator shrink tabs
     */
    public void toggleShrinkTabs() {
        toggleShrinkTabs(null);
    }

    /**
     * trigger indicator shrink tabs with a animator listener
     * can do something with tab during the animation time
     *
     * @param listener a listener bind with tabs animator
     */
    public void toggleShrinkTabs(final AnimatorListener listener) {
        if(isAnimationWorking) return;

        if(isTabsShrink) return;

        isAnimationWorking = true;

        mTextView.setVisibility(View.VISIBLE);

        TranslateAnimation translate = new TranslateAnimation(0, 0, mTabsLayout.getHeight(), mTabsLayout.getHeight() / 2) {
            float offset = 0.0f;
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) HWTabPageIndicator.this.getLayoutParams();
                params.topMargin -= (HWTabPageIndicator.this.getHeight() / 2) * interpolatedTime - offset;
                HWTabPageIndicator.this.setLayoutParams(params);
                offset = (mTabLayout.getHeight() / 2) * interpolatedTime;

            }
        };
        translate.setDuration(mTabShrinkAnimationTime);
        this.startAnimation(translate);

        TabView tabView = (TabView) mTabLayout.getChildAt(mSelectedTabIndex);
        mTextView.setText(tabView.getText().toString());
        ObjectAnimator alphaTab = ObjectAnimator.ofFloat(mTabsWrapper, View.ALPHA, 1.0f, 0.0f);
        ObjectAnimator alphaTXT = ObjectAnimator.ofFloat(mTextView, View.ALPHA, 0.0f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(alphaTab, alphaTXT);
        animSet.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                if(null != listener) listener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if(null != listener) listener.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(null != listener) listener.onAnimationEnd(animation);
                isAnimationWorking = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if(null != listener) listener.onAnimationCancel(animation);

            }
        });
        animSet.start();
        isTabsShrink = true;
    }

    /**
     * trigger indicator expend tabs
     */
    public void toggleExpandTabs() {
        toggleExpandTabs(null);
    }

    /**
     * trigger indicator expand tabs with a animator listener
     * can do something with tab during the animation time
     *
     * @param listener a listener bind with tabs animator
     */
    public void toggleExpandTabs(final AnimatorListener listener) {
        if(isAnimationWorking) return;

        if(!isTabsShrink) return;

        isAnimationWorking = true;

        TranslateAnimation translate = new TranslateAnimation(0, 0, mTabsLayout.getHeight(), mTabsLayout.getHeight() / 2) {
            float offset = 0.0f;
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) HWTabPageIndicator.this.getLayoutParams();
                if(params.topMargin <= 0) {
                    params.topMargin += (HWTabPageIndicator.this.getHeight() / 2) * interpolatedTime - offset;
                    HWTabPageIndicator.this.setLayoutParams(params);
                    offset = (HWTabPageIndicator.this.getHeight() / 2) * interpolatedTime;
                }
            }
        };
        translate.setDuration(mTabShrinkAnimationTime);
        this.startAnimation(translate);
        ObjectAnimator alphaTab = ObjectAnimator.ofFloat(mTabsWrapper, View.ALPHA, 0.0f, 1.0f);
        ObjectAnimator alphaTXT = ObjectAnimator.ofFloat(mTextView, View.ALPHA, 1.0f, 0.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(alphaTab, alphaTXT);
        animSet.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                if(null != listener) listener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if(null != listener) listener.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(null != listener) listener.onAnimationEnd(animation);
                isAnimationWorking = false;
                mTextView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if(null != listener) listener.onAnimationCancel(animation);

            }
        });
        animSet.start();
        isTabsShrink = false;
    }
    /** tabs shrink animation end **/


    private class UnderLine extends View {
    	private static final int SLIDING_DIRECTION_LEFT = 2;
        private static final int SLIDING_DIRECTION_RIGHT = 1;
        private static final int SLIDING_DIRECTION_NONE = 0;
        private int mSlidingDirection;
        private boolean isLockSlidingOrientation = false;
        private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private int mScrollState;
        private int mCurrentPage;
        private float mPositionOffset;
        private boolean mUnderlineIncludePaddings = false;

        public UnderLine(Context context) {
            this(context, null);
        }

        public UnderLine(Context context, AttributeSet attrs) {
            this(context, attrs, R.attr.hwUnderlinePageIndicatorStyle);
        }

        @SuppressWarnings("deprecation")
		public UnderLine(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            if (isInEditMode()) return;

            final Resources res = getResources();

            //Load defaults from resources
            final int defaultSelectedColor = res.getColor(R.color.hw_default_underline_indicator_selected_color);

            //Retrieve styles attributes
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UnderlinePageIndicator, defStyle, 0);

            setSelectedColor(a.getColor(R.styleable.UnderlinePageIndicator_hw_selectedColor, defaultSelectedColor));

            Drawable background = a.getDrawable(R.styleable.UnderlinePageIndicator_hw_indicator_background);
            if (background != null) {
              setBackgroundDrawable(background);
            }

            mUnderlineIncludePaddings = a.getBoolean(R.styleable.UnderlinePageIndicator_hw_underlineIncludePaddings, false);

            a.recycle();
        }

        @SuppressWarnings("unused")
		public int getSelectedColor() {
            return mPaint.getColor();
        }

        public void setSelectedColor(int selectedColor) {
            mPaint.setColor(selectedColor);
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mViewPager == null) {
                return;
            }
            final int count = mViewPager.getAdapter().getCount();
            if (count == 0) {
                return;
            }

            if (mCurrentPage >= count) {
                setCurrentItem(count - 1);
                return;
            }

            final float left = getCurrentPositionLeft() + ((getCurrentPositionWidth())*mPositionOffset);
            final float right = getCurrentPositionRight() + ((getNextPositionWidth())*mPositionOffset);
            final float top = getPaddingTop();
            final float bottom = getHeight() - getPaddingBottom();
            canvas.drawRect(left, top, right, bottom, mPaint);
        }

//        private int getCurrentPositionLeft() {
//        	int paddingLeft = 0;
//        	if(!mUseEvenTabWidth)
//        		paddingLeft = getTabLayout().getChildAt(mCurrentPage).getPaddingLeft();
//        	return getTabLayout().getChildAt(mCurrentPage).getLeft() + paddingLeft;
//        }
//        private int getCurrentPositionRight() {
//        	int paddingRight = 0;
//        	if(!mUseEvenTabWidth)
//        		paddingRight = getTabLayout().getChildAt(mCurrentPage).getPaddingRight();
//        	return getTabLayout().getChildAt(mCurrentPage).getRight() - paddingRight;
//        }

        private int getCurrentPositionLeft() {
        	int paddingLeft = getTabLayout().getChildAt(mCurrentPage).getPaddingLeft();
        	if(mUnderlineIncludePaddings){
        	    return getTabLayout().getChildAt(mCurrentPage).getLeft() ;//+ paddingLeft;
        	} else {
        	    return getTabLayout().getChildAt(mCurrentPage).getLeft() + paddingLeft;
        	}
        }
        private int getCurrentPositionRight() {
        	int paddingRight = getTabLayout().getChildAt(mCurrentPage).getPaddingRight();
        	if(mUnderlineIncludePaddings){
        	    return getTabLayout().getChildAt(mCurrentPage).getRight() ;//- paddingRight;
        	} else {
        	    return getTabLayout().getChildAt(mCurrentPage).getRight() - paddingRight;
        	}
        }

        private float getCurrentPositionWidth() {
        	return getTabLayout().getChildAt(mCurrentPage).getWidth();
        }

        private float getNextPositionWidth() {
        	int position = mCurrentPage;
        	if(mSlidingDirection == SLIDING_DIRECTION_RIGHT) {
        		position = mCurrentPage - 1;
        	} else if(mSlidingDirection == SLIDING_DIRECTION_LEFT) {
        		position = mCurrentPage + 1;
        	}
        	float width;
        	if(position >= 0 && position < getTabLayout().getChildCount()) {
        		width = getTabLayout().getChildAt(position).getWidth();
        	} else {
        		if(Math.abs(mCurrentPage - 0) < Math.abs(mCurrentPage - (getTabLayout().getChildCount() - 1)))
        			width = getTabLayout().getChildAt(1).getWidth();
        		else
        			width = getTabLayout().getChildAt(getTabLayout().getChildCount() - 2).getWidth();
        	}
        	return width;
        }

        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        	if(mScrollState == ViewPager.SCROLL_STATE_DRAGGING && !isLockSlidingOrientation) {
        		if (mPositionOffset > positionOffset) {
        			mSlidingDirection = SLIDING_DIRECTION_RIGHT;
        		} else if (mPositionOffset < positionOffset) {
        			mSlidingDirection = SLIDING_DIRECTION_LEFT;
        		} else {
        			mSlidingDirection = SLIDING_DIRECTION_NONE;
        		}
        		isLockSlidingOrientation = true;
        	}
        	mCurrentPage = position;
            mPositionOffset = positionOffset;
            invalidate();
        }

        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mCurrentPage = position;
                mPositionOffset = 0;
                invalidate();
            }
            isLockSlidingOrientation = false;
        }
    }
}
