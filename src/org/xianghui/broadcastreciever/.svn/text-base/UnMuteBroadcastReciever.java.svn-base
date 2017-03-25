 package org.xianghui.broadcastreciever;

import java.util.Date;

import org.pzn.common.util.ViewUtil;
import org.xianghui.util.DateFormatUtil;
import org.xianghui.util.VibratorUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
 /**
 * 类说明:取消静音
 * @version 创建时间：2012-5-14 下午7:10:57
 */
public class UnMuteBroadcastReciever extends BroadcastReceiver
{
	public static final String ACTOIN = "org.xianghui.broadcastreciever.UnMuteBroadcastReciever";
	private static final String tag = "IntelligentAssistant";
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.v(tag, DateFormatUtil.get_yyyy_MM_dd_HH_mm_ss(new Date())+"取消静音");		
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		
		VibratorUtil.vibrate(context);
		
		ViewUtil.toast(context, "取消静音");
	}
}
