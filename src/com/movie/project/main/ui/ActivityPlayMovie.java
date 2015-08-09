package com.movie.project.main.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.movie.ui.base.BaseActivity;
import com.ztt.project.R;

public class ActivityPlayMovie extends BaseActivity {
	private String path = "http://cevin.qiniudn.com/jieqoo-HD.mp4";
	private MediaPlayer mediaPlayer;
	private ListView listview;
	private MyListViewAdapter adapter;
	private SurfaceView surfaceView;
	private boolean pause;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_movie);
		initView();
//		play1(path);
	}

	void play1(String path) {
		Intent it = new Intent("com.cooliris.media.MovieView");
		it.setAction(Intent.ACTION_VIEW);
		it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Uri data = Uri.parse(path);
		it.setDataAndType(data, "video/mp4");
		startActivity(it);
	}

	private void initView() {
		listview = (ListView) findViewById(R.id.lv_comment);
		adapter = new MyListViewAdapter();
		listview.setAdapter(adapter);

		// mediaPlayer = MediaPlayer.create(getApplicationContext(),
		// Uri.parse(path));
		mediaPlayer = new MediaPlayer();
		// surfaceView = (SurfaceView) findViewById(R.id.sv);
		// 把输送给surfaceView的视频画面，直接显示到屏幕上,不要维持它自身的缓冲区
//		surfaceView.getHolder()
//				.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		surfaceView.getHolder().setFixedSize(176, 144);
//		surfaceView.getHolder().setKeepScreenOn(true);
//		surfaceView.getHolder().addCallback(new SurfaceCallback());

		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "video/*");
		startActivity(intent);
	}

	private void play(int position) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.setDisplay(surfaceView.getHolder());
			mediaPlayer.prepare();// 缓冲
			mediaPlayer.setOnPreparedListener(new PrepareListener(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final class SurfaceCallback implements Callback {
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		public void surfaceCreated(SurfaceHolder holder) {
			if (position > 0 && path != null) {
				play(position);
				position = 0;
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if (mediaPlayer.isPlaying()) {
				position = mediaPlayer.getCurrentPosition();
				mediaPlayer.stop();
			}
		}
	}

	private class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 30;
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
			if (view == null) {
				view = LayoutInflater.from(ActivityPlayMovie.this).inflate(
						R.layout.item_listview_comment, null);
			}
			return view;
		}

	}

	private final class PrepareListener implements OnPreparedListener {
		private int position;

		public PrepareListener(int position) {
			this.position = position;
		}

		public void onPrepared(MediaPlayer mp) {
			System.out.println("=============================");
			mediaPlayer.start();// 播放视频
			if (position > 0)
				mediaPlayer.seekTo(position);
		}
	}
}
