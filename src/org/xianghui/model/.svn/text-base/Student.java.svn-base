 package org.xianghui.model;

import java.io.Serializable;

import org.pzn.common.database.sqlite.annontation.Constraint;
import org.pzn.common.database.sqlite.annontation.Id;
 /**
 * 类说明:
 * @version 创建时间：2012-5-16 下午2:11:29
 */
public class Student implements Serializable
{
	@Id(isAuto=false)
	private String number;
	private String name;
	@Constraint(isUnique=true)
	private String imei;
	private String classNumber;
	
	public String getNumber()
	{
		return number;
	}
	public void setNumber(String number)
	{
		this.number = number;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getImei()
	{
		return imei;
	}
	public void setImei(String imei)
	{
		this.imei = imei;
	}
	public String getClassNumber()
	{
		return classNumber;
	}
	public void setClassNumber(String classNumber)
	{
		this.classNumber = classNumber;
	}
	
}
