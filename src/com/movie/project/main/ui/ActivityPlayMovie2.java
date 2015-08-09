package com.movie.project.main.ui;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ListView;

import com.movie.ui.base.BaseActivity;
import com.ztt.project.R;

public class ActivityPlayMovie2 extends BaseActivity implements OnPreparedListener{
	private String paths = "http://cevin.qiniudn.com/jieqoo-HD.mp4";
	private MediaPlayer mediaPlayer;
	private ListView listview;
	private SurfaceView surfaceView;
	private boolean pause;
	private int position;
	private SurfaceHolder surfaceHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_movie2);
//		initView();
	}

	private void initView() {
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);  
		surfaceHolder = surfaceView.getHolder();
		// 设置Holder类型,该类型表示surfaceView自己不管理缓存区,虽然提示过时，但最好还是要设置  
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
		// 设置surface回调  
		surfaceHolder.addCallback(new SurfaceCallback());  
	}
	
	// SurfaceView的callBack  
    private class SurfaceCallback implements SurfaceHolder.Callback {  
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {  
            // SurfaceView的大小改变  
        }  
  
        public void surfaceCreated(SurfaceHolder holder) {  
            // surfaceView被创建  
            // 设置播放资源  
            playVideo();  
        }  
  
        public void surfaceDestroyed(SurfaceHolder holder) {  
            // surfaceView销毁  
            // 如果MediaPlayer没被销毁，则销毁mediaPlayer  
            if (null != mediaPlayer) {  
                mediaPlayer.release();  
                mediaPlayer = null;  
            }  
        }  
    }  
    
    /** 
     * 播放视频 
     */  
    public void playVideo() {  
        // 初始化MediaPlayer  
        mediaPlayer = new MediaPlayer();  
        // 重置mediaPaly,建议在初始滑mediaplay立即调用。  
        mediaPlayer.reset();  
        // 设置声音效果  
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  
       // 设置播放完成监听  
//        mediaPlayer.setOnCompletionListener(this);  
        // 设置媒体加载完成以后回调函数。  
        mediaPlayer.setOnPreparedListener(this);  
        // 错误监听回调函数  
//        mediaPlayer.setOnErrorListener(this);  
        // 设置缓存变化监听  
//        mediaPlayer.setOnBufferingUpdateListener(this);  
        Uri uri = Uri  
                .parse(this.paths);  
        try {  
             mediaPlayer.reset();  
            mediaPlayer.setDataSource(ActivityPlayMovie2.this, uri);  
            // 设置异步加载视频，包括两种方式 prepare()同步，prepareAsync()异步  
            mediaPlayer.prepareAsync();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }

	@Override
	public void onPrepared(MediaPlayer arg0) {
		  // 播放视频  
        mediaPlayer.start();  
        // 设置显示到屏幕  
        mediaPlayer.setDisplay(surfaceHolder);  
        // 设置surfaceView保持在屏幕上  
        mediaPlayer.setScreenOnWhilePlaying(true);  
        surfaceHolder.setKeepScreenOn(true);  
        // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误  
     /*   seekBar.setProgress(0);  
        seekBar.setMax(mediaPlayer.getDuration());  
        // 设置播放时间  
        videoTimeString = getShowTime(mediaPlayer.getDuration());  
        vedioTiemTextView.setText("00:00:00/" + videoTimeString);  
        // 设置拖动监听事件  
        seekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());  
        // 设置按钮监听事件  
        // 重新播放  
        replayButton.setOnClickListener(SurfaceViewTestActivity.this);  
        // 暂停和播放  
        playButton.setOnClickListener(SurfaceViewTestActivity.this);  
        // 截图按钮  
        screenShotButton.setOnClickListener(SurfaceViewTestActivity.this);  
        seekBarAutoFlag = true;  
        // 开启线程 刷新进度条  
        thread.start();  */
	}  

}
