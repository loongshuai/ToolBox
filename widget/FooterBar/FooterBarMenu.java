
package hwdroid.widget.FooterBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.hw.droid.R;

public class FooterBarMenu extends FooterBarView {

    private final static int MAXITEMSIZE = 10;
    private final static int SHOW_MAX_ITEM = 4;
    
    private int mShowCount = SHOW_MAX_ITEM;

    private int items_margin;
    private int menu_item_margin_top;
    private int menu_item_margin_bottom;

    private FooterBarMenuItem mMoreItem;
    private Context mContext;
    
    private FooterBarMenuPopup mPopupDialog;

    public FooterBarMenu(Context context) {
        this(context, null);
    }

    public FooterBarMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setItemType(VERTICAL_ITEM_TYPE);
        setMaxItemSize(MAXITEMSIZE);

        mContext = this.getContext();

        items_margin = mContext.getResources().getDimensionPixelSize(
                R.dimen.hw_footerbar_menu_item__half_margin);
        menu_item_margin_top = mContext.getResources().getDimensionPixelSize(
                R.dimen.hw_footerbar_menu_item_margin_top);
        menu_item_margin_bottom = mContext.getResources().getDimensionPixelSize(
                R.dimen.hw_footerbar_menu_item_margin_bottom);

        mMoreItem = new FooterBarMenuItem(this.getContext());
        Drawable d = getResources().getDrawable(R.drawable.hw_footerbar_menu_item_more_icon);
        mMoreItem.setItemDrawableIcon(d);
        mMoreItem.setItemTitle(R.string.hw_more);
        mMoreItem.setItemTextColor(getResources().getColor(R.color.hw_footerbar_menu_text_color));
        mMoreItem.setItemTextSize(getResources().getDimensionPixelSize((R.dimen.hw_foooterbar_menu_textsize)));
        mMoreItem.setOrientation(VERTICAL_ITEM_TYPE);
        mMoreItem.setGravity(Gravity.CENTER_HORIZONTAL);
        mMoreItem.setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.hw_footerbar_menu_item_width));

        mMoreItem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//TODO: the code unused. delete?
                if(mPopupDialog.dimssWithAnimation(mContext)) {
                	mMoreItem.setSelected(false);
                	return;
                }
                
                if(mPopupDialog != null) {
                	mPopupDialog.showPopup(FooterBarMenu.this.getContext(), mShowCount, mItemList, mMoreItem);
                }
                
                mMoreItem.setSelected(true);
                for (int i = 0; i < mItemList.size(); i++) {
                    mItemList.get(i).setItemSelected(false);
                }
            }
        });
        
        mPopupDialog = new FooterBarMenuPopup(this.getContext(), this);
    }
    
    @Override
    protected boolean onFooterBarViewClick() {
        if(mPopupDialog!= null) {
        	return mPopupDialog.dimssWithAnimation(mContext);
        } else {
        	return false;
        }
    }

    protected void addItemView() {
        mContentView.removeAllViews();
        mContentView.setPadding(0, menu_item_margin_top, 0, menu_item_margin_bottom);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                LayoutParams.MATCH_PARENT, 1);

        boolean marginsFlag = false;

        int showItemCount = 0;
        boolean showMoreItem = false;
        if (mItemList.size() > mShowCount) {
            showItemCount = mShowCount;
            showMoreItem = true;
        } else {
            showItemCount = mItemList.size();
            showMoreItem = false;
        }

        FooterBarMenuItem item;
        for (int i = 0; i < showItemCount; i++) {
            item = (FooterBarMenuItem) mItemList.get(i);

            if (marginsFlag == true) {
                layoutParams.setMargins(items_margin, 0, items_margin, 0);

            } else {
                layoutParams.setMargins(0, 0, 0, 0);
            }

            mContentView.addView(item, layoutParams);

            marginsFlag = true;
        }

        if (showMoreItem) {
            layoutParams.setMargins(items_margin, 0, items_margin, 0);
            mContentView.addView(mMoreItem, layoutParams);
            mPopupDialog.setOnFooterItemClick(mOnFooterBarItemListener);
        }
    }
    
    public FooterBarMenuItem addItem(int itemId, CharSequence txt) {
        return addItem(itemId, txt, null, (float) 1.0);
    }

    @Override
    public FooterBarMenuItem addItem(int itemId, CharSequence txt, Drawable icon) {
        return addItem(itemId, txt, icon, (float) 1.0);
    }

    private FooterBarMenuItem addItem(int itemId, CharSequence txt, Drawable icon, float weight) {
        if (mItemList.size() < getMaxItemSize()) {
            FooterBarMenuItem menuItem = new FooterBarMenuItem(this.getContext());
            menuItem.setItemTextColor(getResources().getColor(R.color.hw_footerbar_menu_text_color));
            menuItem.setItemTextSize(getResources().getDimensionPixelSize((R.dimen.hw_foooterbar_menu_textsize)));
            menuItem.setItemId(itemId);
            menuItem.setItemTitle(txt);
            menuItem.setItemDrawableIcon(icon);
            menuItem.setOrientation(VERTICAL_ITEM_TYPE);
            menuItem.setOnClickListener(mOnClickListener);
            menuItem.setGravity(Gravity.CENTER_HORIZONTAL);
            menuItem.setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.hw_footerbar_menu_item_width));

            mItemList.add(menuItem);
            return menuItem;
        } else {
            return null;
        }
    }

    @Override
    public void removeItem(int itemId) {
        FooterBarItem item = (FooterBarItem) getItem(itemId);
        mItemList.remove(item);
    }
    
	@Override
	public void setPrimaryItemCount(int count) {
		if(count < 0) {
			throw new RuntimeException("<FooterBarMenu> count < 0 ");
		}
		
		if(count < SHOW_MAX_ITEM) {
			mShowCount = count;
		} else {
		    mShowCount = SHOW_MAX_ITEM;
		}
	}
	
	@Override
	public void setOnFooterItemClick(OnFooterItemClick listener) {
		mOnFooterBarItemListener = listener;
		if(mPopupDialog != null) {
			mPopupDialog.setOnFooterItemClick(mOnFooterBarItemListener);
		}
	}
	
	public void dismissPopup() {
        if(mPopupDialog != null && mPopupDialog.dimssWithAnimation(mContext)) {
        	mMoreItem.setSelected(false);
        }
	}
	
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        mPopupDialog.onWindowFocusChanged(this.getContext(), hasWindowFocus);
        super.onWindowFocusChanged(hasWindowFocus);
    }
}
