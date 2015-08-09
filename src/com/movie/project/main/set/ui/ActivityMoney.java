package com.movie.project.main.set.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.movie.ui.base.BaseActivity;
import com.ztt.project.R;

public class ActivityMoney extends BaseActivity
{
    
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        
        initViews();
    }

    private void initViews()
    {
//        title = (TextView) findViewById(R.id.tv);
//        title.setText("1111111111111111");
    }
}
