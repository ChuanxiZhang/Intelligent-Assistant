 package org.xianghui.activity;

import java.util.List;

import org.pzn.common.activity.CommonActivity;
import org.pzn.common.annotation.Component;
import org.pzn.common.database.sqlite.dao.CommonDao;
import org.xianghui.adapter.ScheduleAdapter;
import org.xianghui.application.MainApplication;
import org.xianghui.model.Schedule;




import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
 /**
 * 类说明:日程安排
 * @version 创建时间：2012-5-15 下午9:38:36
 */
public class ScheduleAcitvity extends CommonActivity
{
	private static final String tag = "IntelligentAssistant";
	/////////////////////
	private View view;
	private Button btn;
	private PopupWindow mPopupWindow;
	private View[] btns;
	///////////////////
	private static final int MENU_ADD = 1;
	private static final int MENU_DELETE = 2;
	private static final int MENU_HAS_ALERT = 3;
	private static final int MENU_NOT_ALERT = 4;
	private static final int MENU_ALL = 5;
	private static final int CONTEXT_MENU_UPADTE = 1;
	private static final int CONTEXT_MENU_DELETE = 2;
	
	@Component
	private ListView listView = null;
	private CommonDao dao = null;
	private List<Schedule> schedules = null;
	private ScheduleAdapter adapter = null;
	
	private int selectType = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listnew);
		dao = ((MainApplication)getApplication()).getDao();
		////////////////////////////////ZMQ
        btn=(Button) this.findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener(){

			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow(btn);
			}
        	
        });
        initPopupWindow(R.layout.popwindowbyzmq);
		///////////////////////////////ZMQ↑
		registerForContextMenu(listView);
		listView.setClickable(true);
		loadScheduleList();
	}
	
	////////////////////////////////////////ZMQ
	private void initPopupWindow(int resId){
		LayoutInflater mLayoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
	    view = mLayoutInflater.inflate(resId, null);
	        
		mPopupWindow = new PopupWindow(view, 400,LayoutParams.WRAP_CONTENT);
//		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//必须设置background才能消失
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_frame));
		mPopupWindow.setOutsideTouchable(true);
		
		//自定义动画
//		mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
		//使用系统动画
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mPopupWindow.update();
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		
		btns=new View[7];
		btns[0]=view.findViewById(R.id.btn_0);
		btns[1]=view.findViewById(R.id.btn_1);
		btns[2]=view.findViewById(R.id.btn_2);
		btns[0].setOnClickListener(new OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
				startActivityForResult(new Intent(ScheduleAcitvity.this, ScheduleAddOrUpdateActivity.class), 0);
				//startActivity(intent);
			}
		});
		btns[1].setOnClickListener(new OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
			}
		});
		btns[2].setOnClickListener(new OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
			}
		});
	}
	private void showPopupWindow(View view) {
		if(!mPopupWindow.isShowing()){
//			mPopupWindow.showAsDropDown(view,0,0);
			mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		}
	}
	
	
	/////下面的貌似没什么用
	public Bitmap setRotate(int resId) {
		Matrix mFgMatrix = new Matrix();
		Bitmap mFgBitmap = BitmapFactory.decodeResource(getResources(), resId);
		mFgMatrix.setRotate(180f);
		return mFgBitmap=Bitmap.createBitmap(mFgBitmap, 0, 0, 
				mFgBitmap.getWidth(), mFgBitmap.getHeight(), mFgMatrix, true);
	}
	
	
	///////////////////////////////////////ZMQ
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(MENU_ADD, MENU_ADD, MENU_ADD, "添加新日程").setIcon(R.drawable.add);
		menu.add(MENU_DELETE, MENU_DELETE, MENU_DELETE, "批量删除").setIcon(R.drawable.piliang_delete);
		menu.add(MENU_HAS_ALERT, MENU_HAS_ALERT, MENU_HAS_ALERT, "过期日程").setIcon(R.drawable.outdata_schedule);
		menu.add(MENU_NOT_ALERT, MENU_NOT_ALERT, MENU_NOT_ALERT, "未来日程").setIcon(R.drawable.future_schedule);
		menu.add(MENU_ALL, MENU_ALL, MENU_ALL, "全部日程").setIcon(R.drawable.all_schedule);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	{
		menu.add(CONTEXT_MENU_UPADTE, CONTEXT_MENU_UPADTE, CONTEXT_MENU_UPADTE, "修改日程");
		menu.add(CONTEXT_MENU_DELETE, CONTEXT_MENU_DELETE, CONTEXT_MENU_DELETE, "删除日程");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case MENU_ADD:
			startActivityForResult(new Intent(this, ScheduleAddOrUpdateActivity.class), 0);
			break;
		case MENU_DELETE:
			deleteMulit();
			break;
		case MENU_NOT_ALERT:
			selectType = 0;
			loadScheduleList();
			break;
		case MENU_HAS_ALERT:
			selectType = 1;
			loadScheduleList();
			break;
		case MENU_ALL:
			selectType = 2;
			loadScheduleList();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	*/
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Log.v(tag, "select index = "+info.position);
		switch (item.getItemId())
		{
		case CONTEXT_MENU_DELETE:
			dao.delete(schedules.get(info.position));
			toast("删除成功");
			loadScheduleList();
			
			break;
		case CONTEXT_MENU_UPADTE:
			Intent intent = new Intent(this, ScheduleAddOrUpdateActivity.class);
			intent.putExtra("isUpdate", true);
			intent.putExtra("schedule", schedules.get(info.position));
			startActivityForResult(intent, 0);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		loadScheduleList();
		super.onActivityResult(requestCode, resultCode, data);
	}
	///////
	private void loadScheduleList()
	{
		new AsyncTask<Void, Void, List<Schedule>>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(ScheduleAcitvity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在加载日程列表...");
				dialog.show();
			};
			
			@Override
			protected List<Schedule> doInBackground(Void... params)
			{
				String sql = "";
				//未过期的
				if(selectType == 0)
				{
					sql = "select * from Schedule where hasAlert=0 order by startDate";
					//已过期的
				}else if(selectType == 1)
				{
					sql = "select * from Schedule where hasAlert=1 order by startDate";
					
				}else if(selectType == 2)
				{
					sql = "select * from Schedule order by startDate";
				}
				schedules = dao.queryForList(Schedule.class,sql);
				return schedules;
			}
			
			protected void onPostExecute(List<Schedule> result) 
			{
				
				if(result == null || result.size()<=0)
				{
					toast("暂无日程安排");

					adapter = new ScheduleAdapter(result, LayoutInflater.from(ScheduleAcitvity.this));
					listView.setAdapter(adapter);
					
				}else
				{
					adapter = new ScheduleAdapter(result, LayoutInflater.from(ScheduleAcitvity.this));
					listView.setAdapter(adapter);
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}
	
	private void deleteMulit()
	{
		
		final List<Schedule> selectSchedules = adapter.getSelectSchedules();
		
		if(selectSchedules == null||selectSchedules.size()<=0)
		{
			messageBox("请至少选择一项进行删除");
			return;
		}
		
		new AsyncTask<Void, Void, Void>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(ScheduleAcitvity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在批量删除日程...");
				dialog.show();
			};
			
			@Override
			protected Void doInBackground(Void... params)
			{
				dao.beginTransaction();
				
				try
				{
					for(Schedule sc:selectSchedules)
					{
						dao.delete(sc);
					}
					dao.commitTransaction();
				} catch (Exception e)
				{
					e.printStackTrace();
					dao.rollback();
				}
				
				return null;
			}
			
			protected void onPostExecute(Void result) 
			{
				toast("删除完成");
				loadScheduleList();

				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();	
	}
	
}
