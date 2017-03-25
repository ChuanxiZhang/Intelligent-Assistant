 package org.xianghui.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import org.pzn.common.service.CommonService;
import org.xianghui.listener.RecieverMessageListener;
import org.xianghui.model.BMsg;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
 /**
 * 类说明:
 * @version 创建时间：2012-5-16 下午12:31:42
 */
public class StudentBlueToothService extends CommonService implements Runnable
{
	private static final String tag = "IntelligentAssistant";
	private static final String UUID = "aa6530f0-9f1a-11e1-a8b0-0800200c9a66";
	private static StudentBlueToothService service = null;
	
	private BluetoothAdapter adapter = null;
	private BluetoothSocket bluetoothSocket = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private RecieverMessageListener recieverMessageListener = null;
	private boolean isRuning = true;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override
	public void onCreate()
	{
		service = this;
		adapter = BluetoothAdapter.getDefaultAdapter();
		super.onCreate();
	}
	
	@Override
	public void onDestroy()
	{
		service = null;
		isRuning = false;
		close();
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
	}
	
	public boolean connect(BluetoothDevice device)
	{
		adapter.cancelDiscovery();
		try
		{
			if(bluetoothSocket!=null)
			{
				bluetoothSocket.close();
			}
			bluetoothSocket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
			bluetoothSocket.connect();
			oos = new ObjectOutputStream(bluetoothSocket.getOutputStream());
			ois = new ObjectInputStream(bluetoothSocket.getInputStream());
			Log.v(tag, "bluetoothSocket = "+bluetoothSocket);
			new Thread(this).start();
			return true;
			
		} catch (IOException e)
		{
			e.printStackTrace();
			if(bluetoothSocket!=null)
			{
				try
				{
					bluetoothSocket.close();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
				bluetoothSocket = null;
			}
			return false;
		} 
	}
	
	public boolean sendMsg(BMsg msg)
	{
		if(oos!=null)
		{
			try
			{
				oos.writeObject(msg);
				oos.flush();
				return true;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private void close()
	{
		
		if(oos !=null)
		{
			try
			{
				Log.v(tag, "断开蓝牙连接");
				oos.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
			oos = null;
		}
		
		if(ois !=null)
		{
			try
			{
				ois.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
			ois = null;
		}
		
		if(bluetoothSocket!=null)
		{
			try
			{
				Log.v(tag, "断开蓝牙连接");
				bluetoothSocket.close();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
			bluetoothSocket= null;
		}
	}
	
	public void searchDevice()
	{
		Log.v(tag, "开始搜索蓝牙设备");
		adapter.cancelDiscovery();
		adapter.startDiscovery();
	}
	
	public Set<BluetoothDevice> getBondBluetoothDevices()
	{
		if(adapter!=null)
			return adapter.getBondedDevices(); 
		
		return new HashSet<BluetoothDevice>();
	}
///////
	@Override
	public void run()
	{
		BMsg msg = null;

		try
		{
			while(isRuning)
			{
				msg = (BMsg) ois.readObject();
				
				Log.v(tag, "接收到消息:"+msg.getContent());
				
				if(msg!=null&&recieverMessageListener!=null)
				{
					recieverMessageListener.onReciever(msg);
				}
				
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
			msg = null;
			if(recieverMessageListener!=null&&isRuning)
				recieverMessageListener.disconnnect();
		}
		close();
	}

	public RecieverMessageListener getRecieverMessageListener()
	{
		return recieverMessageListener;
	}

	public void setRecieverMessageListener(RecieverMessageListener recieverMessageListener)
	{
		this.recieverMessageListener = recieverMessageListener;
	}
	
	public static StudentBlueToothService getInstance()
	{
		return service;
	}
}

