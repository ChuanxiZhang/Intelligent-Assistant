 package org.xianghui.listener;

import java.lang.reflect.Method;

import org.pzn.common.database.sqlite.dao.CommonDao;
import org.xianghui.common.Global;
import org.xianghui.model.Contact;

import com.android.internal.telephony.ITelephony;

import android.content.Context;
import android.media.AudioManager;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
 /**
 * 类说明:电话状态监听器
 * @version 创建时间：2012-5-15 下午2:07:12
 */
public class PhoneStateListener extends android.telephony.PhoneStateListener
{
	private static final String tag = "IntelligentAssistant";
	private AudioManager audioManager = null;
	private CommonDao dao = null;
	private TelephonyManager telephonyManager = null;
	private ITelephony iTelephony = null;
	
	public PhoneStateListener(Context context,CommonDao dao)
	{
		this.dao = dao;
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		audioManager =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
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

	@Override
	public void onCallStateChanged(int state, String incomingNumber)
	{
		
		Log.v(tag, "来电拦截，电话 = "+incomingNumber);   

		String phoneNumber =incomingNumber;
		
			for(Contact c:Global.getBlackList(dao))
			{
				if(c.getNumber().equals(phoneNumber))
				{
				    //先静音处理    
					audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);  
					try
					{
						Log.v(tag, "拒接来电成功 = "+iTelephony.endCall());
					} catch (RemoteException e)
					{
						e.printStackTrace();
					}  
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);  
					Log.v(tag, "PhoneStateListener拦截来电号码"+c.getNumber()+" 姓名:"+c.getName());
				}
			}

		super.onCallStateChanged(state, incomingNumber);
	}
}
