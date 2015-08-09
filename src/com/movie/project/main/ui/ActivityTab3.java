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

public class ActivityTab3 extends BaseActivity{
	private GridView gvFriend;
	private MyGrideViewAdapter myGrideViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab3);
		initView();
	}
	
	
	private void initView() {
		gvFriend = (GridView) findViewById(R.id.gv_friend);
		myGrideViewAdapter = new MyGrideViewAdapter();
		gvFriend.setAdapter(myGrideViewAdapter);
		gvFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(ActivityTab3.this,ActivityPlayMovie.class));
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
				view=LayoutInflater.from(ActivityTab3.this).inflate(R.layout.item_listview_system, null);
			}
			return view;
		}
		
	}
}
