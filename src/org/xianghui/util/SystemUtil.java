 package org.xianghui.util;

import android.content.Context;
import android.telephony.TelephonyManager;
 /**
 * ��˵��:
 * @version ����ʱ�䣺2012-5-18 ����11:45:19
 */
public class SystemUtil
{
	public static String getIMEI(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();//String   
	}
	
}
