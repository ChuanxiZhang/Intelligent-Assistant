 package org.xianghui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
 /**
 * 类说明:
 * @version 创建时间：2012-5-14 下午8:02:49
 */
public class DateFormatUtil
{
	public static final String get_yyyy_MM_dd_HH_mm_ss(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
