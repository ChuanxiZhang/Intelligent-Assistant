 package org.xianghui.util;

import java.util.Date;

import android.util.Log;
 /**
 * 类说明:
 * @version 创建时间：2012-5-18 下午2:27:33
 */
public class ClassTimeUtil
{
	private static final String tag = "IntelligentAssistant";
	public static int classNumber()
	{
		Date date = new Date();
		Date d = new Date();
		long off;

		//=================1
		date.setHours(9);
		date.setMinutes(15);
		date.setSeconds(0);
		off = date.getTime();
		Log.v(tag, d.getTime()+"<"+off);
		if(d.getTime()<off)
		{
			return 1;
		}
		//=================2

		date.setHours(10);
		date.setMinutes(10);
		date.setSeconds(0);
		off = date.getTime();

		Log.v(tag, d.getTime()+"<"+off);
		if(d.getTime()<off)
		{
			return 2;
		}
		//=================3

		date.setHours(11);
		date.setMinutes(15);
		date.setSeconds(0);
		off = date.getTime();

		Log.v(tag, d.getTime()+"<"+off);
		if(d.getTime()<off)
		{
			return 3;
		}
		//=================4

		date.setHours(12);
		date.setMinutes(10);
		date.setSeconds(0);
		off = date.getTime();

		Log.v(tag, d.getTime()+"<"+off);
		if(d.getTime()<off)
		{
			return 4;
		}
		//=================5

		date.setHours(14);
		date.setMinutes(45);
		date.setSeconds(0);
		off = date.getTime();

		Log.v(tag, d.getTime()+"<"+off);
		if(d.getTime()<off)
		{
			return 5;
		}
		//=================6

		date.setHours(15);
		date.setMinutes(40);
		date.setSeconds(0);
		off = date.getTime();

		Log.v(tag, d.getTime()+"<"+off);
		if(d.getTime()<off)
		{
			return 6;
		}
		//=================7

		date.setHours(16);
		date.setMinutes(35);
		date.setSeconds(0);
		off = date.getTime();

		Log.v(tag, d.getTime()+"<"+off);
		if(d.getTime()<off)
		{
			return 7;
		}
		//=================8

		date.setHours(17);
		date.setMinutes(30);
		date.setSeconds(0);
		off = date.getTime();

		Log.v(tag, d.getTime()+"<"+off);
		if(d.getTime()<off)
		{
			return 8;
		}
		
		return -1;
	}
	
	public static Date getFromTime(int number)
	{
		Date date = new Date();
		switch (number)
		{
		case 1:
			date.setHours(8);
			date.setMinutes(30);
			date.setSeconds(0);
			
			break;
		case 2:
			date.setHours(9);
			date.setMinutes(25);
			date.setSeconds(0);

			break;
		case 3:
			date.setHours(10);
			date.setMinutes(30);
			date.setSeconds(0);
			
			break;
		case 4:
			date.setHours(11);
			date.setMinutes(25);
			date.setSeconds(0);
			
			break;
		case 5:
			date.setHours(14);
			date.setMinutes(0);
			date.setSeconds(0);
			
			break;
		case 6:
			date.setHours(14);
			date.setMinutes(55);
			date.setSeconds(0);
			break;
		case 7:
			date.setHours(15);
			date.setMinutes(50);
			date.setSeconds(0);
			break;
		case 8:
			date.setHours(16);
			date.setMinutes(45);
			date.setSeconds(0);
			break;
		default:
			break;
		}
		return date;
	}
	
	public static  Date getToTime(int number)
	{
		Date date = new Date();

		switch (number)
		{
		case 1:
			date.setHours(9);
			date.setMinutes(15);
			date.setSeconds(0);
			
			break;
		case 2:
			date.setHours(10);
			date.setMinutes(10);
			date.setSeconds(0);
			break;
		case 3:
			date.setHours(11);
			date.setMinutes(15);
			date.setSeconds(0);
			
			break;
		case 4:
			date.setHours(12);
			date.setMinutes(10);
			date.setSeconds(0);
			
			break;
		case 5:
			date.setHours(14);
			date.setMinutes(45);
			date.setSeconds(0);
			
			break;
		case 6:
			date.setHours(15);
			date.setMinutes(40);
			date.setSeconds(0);
			break;
		case 7:
			date.setHours(16);
			date.setMinutes(35);
			date.setSeconds(0);
			break;
		case 8:
			date.setHours(17);
			date.setMinutes(30);
			date.setSeconds(0);
			break;
		default:
			break;
		}
		return date;
	}
	
	public static int isLate(Date d)
	{
		long on,off;

		//=================1
		on = getFromTime(1).getTime();
		off = getToTime(1).getTime();
		
		if(d.getTime()>on&&d.getTime()<off)
		{
			return (int) ((d.getTime()-on)/1000/60);
		}
		//=================2
		on = getFromTime(2).getTime();
		off = getToTime(2).getTime();
		
		if(d.getTime()>on&&d.getTime()<off)
		{
			return (int) ((d.getTime()-on)/1000/60);
		}
		//=================3
		on = getFromTime(3).getTime();
		off = getToTime(3).getTime();
		
		if(d.getTime()>on&&d.getTime()<off)
		{
			return (int) ((d.getTime()-on)/1000/60);
		}
		//=================4
		on = getFromTime(4).getTime();
		off = getToTime(4).getTime();
		
		if(d.getTime()>on&&d.getTime()<off)
		{
			return (int) ((d.getTime()-on)/1000/60);
		}
		//=================5
		on = getFromTime(5).getTime();
		off = getToTime(5).getTime();
		
		if(d.getTime()>on&&d.getTime()<off)
		{
			return (int) ((d.getTime()-on)/1000/60);
		}
		//=================6
		on = getFromTime(6).getTime();
		off = getToTime(6).getTime();
		
		if(d.getTime()>on&&d.getTime()<off)
		{
			return (int) ((d.getTime()-on)/1000/60);
		}
		//=================7
		on = getFromTime(7).getTime();
		off = getToTime(7).getTime();
		
		if(d.getTime()>on&&d.getTime()<off)
		{
			return (int) ((d.getTime()-on)/1000/60);
		}
		//=================8
		on = getFromTime(8).getTime();
		off = getToTime(8).getTime();
		
		if(d.getTime()>on&&d.getTime()<off)
		{
			return (int) ((d.getTime()-on)/1000/60);
		}
		
		return 0;
	}
}
