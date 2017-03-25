 package org.xianghui.model;

import java.io.Serializable;

import org.pzn.common.database.sqlite.annontation.Id;
 /**
 * ��˵��:��ϵ��
 * @version ����ʱ�䣺2012-5-15 ����11:29:18
 */
public class Contact implements Serializable
{
	@Id(isAuto=true)
	private Integer id;
	private String name;
	private String number;
	private Integer type;
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public String getNumber()
	{
		return number;
	}
	public void setNumber(String number)
	{
		this.number = number;
	}
	
}
