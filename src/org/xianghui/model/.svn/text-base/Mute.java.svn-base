 package org.xianghui.model;

import org.pzn.common.database.sqlite.annontation.Id;
 /**
 * 类说明:静音类
 * @version 创建时间：2012-5-14 下午6:06:12
 */
public class Mute
{
	@Id(isAuto=true)
	private Integer id;
	private Integer week;
	private Integer number;
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getWeek()
	{
		return week;
	}
	public void setWeek(Integer week)
	{
		this.week = week;
	}
	public Integer getNumber()
	{
		return number;
	}
	public void setNumber(Integer number)
	{
		this.number = number;
	}
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Mute))
			return false;
		
		Mute m = (Mute)o;
		return week == m.getWeek() && number == m.getNumber();
	}
}
