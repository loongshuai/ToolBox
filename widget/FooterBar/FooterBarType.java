package hwdroid.widget.FooterBar;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface FooterBarType {

	public static interface OnFooterItemClick {
		void onFooterItemClick(View view, int id);
	}
	
	public FooterBarItem addItem(int itemId, CharSequence txt, Drawable icon);
	
	public void updateItems();	
	public void removeItem(int itemId);
    public void clear();
    public int size();
    
    public void setTextColor(ColorStateList colors);
    public void setTextColor(int colorId);
    public void setItemTextColor(int itemId, int colorId);
    public void setItemTextColor(int itemId, ColorStateList colors);
    public void setTextSize(float size);
    public void setItemTextSize(int itemId, float size);
    
    public void setItemBackgroundResource(int itemId, int resId);
    public void setItemBackgroundColor(int itemId, int colorId);
    public void setFooterBarBackgroundResource(int resId);
    public void setFooterBarBackgroundColor(int colorId);

    public void setItemTag(int itemId, Object tag);
    public Object getItemTag(int itemId);
    public void setItemEnable(int itemId, boolean enabled);
    public boolean isItemEnable(int itemId);
    public void setItemSelected(int itemId, boolean selected);
    public boolean isItemSelected(int itemId);

	public void setOnFooterItemClick(OnFooterItemClick listener);
	
	public void setPrimaryItemCount(int count);
}
