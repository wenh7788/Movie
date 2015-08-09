package com.movie.project.main.set.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.ui.base.BaseActivity;
import com.ztt.project.R;

public class ActivitySet extends BaseActivity implements OnClickListener {

	private LinearLayout llMovie;
	private LinearLayout llMoney;
	private LinearLayout llIp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		initViews();
	}

	private void initViews() {
		llMovie = (LinearLayout) findViewById(R.id.llMovie);
		llMoney = (LinearLayout) findViewById(R.id.llMoney);
		llIp = (LinearLayout) findViewById(R.id.llIp);
		llMovie.setOnClickListener(this);
		llMoney.setOnClickListener(this);
		llIp.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.llMovie:
			activityManager.startNextActivity(ActivityMovie.class);
			break;
		case R.id.llMoney:
			activityManager.startNextActivity(ActivityMoney.class);
			break;
		case R.id.llIp:
			activityManager.startNextActivity(ActivityIp.class);
			break;

		default:
			break;
		}
	}
}
