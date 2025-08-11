package hwdroid.widget.searchview;

import hwdroid.widget.searchview.SearchEditBox.OnBackDownListener;
import hwdroid.widget.searchview.SearchEditBox.SearchEditBoxListener;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.hw.droid.R;

public class SearchView extends LinearLayout {
    
    public interface SearchViewListener {
        void startOutAnimation(int time);
        void startInAnimation(int time);
        void doTextChanged(CharSequence s);
    }
    
    private SearchViewListener mListener;
    private OnQueryTextListener mOnQueryChangeListener;
    private OnCloseListener mOnCloseListener;
    private OnItemClickListener mItemClickListener;
    private OnClickListener mOnDomainClickListener;
    
    private ListAdapter mAdapter;
    
    private LinearLayout mSearchLayout;
	private SearchEditBox mAutoCompleteTextView;
	private View mSearchDivider;
	private TextView mDomainLayout;
	private EditText mMaskLayout;
	private TextView mBackButton;
	private View mAnchorView;
	private CharSequence mOldQueryText;
	private String mDomainText;
	private int mActionBarHeight = 0;
	private int mStatusBarHeight = 0;

	private boolean mImeVisible;

	private final int ANIMATION_TIME = 300;

	public SearchView(Context context) {
		super(context);
		init(context);
	}

	public SearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.hw_action_bar_search_view, this);
		this.setOrientation(HORIZONTAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setBackgroundColor(getResources().getColor(R.color.hw_action_bar_search_view_background));
		
		this.mMaskLayout = (EditText) findViewById(R.id.hw_searchview_mask);

        mMaskLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    initRealEditText();
				animateToSearch();
			}
		});

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect outRect = new Rect();
				getWindowVisibleDisplayFrame(outRect);
				mImeVisible = getRootView().getHeight() - (outRect.bottom - outRect.top) > 100;
			}
		});
	}
	
	private void initRealEditText() {
	    if(mAutoCompleteTextView != null) return; // no need init again
        
	    this.mBackButton = (TextView) findViewById(R.id.hw_searchview_back);
	    this.mBackButton.setVisibility(View.VISIBLE);
	    MarginLayoutParams params = (MarginLayoutParams) mBackButton.getLayoutParams();
        params.rightMargin = - mBackButton.getWidth();
        mBackButton.setLayoutParams(params);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animateToNormal();
            }
        });
        
        ViewStub stub = (ViewStub)findViewById(R.id.hw_viewstub_searchview_atext);
        this.mSearchLayout = (LinearLayout) stub.inflate();
        this.mSearchDivider = this.mSearchLayout.findViewById(R.id.hw_searchview_divider);
        this.mDomainLayout = (TextView) this.mSearchLayout.findViewById(R.id.hw_searchview_domain);
        this.mAutoCompleteTextView = (SearchEditBox) this.mSearchLayout.findViewById(R.id.hw_searchview_atext);
        mAutoCompleteTextView.setHint(mMaskLayout.getHint());

		if (!TextUtils.isEmpty(mDomainText)) {
			this.mDomainLayout.setText(mDomainText);
			enableDomainMode();
		} else {
			disableDomainMode();
		}
        
        if(mAdapter != null) {
            this.mAutoCompleteTextView.setAdapter(mAdapter);
            this.mAutoCompleteTextView.setShowOnPopupWindows(true);
        }
        
        if(mItemClickListener != null)
            this.mAutoCompleteTextView.setOnItemClickListener(mItemClickListener);
        
        mAutoCompleteTextView.setSearchEditBoxListener(new SearchEditBoxListener(){

            @Override
            public void doTextChanged(CharSequence newText) {
                if(mListener != null) {
                    mListener.doTextChanged(newText);
                }

                if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
                    mOnQueryChangeListener.onQueryTextChange(newText.toString());
                }
                mOldQueryText = newText.toString();
            }

            @Override
            public void onQueryTextSubmit(String query) {
                if (mOnQueryChangeListener != null) {
                    mOnQueryChangeListener.onQueryTextSubmit(query);
                }
            }});
        
        mAutoCompleteTextView.setBackDownListener(new OnBackDownListener() {
            @Override
            public void onBackDown() {
                animateToNormal();
            }
        });
        
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        
        //int offset = this.getResources().getDimensionPixelSize(R.dimen.hw_action_bar_search_view_offset);
        mActionBarHeight = Math.round(getContext().getResources().getDimension(R.dimen.hw_action_bar_height));
        
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight() - mActionBarHeight - mStatusBarHeight;
                
        mAutoCompleteTextView.setDropDownHeight(height);
        mAutoCompleteTextView.setDropDownWidth(width);
        mAutoCompleteTextView.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.hw_footerbar_popup_window_background_color));
        //mAutoCompleteTextView.setDropDownVerticalOffset(offset);
	}

    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
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

    /**
     * we suggest search view should layout below the anchor view
     * if anchor view has been setup, search view will try to apply 
     * a specific animation with it.
     * 
     * @param actionBarView search view should be align the anchor view's bottom
     */
	public void setAnchorView(View actionBarView) {
		this.mAnchorView = actionBarView;
		this.setVisibility(View.VISIBLE);
	}

	 /**
     * Returns the query string currently in the text field.
     *
     * @return the query string
     */
    public CharSequence getQuery() {
        if(mAutoCompleteTextView != null)
            return mAutoCompleteTextView.getText();
        else
            return mOldQueryText;
    }

    /**
     * Sets a query string in the text field and optionally submits the query as well.
     *
     * @param query the query string. This replaces any query text already present in the
     * text field.
     */
    public void setQuery(CharSequence query) {
        if(!TextUtils.isEmpty(query)) {
            if(mAutoCompleteTextView != null) {
                mAutoCompleteTextView.setText(query);
                mAutoCompleteTextView.setSelection(mAutoCompleteTextView.getText().length());
            } else {
                mOldQueryText = query;
            }
        }
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
        mMaskLayout.setHint(hint);
        if(mAutoCompleteTextView != null)
            mAutoCompleteTextView.setHint(hint);
    }

    public void animateToNormal() {
        if(mAutoCompleteTextView == null)
            return;
		if (mImeVisible) {
			InputMethodManager imm = (InputMethodManager) getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindowToken(), 0,
					new ResultReceiver(getHandler()) {
						@Override
						protected void onReceiveResult(int resultCode,
								Bundle resultData) {
							postDelayed(new Runnable() {

								@Override
								public void run() {
									doAnimationToNormal();
								}
							}, 200);
							super.onReceiveResult(resultCode, resultData);
						}
					});
		} else {
			doAnimationToNormal();
		}
	}

    private void doAnimationToNormal() {
        if(mDomainLayout != null)
            mDomainLayout.setVisibility(View.GONE);

		mAutoCompleteTextView.setSearchMode(false);
		mAutoCompleteTextView.setText("");
		mMaskLayout.setVisibility(View.VISIBLE);
		mSearchLayout.setVisibility(View.GONE);
		mAutoCompleteTextView.dismissDropDown();
		mAutoCompleteTextView.clearFocus();
		
		final Animation backTranslate = new Animation() {
			float prerPercent = 0.0f;
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
				MarginLayoutParams params = (MarginLayoutParams) mBackButton
						.getLayoutParams();
				if (params.rightMargin >= -mBackButton.getWidth()) {
					params.rightMargin -= mBackButton.getWidth() * 1.1f
							* (interpolatedTime - prerPercent);
					if (params.rightMargin < -mBackButton.getWidth()) {
						params.rightMargin = -mBackButton.getWidth();
					}
					mBackButton.setLayoutParams(params);
					prerPercent = interpolatedTime;
				}
			}
            
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        backTranslate.setDuration(ANIMATION_TIME);
		mBackButton.startAnimation(backTranslate);

        if(mAnchorView != null) {
            Animation actionbarTranslate = new Animation() {
    			float prerPercent = 0.0f;
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    MarginLayoutParams params = (MarginLayoutParams) mAnchorView.getLayoutParams();
                	if(params.topMargin <= 0) {
    	        		params.topMargin += mAnchorView.getHeight() * 1.1f * (interpolatedTime - prerPercent);
    	        		if(params.topMargin > 0) {
    	        			params.topMargin = 0;
    	        		} 
    	        		mAnchorView.setLayoutParams(params);
    	                prerPercent = interpolatedTime;
                	}
                }
                
                @Override
                public boolean willChangeBounds() {
                    return true;
                }
    		};
    		actionbarTranslate.setDuration(ANIMATION_TIME);
			mAnchorView.startAnimation(actionbarTranslate);
        }


        if(mListener != null) {
            mListener.startOutAnimation(ANIMATION_TIME);
        }
        if(mOnCloseListener != null)
            mOnCloseListener.onClose();
    }
	
    public void animateToSearch() {
        if(mAutoCompleteTextView == null)
            return;

        if(mDomainLayout != null && !TextUtils.isEmpty(mDomainText))
            mDomainLayout.setVisibility(View.VISIBLE);

		mAutoCompleteTextView.setSearchMode(true);
		mMaskLayout.setVisibility(View.GONE);
		mSearchLayout.setVisibility(View.VISIBLE);
		mAutoCompleteTextView.requestFocus();
		Animation backTranslate = new Animation() {
            float prerPercent = 0.0f;
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
				MarginLayoutParams params = (MarginLayoutParams) mBackButton
						.getLayoutParams();
				if (params.rightMargin <= 0) {
					params.rightMargin += mBackButton.getWidth() * 1.1f
							* (interpolatedTime - prerPercent);
					if (params.rightMargin > 0) {
						params.rightMargin = 0;
					}
					mBackButton.setLayoutParams(params);
					prerPercent = interpolatedTime;
				}
			}
            
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        backTranslate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(!mAutoCompleteTextView.isPopupShowing())
					mAutoCompleteTextView.showDropDown();
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(mAutoCompleteTextView, InputMethodManager.SHOW_IMPLICIT);
				
			}
		});
        backTranslate.setDuration(ANIMATION_TIME);
        mBackButton.startAnimation(backTranslate);
        
        if(mAnchorView != null) {
	        Animation actionbarTranslate = new Animation(){
				float prerPercent = 0.0f;
	            @Override
	            protected void applyTransformation(float interpolatedTime, Transformation t) {
                    MarginLayoutParams params = (MarginLayoutParams) mAnchorView.getLayoutParams();
	            	if(params.topMargin >= -mAnchorView.getHeight()) {
		        		params.topMargin -= mAnchorView.getHeight()* 1.1f * (interpolatedTime - prerPercent);
		        		if(params.topMargin < -mAnchorView.getHeight()) {
		        			params.topMargin = -mAnchorView.getHeight();
		        		} 
		        		mAnchorView.setLayoutParams(params);
		                prerPercent = interpolatedTime;
	            	}
	            }
	            
	            @Override
	            public boolean willChangeBounds() {
	                return true;
	            }
			};
			actionbarTranslate.setDuration(ANIMATION_TIME);
			mAnchorView.startAnimation(actionbarTranslate);
        }
        
        if(mListener != null) {
            mListener.startInAnimation(ANIMATION_TIME);
        }
	}

	public void setSearchAdapter(ListAdapter adapter) {
        this.mAdapter = adapter;
        if(mAutoCompleteTextView != null) {
            this.mAutoCompleteTextView.setAdapter(adapter);
            this.mAutoCompleteTextView.setShowOnPopupWindows(true);
	    }
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        mItemClickListener = l;
        if(mAutoCompleteTextView != null)
            this.mAutoCompleteTextView.setOnItemClickListener(l);
    }

	public void setOnDomainClickListener(OnClickListener l) {
		this.mOnDomainClickListener = l;
	}

	public void setDomainText(String text) {
		this.mDomainText = shrinkDomainText(text);
		if(this.mDomainLayout != null)
			if(!TextUtils.isEmpty(text)) {
				mDomainLayout.setText(mDomainText);
				enableDomainMode();
			} else {
				disableDomainMode();
			}
	}

	private String shrinkDomainText(String oriText) {
		if(TextUtils.isEmpty(oriText)) return null;

		StringBuffer result = new StringBuffer();

		if(oriText.length() > 5) {
			result.append(oriText.substring(0, 4));
			result.append("...");
		} else {
			result.append(oriText);
		}
		return result.toString();
	}

	public void enableDomainMode() {
		if(mAutoCompleteTextView == null ||
				mDomainLayout == null || mSearchDivider == null) {
			throw new RuntimeException("enbale domain mode operation should do after layout init");
		}
		mAutoCompleteTextView.setBackgroundResource(R.drawable.hw_textfield_searchview_normal_right);
		mDomainLayout.setVisibility(View.VISIBLE);
		mSearchDivider.setVisibility(View.VISIBLE);
        mDomainLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnDomainClickListener != null)
					mOnDomainClickListener.onClick(v);
			}
		});
	}

	public void disableDomainMode() {
		if(mAutoCompleteTextView == null ||
				mDomainLayout == null || mSearchDivider == null) {
			throw new RuntimeException("disbale domain mode operation should do after layout init");
		}
		mAutoCompleteTextView.setBackgroundResource(R.drawable.hw_textfield_searchview_normal);
		mDomainLayout.setVisibility(View.GONE);
		mSearchDivider.setVisibility(View.GONE);
	}

	public String getDomainText() {
		return this.mDomainText;
	}

	/**
	 * control search view display
	 * 
	 * @param enable show search view if true or hide otherwise.
	 */
	public void enableSearchView(boolean enable) {
		this.setVisibility(enable ? View.VISIBLE : View.GONE);
	}
}
