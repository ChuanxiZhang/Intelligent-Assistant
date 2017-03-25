 package org.xianghui.util;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
 /**
 * 类说明:震动工具类
 * @version 创建时间：2012-5-16 上午11:15:12
 */
public class VibratorUtil
{
	public static void vibrate(Context context)
	{
		vibrate(context,new long[]{100,500,100,500}, -1);//-1表示不重复
	}
	
	public static void vibrate(Context context,int repeat)
	{
		vibrate(context,new long[]{100,500,100,500},repeat);//-1表示不重复
	}
	
	public static void vibrate(Context context,long []partten,int repeat)
	{
		Vibrator vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(partten, repeat);//-1表示不重复
	}
}
