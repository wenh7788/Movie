package com.movie.project.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.movie.ui.base.BaseActivity;
import com.ztt.project.R;

import fly.MediaPlayer.MediaPlayerActivity;


public class ActivityTab2 extends BaseActivity{
	private GridView gvSystem;
	private MyGrideViewAdapter myGrideViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab2);
		initView();
	}

	private void initView() {
		gvSystem = (GridView) findViewById(R.id.gv_system);
		myGrideViewAdapter = new MyGrideViewAdapter();
		gvSystem.setAdapter(myGrideViewAdapter);
		gvSystem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				activityManager.startNextActivity(ActivityPlayMovie.class);
//				startActivity(new Intent(ActivityTab2.this,MediaPlayerActivity.class));
				startActivity(new Intent(ActivityTab2.this,MainActivityPlayMovie.class));
//				startActivity(new Intent(ActivityTab2.this,MediaPlayerActivity.class));
			}
		});
	}
	
	
	private class MyGrideViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 20;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			if(view==null){
				view=LayoutInflater.from(ActivityTab2.this).inflate(R.layout.item_listview_system, null);
			}
			return view;
		}
		
	}
}
