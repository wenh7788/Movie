package fly.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ztt.project.R;

import fly.MediaList.MediaListActivity;
import fly.UpdateMediaList.UpdateMediaListActivity;
import fly.utils.SDCardMedia;
import fly.utils.TimeFormate;

public class MediaPlayerActivity extends Activity 
{
    private TextView nameText,currentTime,maxTime;
    private Button chooseButon,checkButton;
    private ImageButton volumeButton;
    private ImageView goView;
    private SeekBar timebar;
    private ProgressBar volumeBar;
    private LinearLayout volumeLayout;
    private String path;
    private String filepath;
    private String[] filepaths;
    private Map<String, Object> videoMap;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private SurfaceView surfaceView;
    private ProgressDialog dialog ;
    private boolean pause,filechanged,Handlerpost;
    private int position;
    private int Index;
    private int count;
    private int maxVolume,curVolume,i;
    private boolean isMute,hasFile,isComeFromList,volumeBarVisible;
    private static final int LIST=1,UPDATELIST=2,ABOUT=3,EXIT=4;
    private static final int FILE_RESULT_CODE = 1;
    private static final int LIST_RESULT_CODE = 2;
    private static final int UPDATE_RESULT_CODE = 3;
    
    //
    private ListView listview;
    private MyListViewAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        initViews(); 
        mediaPlayer = new MediaPlayer();
        nameText = (TextView) findViewById(R.id.filename);
        currentTime =(TextView) findViewById(R.id.curtime);
        maxTime =(TextView) findViewById(R.id.maxtime);
        chooseButon =(Button) findViewById(R.id.choose);
        checkButton =(Button) findViewById(R.id.check);
        volumeButton =(ImageButton) findViewById(R.id.volumebutton);
        volumeLayout =(LinearLayout) findViewById(R.id.volumeLayout);
        volumeBar =(ProgressBar) findViewById(R.id.volumeBar);
        chooseButon.setOnClickListener(new ChooseListenet());
        checkButton.setOnClickListener(new CheckListenet());
        goView =(ImageView) findViewById(R.id.gobutton);
        timebar =(SeekBar) findViewById(R.id.timebar);
        timebar.setOnSeekBarChangeListener(new SeekBarListener());
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        
        surfaceView.setOnTouchListener(new TouchListener());
        //�����͸�surfaceView����Ƶ���棬ֱ����ʾ����Ļ��,��Ҫά��������Ļ�����
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );//ȡ���������
		volumeBar.setMax(maxVolume);
        //serach for last if has lastpath
        //nameText.setText("�ϴβ���:"+lastpath);
    }
    private void initViews() {
    	listview = (ListView) findViewById(R.id.lv_comment);
		adapter = new MyListViewAdapter();
		listview.setAdapter(adapter);
		
	}
	/*��SurfaceView���ڵ�Activity�뿪��ǰ̨,SurfaceView�ᱻdestroy,
     *��Activity�ֻص���ǰ̨ʱ��SurfaceView�ᱻ���´�������������OnResume()����֮�󱻴���*/
    private final class SurfaceCallback implements Callback
    {
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
		public void surfaceCreated(SurfaceHolder holder) //����SurfaceViewʱ��ʼ���ϴ�λ�ò��Ż����²���
		{
			if(isComeFromList)
			{
				playMedia();
				isComeFromList=false;
				curVolume= audioManager.getStreamVolume( AudioManager.STREAM_MUSIC );//ȡ�õ�ǰ����
			}
			else if(position>0 && path!=null)
			{
				play(position);
				position = 0;
			}
		}
		public void surfaceDestroyed(SurfaceHolder holder) //�뿪SurfaceViewʱֹͣ���ţ����沥��λ��
		{
			if(mediaPlayer==null)return;
			if(mediaPlayer.isPlaying())
			{
				position = mediaPlayer.getCurrentPosition();
				mediaPlayer.stop();
			}
		}
    }
	@Override
	protected void onDestroy()//Activity����ʱ�ͷ�mediaPlayer����������ӹ���Ƶ�б��ٴδ�ʱ��
	{
		if(mediaPlayer!=null){
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		this.pauseMedia();
		super.onPause();
	}
	class ChooseListenet implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			ChooseFile();
		}
	}
	public void ChooseFile()//ѡ����Ƶ�ļ�
	{
		Intent intent = new Intent();
    	intent.setClassName(this, "fly.ChooseFile.FileChooserActivity");
		startActivityForResult(intent, FILE_RESULT_CODE);	
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)//�õ���ͬActivity�ķ��ؽ��
	{
		Bundle bundle = null;
		switch(requestCode)
		{
		   case FILE_RESULT_CODE:              //�����ļ�ѡ�񷵻صĽ��
				if(data!=null&&(bundle=data.getExtras())!=null)
				{
					 List<String> filelist=bundle.getStringArrayList("filelist");
					 count=filelist.size();
					 filepaths=new String[count];
					 int i=0;
					 for (Iterator<String> iterator = filelist.iterator(); iterator.hasNext();i++)
					 {
						 filepaths[i]= (String) iterator.next();
					 }
					 hasFile=data.getBooleanExtra("hasFile", false);
					 if(hasFile)
					 {
						 filepath = filepaths[0];
						 boolean exist=SDCardMedia.addVideos(filepaths);//��ѡ������ӵ������б���
						 nameText.setText(filepath.substring(filepath.lastIndexOf("/")+1));
						 if(exist)
						   Toast.makeText(this, R.string.existInList, 1).show(); 
						 else
						   Toast.makeText(this, R.string.success, 1).show();
					 }
					 else
						 Toast.makeText(this, R.string.NoFileSelected, 1).show();
				}
				break;
		   case LIST_RESULT_CODE:				//����Ӳ����б��صĽ��
			    if(data!=null&&(bundle=data.getExtras())!=null)
			    {
				   videoMap=new HashMap<String, Object>();
				   videoMap.put("title",bundle.getString("title"));
				   videoMap.put("path",bundle.getString("path"));
				   videoMap.put("type",bundle.getString("type"));
				   videoMap.put("size",bundle.getLong("size"));
				   nameText.setText((String)videoMap.get("title"));
				   filepath=(String)videoMap.get("path");
				   isComeFromList=true;//������ֻ��Ҫ���ñ�־��Ϊtrue���ɣ���������MediaPlayer,��Ϊ����Activityʱ��SurfaceCallback����жϸñ�־�Զ�����
				   filepaths=SDCardMedia.getAllPaths();
				   count=filepaths.length;
			    }
			    break;
		  case UPDATE_RESULT_CODE:          //����ѡ����·�ʽ���صĽ��
			   if(data!=null)
			   {
				 int choice = data.getIntExtra("choice", 0);
				 if(choice==1)
				 {
					Intent intent = new Intent();
			    	intent.setClassName(this, "fly.ChooseFile.FileChooserActivity");
					startActivityForResult(intent, FILE_RESULT_CODE);
				 }
				 else
				 {
					showUpdateDialog(); //��ʾ�ȴ��Ի���
				 }
			   }
			   break;
		}
	}
	private void showUpdateDialog() //��ʾ�ȴ��Ի���
	{
		dialog = new ProgressDialog(this);
		dialog.setTitle("���Ժ�");// ����ProgressDialog ����
		dialog.setMessage("����ɨ��洢��......");// ����ProgressDialog��ʾ��Ϣ
		dialog.setIcon(R.drawable.search);// ����ProgressDialog����ͼ��
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���ý�������񣬷��ΪԲ�Σ���ת�� 
		dialog.setIndeterminate(false);// ����ProgressDialog �Ľ������Ƿ���ȷ false ���ǲ�����Ϊ����ȷ 
		dialog.setCancelable(true); // ����ProgressDialog �Ƿ���԰��˻ؼ�ȡ��   
		dialog.show();
		String rootpath=Environment.getExternalStorageDirectory().getPath();
		new Thread(new UpdateThread(rootpath)).start();//�������̣߳������߳���ɨ��SD��
	}
	class UpdateThread implements Runnable
	{
		private String rootpath;
		public UpdateThread(String rootpath) 
		{
			this.rootpath = rootpath;
		}
		@Override
		public void run() 
		{
			int count=SDCardMedia.scanSDMedia(rootpath);//ɨ��SD���ϵ���Ƶ�ļ�
			dialog.cancel();
			Message msg=handler.obtainMessage();
			Bundle bundle=msg.getData();
			bundle.putInt("count", count);
			msg.setData(bundle);
			handler.sendMessage(msg);//ɨ����Ϸ�����Ϣ��������ʾ
		}
	}
	Handler handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			int count=msg.getData().getInt("count");
			Toast.makeText(MediaPlayerActivity.this, "��������"+count+"����¼�������鿴�б�ť��MENU���鿴", 1).show();
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) //�����˵�
	{
		menu.add(0, LIST, 1, R.string.playlist);
		menu.add(0, UPDATELIST, 2, R.string.updatelist);
		menu.add(0, ABOUT,3, R.string.about);
		menu.add(0, EXIT, 4, R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) 
	{
		if (item.getItemId()==LIST)//���²����б�˵���
		{
		  ShowMediaList();//��ʾ�����б�
		} 
		if (item.getItemId()==UPDATELIST)//���¸����б�˵���
		{
			Intent intent=new Intent(this, UpdateMediaListActivity.class);
			startActivityForResult(intent, UPDATE_RESULT_CODE);
		}
		if (item.getItemId()==ABOUT) //���¹��ڲ˵���
		{
			showAboutDialog();
		}
		if(item.getItemId()==EXIT)//�����˳��˵���
		{
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	private void ShowMediaList() //��ʾ��Ƶ�б�
	{
		Intent intent=new Intent(this, MediaListActivity.class);
		startActivityForResult(intent, LIST_RESULT_CODE);
	}
	
	class CheckListenet implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			ShowMediaList();//�鿴��Ƶ�б�
		}
	}
	public void setvolume(View v)//��������
	{
		switch (v.getId())
		{
		  case R.id.volumebutton:
			   if(mediaPlayer.isPlaying())
			   {
			     if (isMute) 
			     {
                     audioManager.setStreamMute(AudioManager.STREAM_MUSIC ,false);
			         volumeButton.setImageResource(R.drawable.volume);
			     }
			     else 
			     {
                     audioManager.setStreamMute(AudioManager.STREAM_MUSIC , true);
                     volumeButton.setImageResource(R.drawable.mute);
			     }
			     isMute=!isMute;
			   }
			   break;
		  case R.id.upbutton:
			   if(!isMute&&mediaPlayer.isPlaying())
			   {
				   curVolume++;
				   if(curVolume>maxVolume)
					  curVolume=maxVolume;
				   mediaPlayer.setVolume(curVolume, curVolume);
				   volumeBar.setProgress(curVolume);
				   volumeLayout.setVisibility(ViewGroup.VISIBLE);
				   volumeBarVisible=true;
			   }
			   
			   break;   
		  case R.id.downbutton:
			  if(!isMute&&mediaPlayer.isPlaying())
			   {
				   curVolume--;
				   if(curVolume<0)
					  curVolume=0;
				   mediaPlayer.setVolume(curVolume, curVolume);
				   volumeBar.setProgress(curVolume);
				   volumeLayout.setVisibility(ViewGroup.VISIBLE); 
				   volumeBarVisible=true;
			   }
			   break;
		}
	}
	public void mediaplay(View v)//���ſ���
	{
    	switch (v.getId())
    	{
			case R.id.playbutton:
			     playMedia();
				 break;
			case R.id.pausebutton:
				 pauseMedia();
				 break;
			case R.id.resetbutton:
			     replayMedia();
				 break;
			case R.id.stopbutton:
				 stopMedia();
				 break;
			case R.id.backbutton:
				 playLastMedia();
				 break;
			case R.id.nextbutton:
				 palyNextMedia();
				 break;
			case R.id.gobutton:
				 continueMedia();
				 break;
		}
    }
	public void playMedia()//������Ƶ
	{
		filepath="http://cevin.qiniudn.com/jieqoo-HD.mp4";
		if(filepath!=null&&!filepath.equals(""))
		{
			if(filepath.startsWith("http"))//���߲���HTTP��Ƶ��Դ
			{
				path = filepath;
				if(pause)goView.setVisibility(ViewGroup.INVISIBLE);
				play(0);
				filechanged=true;
				curVolume= audioManager.getStreamVolume( AudioManager.STREAM_MUSIC );//ȡ�õ�ǰ����
				if(!Handlerpost)
				{
				   updateBarHandler.post(updateThread);
				   Handlerpost=true;
				}
			}
			else//���ű�����Ƶ
			{
				File file = new File(filepath);
				if(file.exists())
				{
					path = file.getAbsolutePath();
					if(pause)goView.setVisibility(ViewGroup.INVISIBLE);
					play(0);
					filechanged=true;
					curVolume= audioManager.getStreamVolume( AudioManager.STREAM_MUSIC );//ȡ�õ�ǰ����
					if(!Handlerpost)
					{
					   updateBarHandler.post(updateThread);
					   Handlerpost=true;
					}
				}
				else
				{
					path = null;
					Toast.makeText(this, R.string.filenoexsit, 1).show();
				}
			}
		}
		else
			Toast.makeText(this, R.string.notchoose, 1).show();
	}
	public void pauseMedia() //��ͣ����
	{
		if(mediaPlayer.isPlaying())
		{
			mediaPlayer.pause();
			pause = true;
			goView.setVisibility(ViewGroup.VISIBLE);
		}
		else
		{
			continueMedia();
		}
	}
	public void replayMedia() //�ز�
	{
		if(mediaPlayer.isPlaying())
		{
		   mediaPlayer.seekTo(0);
		}
		else
		{
			if(path!=null)
			{
			   if(pause)goView.setVisibility(ViewGroup.INVISIBLE);
			   play(0);
			}
		}
		if(path!=null)
		{
			if(!Handlerpost)
			 {
			   updateBarHandler.post(updateThread);
			   Handlerpost=true;
			 }
		}
	}
	public void stopMedia()//ֹͣ����
	{
		if(mediaPlayer.isPlaying())
		{
		   mediaPlayer.stop();
		   timebar.setProgress(0);
		   currentTime.setText("00:00:00");
		   maxTime.setText("00:00:00");
		}
	}
	public void playLastMedia() //������һ����Ƶ
	{
		if(filepath!=null&&!filepath.equals(""))
		 {
			 Index--;
			 if(Index==-1)
				Index=count-1;
			 filepath = filepaths[Index];
			 path=filepath;
			 if(pause)goView.setVisibility(ViewGroup.INVISIBLE);
			 play(0);
			 filechanged=true;
			 nameText.setText(filepath.substring(filepath.lastIndexOf("/")+1));
			 if(!Handlerpost)
			 {
			   updateBarHandler.post(updateThread);
			   Handlerpost=true;
			 }
		  }
		else
			Toast.makeText(this, R.string.notchoose, 1).show();
	}
	public void palyNextMedia() //������һ����Ƶ
	{
		if(filepath!=null&&!filepath.equals(""))
		 {
			 Index++;
			 if(Index==count)
				Index=0;
			 filepath = filepaths[Index];
			 path=filepath;
			 if(pause)goView.setVisibility(ViewGroup.INVISIBLE);
			 play(0);
			 filechanged=true;
			 nameText.setText(filepath.substring(filepath.lastIndexOf("/")+1));
			 if(!Handlerpost)
			 {
				   updateBarHandler.post(updateThread);
				   Handlerpost=true;
			 }
		 }
		else
			Toast.makeText(this, R.string.notchoose, 1).show();
	}
	public void continueMedia()//����������ͣ����Ƶ
	{
		if(pause)
		{
			goView.setVisibility(ViewGroup.INVISIBLE);
			mediaPlayer.start();
			pause = false;
		 }
	}
	private void play(int position)//����mediaPlayer������Ƶ
	{
		try 
		{
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.setDisplay(surfaceView.getHolder());
			mediaPlayer.prepare();//����
			mediaPlayer.setOnPreparedListener(new PrepareListener(position));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private final class PrepareListener implements OnPreparedListener//����mediaPlayer�Ƿ񻺳����
	{
		private int position;
		public PrepareListener(int position) 
		{
		     this.position = position;
		}
		public void onPrepared(MediaPlayer mp)//�������
		{
			mediaPlayer.start();//������Ƶ
			if(position>0) 
			   mediaPlayer.seekTo(position);
		}
	}
	class TouchListener implements OnTouchListener//SurfaceView����������
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			if(event.getAction() == MotionEvent.ACTION_DOWN)//������Ļʱֹͣ���Ų���ʾֹͣ�ؼ�
			{
				 if(mediaPlayer.isPlaying())
				 {
					mediaPlayer.pause();
					pause = true;
					goView.setVisibility(ViewGroup.VISIBLE);
				 }
				return true;
			}
			return false;
		}	
	}
	Handler updateBarHandler = new Handler()//�ڲ���Ϣ�����࣬��Ҫ��ȡupdateThread������CurrentPosition��MaxPosition���ø�SeekBar
    {
		public void handleMessage(Message msg) 
		{
			if(mediaPlayer==null){
				return;
			}
			if(mediaPlayer.isPlaying())
			{
				if(filechanged)
			    {
			      timebar.setMax(msg.getData().getInt("MaxPosition")-1);
			      maxTime.setText(new TimeFormate(mediaPlayer.getDuration()).formatetime());
			      filechanged=false;
			    }
				timebar.setProgress(msg.getData().getInt("CurrentPosition"));
				currentTime.setText(new TimeFormate(mediaPlayer.getCurrentPosition()).formatetime());
				if(volumeBarVisible)//����������ʾ��Ϊ���ɼ�
				{
					i++;
					if(i==4)
					{
						volumeBarVisible=false;
						i=0;
						volumeLayout.setVisibility(ViewGroup.INVISIBLE);
					}
				}
			}
			updateBarHandler.post(updateThread);
		}
    	
    };
    Runnable updateThread = new Runnable()//�ڲ��߳��࣬��Ҫ��ȡmediaPlayer��CurrentPosition��MaxPosition���͸�Handler����
	 {
		int CurrentPosition=0,MaxPosition=0;
		@Override
		public void run() 
		{
			 Message msg = updateBarHandler.obtainMessage();
			 Bundle bundle=msg.getData();
			 CurrentPosition=mediaPlayer.getCurrentPosition();
			 bundle.putInt("CurrentPosition", CurrentPosition);
			 if(filechanged)
			 {
				 MaxPosition=mediaPlayer.getDuration();
				 bundle.putInt("MaxPosition", MaxPosition);
			 }
			 msg.setData(bundle);
			 try 
			 {
				Thread.sleep(500);//���õ�ǰ�߳�˯��500����
			 }
			 catch (InterruptedException e) 
			 {
				e.printStackTrace();
			 }
			 if((CurrentPosition > MaxPosition-1)&&(CurrentPosition!=0&&MaxPosition!=0))
			 {
			   updateBarHandler.removeCallbacks(updateThread);
			   CurrentPosition=0;
			   MaxPosition=0;
			   Handlerpost=false;
			 }
			 else
			 {
				 updateBarHandler.sendMessage(msg);
			 }
		}
	};
	 private class SeekBarListener implements SeekBar.OnSeekBarChangeListener//SeekBar������
     {
    	int startPosition;
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser)
		{
			currentTime.setText(new TimeFormate(seekBar.getProgress()).formatetime());
		}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
			startPosition=seekBar.getProgress();
		}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) //���û������Ի���Ļ���ʱ����mediaPlayer����λ����Ϊ���������Ӧλ��
		{
			currentTime.setText(new TimeFormate(seekBar.getProgress()).formatetime());
			if(mediaPlayer.isPlaying())
			   mediaPlayer.seekTo(seekBar.getProgress());
			else
			   seekBar.setProgress(startPosition);
		}   	
     }
	private void showAboutDialog() 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Product Information");
		builder.setMessage("Soft Name: \nVideoPlayer Version1.0 \nAuthor: \nMade By FlyLiang,\n@2012,June,CUMT");
		Dialog dialog = builder.create();
		dialog.show();
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
				view = LayoutInflater.from(MediaPlayerActivity.this).inflate(
						R.layout.item_listview_comment, null);
			}
			return view;
		}

	}
	
	@Override
	protected void onStop() {
		mediaPlayer.release();
    	mediaPlayer = null;
		super.onStop();
	}
	
}