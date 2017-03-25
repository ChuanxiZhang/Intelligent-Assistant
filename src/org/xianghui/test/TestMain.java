 package org.xianghui.test;

import java.util.Date;
import java.util.List;

import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.database.sqlite.dao.SQLManager;
import org.xianghui.broadcastreciever.MuteBroadcastReciever;
import org.xianghui.broadcastreciever.PerDaySettingBroadcastReciever;
import org.xianghui.broadcastreciever.UnMuteBroadcastReciever;
import org.xianghui.model.Contact;
import org.xianghui.model.Schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.test.AndroidTestCase;
import android.util.Log;
 /**
 * 类说明:
 * @version 创建时间：2012-5-14 下午6:59:15
 */
public class TestMain extends AndroidTestCase
{
	private static final String tag = "IntelligentAssistant";
	private CommonDao dao = null;

	
	public void test() throws Exception
	{
		dao = new CommonDao();
		dao.openDB(this.mContext, "IntelligentAssistant.db");
		
//		dao.dropTable(Mute.class);
		List<Schedule> schedules = dao.queryForList(Schedule.class, "select * from Schedule where hasAlert=0");
		
		for(Schedule s:schedules)
		{
			Log.v(tag, "标题："+s.getTitle());
			s.setHasAlert(true);
			dao.update(s);
		}
	}
	
	public void testBroadcast() throws Exception
	{
		Intent intent = new Intent(MuteBroadcastReciever.ACTOIN);
		mContext.sendBroadcast(intent);
		Log.v(tag, "广播发送完毕");
		
		
		intent = new Intent(UnMuteBroadcastReciever.ACTOIN);
		mContext.sendBroadcast(intent);

		intent = new Intent(PerDaySettingBroadcastReciever.ACTOIN);
		mContext.sendBroadcast(intent);
		Thread.sleep(3000);
	}
	public void addAlarm() throws Exception
	{
		Intent intent = new Intent(PerDaySettingBroadcastReciever.ACTOIN);
		PendingIntent sender = PendingIntent.getBroadcast(mContext, 0	, intent, 0);
		
		Date date = new Date();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		
		AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//		alarmManager.cancel(sender);
//		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, date.getTime(), 2000, sender);//1000*60*60*24
//		
//		mContext.sendBroadcast(intent);
//		Thread.sleep(10000);
	}
	
	public void SILENT() throws Exception
	{
		AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//		audioManager.setRingerMode(vibrate ? AudioManager.RINGER_MODE_VIBRATE :AudioManager.RINGER_MODE_SILENT);
		audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}
	
	public void testContactSQL() throws Exception
	{
		SQLManager manager = new SQLManager();
		
		Log.v(tag, manager.createCreateTableSQL(Contact.class));
	}
}
