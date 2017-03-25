 package org.xianghui.activity;

import java.util.ArrayList;
import java.util.List;

import org.pzn.common.activity.CommonActivity;
import org.xianghui.adapter.MenuAdapter;
import org.xianghui.adapter.MenuInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableRow.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
 /**
 * 类说明:主界面
 * @version 创建时间：2012-5-15 上午11:18:36
 */
public class AppMainActivity extends CommonActivity
{
	/** Called when the activity is first created. */
	//Context con1;

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
	
	//功能主界面采用网格布局
	private GridView gridView = null;
	//用来存放各个功能
	private List<Item> items = new ArrayList<Item>();
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        //con1=this;

		gridView = (GridView)findViewById(R.id.gridView);

		items.add(new Item("课表设置", R.drawable.ic_launcher));
		items.add(new Item("重要联系人", R.drawable.addressbook_icon));
		items.add(new Item("黑名单", R.drawable.block_icon));
		items.add(new Item("日程安排", R.drawable.ic_launcher));
		items.add(new Item("学生点名", R.drawable.student_icon));
		items.add(new Item("教师端", R.drawable.teacher_icon));

		gridView.setAdapter(new ImageAdapter());
		
		gridView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int ps,long arg3)
			{
				switch (ps)
				{
				case 0:
					startActivity(new Intent(AppMainActivity.this, MutetActivity.class));
					break;
				case 1:
					startActivity(new Intent(AppMainActivity.this, ImportantlistActivity.class));
					break;
				case 2:
					startActivity(new Intent(AppMainActivity.this, BlacklistActivity.class));
					break;
				case 3:
					startActivity(new Intent(AppMainActivity.this, ScheduleAcitvity.class));
					break;
				case 4:
					startActivity(new Intent(AppMainActivity.this, StudentActivity.class));
					break;
				case 5:
					startActivity(new Intent(AppMainActivity.this, TeacherActivity.class));
					break;

				default:
					Toast.makeText(AppMainActivity.this, "本功能还在开发中", Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
		initPopuWindows();
	}
	
	
	@Override
	/*Menu菜单按钮*/
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//menu.add(0, 1, 1, R.string.exit).setIcon(R.drawable.exit);//item id=1
		//menu.add(0, 2, 2, R.string.about).setIcon(R.drawable.info);//item id=2
		//return super.onCreateOptionsMenu(menu);
		return true;
	}
	
	
	/**
	 * 设置PopupWindows
	 */
    private void initPopuWindows() {
    	//初始化gridview
		menuGridView=(GridView)View.inflate(this, R.layout.gridview_menu2, null);
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
				case MenuUtils1.MENU_ABOUT:
					Toast.makeText(AppMainActivity.this, "ok", 1).show();
					
					//return super.onCreateOptionsMenu(menu);
					
					break;
				case MenuUtils1.MENU_EXIT:
					Toast.makeText(AppMainActivity.this, "exit", 1).show();
					
					//return super.onCreateOptionsMenu(menu);
					
					break;
				}
			}
		});
	}
	
    @Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if (popup != null) {
			menulists = MenuUtils1.getMenuList();
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==1){
			finish();
		}
		if(item.getItemId()==2){
			AlertDialog.Builder b = new AlertDialog.Builder(AppMainActivity.this);
		    b.setTitle("版权信息").setMessage("版权信息");
				  android.content.DialogInterface.OnClickListener dr =  new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				};
				    b.setPositiveButton("确定", dr);
				    AlertDialog ad = b.create();
				   ad.show();
				   Toast.makeText(AppMainActivity.this, "Test toast", Toast.LENGTH_SHORT).show();

		}
		return super.onOptionsItemSelected(item);
	}


	private class ImageAdapter extends BaseAdapter
	{

		// 获取图片的个数
		public int getCount()
		{
			return items.size();
		}

		// 获取图片在库中的位置
		public Object getItem(int position)
		{
			return items.get(position);
		}


		// 获取图片ID
		public long getItemId(int position)
		{
			return position;
		}

		//获取各个功能在主界面上的图像
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LinearLayout ll = null;
			
			if(convertView==null)
			{
				ll = (LinearLayout)LayoutInflater.from(AppMainActivity.this).inflate(R.layout.grid_item, null);
			
			}else
			{
				ll = (LinearLayout)convertView;
			}
			
			TextView tv = (TextView)ll.findViewById(R.id.item_title);
			ImageView iv = (ImageView)ll.findViewById(R.id.item_image);
			
			tv.setText(items.get(position).title);
			iv.setImageResource(items.get(position).iconId);
			
			convertView = ll;
			
			
			return convertView;
		}
	}
	
	private class Item
	{
		public String title;
		public int iconId;
		
		public Item( String title, int iconId)
		{
			super();
			this.title = title;
			this.iconId = iconId;
		}
		
	}
	//没太大用处
	public GridView getGridView()
	{
		return gridView;
	}

	public void setGridView(GridView gridView)
	{
		this.gridView = gridView;
	}
}
