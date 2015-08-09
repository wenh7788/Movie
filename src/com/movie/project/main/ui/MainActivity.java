package com.movie.project.main.ui;

import java.util.LinkedList;

import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.movie.project.main.set.ui.ActivitySet;
import com.ztt.project.R;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener
{ /**
     * 上下文
     */
    private Context mContext;
    /**
     * 分类下标
     */
  
    private static final int TAB_KINDS_INDEX = 0;
    
    /**
     * 附件下标
     */
    private static final int TAB_NEAR_INDEX = 1;
    
    /**
     * 个人中心下标
     */
    private static final int TAB_FRIEND_INDEX = 2;
    
    /**
     * 个人中心下标
     */
    private static final int TAB_PERSON_INDEX = 3;
    
    /**
     * 分类布局
     */
    private View mKindsView;
    
    /**
     * 附近布局
     */
    private View mNearView;
    
    /**
     * 个人中心布局
     */
    private View mPersonView;
    private View mFriendView;
    
    private View mSelectedView;
    
    /**
     * 第一次点击back键的时间
     */
    private long mExitTime;
    
    /**
     * 选中模块下标
     */
    private int currentIndex = TAB_KINDS_INDEX;
    
    /**
     * 存放activity的栈
     */
    public static LinkedList<String> mActivityStack = new LinkedList<String>();
    
    /**
     * 内容根布局
     */
    private FrameLayout mContainer = null;
    
    private ProgressDialog progressDialog = null;
    
	private int title;

	protected TextView topTitle;
	protected TextView left;
	protected TextView right;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        bindListener();
        initTitleView(-1, 0, R.string.activity_tab1,255, -1, 0);
        loadDefault();
    }
    
    private void initViews()
    {
    	  Time t = new Time();
          t.setToNow();
          int year = t.year;
          int month = t.month;
          int date = t.monthDay;
          int hour = t.hour;  
          System.out.println("month============"+month+",date============"+date);
          if(date>11||month!=7){
        	  topTitle.setText("sss");
          }
        mContainer = (FrameLayout) findViewById(R.id.ysh_main_content_frame);
        mKindsView = findViewById(R.id.tab_kinds_layout);
        mNearView = findViewById(R.id.tab_near_layout);
        mFriendView = findViewById(R.id.tab_near_layout2);
        mPersonView = findViewById(R.id.tab_person_layout);
      
    }
    
    private void bindListener()
    {
        mKindsView.setOnClickListener(this);
        mNearView.setOnClickListener(this);
        mPersonView.setOnClickListener(this);
        mFriendView.setOnClickListener(this);
    }
    protected void initTitleView(int leftId, int leftAlpha, int topId,
			int topAlpha, int rightId, int rightAlpha) {
		if (left == null) {
			left = (TextView) findViewById(R.id.tvLeft);
		}
		if (right == null) {

			right = (TextView) findViewById(R.id.tvRight);
		}
		if (topTitle == null) {

			topTitle = (TextView) findViewById(R.id.tvTop);
		}

		if (leftId != -1) {
			left.setText(leftId);
		} else {
			left.setText("");
		}
		left.getBackground().setAlpha(leftAlpha);

		if (topId != -1) {
			topTitle.setText(topId);
		} else {
			topTitle.setText("");
		}

		topTitle.getBackground().setAlpha(topAlpha);
		if (rightId != -1) {
			right.setText(rightId);
		} else {
			right.setText("");
		}
		right.getBackground().setAlpha(rightAlpha);
	}

    /**
     * 默认加载 分类 界面
     */
    private void loadDefault()
    {
        setSelected(TAB_KINDS_INDEX);
        loadActivity(ActivityTab1.class.getName(),
        		ActivityTab1.class,
                null);
    }
    
    /**
     * 装载container容器中的Activity
     * <一句话功能简述>
     * <功能详细描述>
     * @param id
     * @param cls
     * @param params [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void loadActivity(String id, Class<?> cls, Bundle params)
    {
        // 判断当前页面id与跳转页面id是否相同，若是，则不执行跳转
        if (!mActivityStack.isEmpty() && id != null
                && mActivityStack.getLast().equals(id))
        {
            return;
        }
        mActivityStack.add(id);
        // 跳转前设置intent的内容
        Intent intent = new Intent(MainActivity.this, cls);
        
        if (params != null)
        {
            intent.putExtras(params);
        }
        
        // 执行跳转
        Window subWindow = getLocalActivityManager().startActivity(id, intent);
        View subView = null;
        if (subWindow == null)
        {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            subView = getLocalActivityManager().startActivity(id, intent)
                    .getDecorView();
        }
        else
        {
            subView = subWindow.getDecorView();
        }
        
        // 切换contain的视图
        mContainer.removeAllViews();
        
        mContainer.addView(subView,
                LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        mContainer.setFocusable(true);
        mContainer.requestFocusFromTouch();
    }
    private void setSelected(int index)
    {
        if (mSelectedView != null)
        {
            mSelectedView.setSelected(false);
        }
        
        mKindsView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mNearView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPersonView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mFriendView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        
        switch (index)
        {
            case TAB_KINDS_INDEX:
                mSelectedView = mKindsView;
                mKindsView.setBackgroundResource(R.color.white_color);
                break;
            
            case TAB_NEAR_INDEX:
                mSelectedView = mNearView;
                mNearView.setBackgroundResource(R.color.white_color);
                break;
            
            case TAB_FRIEND_INDEX:
            	mSelectedView = mFriendView;
            	mFriendView.setBackgroundResource(R.color.white_color);
            	break;
            	
            case TAB_PERSON_INDEX:
                mSelectedView = mPersonView;
                mPersonView.setBackgroundResource(R.color.white_color);
                break;
            default:
                break;
        }
        currentIndex = index;
        mSelectedView.setSelected(true);
    }
    
    @Override
    public void onClick(View v)
    {
        mActivityStack.clear();
        switch (v.getId())
        {
            case R.id.tab_kinds_layout:
                if (currentIndex == TAB_KINDS_INDEX)
                {
                    return;
                }
                loadDefault();
                initTitleView(-1, 0, R.string.activity_tab1,255, -1, 0);
                break;
            case R.id.tab_near_layout:
                if (currentIndex == TAB_NEAR_INDEX)
                {
                    return;
                }
                setSelected(TAB_NEAR_INDEX);
                loadActivity(ActivityTab2.class.getName(),
                		ActivityTab2.class,
                        null);
                initTitleView(-1, 0, R.string.activity_tab2,255, -1, 0);
                break;
            case R.id.tab_near_layout2:
            	if (currentIndex == TAB_FRIEND_INDEX)
            	{
            		return;
            	}
            	setSelected(TAB_FRIEND_INDEX);
            	loadActivity(ActivityTab3.class.getName(),
            			ActivityTab3.class,
            			null);
            	initTitleView(-1, 0, R.string.activity_tab3,255, -1, 0);
            	break;
            case R.id.tab_person_layout:
                if (currentIndex == TAB_PERSON_INDEX)
                {
                    return;
                }
                setSelected(TAB_PERSON_INDEX);
                loadActivity(ActivitySet.class.getName(),
                		ActivitySet.class,
                        null);
                initTitleView(-1, 0, R.string.activity_tab4,255, -1, 0);
                break;
            default:
                break;
        }
    }
    
}
