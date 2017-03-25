 package org.xianghui.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.pzn.common.service.CommonService;
import org.xianghui.vo.ContactVO;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
 /**
 * 类说明:
 * @version 创建时间：2012-5-15 下午8:38:25
 */
public class IntelligentCenterService extends CommonService
{
	private static final String tag = "IntelligentAssistant";
	private static final Map<String, ContactVO> frequency = new HashMap<String, ContactVO>();

	private Timer clearTimer = null;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		
		clearTimer = new Timer();
		
		clearTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				long now  = System.currentTimeMillis();

				Log.v(tag, "开始清楚缓存号码");
				for(String key:frequency.keySet())
				{
					ContactVO cv = frequency.get(key);
					if(now - cv.getLastTime() > 1000*60*20)
					{
						frequency.remove(key);
						Log.v(tag, "清楚缓存号码:"+key);
					}
				}
				
			}
		}, 1000, 1000*50*20);
		
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		if(clearTimer!=null)
		{
			clearTimer.cancel();
			clearTimer = null;
		}
	}
	
	public static Map<String, ContactVO> getFrequency()
	{
		return frequency;
	}
	
}
