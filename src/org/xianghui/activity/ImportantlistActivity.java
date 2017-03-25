 package org.xianghui.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pzn.common.annotation.Component;
import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.database.sqlite.interfaces.SQLiteExecution;
import org.xianghui.adapter.ContactAdapter;
import org.xianghui.adapter.MenuAdapter;
import org.xianghui.adapter.MenuInfo;
import org.xianghui.application.MainApplication;
import org.xianghui.common.Global;
import org.xianghui.model.Contact;
import org.xianghui.model.ContactType;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;
 /**
 * 类说明:重要人员列表
 * @version 创建时间：2012-5-15 上午11:38:41
 */
public class ImportantlistActivity extends ContactsBaseActivity
{

	/**
	 * 定义popupwindow
	 */	
	private PopupWindow popup;
	/**
	 * 定义适配器
	 */
	private MenuAdapter menuAdapter;
	//菜单项列表
	private List<MenuInfo> menulists;
	//定义gridview
	private GridView menuGridView;
	
	//以上由ZMQ添加
	private static final String tag = "IntelligentAssistant";
	private static final int MENU_APPLY = 1;

	private static final int REQUEST_CODE_ADD = 1;
	
	@Component
	private ListView listView = null;
	
	private CommonDao dao = null;
	private List<Contact> contacts = null;
	private ContactAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		initPopuWindows();
		dao = ((MainApplication)getApplication()).getDao();
		
		loadBlackList();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//menu.add(MENU_APPLY,MENU_APPLY,MENU_APPLY,"应用设置");//MENU菜单修改位置 ZMQ
		//return super.onCreateOptionsMenu(menu);
		return true;
	}
	
	
	/**
	 * 设置PopupWindows
	 */
    private void initPopuWindows() {
    	//初始化gridview
		menuGridView=(GridView)View.inflate(this, R.layout.gridview_menu, null);
		//初始化PopupWindow,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT控制显示
		popup = new PopupWindow(menuGridView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// 设置menu菜单背景
		popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_background));
		// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		popup.setFocusable(true);
		//设置显示和隐藏的动画
		popup.setAnimationStyle(R.style.menushow);
		popup.update();
		//设置触摸获取焦点
		menuGridView.setFocusableInTouchMode(true);
		//设置键盘事件,如果按下菜单键则隐藏菜单
		menuGridView.setOnKeyListener(new android.view.View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (popup.isShowing())) {
					popup.dismiss();  
					return true;
					
				}
				return false;
			}

		});
		//添加菜单按钮事件
		menuGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				MenuInfo mInfo = menulists.get(arg2);
				popup.dismiss();
				if (mInfo.ishide) {
					return;
				}
				switch (mInfo.menuId) {
				case MenuUtils.MENU_APPLY:
					//Toast.makeText(ImportantlistActivity.this, "ok", 1).show();
					
					//return super.onCreateOptionsMenu(menu);
					apply();
					break;
				}
			}
		});
	}
	
    @Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if (popup != null) {
			menulists = MenuUtils.getMenuList();
			menuAdapter = new MenuAdapter(this, menulists);
			menuGridView.setAdapter(menuAdapter);
			popup.showAtLocation(this.findViewById(R.id.linearlayout), Gravity.BOTTOM, 0, 0);
		}
		return false;// 返回为true 则显示系统menu
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("menu");
		return super.onPrepareOptionsMenu(menu);
	}
    
	
	
	
	
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case MENU_APPLY:
			apply();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		switch (requestCode)
		{
		case REQUEST_CODE_ADD:
			List<Contact> cs = (List<Contact>) data.getSerializableExtra("contacts");
			Log.v(tag, "cs.size = "+cs.size());
			break;

		default:
			break;
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private void loadBlackList()
	{
		new AsyncTask<Void, Void, List<Contact>>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(ImportantlistActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在加载重要联系人列表...");
				dialog.show();
			};
			
			@Override
			protected List<Contact> doInBackground(Void... params)
			{
				List<Contact> importantList = dao.queryForList(Contact.class, "select * from Contact where type=?",new String[]{ContactType.TYPE_IMPORTANT+""});
				Log.v(tag, "重要联系人数量:"+importantList.size());
				contacts = loadContacts(importantList,ContactType.TYPE_IMPORTANT);
				
				Collections.sort(contacts,new Comparator<Contact>()
				{
					@Override
					public int compare(Contact c1, Contact c2)
					{
						return c2.getType() - c1.getType();
					}
				});
				return contacts;
			}
			
			protected void onPostExecute(List<Contact> result) 
			{
				
				if(result == null || result.size()<=0)
				{
					toast("暂无重要联系人");
					
				}else
				{
					adapter = new ContactAdapter(contacts, LayoutInflater.from(ImportantlistActivity.this));
					listView.setAdapter(adapter);
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}
	//////////
	public void apply()
	{
		new AsyncTask<Void, Void, Boolean>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(ImportantlistActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在应用设置");
				dialog.show();
			};
			
			@Override
			protected Boolean doInBackground(Void... params)
			{
				boolean flag = false;
				dao.execute(new SQLiteExecution()
				{
					@Override
					public void onExecute(SQLiteDatabase db)
					{
						db.execSQL("delete from Contact where type=?",new String[]{ContactType.TYPE_IMPORTANT+""});
					}
				});
				
				dao.beginTransaction();
				
				try
				{
					List<Contact> list = adapter.getSelectContacts();
					Log.v(tag, "重要联系人数量:"+list.size());
					for(Contact c:list)
					{
						c.setType(ContactType.TYPE_IMPORTANT);
						dao.save(c);
					}
					
					dao.commitTransaction();
					flag = true;
					
				} catch (Exception e)
				{
					e.printStackTrace();
					dao.rollback();
					flag = false;
				}
				
				Global.reloadImportantlist(dao);
				return flag;
			}
			
			protected void onPostExecute(Boolean result) 
			{
				if(result)
				{
					toast("应用成功");
					finish();
					
				}else
				{
					messageBox("应用出现异常，请检查联系人列表");//ZMQ
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}
	
}
