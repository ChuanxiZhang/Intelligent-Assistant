 package org.xianghui.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.service.CommonService;
import org.xianghui.activity.ScheduleShowActivity;
import org.xianghui.application.MainApplication;
import org.xianghui.model.Schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
 /**
 * 类说明:设置日程提醒service
 * @version 创建时间：2012-5-16 上午10:10:11
 */
public class SettingScheduleService extends CommonService
{
	private static final String tag = "IntelligentAssistant";
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private CommonDao dao = null;
	private AlarmManager alarmManager = null;
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.v(tag, "SettingScheduleService启动");
		dao = ((MainApplication)getApplication()).getDao();
		alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		
		Intent intent = new Intent(this,ScheduleShowActivity.class);
		PendingIntent sender = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(sender);
		
		List<Schedule> schedules = dao.queryForList(Schedule.class, "select * from Schedule where hasAlert=0 order by remindDate asc");
		long now = System.currentTimeMillis();
		
		if(schedules!=null&&schedules.size()>0)
		{
			for(Schedule schedule :schedules)
			{
				if(schedule.getRemindDate().getTime()>now)
				{
					intent = new Intent(this,ScheduleShowActivity.class);
					Log.v(tag, "scheduleId = "+schedule.getId());
					intent.putExtra("scheduleId", schedule.getId().intValue());
					sender = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					alarmManager.set(AlarmManager.RTC_WAKEUP, schedule.getRemindDate().getTime(), sender);
					Log.v(tag, "添加下一个日程："+sdf.format(schedule.getRemindDate())+" title:"+schedule.getTitle());
					break;
				}else
				{
					schedule.setHasAlert(true);
					dao.update(schedule);
				}
				
			}
		}
		
		this.stopSelf();
	}

	@Override
	public void onDestroy()
	{
		Log.v(tag, "结束SettingScheduleService");
		super.onDestroy();
	}
}
