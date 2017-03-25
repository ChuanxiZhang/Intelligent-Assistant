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
 /**
 * 类说明:黑名单列表
 * @version 创建时间：2012-5-15 上午11:38:41
 */
public class BlacklistActivity extends ContactsBaseActivity
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
	
	
	//输出日志的标签
	private static final String tag = "IntelligentAssistant";
	//标识“黑名单”的menu
	private static final int MENU_APPLY = 1;
	
	//调用标识，用来标记是哪个被调用的Activity返回的结果
	private static final int REQUEST_CODE_ADD = 1;
	
	
	@Component
	private ListView listView = null;
	//访问数据库
	private CommonDao dao = null;
	//保存联系人
	private List<Contact> contacts = null;
	//适配器
	private ContactAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		dao = ((MainApplication)getApplication()).getDao();
		//加载黑名单
		loadBlackList();
		initPopuWindows();
	}
	//生成菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//menu.add(MENU_APPLY,MENU_APPLY,MENU_APPLY,"应用设置");
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
					//Toast.makeText(MutetActivity.this, "ok", 1).show();
					
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
    
	
	
	
	
	
	
	
	
	//点击“应用设置”后触发
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
		//异步，比较耗时，采用异步
		new AsyncTask<Void, Void, List<Contact>>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(BlacklistActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在加载黑名单列表...");
				dialog.show();
			};
			
			@Override
			protected List<Contact> doInBackground(Void... params)
			{
				//查找黑名单
				List<Contact> blacklist = dao.queryForList(Contact.class, "select * from Contact where type=?",new String[]{ContactType.TYPE_BLACK+""});
				Log.v(tag, "黑名单数量:"+blacklist.size());
				//先把所有联系人的黑名单标识出来
				contacts = loadContacts(blacklist,ContactType.TYPE_BLACK);
				//进行排序，把黑名单排在前面
				Collections.sort(contacts,new Comparator<Contact>()
				{
					@Override
					public int compare(Contact c1, Contact c2)
					{
						//黑名单的标识数字是3，最大，排最前面
						return c2.getType() - c1.getType();
					}
				});
				return contacts;
			}
			//把名单显示出来
			protected void onPostExecute(List<Contact> result) 
			{
				
				if(result == null || result.size()<=0)
				{
					toast("暂无黑名单列表");
					
				}else
				{
					adapter = new ContactAdapter(contacts, LayoutInflater.from(BlacklistActivity.this));
					listView.setAdapter(adapter);
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}
	////////
	public void apply()
	{
		new AsyncTask<Void, Void, Boolean>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(BlacklistActivity.this);

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
					//把原先的黑名单删掉
					@Override
					public void onExecute(SQLiteDatabase db)
					{
						db.execSQL("delete from Contact where type=?",new String[]{ContactType.TYPE_BLACK+""});
					}
				});
				//开始事务
				dao.beginTransaction();
				
				try
				{
					//获取被勾选的联系人
					List<Contact> list = adapter.getSelectContacts();
					Log.v(tag, "黑名单数量:"+list.size());
					for(Contact c:list)
					{
						c.setType(ContactType.TYPE_BLACK);
						dao.save(c);
					}
					//提交事务
					dao.commitTransaction();
					flag = true;
					
				} catch (Exception e)
				{
					e.printStackTrace();
					dao.rollback();
					flag = false;
				}
				
				Global.reloadBlacklist(dao);
				
				return flag;
			}
			//与上一个函数处在不同的线程，result是上函数返回结果，上函数不能更新界面，会出错
			protected void onPostExecute(Boolean result) 
			{
				
				if(result)
				{
					toast("应用成功");
					finish();
					
				}else
				{
					messageBox("应用出现异常");
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}
	
}
