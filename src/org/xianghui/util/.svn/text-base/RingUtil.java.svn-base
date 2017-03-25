 package org.xianghui.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.MediaPlayer.OnCompletionListener;
 /**
 * 类说明:
 * @version 创建时间：2012-5-16 上午10:55:30
 */
public class RingUtil
{
	public static void ringNotification(Context context)
	{
		MediaPlayer mp = new MediaPlayer();               
		 try
		{
			mp.reset();     
			mp.setDataSource(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			mp.prepare();
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener()
			{
				@Override
				public void onCompletion(MediaPlayer mediaPlayer)
				{
					mediaPlayer.stop();
					mediaPlayer.release();
				}
			});
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
