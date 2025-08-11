
package hwdroid.widget.FooterBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class FooterBarMenuItem extends FooterBarItem {
    public FooterBarMenuItem(Context context) {
        super(context);
    }

    public FooterBarMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected View createIconBackground(View icon) {
        return icon;
    }

    protected void setTextViewLayoutParams(Context context, TextView tv, LayoutParams lp_tv) {
        lp_tv.width = LayoutParams.MATCH_PARENT;
        lp_tv.height = 0;
        lp_tv.weight = 1;

        tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
    }
}
