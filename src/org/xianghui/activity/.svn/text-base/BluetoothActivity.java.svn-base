 package org.xianghui.activity;

import org.pzn.common.activity.CommonActivity;
import org.pzn.common.util.ViewUtil;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

 /**
 * 类说明:蓝牙点名类
 * @version 创建时间：2012-5-16 下午12:13:13
 */
public class BluetoothActivity extends CommonActivity
{
	private BluetoothAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		checkBluetoothEnable();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		checkBluetoothEnable();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void checkBluetoothEnable()
	{
		adapter = BluetoothAdapter.getDefaultAdapter();
		if(!adapter.isEnabled())
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
			choiceStudentOrTeacher();
		}
	}
	
	private void choiceStudentOrTeacher()
	{
		new AlertDialog.Builder(this)
		.setTitle("请选择")
		.setMessage("请选择一个客户端")
		.setPositiveButton("学生端", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				startActivity(new Intent(BluetoothActivity.this, StudentActivity.class));
				finish();
			}
		})
		.setNegativeButton("教师端", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				startActivity(new Intent(BluetoothActivity.this, TeacherActivity.class));
				finish();
			}
		})
		.show();
	}
}
