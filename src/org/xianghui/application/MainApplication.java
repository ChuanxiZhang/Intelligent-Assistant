 package org.xianghui.application;

import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.database.sqlite.exception.CreateForeignKeyException;
import org.pzn.common.database.sqlite.exception.CreateTableErrorException;
import org.pzn.common.database.sqlite.exception.OpenDataBaseErrorException;
import org.pzn.common.util.ViewUtil;
import org.xianghui.model.Contact;
import org.xianghui.model.Mute;
import org.xianghui.model.Reach;
import org.xianghui.model.Schedule;
import org.xianghui.model.Student;
import org.xianghui.service.IntelligentCenterService;

import android.app.Application;
import android.content.Intent;
 /**
 * 类说明:
 * 管理程序的生命周期，Activity、Service一般都由其来管理
 * @version 创建时间：2012-5-14 下午6:09:56
 */
public class MainApplication extends Application
{
	private CommonDao dao = null;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		dao = new CommonDao();
		
		try
		{
			dao.openDB(this, "IntelligentAssistant.db");
			
		} catch (OpenDataBaseErrorException e)
		{
			ViewUtil.toast(this, "打开或者创建数据库失败");
			e.printStackTrace();
			return;
		}
		
		try
		{
			dao.createTable(Mute.class);
			dao.createTable(Contact.class);
			dao.createTable(Schedule.class);
			dao.createTable(Student.class);
			dao.createTable(Reach.class);
			
		} catch (CreateTableErrorException e)
		{
			e.printStackTrace();
			
		} catch (CreateForeignKeyException e)
		{
			e.printStackTrace();
		}
		
		initListener();
		initService();
	}
	
	private void initListener()
	{
	//	TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);   
	//	telManager.listen(new  org.xianghui.listener.PhoneStateListener(this, dao) ,PhoneStateListener.LISTEN_CALL_STATE);   
	}
	
	private void initService()
	{
		startService(new Intent(this,IntelligentCenterService.class ));
	}

	public CommonDao getDao()
	{
		return dao;
	}
	public void setDao(CommonDao dao)
	{
		this.dao = dao;
	}
}
