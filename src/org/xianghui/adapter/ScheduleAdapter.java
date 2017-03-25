 package org.xianghui.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xianghui.activity.R;
import org.xianghui.model.Schedule;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
 /**
 * 类说明:
 * @version 创建时间：2012-5-15 上午11:54:04
 */
public class ScheduleAdapter extends BaseAdapter
{
	private static final String tag = "IntelligentAssistant";
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private List<Schedule> schedules = new ArrayList<Schedule>();
	private List<Schedule> selectSchedules = new ArrayList<Schedule>();
	private LayoutInflater inflater = null;
	
	public ScheduleAdapter(List<Schedule> schedules,LayoutInflater inflater)
	{
		if(schedules!=null)
		{
			this.schedules = schedules;
		}
		
		this.inflater = inflater;
	}

	@Override
	public int getCount()
	{
		return schedules.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return schedules.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup group)
	{
		if(view==null)
		{
			view = inflater.inflate(R.layout.schedule_list_item, null);
		}

		TextView title = (TextView)view.findViewById(R.id.title);
		TextView startTime = (TextView)view.findViewById(R.id.startTime);
		TextView endTime = (TextView)view.findViewById(R.id.endTime);
		TextView remindTime = (TextView)view.findViewById(R.id.remindTime);
		TextView content = (TextView)view.findViewById(R.id.content);
		CheckBox box = (CheckBox)view.findViewById(R.id.checkBox);
		
		title.setText(schedules.get(position).getTitle());
		startTime.setText(sdf.format(schedules.get(position).getStartDate()));
		endTime.setText(sdf.format(schedules.get(position).getEndDate()));
		remindTime.setText(sdf.format(schedules.get(position).getRemindDate()));
		content.setText(schedules.get(position).getContent());
		box.setTag(schedules.get(position));
		
		box.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				Schedule c = (Schedule) buttonView.getTag();
				
				if(isChecked)
				{
					Log.v(tag, "add "+c.getTitle());
					selectSchedules.remove(c);
					selectSchedules.add(c);
					
				}else
				{
					selectSchedules.remove(c);
				}
				Log.v(tag, "选中数量:"+selectSchedules.size());
			}
		});
		
		return view;
	}

	public List<Schedule> getSelectSchedules()
	{
		return selectSchedules;
	}
}
