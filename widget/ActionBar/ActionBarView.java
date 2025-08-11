package hwdroid.widget.ActionBar;

import com.hw.droid.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import hwdroid.widget.ActionBar.ActionBarOptionMenuPopup.ActionBarOptionMenuListener;
import hwdroid.widget.searchview.SearchView;
import hwdroid.widget.searchview.SearchView.SearchViewListener;

import java.lang.ref.WeakReference;

public class ActionBarView extends LinearLayout implements ActionBarOptionMenuListener{
    private static final int mLayoutID = R.layout.hw_action_bar_title;
    public static final int BackKeyIcon = R.drawable.hw_actionbar_back_icon;
    private static final int mMaxItemsCount = 1;

    private TextView mTitleView;
    private TextView mSubTitleView;
    private LinearLayout mLeftLL;
    private LinearLayout mRightLL;
    private ImageView mDivider;

    private boolean mShowBackKeyStatus;

    private LinearLayout mTitleLayout;

    final public static int ID_BACKKEY = 0x01001;
    final public static int ID_CUSTOM_LEFT_WIDGET = 0x01002;
    final public static int ID_CUSTOM_RIGHT_WIDGET = 0x01003;

    private OnBackKeyItemClick mOnBackKeyItemClick;
    private OnTitle2ItemClick mOnTitle2ItemClick;
    private OnLeftWidgetItemClick mOnLeftWidgetItemClick;
    private OnRightWidgetItemClick mOnRightWidgetItemClick;
    private OnLeftWidgetItemClick2 mOnLeftWidgetItemClick2;
    private OnRightWidgetItemClick2 mOnRightWidgetItemClick2;
    private OnOptionMenuClick mOptionMenuClick;
    private OnCreateOptionMenu mOnCreateOptionMenu;

    private UpdateHandler mUpdateHandler;

    private final static int UPDATE_ID_SHOW_BACKKEY             = 0x03001;
    private final static int UPDATE_ID_ADD_LEFTVIEW             = 0x03002;
    private final static int UPDATE_ID_ADD_RIGHTVIEW            = 0x03003;
    private final static int UPDATE_ID_SET_TITLE                = 0x03004;
    private final static int UPDATE_ID_SET_OPTION_TITLE         = 0x03005;
    private final static int UPDATE_ID_SET_OPTION_ITEMS         = 0x03006;
    private final static int UPDATE_ID_SHOW_OPTION_POPUP        = 0x03007;
    private final static int UPDATE_ID_HIDE_OPTION_POPUP        = 0x03008;
    private final static int UPDATE_ID_UPDATE_LEFT_ENABLED      = 0x03009;
    private final static int UPDATE_ID_UPDATE_RIGHT_ENABLED     = 0x03010;

    public ActionBarView(Context context) {
        super(context);
        initView();
    }

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(mLayoutID, this);
        setOrientation(VERTICAL);
        mTitleLayout = (LinearLayout) findViewById(R.id.hw_action_bar_title_layout);
        mTitleLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(mOnTitle2ItemClick != null) {
					mOnTitle2ItemClick.onTitle2ItemClick();
				}
			}});

        mTitleView = (TextView) findViewById(R.id.hw_action_bar_title);
        mSubTitleView = (TextView) findViewById(R.id.hw_action_bar_subtitle);
        mLeftLL = (LinearLayout) findViewById(R.id.hw_action_bar_left_container);
        mRightLL = (LinearLayout) findViewById(R.id.hw_action_bar_right_container);
        mDivider = (ImageView) findViewById(R.id.hw_action_bar_divider);

        mUpdateHandler = new UpdateHandler(this.getContext(), this, mLeftLL, mRightLL, mTitleView);
    }
    
    public interface OnCreateOptionMenu {
        CharSequence[] onCreateOptionMenu();
        boolean[] onOptionMenuDisabled();
    }

    public interface OnOptionMenuClick {
    	void onOptionMenuClick(int pos);
    }

    public interface OnBackKeyItemClick {
        void onBackKeyItemClick();
    }

    public interface OnTitle2ItemClick {
    	void onTitle2ItemClick();
    }

    public interface OnLeftWidgetItemClick {
    	void onLeftWidgetItemClick();
    }

    public interface OnRightWidgetItemClick {
    	void onRightWidgetItemClick();
    }

    public interface OnLeftWidgetItemClick2 {
        void onLeftWidgetItemClick(View arg0);
    }

    public interface OnRightWidgetItemClick2 {
        void onRightWidgetItemClick(View arg0);
    }

    public void setOnTitle2ItemClickListener(OnTitle2ItemClick click) {
    	mOnTitle2ItemClick = click;
    }

    public void setOnLeftWidgetItemClickListener(OnLeftWidgetItemClick click) {
    	mOnLeftWidgetItemClick = click;
    }

    public void setOnRightWidgetItemClickListener(OnRightWidgetItemClick click) {
    	mOnRightWidgetItemClick = click;
    }

    public void setOnLeftWidgetItemClickListener2(OnLeftWidgetItemClick2 click) {
        mOnLeftWidgetItemClick2 = click;
    }

    public void setOnRightWidgetItemClickListener2(OnRightWidgetItemClick2 click) {
        mOnRightWidgetItemClick2 = click;
    }

    public void setLeftWidgetItemEnable(boolean enabled) {
    	mLeftLL.setEnabled(enabled);

        Message msg = new Message();
        msg.what = UPDATE_ID_UPDATE_LEFT_ENABLED;
        msg.obj = enabled;

        mUpdateHandler.sendMessage(msg);
    }

    public void setRightWidgetItemEnable(boolean enabled) {
    	mRightLL.setEnabled(enabled);

        Message msg = new Message();
        msg.what = UPDATE_ID_UPDATE_RIGHT_ENABLED;
        msg.obj = enabled;

        mUpdateHandler.sendMessage(msg);
    }

	public boolean isLeftWidgetItemEnabled() {
		return mLeftLL.isEnabled();
	}

	public boolean isRightWidgetItemEnabled() {
		return mRightLL.isEnabled();
	}

    public void setOptionTitle(CharSequence title) {
    	setTitle(title);

		setOnTitle2ItemClickListener(new OnTitle2ItemClick(){

			@Override
			public void onTitle2ItemClick() {
			    if(mOnCreateOptionMenu != null) {
			        CharSequence[] items = mOnCreateOptionMenu.onCreateOptionMenu();
			        mUpdateHandler.setOptionItemsInner(items);
			        
			        boolean[] disables = mOnCreateOptionMenu.onOptionMenuDisabled();
			        mUpdateHandler.setOptionItemsDisabledInner(disables);
			    }
			    
			    mUpdateHandler.sendEmptyMessage(UPDATE_ID_SHOW_OPTION_POPUP);
			}});

		mUpdateHandler.removeMessages(UPDATE_ID_SET_OPTION_TITLE);
		mUpdateHandler.sendEmptyMessage(UPDATE_ID_SET_OPTION_TITLE);
    }

    public void setOptionItems(CharSequence[] items, OnOptionMenuClick click) {
        mUpdateHandler.setOptionItemsInner(items);
    	mOptionMenuClick = click;
    }
    
    public void setOptionItems(OnCreateOptionMenu listener, OnOptionMenuClick click) {
        mOptionMenuClick = click;
        mOnCreateOptionMenu = listener;
    }

    public void setTitle(CharSequence title) {
        setOnTitle2ItemClickListener(null);

        mUpdateHandler.removeMessages(UPDATE_ID_SET_TITLE);

        Message msg = new Message();
        msg.what = UPDATE_ID_SET_TITLE;
        msg.obj = title;

        mUpdateHandler.sendMessage(msg);
    }

    public void setSubTitle(CharSequence subtitle) {
    	if (mSubTitleView != null) {
    		mSubTitleView.setText(subtitle);
    		mSubTitleView.setVisibility(View.VISIBLE);
    	}
    }

    public void setTitleDividerColor(int color) {
    	mDivider.setBackgroundColor(color);
    }

    public void setTitleDividerResource(int resource) {
    	mDivider.setBackgroundResource(resource);
    }

	public void showBackKey(boolean show, OnBackKeyItemClick click) {
		mShowBackKeyStatus = show;
		removeLeftItem();
		mLeftLL.setEnabled(true);

		if(show) {
	      mLeftLL.setOnClickListener(new OnClickListener(){

	            @Override
	            public void onClick(View arg0) {
                    if(mShowBackKeyStatus == true && mOnBackKeyItemClick!= null) {
                        mOnBackKeyItemClick.onBackKeyItemClick();
                    }
	            }});

			mOnBackKeyItemClick = click;
			mUpdateHandler.sendEmptyMessage(UPDATE_ID_SHOW_BACKKEY);
		}
	}

    public void addLeftItem(View view) {
        removeLeftItem();

        mLeftLL.setEnabled(true);
    	mLeftLL.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                //delete this case.
                if(mOnLeftWidgetItemClick != null) {
                    mOnLeftWidgetItemClick.onLeftWidgetItemClick();
                }

                //new case.
                if(mOnLeftWidgetItemClick2 != null) {
                    View arg1 = null;
                    if(mLeftLL.getChildCount() == 1) {
                        arg1 = mLeftLL.getChildAt(0);
                    }

                    mOnLeftWidgetItemClick2.onLeftWidgetItemClick(arg1);
                }
            }});

        if (mLeftLL.getChildCount() > mMaxItemsCount) {
            return;
        }

        Message msg = new Message();
        msg.what = UPDATE_ID_ADD_LEFTVIEW;
        msg.obj = view;

        mUpdateHandler.sendMessage(msg);
    }



    public void removeLeftItem() {
        mOnBackKeyItemClick = null;
        mUpdateHandler.removeMessages(UPDATE_ID_SHOW_BACKKEY);
        mUpdateHandler.removeMessages(UPDATE_ID_ADD_LEFTVIEW);
    	removeItem(ID_CUSTOM_LEFT_WIDGET);
    	removeItem(ID_BACKKEY);
    }

    public void addRightItem(View view) {
        addRightItem(view, false);
    }
    
    public void addRightItem(View view, boolean clickable) {
        removeRightItem();

        mRightLL.setEnabled(true);
        if(!clickable) {
            mRightLL.setOnClickListener(new OnClickListener(){
    
                @Override
                public void onClick(View arg0) {
                    //delete this case
                    if(mOnRightWidgetItemClick != null) {
                        mOnRightWidgetItemClick.onRightWidgetItemClick();
                    }
    
                    //new case
                    if(mOnRightWidgetItemClick2 != null) {
                        View arg1 = null;
                        if(mRightLL.getChildCount() == 1) {
                            arg1 = mRightLL.getChildAt(0);
                        }
    
                        mOnRightWidgetItemClick2.onRightWidgetItemClick(arg1);
                    }
                }});
        }

        if (mRightLL.getChildCount() > mMaxItemsCount) {
            return;
        }

        Message msg = new Message();
        msg.what = UPDATE_ID_ADD_RIGHTVIEW;
        msg.obj = view;
        msg.arg1 = (clickable == false) ? 0 : 1;

        mUpdateHandler.sendMessage(msg);
    }

    public void removeRightItem() {
        mUpdateHandler.removeMessages(UPDATE_ID_ADD_RIGHTVIEW);
    	removeItem(ID_CUSTOM_RIGHT_WIDGET);
    }

    private void removeItem(int id) {
        View child;

        for (int i = 0; i < mLeftLL.getChildCount(); i++) {
            child = mLeftLL.getChildAt(i);
            if (child.getId() == id) {
                mLeftLL.removeView(child);
                mOnLeftWidgetItemClick = null;
                return;
            }
        }

        for (int i = 0; i < mRightLL.getChildCount(); i++) {
            child = mRightLL.getChildAt(i);
            if (child.getId() == id) {
                mRightLL.removeView(child);
                mOnRightWidgetItemClick = null;
                return;
            }
        }
    }
    
    public void setTabPageIndicator(View indicator) {
        if(indicator == null) {
            return;  
        }
        
        this.removeAllViews();
        this.addView(indicator,
                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

	@Override
	public void onOptionMenuClick(int pos) {
		mOptionMenuClick.onOptionMenuClick(pos);
	}

	@Override
	public void onOptionMenuClose(boolean isClose) {
    	if (mTitleView != null) {
    		int iconId;

    		if(isClose) {
    			iconId = R.drawable.hw_actionbar_option_icon_down;
    		} else {
    			iconId = R.drawable.hw_actionbar_option_icon_up;
    		}

    		mTitleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconId, 0);
    	}
	}

	private static final class UpdateHandler extends Handler {
	    private final WeakReference<Context> mContext;
	    private final WeakReference<ActionBarView> mAcionBarView;
	    private final WeakReference<LinearLayout> mLeftView;
	    private final WeakReference<LinearLayout> mRightView;
	    private final WeakReference<TextView> mTitleView;

	    private ActionBarOptionMenuPopup mOptionMenuPopup;

	    private CharSequence[] mOptionItems;
	    private boolean[] mOptionItemsDisabled;

	    UpdateHandler(Context context,
	            ActionBarView actionbarview,
	            LinearLayout leftView,
	            LinearLayout rightView,
	            TextView titleView) {
	        mContext = new WeakReference<Context>(context);
	        mAcionBarView = new WeakReference<ActionBarView>(actionbarview);
	        mLeftView = new WeakReference<LinearLayout>(leftView);
	        mRightView = new WeakReference<LinearLayout>(rightView);
	        mTitleView = new WeakReference<TextView>(titleView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_ID_SHOW_BACKKEY:
                    showBackKeyInner();
                    break;
                case UPDATE_ID_ADD_LEFTVIEW:
                    View leftview = (View)msg.obj;
                    addLeftItemInner(leftview,
                            new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT),
                            ID_CUSTOM_LEFT_WIDGET);
                    break;
                case UPDATE_ID_ADD_RIGHTVIEW:
                    View rightview = (View)msg.obj;
                    boolean clickable = (msg.arg1 == 0)? false : true;
                    addRightItemInner(rightview,
                            new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT),
                            ID_CUSTOM_RIGHT_WIDGET, clickable);
                    break;
                case UPDATE_ID_SET_TITLE:
                    CharSequence title = (CharSequence)msg.obj;
                    setTitleInner(title);
                    break;
                case UPDATE_ID_SET_OPTION_TITLE:
                    setOptionTitleInner();
                    break;
                case UPDATE_ID_SET_OPTION_ITEMS:
                    break;
                case UPDATE_ID_SHOW_OPTION_POPUP:
                    showPopupInner();
                    break;
                case UPDATE_ID_HIDE_OPTION_POPUP:
                    showPopupInner();
                    break;
                case UPDATE_ID_UPDATE_LEFT_ENABLED:
                    Boolean leftenable = (Boolean)msg.obj;
                    setLeftWidgetItemEnableInner(leftenable);
                    break;
                case UPDATE_ID_UPDATE_RIGHT_ENABLED:
                    Boolean rightenable = (Boolean)msg.obj;
                    setRightWidgetItemEnableInner(rightenable);
                    break;
            }
        }

        private void showBackKeyInner() {
            ImageButton backView = new ImageButton(mContext.get());
            backView.setBackgroundResource(ActionBarView.BackKeyIcon);
            backView.setClickable(false);

            addLeftItemInner(backView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT), ID_CUSTOM_LEFT_WIDGET);
        }


        private void addLeftItemInner(View view, LayoutParams layoutParams, int id) {
            if(mLeftView.get() == null) return;

            if(view == null) {
                throw new RuntimeException("<HW ActionBarView> the added view is null!!");
            }

            view.setClickable(false);

            try {
                ViewGroup group = (ViewGroup) view;
                int count = group.getChildCount();

                View v = null;
                for(int i = 0; i < count; i++) {
                    v = group.getChildAt(i);
                    if(v != null) {
                        v.setClickable(false);
                    }
                }
            } catch(Exception e) {

            }

            view.setId(id);
            mLeftView.get().addView(view, layoutParams);
            mLeftView.get().setVisibility(View.VISIBLE);
        }

        private void addRightItemInner(View view, LayoutParams layoutParams, int id, boolean clickable) {
            if(mRightView.get() == null) return;

            if(view == null) {
                throw new RuntimeException("<HW ActionBarView> the added view is null!!");
            }

            view.setClickable(clickable);

            try {
                ViewGroup group = (ViewGroup) view;
                int count = group.getChildCount();

                View v = null;
                for(int i = 0; i < count; i++) {
                    v = group.getChildAt(i);
                    if(v != null) {
                        v.setClickable(clickable);
                    }
                }
            } catch(Exception e) {

            }

            view.setId(id);
            mRightView.get().addView(view, layoutParams);
            mRightView.get().setVisibility(View.VISIBLE);
        }

        private void setTitleInner(CharSequence title) {
            if(mTitleView.get() == null) return;

            mTitleView.get().setText(title);
            mTitleView.get().setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        private void setOptionTitleInner() {
            if(mTitleView.get() == null) return;

            mTitleView.get().setCompoundDrawablePadding(0);
            mTitleView.get().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hw_actionbar_option_icon_down, 0);

            if(mOptionMenuPopup == null) {
                mOptionMenuPopup = new ActionBarOptionMenuPopup(mContext.get(), mAcionBarView.get());
            }
        }

        public void setOptionItemsInner(CharSequence[] items) {
            mOptionItems = items;
        }
        
        public void setOptionItemsDisabledInner(boolean[] disabled) {    
            if(disabled != null && disabled.length != mOptionItems.length) {
                throw new IllegalArgumentException("setOptionItemsDisabledInner: array length was error.");
            } else {
                mOptionItemsDisabled = disabled;
            }
        }

        private void showPopupInner() {
            if(mOptionMenuPopup != null) {
                if(mOptionMenuPopup.isShowing()) {
                    mOptionMenuPopup.dimssWithAnimation(mContext.get());
                } else {
                    mOptionMenuPopup.showOptionView(mAcionBarView.get(), mOptionItems, mOptionItemsDisabled);
                }
            }
        }


        private void setLeftWidgetItemEnableInner(boolean enabled) {
            int count = mLeftView.get().getChildCount();

            View v = null;
            for(int i = 0; i < count; i++) {
                v = mLeftView.get().getChildAt(i);

                try {
                    ViewGroup group = (ViewGroup) v;
                    int count1 = group.getChildCount();

                    View clildview = null;
                    for(int j = 0; j < count1; j++) {
                        clildview = group.getChildAt(j);
                        if(clildview != null) {
                            clildview.setEnabled(false);
                        }
                    }
                } catch(Exception e) {

                }

                if(v != null) {
                    v.setEnabled(enabled);
                }
            }
        }

        private void setRightWidgetItemEnableInner(boolean enabled) {
            int count = mRightView.get().getChildCount();

            View v = null;
            for(int i = 0; i < count; i++) {
                v = mRightView.get().getChildAt(i);

                try {
                    ViewGroup group = (ViewGroup) v;
                    int count1 = group.getChildCount();

                    View clildview = null;
                    for(int j = 0; j < count1; j++) {
                        clildview = group.getChildAt(j);
                        if(clildview != null) {
                            clildview.setEnabled(false);
                        }
                    }
                } catch(Exception e) {

                }

                if(v != null) {
                    v.setEnabled(enabled);
                }
            }
        }
	}

    /**
     * search view mode
     */

    public interface ActionBarSearchViewListener {
        void startOutAnimation(int time);
        void startInAnimation(int time);
        void doTextChanged(CharSequence s);
    }

    public void setSearchViewListener(ActionBarSearchViewListener listener) {
        mActionBarSearchListener = listener;
    }

    /**
     * Sets a listener for user actions within the SearchView.
     *
     * @param listener the listener object that receives callbacks when the user performs
     * actions in the SearchView such as clicking on buttons or typing a query.
     */
    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    /**
     * Sets a listener to inform when the user closes the SearchView.
     *
     * @param listener the listener to call when the user closes the SearchView.
     */
    public void setOnCloseListener(OnCloseListener listener) {
        mOnCloseListener = listener;
    }

    private ActionBarSearchViewListener mActionBarSearchListener;
    private OnQueryTextListener mOnQueryChangeListener;
    private OnCloseListener mOnCloseListener;
    private SearchView mSearchView;
    /**
     * Returns the query string currently in the text field.
     *
     * @return the query string
     */
    public CharSequence getQuery() {
        return mSearchView.getQuery();
    }

    /**
     * Sets a query string in the text field and optionally submits the query as well.
     *
     * @param query the query string. This replaces any query text already present in the
     * text field.
     */
    public void setQuery(CharSequence query) {
        mSearchView.setQuery(query);
    }

    /**
     * Sets the hint text to display in the query text field. This overrides any hint specified
     * in the SearchableInfo.
     *
     * @param hint the hint text to display
     *
     * @attr ref android.R.styleable#SearchView_queryHint
     */
    public void setQueryHint(CharSequence hint) {
        mSearchView.setQueryHint(hint);
    }

    /**
     * enable the actionBar view inner search view
     *
     * @param enable generate a search view attach to search view, show it if true or hide otherwise
     */
    public void enableSearchView(boolean enable) {
        if(mSearchView == null) {
            mSearchView = new SearchView(getContext());
            this.addView(mSearchView);
            mSearchView.setAnchorView(this.findViewById(R.id.hw_action_bar_container));
            mSearchView.setSearchViewListener(new SearchViewListener(){

                @Override
                public void startOutAnimation(int time) {
                    if(mActionBarSearchListener != null) {
                        mActionBarSearchListener.startOutAnimation(time);
                    }
                }

                @Override
                public void startInAnimation(int time) {
                    if(mActionBarSearchListener != null) {
                        mActionBarSearchListener.startInAnimation(time);
                    }
                }

                @Override
                public void doTextChanged(CharSequence s) {
                    if(mActionBarSearchListener != null) {
                        mActionBarSearchListener.doTextChanged(s);
                    }
                }});
            mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(mOnQueryChangeListener != null)
                        return mOnQueryChangeListener.onQueryTextSubmit(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(mOnQueryChangeListener != null)
                        return mOnQueryChangeListener.onQueryTextChange(newText);
                    return false;
                }
            });
            mSearchView.setOnCloseListener(new OnCloseListener() {

                @Override
                public boolean onClose() {
                    if(mOnCloseListener != null)
                        return mOnCloseListener.onClose();
                    return false;
                }
            });
        }

        if(enable) mSearchView.setVisibility(View.VISIBLE);
        else mSearchView.setVisibility(View.GONE);
    }


}
