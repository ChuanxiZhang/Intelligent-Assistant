package org.xianghui.broadcastreciever;

import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.database.sqlite.exception.OpenDataBaseErrorException;
import org.pzn.common.util.ViewUtil;
import org.xianghui.common.Global;
import org.xianghui.model.Contact;
import org.xianghui.util.RingUtil;
import org.xianghui.util.VibratorUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Vibrator;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

/**
 * 类说明:短信广播拦截器
 * 
 * @version 创建时间：2012-4-27 下午5:35:13
 */

@SuppressWarnings("deprecation")
public class SmsBroadcastReciever extends BroadcastReceiver
{
	private static final String tag = "IntelligentAssistant";
//	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static AudioManager audioManager = null;
	private static Vibrator vibrator = null;
	private static CommonDao dao = null;

	//接收到了短信
	@Override
	public void onReceive(Context context, Intent intent)
	{
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
		
		if(audioManager == null)
		{
			audioManager =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		}
		
		if(vibrator==null)
		{
			vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
		}
		
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");

		if (pdus != null && pdus.length > 0)
		{
			SmsMessage[] messages = new SmsMessage[pdus.length];

			for (int i = 0; i < pdus.length; i++)
			{
				byte[] pdu = (byte[]) pdus[i];
				messages[i] = SmsMessage.createFromPdu(pdu);
			}

			for (SmsMessage message : messages)
			{
				String content = message.getMessageBody();// 得到短信内容
				String sender = message.getOriginatingAddress();// 得到发信息的号码
//				Date date = new Date(message.getTimestampMillis());
				
				Contact c = Global.isInImportantlist(sender, dao);
				Log.v(tag, "接收到短信:sender = "+sender+"  content="+content);
				if(c!=null)
				{
					Log.v(tag, "重要联系人:"+c.getName()+"  "+c.getNumber());
					ViewUtil.toast(context, "<重要联系人>"+c.getName()+"的信息");
					RingUtil.ringNotification(context);
					VibratorUtil.vibrate(context);
					 
					
				}else if((c = Global.isInBlackList(sender, dao))!=null)
				{
					Log.v(tag, "黑名单:"+c.getName()+"  "+c.getNumber());
					this.abortBroadcast();
				}
			}
		}
	}
}
