package org.xianghui.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pzn.common.activity.CommonActivity;
import org.pzn.common.annotation.Component;
import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.database.sqlite.interfaces.SQLiteExecution;
import org.pzn.common.util.ViewUtil;
import org.xianghui.application.MainApplication;
import org.xianghui.model.Mute;
import org.xianghui.service.PerDaySettingService;

import org.xianghui.adapter.*;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableRow.LayoutParams;

public class MutetActivity extends CommonActivity implements OnClickListener
{
	/** Called when the activity is first created. */
	//课表设置 ZMQ
	
	
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
	
	private static final int MENU_APPLY = 1;
	
	@Component
	private TableLayout curriculumTableLayout;
	private Button [][]buttons = new Button[8][5];
	
	private CommonDao dao = null;
	
	private List<Mute>mutes = new ArrayList<Mute>();
	private boolean isChange = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mute);
		dao = ((MainApplication)this.getApplication()).getDao();

		initData();
		
		initView();
		initPopuWindows();
	}
	
	
	@Override
	public void onBackPressed()
	{
		if(isChange)
		{
			ViewUtil.showConfirmDialog(this, "注意", "检测到课程表已改变，是否应用？", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					toApply();
				}
			}, new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					finish();
				}
			});
			
		}else
		{
			super.onBackPressed();
		}
	}
	//创建菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//menu.add(MENU_APPLY, MENU_APPLY, MENU_APPLY, "应用");//edit by zmq
		//return super.onCreateOptionsMenu(menu);//edit by zmq
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
					toApply();
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
    
    
    
    
    
	
	
	//点击“应用”触发
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case MENU_APPLY:
			toApply();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//初始化数据
	@SuppressWarnings("unchecked")
	private void initData()
	{
		mutes = dao.queryForList(Mute.class, "select * from Mute", null);
		
		if(mutes == null)
		{
			mutes = new ArrayList<Mute>();
		}
	}
	
	//显示课表
	private void initView()
	{
		LayoutParams lp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 5, 5, 5);
		
		for(int row=0;row<8;row++)
		{
			TableRow tr = new TableRow(this);
			for(int col=0;col<5;col++)
			{
				buttons[row][col] = new Button(this);
				buttons[row][col].setText("第"+(row+1)+"节");
				buttons[row][col].setLayoutParams(lp);
				buttons[row][col] .setOnClickListener(this);

				Mute mute = new Mute();
				mute.setWeek(col+1);
				mute.setNumber(row+1);
				
				buttons[row][col].setTag(mute);
				tr.addView(buttons[row][col]);
			}
			
			curriculumTableLayout.addView(tr);
		}
		
		refreshView();
	}
	//设置为黄色
	private void refreshView()
	{
		for(int row=0;row<8;row++)
		{
			for(int col=0;col<5;col++)
			{
				if(mutes.contains(buttons[row][col].getTag()))
				{
					buttons[row][col].setBackgroundColor(Color.YELLOW);
					
				}else
				{
					buttons[row][col].setBackgroundColor(Color.GRAY);
				}
			}
		}
	}

	private void toApply()
	{

		new AsyncTask<Void, Void, Void>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(MutetActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在应用设置");
				dialog.show();
			};
			
			@Override
			protected Void doInBackground(Void... params)
			{
				apply();
				return null;
			}
			
			protected void onPostExecute(Void result) 
			{
				if(dialog.isShowing())
				{
					dialog.cancel();
					finish();
				}
			};
			
		}.execute();
	}
	
	/**
	 * 应用
	 */
	private void apply()
	{

		if(mutes.size()>0)
		{
			dao.execute(new SQLiteExecution()
			{
				//先删除原来表里的内容
				@Override
				public void onExecute(SQLiteDatabase db)
				{
					db.execSQL("delete from Mute");
				}
			});
			
			try
			{
				dao.beginTransaction();
				
				for(Mute mute:mutes)
				{
					dao.save(mute);
				}
				
				dao.commitTransaction();
				
			} catch (Exception e)
			{
				e.printStackTrace();
				dao.rollback();
				ViewUtil.toast(this, "保存数据库失败");
			}
		}
		
		addAlarm();
		
		isChange = false;
	}
	
	/**
	 * 添加定时设置
	 */
	private void addAlarm()
	{
		Intent intent = new Intent(this,PerDaySettingService.class);
		PendingIntent sender = PendingIntent.getService(this, 0	, intent, 0);
		
		Date date = new Date();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, date.getTime(), 1000*60*60*24, sender);//1000*60*60*24
		
		startService(intent);
	}
	
	@Override
	public void onClick(View v)
	{
		isChange = true;
		
		if(mutes.contains(v.getTag()))
		{
			mutes.remove(v.getTag());
			
		}else
		{
			mutes.add((Mute) v.getTag());
		}
		refreshView();
	}
}