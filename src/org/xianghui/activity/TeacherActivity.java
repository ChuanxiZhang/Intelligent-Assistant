 package org.xianghui.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pzn.common.activity.CommonActivity;
import org.pzn.common.annotation.Component;
import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.util.ViewUtil;
import org.xianghui.application.MainApplication;
import org.xianghui.listener.ReachListener;
import org.xianghui.model.Reach;
import org.xianghui.model.Student;
import org.xianghui.service.TeacherBlueToothService;
import org.xianghui.util.ClassTimeUtil;
import org.xianghui.util.DateFormatUtil;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 类说明:
 * @version 创建时间：2012-5-16 下午12:36:15
 */
public class TeacherActivity extends CommonActivity implements ReachListener
{
	private static final String tag = "IntelligentAssistant";
	
	@Component
	private GridView gridView = null;
	private List<Item> items = new ArrayList<Item>();
	private Map<String,Integer> iconMap = new HashMap<String, Integer>();
	private List<Student> students = new ArrayList<Student>();
	private BluetoothReciever reciever = null;
	private CommonDao dao = null;
	private ImageAdapter adapter = new ImageAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		dao = ((MainApplication)getApplication()).getDao();
		
		reciever = new BluetoothReciever();

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND); 
		registerReceiver(reciever, filter); 

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); 
		registerReceiver(reciever, filter); 
		
		gridView.setAdapter(adapter);
		loadStudent();
		checkBluetoothEnable();
	}
	
	@Override
	protected void onDestroy()
	{
		unregisterReceiver(reciever);
		super.onDestroy();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		checkBluetoothEnable();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void checkBluetoothEnable()
	{
		if(!BluetoothAdapter.getDefaultAdapter().isEnabled())
		{
			ViewUtil.showConfirmDialog(this, "注意", "检测到你的蓝牙未开启，是否去开启蓝牙?", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
					startActivityForResult(enabler,0);//同startActivity(enabler);
					
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
			startService(new Intent(this, TeacherBlueToothService.class));
			new Thread()
			{
				public void run() 
				{
					try
					{
						sleep(1000);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					TeacherBlueToothService.getInstance().setReachListener(TeacherActivity.this);
					
						runOnUiThread(new Runnable()
						{
							
							@Override
							public void run()
							{
								if(!TeacherBlueToothService.getInstance().startServer())
								{
									ViewUtil.showMesssageDialog(TeacherActivity.this, "错误", "蓝牙服务无法启动，程序即将退出", new DialogInterface.OnClickListener()
									{
										
										@Override
										public void onClick(DialogInterface dialog, int which)
										{
											// TODO Auto-generated method stub
											stopService(new Intent(TeacherActivity.this, TeacherBlueToothService.class));
											finish();
										}
									});
								
								}else
								{
									toast("蓝牙服务已经启动，学生可以开始进行点名操作");
								}
							}
						});
				};
			}.start();
		}
	}
	
	
	public void loadStudent()
	{
		new AsyncTask<Void, Void, Void>()
		{
			private ProgressDialog dialog = new ProgressDialog(TeacherActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在获取点名信息...");
				dialog.show();
			};
			
			@Override
			protected Void doInBackground(Void... params)
			{
				students = dao.queryForList(Student.class,	"select  * from Student order by number asc,classNumber asc");
				
				if(students!=null)
				{
					for(Student student:students)
					{
						items.add(new Item("("+student.getClassNumber()+"班)"+student.getName(), R.drawable.red,student.getImei()));
						iconMap.put(student.getImei(), R.drawable.red);
					}
				}
				
				return null;
			}
			
			protected void onPostExecute(Void result) 
			{
				if(students.size()<=0)
				{
					toast("暂无学生信息");
				
				}else
				{
					adapter.notifyDataSetChanged();
					refresh(true);
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}
	
	public void refresh(final boolean isShowProgressDialog)
	{
		
		new AsyncTask<Void, Void, Void>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(TeacherActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在获取点名信息...");
				if(isShowProgressDialog)
					dialog.show();
			};
			
			@Override
			protected Void doInBackground(Void... params)
			{
				int classNumber = ClassTimeUtil.classNumber();
				Log.v(tag, "classNumber = "+classNumber+" "+ClassTimeUtil.getFromTime(classNumber)+" -- "+ClassTimeUtil.getToTime(classNumber));
				long fromTime = ClassTimeUtil.getFromTime(classNumber).getTime()-10*1000*60;
				long toTime = ClassTimeUtil.getToTime(classNumber).getTime();
				List<Reach> reachs = dao.queryForList(Reach.class, "select * from Reach where classNumber=? and date>? and date<?",new String[]{classNumber+"",fromTime+"",toTime+""});
				
				File dirFile = new File(Environment.getExternalStorageDirectory()+"/点名记录");
				if(!dirFile.exists())
				{
					dirFile.mkdirs();
				}
				
				DataOutputStream dos = null;
				
				try
				{
					File file = new File(Environment.getExternalStorageDirectory()+"/"+DateFormatUtil.get_yyyy_MM_dd_HH_mm_ss(new Date())+"---第"+ClassTimeUtil.classNumber()+"节课.txt");
					if(file.exists())
						file.delete();
					
					dos = new DataOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+DateFormatUtil.get_yyyy_MM_dd_HH_mm_ss(new Date())+"---第"+ClassTimeUtil.classNumber()+"节课.txt"));
					
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
					dos = null;
					
				}
				
				if(dos!=null)
				{
					try
					{
						dos.writeUTF("学号\t姓名\t班级\t考勤\t\r\n");
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}

				for(Reach r:reachs)
				{
					String rs = "";
					
					for(Student s:students)
					{
						if(s.getImei().trim().equals(r.getImei().trim()))
						{
							rs = s.getNumber()+"\t"+s.getName()+"\t"+s.getClassNumber()+"\t";
						}
					}
					
					
					if(!r.isReach())
					{
						rs+="缺席\t";
						iconMap.put(r.getImei(), R.drawable.red);
						Log.v(tag, r.getImei()+" -- 缺席");
						
					}else if(r.getLate()>0)
					{
						rs+="迟到("+r.getLate()+"分钟)\t";
						iconMap.put(r.getImei(), R.drawable.yellow);
						Log.v(tag, r.getImei()+" -- 迟到");
						
					}else
					{
						rs+="正常\t";
						iconMap.put(r.getImei(), R.drawable.green);
						Log.v(tag, r.getImei()+" -- 正常");
					}
					
					if(dos!=null)
					{
						try
						{
							dos.writeUTF(rs+"\r\n");
							dos.flush();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					
				}
					
				if(dos!=null)
				{
					try
					{
						dos.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				dos = null;
				return null;
			}
			
			protected void onPostExecute(Void result) 
			{
				
				adapter.notifyDataSetChanged();
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}
	
	/**
	 * 蓝牙广播接收器
	 * @author aquan
	 *
	 */
	public class BluetoothReciever extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction(); 

			if (BluetoothDevice.ACTION_FOUND.equals(action)) 
			{ 
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 

				if (device.getBondState() != BluetoothDevice.BOND_BONDED) 
				{ 
					Log.v(tag, "发现设备:" + device.getName() + device.getAddress()); 
				} 

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED .equals(action)) 
			{ 
				Log.v(tag, "搜索完成"); 
			} 
		}
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


		public View getView(int position, View convertView, ViewGroup parent)
		{
			LinearLayout ll = null;
			
			if(convertView==null)
			{
				ll = (LinearLayout)LayoutInflater.from(TeacherActivity.this).inflate(R.layout.grid_item, null);
			
			}else
			{
				ll = (LinearLayout)convertView;
			}
			
			TextView tv = (TextView)ll.findViewById(R.id.item_title);
			ImageView iv = (ImageView)ll.findViewById(R.id.item_image);
			
			tv.setText(items.get(position).name);
			iv.setImageResource(iconMap.get(items.get(position).imei));
			
			convertView = ll;
			
			
			return convertView;
		}
		
		@Override
		public void notifyDataSetChanged()
		{
			super.notifyDataSetChanged();
			
			
		}
	}
	
	private class Item
	{
		public String name;
		public int iconId;
		public String imei;
		public Item(String name, int iconId, String imei)
		{
			super();
			this.name = name;
			this.iconId = iconId;
			this.imei = imei;
		}
	}

	@Override
	public void onUpdate()
	{
		runOnUiThread(new Runnable()
		{
			
			@Override
			public void run()
			{
				refresh(false);
			}
		});
	}
	
	@Override
	public void onBackPressed()
	{
		
		messageBox("注意", "是否在后台让学生进行点名?", "后台点名", new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		}, "直接退出", new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				stopService(new Intent(TeacherActivity.this,TeacherBlueToothService.class));
				finish();
			}
		});
	}
}
