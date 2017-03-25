 package org.xianghui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pzn.common.activity.CommonActivity;
import org.pzn.common.annotation.Component;
import org.pzn.common.util.StringUtil;
import org.pzn.common.util.ViewUtil;
import org.xianghui.listener.RecieverMessageListener;
import org.xianghui.model.BMsg;
import org.xianghui.service.StudentBlueToothService;
import org.xianghui.util.SystemUtil;

import android.app.AlertDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
 /**
 * 类说明:
 * @version 创建时间：2012-5-16 下午12:36:47
 */
public class StudentActivity extends CommonActivity  implements OnItemClickListener,RecieverMessageListener,OnClickListener
{
	private static final String tag = "IntelligentAssistant";
	private static final int MENU_SEARCH = 1;
	

	private static final int REQUEST_CODE_REACH = 1;
	private static final int REQUEST_CODE_REGISTER = 2;
	
	private BluetoothReciever reciever = null;
	
	@Component
	private ListView listView = null;
	private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private List<Map<String, String>> data = new ArrayList<Map<String,String>>();
	private SimpleAdapter adapter = null;

	@Component
	private Button registerButton = null;
	@Component
	private Button reachButton = null;
	
	private int requestCode = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student);

		reachButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		
		reciever = new BluetoothReciever();
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND); 
		registerReceiver(reciever, filter); 

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); 
		registerReceiver(reciever, filter); 

		
		adapter = new SimpleAdapter(this, data, R.layout.bluetooth_item, new String[]{"name","address"}, new int[]{R.id.name,R.id.address});
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this );

		checkBluetoothEnable();
		
	}
	
	
	@Override
	protected void onDestroy()
	{
		unregisterReceiver(reciever);
		stopService(new Intent(this, StudentBlueToothService.class));
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(MENU_SEARCH, MENU_SEARCH, MENU_SEARCH, "搜索设备");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case MENU_SEARCH:
			if(StudentBlueToothService.getInstance()!=null)
			{
				data.clear();
				devices.clear();
				adapter.notifyDataSetChanged();
				setTitle("正在搜索蓝牙设备，请稍后...");
				StudentBlueToothService.getInstance().searchDevice();
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		checkBluetoothEnable();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private void loadHasBoundDevice()
	{
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
				for(BluetoothDevice device:StudentBlueToothService.getInstance().getBondBluetoothDevices())
				{
					Log.v(tag, "已经配对的设备:"+device.getName());
					devices.add(device);
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", device.getName());
					map.put("address", device.getAddress());
					data.add(map);
				}
				runOnUiThread(new Runnable()
				{
					
					@Override
					public void run()
					{
						adapter.notifyDataSetChanged();
					}
				});
				StudentBlueToothService.getInstance().searchDevice();
			};
		}.start();
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
			startService(new Intent(this, StudentBlueToothService.class));
			loadHasBoundDevice();
		}
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
					devices.add(device);
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", device.getName());
					map.put("address", device.getAddress());
					data.add(map);
					adapter.notifyDataSetChanged();
				} 

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED .equals(action)) 
			{ 
				toast("搜索完成"); 
				setTitle("搜索完成");
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	
	private void connect(final BluetoothDevice device)
	{
		new AsyncTask<Void, Void, Boolean>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(StudentActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在连接该设备...");
				dialog.show();
			};
			
			@Override
			protected Boolean doInBackground(Void... params)
			{
				return StudentBlueToothService.getInstance().connect(device);
			}
			
			protected void onPostExecute(Boolean result) 
			{
				if(!result)
				{
					ViewUtil.showMesssageDialog(StudentActivity.this, "连接失败");
					
				}else
				{
					toast("连接成功");
					setTitle("已经连接到'"+device.getName());
					registerButton.setEnabled(true);
					reachButton.setEnabled(true);
					StudentBlueToothService.getInstance().setRecieverMessageListener(StudentActivity.this);
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int ps, long arg3)
	{
		ViewUtil.showConfirmDialog(this, "注意", "是否连接该设备?", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				connect(devices.get(ps));
			}
			
		}, null);
	}
	
	
	
	private void showRegisterDialog()
	{
		View view = LayoutInflater.from(this).inflate(R.layout.register, null);
		final EditText numberEditText = (EditText) view.findViewById(R.id.numberEditText); 
		final EditText nameEditText = (EditText) view.findViewById(R.id.nameEditText); 
		final EditText classNumberEditText = (EditText) view.findViewById(R.id.classNumberEditText); 
		
		new AlertDialog.Builder(this)
		.setTitle("请输入相关信息")
		.setView(view)
		.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String number = numberEditText.getText().toString().trim().trim();
				String name = nameEditText.getText().toString().trim();
				String classNumber = classNumberEditText.getText().toString().trim();
				
				if(StringUtil.isEmptyOrNull(name)||StringUtil.isEmptyOrNull(number)||StringUtil.isEmptyOrNull(classNumber))
				{
					messageBox("用户信息不能为空");
					return;
				}
				
				register(name, number,classNumber);
			}
		})
		.setNegativeButton("取消", null)
		.show();
	}
	
	private void register(final String name,final String number,final String classNumber)
	{
		requestCode = REQUEST_CODE_REGISTER;
		new AsyncTask<Void, Void, Boolean>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(StudentActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在发送注册信息...");
				dialog.show();
			};
			
			@Override
			protected Boolean doInBackground(Void... params)
			{
				if(StudentBlueToothService.getInstance()!=null)
				{
					BMsg msg = new BMsg();
					msg.setCmd(BMsg.CMD_REGISTER);
					msg.setName(name);
					msg.setNumber(number);
					msg.setImei(SystemUtil.getIMEI(StudentActivity.this));
					msg.setClassNumber(classNumber);
					return StudentBlueToothService.getInstance().sendMsg(msg);
				}
				return false;
			}
			
			protected void onPostExecute(Boolean result) 
			{
				if(!result)
				{
					ViewUtil.showMesssageDialog(StudentActivity.this, "发送失败");
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}
	
	
	private void reach()
	{
		requestCode = REQUEST_CODE_REACH;
		new AsyncTask<Void, Void, Boolean>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(StudentActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				dialog.setMessage("正在签到...");
				dialog.show();
			};
			
			@Override
			protected Boolean doInBackground(Void... params)
			{
				if(StudentBlueToothService.getInstance()!=null)
				{
					BMsg msg = new BMsg();
					msg.setCmd(BMsg.CMD_REACH);
					msg.setImei(SystemUtil.getIMEI(StudentActivity.this));
					return StudentBlueToothService.getInstance().sendMsg(msg);
				}
				return false;
			}
			
			protected void onPostExecute(Boolean result) 
			{
				if(!result)
				{
					reachButton.setEnabled(true);
					
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}

	@Override
	public void onReciever(final BMsg msg)
	{
		switch (msg.getCmd())
		{
		case BMsg.CMD_OK:
			
			this.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					switch (requestCode)
					{
					case REQUEST_CODE_REGISTER:
						messageBox(msg.getContent());
						
						break;
						
					case REQUEST_CODE_REACH:
						messageBox(msg.getContent());
						reachButton.setEnabled(false);
						break;

					default:
						break;
					}
					
					requestCode = 0;
				}
			});
			break;
			
		case BMsg.CMD_ERROR:
			this.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					switch (requestCode)
					{
					case REQUEST_CODE_REGISTER:
						messageBox(msg.getContent());
						
						break;
						
					case REQUEST_CODE_REACH:
						messageBox(msg.getContent());
						reachButton.setEnabled(true);
						break;

					default:
						break;
					}
					
					requestCode = 0;
				}
			});
			break;

		default:
			break;
		}
	}


	@Override
	public void onClick(View v)
	{
		if(v == registerButton)
		{
			showRegisterDialog();
			
		}else if(v == reachButton)
		{
//			getSharedPreferences("IntelligentAssistant", Context.MODE_PRIVATE).edit().putLong("reachTime", System.currentTimeMillis()).commit();
			long reachTime = getSharedPreferences("IntelligentAssistant", Context.MODE_PRIVATE).getLong("reachTime", 0);
			
			if(System.currentTimeMillis()-reachTime<1000*60*45)
			{
				messageBox("注意", "这节课你已经签到过了，重复签到有可能会导致迟到，确定继续么?", "确定", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						reachButton.setEnabled(false);
						reach();
					}
				}, "取消", null);
				
			}else
			{
				reachButton.setEnabled(false);
				reach();
			}
		}
	}

	@Override
	public void disconnnect()
	{
		runOnUiThread(new Runnable()
		{
			
			@Override
			public void run()
			{
				registerButton.setEnabled(false);
				reachButton.setEnabled(false);
				
				setTitle("未连接");
				messageBox("已经断开连接");
			}
		});
	}
	
}
