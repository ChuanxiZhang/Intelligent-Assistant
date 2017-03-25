 package org.xianghui.broadcastreciever;

import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.database.sqlite.exception.OpenDataBaseErrorException;
import org.xianghui.model.Schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
 /**
 * 类说明:日程提醒广播接收器
 * @version 创建时间：2012-5-16 上午9:47:51
 */
public class ScheduleBroadcastReciever extends BroadcastReceiver
{
	public static final String ACTOIN = "org.xianghui.broadcastreciever.ScheduleBroadcastReciever";
	private static final String tag = "IntelligentAssistant";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.v(tag, "日程提醒");
		int id = intent.getIntExtra("scheduleId", -1);
		CommonDao dao = new CommonDao();
		try
		{
			dao.openDB(context, "IntelligentAssistant.db");
			
		} catch (OpenDataBaseErrorException e)
		{
			e.printStackTrace();
		}
		
		Schedule schedule = (Schedule) dao.get(Schedule.class, id);
		
	}

}
