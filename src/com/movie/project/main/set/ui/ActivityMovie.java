package com.movie.project.main.set.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.movie.ui.base.BaseActivity;
import com.ztt.project.R;

public class ActivityMovie extends BaseActivity
{
    
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        initViews();
    }

    private void initViews()
    {
//        title = (TextView) findViewById(R.id.tv);
//        title.setText("1111111111111111");
    }
}
