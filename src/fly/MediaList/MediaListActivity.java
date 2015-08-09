package fly.MediaList;

import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ztt.project.R;

import fly.utils.SDCardMedia;
/**
 * ��ʾ��Ƶ�б�
 * @author liangfei
 *
 */
public class MediaListActivity extends ListActivity 
{
	private  List<Map<String, Object>> videoDataList;
	private  Map<String, Object> videoMap;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	 {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medialist);
		ShowMediaList();
	}
	private void ShowMediaList() 
	{
		getAllVideos();
		setListAdapter(new MediaAdapter(this, R.layout.media_row, videoDataList));
	}
	
	private void getAllVideos() 
	{
		videoDataList=new SDCardMedia(this).getAllVideos();		
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) //��Ŀ���ʱ�����ؽ��
	{
		videoMap = videoDataList.get(position);
		Intent data = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("title", (String)videoMap.get("title"));
		bundle.putString("type", (String)videoMap.get("type"));
		bundle.putString("path", (String)videoMap.get("path"));
		bundle.putLong("size", (Long)videoMap.get("size"));
		data.putExtras(bundle);
		setResult(3, data);
		finish();
	}
}
