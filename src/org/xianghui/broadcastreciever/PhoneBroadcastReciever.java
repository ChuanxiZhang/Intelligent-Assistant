package org.xianghui.broadcastreciever;

import java.lang.reflect.Method;

import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.database.sqlite.exception.OpenDataBaseErrorException;
import org.pzn.common.util.ViewUtil;
import org.xianghui.activity.R;
import org.xianghui.common.Global;
import org.xianghui.model.Contact;
import org.xianghui.service.IntelligentCenterService;
import org.xianghui.vo.ContactVO;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.RemoteException;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

/**
 * 类说明:来电拦截的BroadCast
 * 
 * @version 创建时间：2012-4-27 下午5:37:13
 */

public class PhoneBroadcastReciever extends BroadcastReceiver
{
	private static final String tag = "IntelligentAssistant";
	
	private static AudioManager audioManager = null;
	private static TelephonyManager telephonyManager = null;
	private static ITelephony iTelephony = null;
	private static CommonDao dao = null;
	
	private int ringnerMode = 0;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		
		String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		
		if(dao == null)
		{
			dao = new CommonDao();
			try
			{
				dao.openDB(context, "IntelligentAssistant.db");
			} catch (OpenDataBaseErrorException e)
			{
				e.printStackTrace();
			}
		}
		
		if(telephonyManager == null)
		{
			telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		}
		
		if(audioManager == null)
		{
			audioManager =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		}
		
		if(iTelephony == null)
		{
			try 
			{   
				Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);   
				getITelephonyMethod.setAccessible(true);   
				iTelephony = (ITelephony) getITelephonyMethod.invoke(telephonyManager, (Object[]) null);   
				
			} catch (Exception e)
			{   
				e.printStackTrace();  
			}  
		}

			int state = telephonyManager.getCallState();
			Log.v(tag, "[Broadcast]来电=" + phoneNumber);
			
			if(state == TelephonyManager.CALL_STATE_RINGING)
			{
				ringnerMode = audioManager.getRingerMode();

				Contact c = Global.isInImportantlist(phoneNumber, dao);
				if(c!=null)
				{
					Log.v(tag, "[Broadcast]重要联系人=" + c.getName());
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL); 
					ViewUtil.toast(context, "<重要联系人>"+c.getName()+"来电");
					
				}else
				{
				   c = Global.isInBlackList(phoneNumber, dao);
					if(c!=null)
					{
						Log.v(tag, "[Broadcast]开始拦截来电=" + phoneNumber);
					    //先静音处理    
						audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);  
						try
						{
							iTelephony.endCall();
							
						} catch (RemoteException e)
						{
							e.printStackTrace();
						}  
						Log.v(tag, "拦截来电号码"+c.getNumber()+" 姓名:"+c.getName());
						
					}else
					{//不是重要联系人也不是黑名单
						ContactVO cv = IntelligentCenterService.getFrequency().get(phoneNumber);
						
						if(cv == null)
						{
							cv = new ContactVO();
							cv.setNumber(phoneNumber);
							IntelligentCenterService.getFrequency().put(phoneNumber, cv);
						}
						cv.setInComingCount(cv.getInComingCount()+1);
						cv.setLastTime(System.currentTimeMillis());
						
					}
					
				}
				
			}else
			{
				audioManager.setRingerMode(ringnerMode); 
				Log.v(tag, "恢复提醒模式");
			}
			
			if(TelephonyManager.CALL_STATE_OFFHOOK == state)
			{//电话接通了,删除缓存联系人
				IntelligentCenterService.getFrequency().remove(phoneNumber);
				
			}else if(TelephonyManager.CALL_STATE_IDLE == state)
			{
				ContactVO cv = IntelligentCenterService.getFrequency().get(phoneNumber);
				if(cv!=null&&cv.getInComingCount()>=3)
				{
					Intent action=new Intent();
					action.setAction(Intent.ACTION_CALL_BUTTON);
					PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, action, 0);
					showNotification(context, "您有紧急来电", pendingIntent);
					IntelligentCenterService.getFrequency().remove(phoneNumber);
					
					Vibrator vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
					vibrator.vibrate(new long[]{100,10,100,1000}, -1);//-1表示不重复
					ViewUtil.toast(context, "您有紧急来电");
					
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
				
	}
	
	private void showNotification(Context context,String message,PendingIntent pendingIntent)
	{
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		int notificationId = (int) System.currentTimeMillis();
		Notification notification = new Notification(R.drawable.ic_launcher,message, System.currentTimeMillis()/*显示的时间*/);
		
		notification.setLatestEventInfo(context, message, message, pendingIntent);//将PendIntent添加到通知里面
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_ALL;//当现实的时候，发出全部的提示，包括声音、震动等
		notificationManager.notify(notificationId, notification);//发出通知
	}
}
