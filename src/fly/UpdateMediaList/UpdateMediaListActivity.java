package fly.UpdateMediaList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ztt.project.R;

public class UpdateMediaListActivity extends ListActivity 
{
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.updatelist);
	List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	HashMap<String,String> map=new HashMap<String, String>();
	HashMap<String,String> map1=new HashMap<String, String>();
	map.put("choose", "�Զ�����");
	map1.put("choose", "�ֶ����");
	data.add(map);
	data.add(map1);
	SimpleAdapter listAdapter=new SimpleAdapter(this, data, R.layout.update_item, new String[]{"choose"}, new int[]{R.id.chooseText});
	setListAdapter(listAdapter);
  }
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	Intent data = new Intent();
    	data.putExtra("choice", position);
    	setResult(4, data);
    	finish();
	}
}
