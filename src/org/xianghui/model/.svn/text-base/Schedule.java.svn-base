 package org.xianghui.model;

import java.io.Serializable;
import java.util.Date;

import org.pzn.common.database.sqlite.annontation.Id;
 /**
 * 类说明:日程安排
 * @version 创建时间：2012-5-15 下午9:39:55
 */
public class Schedule implements Serializable
{
	@Id(isAuto=true)
	private Integer id;
	private String title;
	private String content;
	private Date startDate;
	private Date endDate;
	private Date remindDate;
	private boolean hasAlert = false;
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	public Date getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	public Date getRemindDate()
	{
		return remindDate;
	}
	public void setRemindDate(Date remindDate)
	{
		this.remindDate = remindDate;
	}
	public boolean isHasAlert()
	{
		return hasAlert;
	}
	public void setHasAlert(boolean hasAlert)
	{
		this.hasAlert = hasAlert;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	
}
