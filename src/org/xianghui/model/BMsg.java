 package org.xianghui.model;

import java.io.Serializable;
 /**
 * 类说明:
 * @version 创建时间：2012-5-16 下午2:02:25
 */
public class BMsg implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int CMD_OK = 1;
	public static final int CMD_CONTENT = 2;
	public static final int CMD_REGISTER = 3;
	public static final int CMD_REACH= 4;
	public static final int CMD_ERROR= 5;
	
	private int cmd;
	private String content;
	private String imei;
	private String name;
	private String number;
	private String classNumber;

	public int getCmd()
	{
		return cmd;
	}

	public void setCmd(int cmd)
	{
		this.cmd = cmd;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getImei()
	{
		return imei;
	}

	public void setImei(String imei)
	{
		this.imei = imei;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
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
