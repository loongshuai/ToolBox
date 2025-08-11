
package hwdroid.widget.item;

import hwdroid.widget.itemview.ItemView;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.hw.droid.R;

/**
 * A DrawableItem displays a single Drawable on the left of the itemview and a
 * status icon (checkbox or radiobutton or Drawable) on the right.
 *
 */
public class DrawableText2Item extends TextItem {

    /**
     * The resource identifier for the left Drawable.
     */
    public Drawable leftDrawable;

    /**
     * The resource identifier for the right Drawable.
     */
    public Drawable rightDrawable;

    /**
     * @hide
     */
    public DrawableText2Item() {
        this(null);
    }

    /**
     * Constructs a new DrawableItem that has no Drawable and displays the given
     * text. Used as it, a DrawableItem is very similar to a TextItem
     *
     * @param text The text of this DrawableItem
     */
    public DrawableText2Item(String text) {
        this(text, "");
    }

    /**
     * Constructs a new DrawableItem that has no Drawable and displays the given
     * text. Used as it, a DrawableItem is very similar to a TextItem
     *
     * @param text The text of this DrawableItem
     */
    public DrawableText2Item(String text, String subtext) {
        this(text, subtext, null, null);
    }

    /**
     * Constructs a new DrawableItem that has no Drawable and displays the given
     * text. Used as it, a DrawableItem is very similar to a TextItem
     *
     * @param text The text of this DrawableItem
     */
    public DrawableText2Item(String text, String subtext, Drawable leftDrawable) {
        this(text, subtext, leftDrawable, null);
    }

    /**
     * Constructs a new DrawableText2Item that has no Drawable and displays the given
     * text. Used as it, a DrawableText2Item is very similar to a TextItem
     *
     * @param text The text of this DrawableItem
     */
    public DrawableText2Item(String text, String subtext, Drawable leftDrawable, Drawable rightDrawable) {
        super(text);
        this.leftDrawable = leftDrawable;
        this.rightDrawable = rightDrawable;
        this.mSubText = subtext;
    }

    @Override
    public ItemView newView(Context context, ViewGroup parent) {
        return createCellFromXml(context, R.layout.hw_drawable_text_2_item_view, parent);
    }

    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs);

        TypedArray a = r.obtainAttributes(attrs, R.styleable.DrawableText2Item);
        //int leftId = a.getResourceId(R.styleable.DrawableText2Item_drawable, 0);
        a.recycle();
    }

}
