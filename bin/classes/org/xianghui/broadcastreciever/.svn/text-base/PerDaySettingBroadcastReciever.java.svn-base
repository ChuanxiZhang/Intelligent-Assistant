 package org.xianghui.broadcastreciever;

import java.util.Date;

import org.xianghui.service.PerDaySettingService;
import org.xianghui.util.DateFormatUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
 /**
 * 类说明:
 * @version 创建时间：2012-5-14 下午7:18:32
 */
public class PerDaySettingBroadcastReciever extends BroadcastReceiver
{
	public static final String ACTOIN = "org.xianghui.broadcastreciever.PerDaySettingBroadcastReciever";
	private static final String tag = "IntelligentAssistant";
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.v(tag, DateFormatUtil.get_yyyy_MM_dd_HH_mm_ss(new Date())+"每天设置");
		
		context.startService(new Intent(context, PerDaySettingService.class));
	}

}
