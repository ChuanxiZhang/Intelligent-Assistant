 package org.xianghui.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.service.CommonService;
import org.xianghui.application.MainApplication;
import org.xianghui.broadcastreciever.MuteBroadcastReciever;
import org.xianghui.broadcastreciever.UnMuteBroadcastReciever;
import org.xianghui.model.Mute;
import org.xianghui.util.ClassTimeUtil;
import org.xianghui.util.DateFormatUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
 /**
 * 类说明:
 * 监听静音时间
 * @version 创建时间：2012-5-14 下午7:51:27
 */
public class PerDaySettingService extends CommonService
{
	private static final String tag = "IntelligentAssistant";
	
	private CommonDao dao = null;
	private AlarmManager alarmManager = null;
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.v(tag, "PerDaySettingService启动");
		
		dao = ((MainApplication)this.getApplication()).getDao();
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
		Log.v(tag, "今天星期"+week);
		List<Mute>mutes = dao.queryForList(Mute.class, "select * from Mute where week=?",new String[]{week+""});
		
		if(mutes==null || mutes.size()<=0)
		{
			Log.v(tag, "本天未找到静音设置");
			this.stopSelf();
			return;
		}
		
		
		for(Mute mute : mutes)
		{
			Date from = ClassTimeUtil.getFromTime(mute.getNumber());
			Date to = ClassTimeUtil.getToTime(mute.getNumber());
			
			if(to.getTime()<System.currentTimeMillis())
			{
				continue;
			}
			
			Intent intent = new Intent(MuteBroadcastReciever.ACTOIN);
			PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
			alarmManager.set(AlarmManager.RTC_WAKEUP, from.getTime(), sender);
			
			intent = new Intent(UnMuteBroadcastReciever.ACTOIN);
			sender = PendingIntent.getBroadcast(this, 0, intent, 0);
			alarmManager.set(AlarmManager.RTC_WAKEUP,to.getTime(), sender);
			
			Log.v(tag, "第"+mute.getNumber()+"节课 开始静音时间"+DateFormatUtil.get_yyyy_MM_dd_HH_mm_ss(from)+" 结束静音时间"+DateFormatUtil.get_yyyy_MM_dd_HH_mm_ss(to));
		}
		
		stopSelf();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.v(tag, "PerDaySettingService关闭");
	}
}
