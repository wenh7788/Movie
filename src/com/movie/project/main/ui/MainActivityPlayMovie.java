package com.movie.project.main.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.ztt.project.R;

/**
 * 
 * ���ſᲥ�ţ�û��bug.
 * 
 * @author hongdawei
 * 
 */

@SuppressLint("NewApi") public class MainActivityPlayMovie extends Activity implements OnClickListener {
	private MediaPlayer mediaPlayer;
	private String path;
	private SurfaceView surfaceView;
	private Boolean iStart = true;
	private Boolean pause = true;
	private Button issrt;
	private int position;
	private SeekBar seekbar;
	private upDateSeekBar update; // ���½�������
	private boolean flag = true; // �����ж���Ƶ�Ƿ��ڲ�����
	private LinearLayout relativeLayout;
	private Button quanping;
	private Button xiaoping;
	private final static int MEDIATHREAD = 0x11;

	private String localUrl;

	private long mediaLength = 0;
	private long readSize = 0;

	private ListView listview;
	private MyListViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر���
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Ӧ������ʱ��������Ļ������������
		setContentView(R.layout.main);
		update = new upDateSeekBar(); // �������½���������
		mediaPlayer = new MediaPlayer();
		initView();
	}

	private void initView() {
		listview = (ListView) findViewById(R.id.listview);
		adapter = new MyListViewAdapter();
		listview.setAdapter(adapter);

		llComment = (LinearLayout) findViewById(R.id.ll_comment);
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String localpath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/VideoCache/" + "tangbohu" + ".mp4";
			File f = new File(localpath);
			if (!f.exists()) {
				path = "http://211.144.85.251/tangbohu.mp4";
			} else {
				path = localpath;
			}
		} else {
			Toast.makeText(this, "SD�������ڣ�", Toast.LENGTH_SHORT).show();
		}
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		issrt = (Button) findViewById(R.id.issrt);
		issrt.setOnClickListener(this);
		surfaceView = (SurfaceView) findViewById(R.id.surface);
		relativeLayout = (LinearLayout) findViewById(R.id.btm);
		quanping = (Button) findViewById(R.id.quanping);
		xiaoping = (Button) findViewById(R.id.xiaoping);
		quanping.setOnClickListener(this);
		xiaoping.setOnClickListener(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mSurfaceViewWidth = dm.widthPixels;
		int mSurfaceViewHeight = dm.heightPixels;
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		lp.width = mSurfaceViewWidth;
		lp.height = mSurfaceViewHeight * 1 / 3;
		surfaceView.setLayoutParams(lp);
		surfaceView.getHolder().setFixedSize(lp.width, lp.height);
		surfaceView.getHolder().setKeepScreenOn(true);
		surfaceView.getHolder().addCallback(new surfaceCallBack());
		surfaceView.setOnClickListener(this);
		seekbar.setOnSeekBarChangeListener(new surfaceSeekBar());
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				issrt.setText("开始");
				pause = true;
				mediaPlayer.seekTo(0);
				seekbar.setProgress(0);
				mediaPlayer.pause();
				issrt.setBackground(getResources().getDrawable(R.drawable.paly_stop));
			}
		});
	}

	private final class surfaceSeekBar implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			int value = seekbar.getProgress() * mediaPlayer.getDuration() // �����������Ҫǰ����λ�����ݴ�С
					/ seekbar.getMax();
			mediaPlayer.seekTo(value);
		}

	}

	private final class surfaceCallBack implements Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if (position > 0) {
				play(position);
				position = 0;
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (mediaPlayer.isPlaying()) {
				position = mediaPlayer.getCurrentPosition();
				mediaPlayer.stop();
			}
		}

	}

	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.issrt:
//			relativeLayout.setVisibility(View.GONE);
			if(myCount!=null){
				myCount.cancel();
			}
			myCount = new MyCount(3000, 1000);
			myCount.start();
			if (iStart) {
				play(0);
//				issrt.setText("暂停");
				iStart = false;
				new Thread(update).start();
				issrt.setBackground(getResources().getDrawable(R.drawable.paly_stop));
			} else {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
//					issrt.setText("开始");
					pause = true;
					issrt.setBackground(getResources().getDrawable(R.drawable.paly_stop));
				} else {
					if (pause) {
//						issrt.setText("暂停");
						issrt.setBackground(getResources().getDrawable(R.drawable.play_start));
						mediaPlayer.start();
						pause = false;
					}
				}
			}
			break;
		case R.id.quanping:
			// ���ú���
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				llComment.setVisibility(View.GONE);
			}
			break;
		case R.id.xiaoping:
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				llComment.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.surface:
			if (relativeLayout.getVisibility() == View.VISIBLE) {
				relativeLayout.setVisibility(View.GONE);
			} else {
				relativeLayout.setVisibility(View.VISIBLE);
			}
			if(myCount!=null){
				myCount.cancel();
			}
			myCount = new MyCount(3000, 1000);
			myCount.start();
			break;
		}
	}

	private void writeMedia() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				FileOutputStream out = null;
				InputStream is = null;

				try {
					URL url = new URL(path);
					HttpURLConnection httpConnection = (HttpURLConnection) url
							.openConnection();

					if (localUrl == null) {
						localUrl = Environment.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/VideoCache/"
								+ "tangbohu" + ".mp4";
					}

					File cacheFile = new File(localUrl);

					if (!cacheFile.exists()) {
						cacheFile.getParentFile().mkdirs();
						cacheFile.createNewFile();
					}

					readSize = cacheFile.length();
					out = new FileOutputStream(cacheFile, true);

					httpConnection.setRequestProperty("User-Agent", "NetFox");
					httpConnection.setRequestProperty("RANGE", "bytes="
							+ readSize + "-");

					is = httpConnection.getInputStream();

					mediaLength = httpConnection.getContentLength();
					if (mediaLength == -1) {
						return;
					}

					mediaLength += readSize;

					byte buf[] = new byte[4 * 1024];
					int size = 0;

					while ((size = is.read(buf)) != -1) {
						try {
							out.write(buf, 0, size);
							readSize += size;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							//
						}
					}

					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							//
						}
					}
				}

			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
			iStart = true;
		}
		super.onDestroy();
	}

	private void play(int position) {
		mediaThread mediathread = new mediaThread();
		new Thread(mediathread).start();
	}

	private class mediaThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msgMessage = new Message();
			msgMessage.arg1 = MEDIATHREAD;
			handler.sendMessage(msgMessage);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case MEDIATHREAD:
				try {
					mediaPlayer.reset();
					mediaPlayer.setDataSource(path);
					mediaPlayer.setDisplay(surfaceView.getHolder());
					mediaPlayer.prepare();// ���л��崦��
					mediaPlayer.setOnPreparedListener(new PreparedListener(
							position));// ���������Ƿ����
				} catch (Exception e) {
				}
				break;
			}
		}
	};

	private final class PreparedListener implements OnPreparedListener {
		private int position;

		public PreparedListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start(); // ������Ƶ
			writeMedia();
			if (position > 0) {
				mediaPlayer.seekTo(position);
			}
		}
	}

	/**
	 * ���½�����
	 */
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (mediaPlayer == null) {
				flag = false;
			} else if (mediaPlayer.isPlaying()) {
				flag = true;
				int position = mediaPlayer.getCurrentPosition();
				int mMax = mediaPlayer.getDuration();
				int sMax = seekbar.getMax();
				seekbar.setProgress(position * sMax / mMax);
			} else {
				return;
			}
		};
	};
	private LinearLayout llComment;
	private MyCount myCount;

	class upDateSeekBar implements Runnable {

		@Override
		public void run() {
			mHandler.sendMessage(Message.obtain());
			if (flag) {
				mHandler.postDelayed(update, 1000);
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ��
		// �����Ļ�ķ�����������
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// ��ǰΪ������ �ڴ˴���Ӷ���Ĵ������
			relativeLayout.setVisibility(View.GONE);
			quanping.setVisibility(View.GONE);
			xiaoping.setVisibility(View.VISIBLE);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int mSurfaceViewWidth = dm.widthPixels;
			int mSurfaceViewHeight = dm.heightPixels;
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			lp.width = mSurfaceViewWidth;
			lp.height = mSurfaceViewHeight;
			surfaceView.setLayoutParams(lp);
			surfaceView.getHolder().setFixedSize(mSurfaceViewWidth,
					mSurfaceViewHeight);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// ��ǰΪ������ �ڴ˴���Ӷ���Ĵ������
			relativeLayout.setVisibility(View.GONE);
			xiaoping.setVisibility(View.GONE);
			quanping.setVisibility(View.VISIBLE);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int mSurfaceViewWidth = dm.widthPixels;
			int mSurfaceViewHeight = dm.heightPixels;
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			lp.width = mSurfaceViewWidth;
			lp.height = mSurfaceViewHeight * 1 / 3;
			surfaceView.setLayoutParams(lp);
			surfaceView.getHolder().setFixedSize(lp.width, lp.height);
		}
		super.onConfigurationChanged(newConfig);
	}

	@SuppressLint("NewApi") @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0
				&& getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			llComment.setVisibility(View.VISIBLE);
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
				view = LayoutInflater.from(MainActivityPlayMovie.this).inflate(
						R.layout.item_listview_comment, null);
			}
			return view;
		}

	}
	
	  /*定义一个倒计时的内部类*/  
    class MyCount extends CountDownTimer {     
        public MyCount(long millisInFuture, long countDownInterval) {     
            super(millisInFuture, countDownInterval);     
        }     
        @Override     
        public void onFinish() {  
        	relativeLayout.setVisibility(View.GONE);
        }     
        @Override     
        public void onTick(long millisUntilFinished) {    
        	
        }    
    } 
}
