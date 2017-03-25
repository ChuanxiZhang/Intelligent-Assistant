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
 * ��˵��:�����㲥
 * @version ����ʱ�䣺2012-5-14 ����7:10:20
 */
public class MuteBroadcastReciever extends BroadcastReceiver
{
	public static final String ACTOIN = "org.xianghui.broadcastreciever.MuteBroadcastReciever";
	private static final String tag = "IntelligentAssistant";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.v(tag, DateFormatUtil.get_yyyy_MM_dd_HH_mm_ss(new Date())+"��ʼ����");
		
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		
		VibratorUtil.vibrate(context);
		
		ViewUtil.toast(context, "��ʼ����");
	}

}
