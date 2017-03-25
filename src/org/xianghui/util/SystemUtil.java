 package org.xianghui.util;

import android.content.Context;
import android.telephony.TelephonyManager;
 /**
 * 类说明:
 * @version 创建时间：2012-5-18 上午11:45:19
 */
public class SystemUtil
{
	public static String getIMEI(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();//String   
	}
	
}
