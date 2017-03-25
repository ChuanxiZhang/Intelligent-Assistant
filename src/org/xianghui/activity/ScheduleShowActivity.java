 package org.xianghui.activity;

import java.text.SimpleDateFormat;

import org.pzn.common.activity.CommonActivity;
import org.pzn.common.annotation.Component;
import org.pzn.common.database.sqlite.dao.CommonDao;
import org.xianghui.application.MainApplication;
import org.xianghui.model.Schedule;
import org.xianghui.service.SettingScheduleService;
import org.xianghui.util.RingUtil;
import org.xianghui.util.VibratorUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
 /**
 * 类说明:提醒时，显示信息
 * @version 创建时间：2012-5-15 下午10:05:54
 */
public class ScheduleShowActivity extends CommonActivity implements OnClickListener
{
	private static final String tag = "IntelligentAssistant";
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Component
	private TextView title;
	@Component
	private TextView content;
	@Component
	private TextView startTime;
	@Component
	private TextView endTime;
	@Component
	private TextView remindTime;
	@Component
	private CheckBox checkBox;
	
	
	private CommonDao dao = null;
	
	private Schedule schedule = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_show);
		dao = ((MainApplication)getApplication()).getDao();

		checkBox.setVisibility(View.GONE);
		int id = getIntent().getIntExtra("scheduleId", -1);
		schedule = (Schedule) dao.get(Schedule.class, id);
		
		if(schedule!=null)
		{
			Log.v(tag, "找到schedule的id为"+schedule.getId());
			title.setText(schedule.getTitle());
			content.setText(schedule.getContent());
			startTime.setText(sdf.format(schedule.getStartDate()));
			endTime.setText(sdf.format(schedule.getEndDate()));
			remindTime.setText(sdf.format(schedule.getRemindDate()));
			schedule.setHasAlert(true);
			dao.update(schedule);
			startService(new Intent(this, SettingScheduleService.class));
			RingUtil.ringNotification(this);
			VibratorUtil.vibrate(this);
			
		}else
		{
			Log.v(tag, "查找不到ID为"+id+"的日程");
			startService(new Intent(this, SettingScheduleService.class));
			finish();
		}
		
	}
	

	@Override
	public void onClick(View v)
	{
	}
}
