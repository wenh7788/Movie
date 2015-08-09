package fly.ChooseFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ztt.project.R;

/**
 * ѡ����Ƶ�ļ�
 * @author liangfei
 *
 */
public class FileChooserActivity extends ListActivity 
{
	private List<String> items = null;
	private List<String> paths = null;
	private List<String> files = null;
	private String rootPath = "/";
	private String curPath = "/";
	private TextView mPath;
    private int clicktime;
    private List<Integer> CheckedPostions;
    private ArrayList<String> filelist;
    private ArrayList<String> pathlist;
    private boolean folderClicked=false,hasFile;
    @Override
	protected void onCreate(Bundle icicle) 
	{
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fileselect);
		mPath = (TextView) findViewById(R.id.mPath);
		files = new ArrayList<String>();
		filelist=new ArrayList<String>();
		pathlist=new ArrayList<String>();
	    CheckedPostions = new ArrayList<Integer>();
		Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
		buttonConfirm.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				Intent data = new Intent();
				data.setClassName(FileChooserActivity.this, "fly.MediaPlayer.MediaPlayerActivity");
				Bundle bundle = new Bundle();
				if(files.isEmpty())
				{
				   filelist=pathlist;
				   hasFile=false;
				}
				else
				{
				   filelist=(ArrayList<String>)files;
				   hasFile=true;
				}
				bundle.putStringArrayList("filelist",filelist);
				data.putExtras(bundle);
				data.putExtra("hasFile", hasFile);
				setResult(2, data);
				CheckedPostions.clear();
				FileAdapter.setCheckItem(CheckedPostions);
				finish();
			}
		});
		Button buttonCancle = (Button) findViewById(R.id.buttonCancle);
		buttonCancle.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				CheckedPostions.clear();
				FileAdapter.setCheckItem(CheckedPostions);
				finish();
			}
		});
		rootPath=Environment.getExternalStorageDirectory().getPath();
		curPath=rootPath;
		getFileDir(rootPath);
		pathlist.add(rootPath);
	}

	private void getFileDir(String filePath)
	{
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();
		if (!filePath.equals(rootPath)||files==null)
		{
			items.add("b1");
			paths.add(rootPath);
			items.add("b2");
			paths.add(f.getParent());
		}
		if(files!=null)
		{
			for (int i = 0; i < files.length; i++) 
			{
				File file = files[i];
				items.add(file.getName());
				paths.add(file.getPath());
			}
		}
		setListAdapter(new FileAdapter(this, items, paths));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		File file = new File(paths.get(position));
		if (file.isDirectory()) 
		{
			CheckedPostions.clear();
			FileAdapter.setCheckItem(CheckedPostions);
			curPath = paths.get(position);
			getFileDir(curPath);
			if(!pathlist.contains(curPath))
			    pathlist.add(curPath);
			folderClicked=true;
		} 
		else 
		{
			if(getMIMEType(file).equals("video"))
			{
				if(clicktime==0)
				{
				    curPath+=File.separator+file.getName();
				    files.add(curPath);
				    folderClicked=false;
				}
				else
				{ 
					if(!folderClicked)//��ͬһ��Ŀ¼�½���ѡ��
					{
						StringBuffer filename=new StringBuffer(curPath);
						String prevChoosefile=filename.substring(filename.lastIndexOf("/")+1);
						if(!prevChoosefile.equals(file.getName()))//�ٴ�ѡ����ļ����ϴ�ѡ��Ĳ�һ��
						{
						   curPath=filename.substring(0, filename.lastIndexOf("/")+1)+file.getName();
						   if(!files.contains(curPath))
						       files.add(curPath);
							else
						       files.remove(curPath);
						}
						else
						{
							if(files.contains(curPath))
							   files.remove(curPath);
							else
							   files.add(curPath);
						}
					}
					else//������һ��Ŀ¼��ѡ��
					{
						curPath+=File.separator+file.getName();
					    files.add(curPath);
					    folderClicked=false;
					}
				}
				if(!CheckedPostions.contains(position))
				{
					CheckedPostions.add(position);
					FileAdapter.setCheckItem(CheckedPostions);
				}
				else
				{
					CheckedPostions.remove(new Integer(position));
					FileAdapter.setCheckItem(CheckedPostions);
				}
				setListAdapter(new FileAdapter(this, items, paths));
				clicktime++;
			}
		}	 
	}
	private String getMIMEType(File f) 
	{
		String type = "";
		String fName = f.getName();
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")||end.equals("xmf") || end.equals("ogg") || end.equals("wav"))
		{
			type = "audio";
		} 
		else if (end.equals("3gp") || end.equals("mp4")||end.equals("rmvb")||end.equals("avi")||
				 end.equals("flv")||end.equals("rm")||end.equals("mkv")) 
		{
			type = "video";
		} 
		else if (end.equals("jpg") || end.equals("gif") || end.equals("png")||
				 end.equals("jpeg") || end.equals("bmp"))
		{
			type = "image";
		} 
		else 
		{
			type = "*";
		}
		return type;
	}
}
