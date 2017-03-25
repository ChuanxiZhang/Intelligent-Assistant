 package org.xianghui.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.database.sqlite.interfaces.SQLiteExecution;
import org.pzn.common.service.CommonService;
import org.pzn.common.util.StringUtil;
import org.xianghui.application.MainApplication;
import org.xianghui.listener.ReachListener;
import org.xianghui.listener.RecieverMessageListener;
import org.xianghui.model.BMsg;
import org.xianghui.model.Reach;
import org.xianghui.model.Student;
import org.xianghui.util.ClassTimeUtil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
 /**
 * 类说明:
 * @version 创建时间：2012-5-16 下午12:31:42
 */
public class TeacherBlueToothService extends CommonService
{
	private static final String UUID = "aa6530f0-9f1a-11e1-a8b0-0800200c9a66";
	private static final String tag = "IntelligentAssistant";

	private BluetoothAdapter adapter = null;
	private BluetoothServerSocket serverSocket = null;
	private boolean run = true;
	private List<BluetoothSocket> bluetoothSockets = new ArrayList<BluetoothSocket>();
	private CommonDao dao = null;
	private ReachListener reachListener = null;
	private static TeacherBlueToothService service = null;
	private RecieverMessageListener recieverMessageListener = null;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		service = this;
		adapter = BluetoothAdapter.getDefaultAdapter();
		dao = ((MainApplication)getApplication()).getDao();
		
	}
	
	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy()
	{
		run = false;
		close();
		super.onDestroy();
	}
	
	
	public boolean startServer()
	{
		java.util.UUID uuid = java.util.UUID.fromString(UUID);
		try
		{
			run = false;
			close();
			serverSocket = adapter.listenUsingRfcommWithServiceRecord("TeacherServer", uuid);
			run = true;
			new AcceptThread().start();
			Log.v(tag, "蓝牙服务启动");
			
			return true;
			
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	
	public void searchBluetoothDevice()
	{
		adapter.startDiscovery();
	}

	public BluetoothAdapter getAdapter()
	{
		return adapter;
	}
	
	/**
	 * 接收蓝牙连接线程
	 * @author aquan
	 *
	 */
	private class AcceptThread extends Thread
	{
		@Override
		public void run()
		{
			BluetoothSocket bluetoothSocket =null;
			Log.v(tag, "服务端启动，等待接收蓝牙设备");
	
				try
				{
					while(run)
					{
						bluetoothSocket = serverSocket.accept();
						Log.v(tag, "接收到蓝牙连接");
						new ProccessThread(bluetoothSocket).start();
					}
					
				} catch (IOException e)
				{
					e.printStackTrace();
					if(recieverMessageListener!=null&&run)
					{
						recieverMessageListener.disconnnect();
					}
					
				}finally
				{
					close();
				}
			Log.v(tag, "停止蓝牙接收");
			run = false;
		}
		
	}
	
	private void close()
	{
		run = false;
		if(serverSocket!=null)
		{
			try
			{
				serverSocket.close();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			serverSocket = null;
		}
	}
	
	/**
	 * 处理蓝牙发送的消息
	 * @author aquan
	 *
	 */
	private class ProccessThread extends Thread
	{
		private BluetoothSocket bluetoothSocket = null;

		private ObjectInputStream ois = null;
		private ObjectOutputStream oos = null;
		
		public ProccessThread(BluetoothSocket bluetoothSocket)
		{
			super();
			this.bluetoothSocket = bluetoothSocket;
		}

		@Override
		public void run()
		{
			bluetoothSockets.add(bluetoothSocket);
			
			try
			{
				ois = new ObjectInputStream(bluetoothSocket.getInputStream());
				oos = new ObjectOutputStream(bluetoothSocket.getOutputStream());

				Log.v(tag, "开始接收到蓝牙数据");
				while(run)
				{
					BMsg msg = (BMsg) ois.readObject();
					if(msg!=null)
					{
						Log.v(tag, "接收到蓝牙数据");
						handlerBMsg(msg);
						msg = new BMsg();
						msg.setCmd(BMsg.CMD_OK);
					}
				}
				
				
			} catch (Exception e)
			{
				e.printStackTrace();
				
			}finally
			{
				try
				{
					oos.close();
					
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				oos = null;
				try
				{
					ois.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				ois = null;
			}
			
			bluetoothSockets.remove(bluetoothSocket);
		}
		
		//////////
		@SuppressWarnings("unchecked")
		private void handlerBMsg(final BMsg msg)
		{
			BMsg rMsg = new BMsg();
			rMsg.setCmd(BMsg.CMD_OK);
			rMsg.setContent("服务端无法解析");
			
			switch (msg.getCmd())
			{
			case BMsg.CMD_REGISTER://注册
				Log.v(tag, "用户注册:"+msg.getName()+"  "+msg.getNumber()+"  "+msg.getImei());
				Student student = new Student();
				if(StringUtil.isEmptyOrNull(msg.getImei()))
				{
					rMsg.setCmd(BMsg.CMD_ERROR);
					rMsg.setContent("IMEI号码为空，非法注册");
					break;
				}
				if(StringUtil.isEmptyOrNull(msg.getName()))
				{
					rMsg.setCmd(BMsg.CMD_ERROR);
					rMsg.setContent("姓名为空，非法注册");
					break;
				}
				if(StringUtil.isEmptyOrNull(msg.getNumber()))
				{
					rMsg.setCmd(BMsg.CMD_ERROR);
					rMsg.setContent("学号为空，非法注册");
					break;
				}
				if(StringUtil.isEmptyOrNull(msg.getNumber()))
				{
					rMsg.setCmd(BMsg.CMD_ERROR);
					rMsg.setContent("学号为空，非法注册");
					break;
				}

				student.setImei(msg.getImei());
				student.setName(msg.getName());
				student.setNumber(msg.getNumber());
				student.setClassNumber(msg.getClassNumber());
				
				try
				{
					dao.save(student);
					rMsg.setCmd(BMsg.CMD_OK);
					rMsg.setContent("注册成功");
					
				} catch (Exception e)
				{
					e.printStackTrace();

					rMsg.setCmd(BMsg.CMD_ERROR);
					rMsg.setContent("注册失败,不能重复注册");
				}
				
				break;
			case BMsg.CMD_REACH://签到
				Log.v(tag, "签到");
				
				List<Student> students = dao.queryForList(Student.class, "select * from Student where imei='"+msg.getImei()+"'");
				
				if(students==null||students.size()<=0)
				{
					rMsg.setCmd(BMsg.CMD_ERROR);
					rMsg.setContent("您尚未注册，无法签到");
					
				}else
				{
					Reach reach = new Reach();
					reach.setDate(new Date());
					reach.setReach(true);
					int late = ClassTimeUtil.isLate(new Date());
					reach.setLate(late<0?0:late);
					reach.setImei(msg.getImei());
					reach.setClassNumber(ClassTimeUtil.classNumber());
					
					try
					{
						
						dao.execute(new SQLiteExecution()
						{
							@Override
							public void onExecute(SQLiteDatabase db)
							{
								int cNumber = ClassTimeUtil.classNumber();
								long fromTime = ClassTimeUtil.getFromTime(cNumber).getTime()-10*1000*60;
								long toTime = ClassTimeUtil.getToTime(cNumber).getTime();
								db.execSQL("delete from Reach where classNumber=? and imei=? and date>? and date<?", new String[]{cNumber+"",fromTime+"",toTime+""});
							}
						});
						
						dao.save(reach);

						rMsg.setCmd(BMsg.CMD_OK);
						rMsg.setContent("签到成功,您迟到了"+late+"分钟，请勿重复签到");
						if(reachListener!=null)
						{
							reachListener.onUpdate();
						}
					} catch (Exception e)
					{
						e.printStackTrace();
						rMsg.setCmd(BMsg.CMD_ERROR);
						rMsg.setContent("签到失败");
					}
					
				}
				
				break;

			default:
				break;
			}
			try
			{
				oos.writeObject(rMsg);
				oos.flush();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public ReachListener getReachListener()
	{
		return reachListener;
	}

	public void setReachListener(ReachListener reachListener)
	{
		this.reachListener = reachListener;
	}
	
	public static TeacherBlueToothService getInstance()
	{
		return service;
	}

	public void setRecieverMessageListener(RecieverMessageListener recieverMessageListener)
	{
		this.recieverMessageListener = recieverMessageListener;
	}
}
