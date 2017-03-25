 package org.xianghui.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
 
import org.pzn.common.database.sqlite.dao.CommonDao;
import org.xianghui.model.Contact;
import org.xianghui.model.ContactType;

import android.content.Context;
/**
 * 类说明:公共数据
 * @version 创建时间：2012-5-15 下午1:54:17
 */
public class Global
{
	private static List<Contact> blackList = null;
	private static List<Contact> importantList = null;
	//重新加载黑名单，放在程序里节省时间
	public static void reloadBlacklist(CommonDao dao)
	{
		blackList = dao.queryForList(Contact.class, "select * from Contact where type=?",new String[]{ContactType.TYPE_BLACK+""});
		if(blackList == null)
		{
			blackList = new ArrayList<Contact>();
		}
	}
	
	public static void reloadImportantlist(CommonDao dao)
	{
		importantList = dao.queryForList(Contact.class, "select * from Contact where type=?",new String[]{ContactType.TYPE_IMPORTANT+""});
		if(importantList == null)
		{
			importantList = new ArrayList<Contact>();
		}
	}

	public static List<Contact> getBlackList(CommonDao dao)
	{
		if(blackList == null)
		{
			reloadBlacklist(dao);
		}
		return blackList;
	}

	public static List<Contact> getImportantList(CommonDao dao)
	{
		if(importantList == null)
		{
			reloadImportantlist(dao);
		}
		return importantList;
	}
	
	
	public static Contact isInBlackList(String number,CommonDao dao)
	{
		if(blackList == null)
		{
			reloadBlacklist(dao);
		}
		
		for(Contact c:blackList)
		{
			if(c.getNumber().equals(number))
			{
				return c;
			}
		}
		return null;
	}
	
	
	public static Contact isInImportantlist(String number,CommonDao dao)
	{
		if(importantList == null)
		{
			reloadImportantlist(dao);
		}
		
		for(Contact c:importantList)
		{
			if(c.getNumber().equals(number))
			{
				return c;
			}
		}
		return null;
	}
	
}
