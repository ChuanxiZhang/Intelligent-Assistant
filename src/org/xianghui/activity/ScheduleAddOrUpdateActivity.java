 package org.xianghui.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.pzn.common.activity.CommonActivity;
import org.pzn.common.annotation.Component;
import org.pzn.common.database.sqlite.dao.CommonDao;
import org.pzn.common.util.StringUtil;
import org.pzn.common.util.ViewUtil;
import org.xianghui.application.MainApplication;
import org.xianghui.model.Schedule;
import org.xianghui.service.SettingScheduleService;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
 /**
 * 类说明:添加或者更新日程
 * @version 创建时间：2012-5-15 下午10:05:54
 */
public class ScheduleAddOrUpdateActivity extends CommonActivity implements OnClickListener
{
	private static final String tag = "IntelligentAssistant";
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Component
	private EditText title;
	@Component
	private EditText content;
	@Component
	private Button startTime;
	@Component
	private Button endTime;
	@Component
	private Button remindTime;
	@Component
	private Button okButton;
	@Component
	private Button cancelButton;
	private CommonDao dao = null;
	
	private boolean isUpdate = false;
	private Schedule schedule = null;
	
	private AlarmManager alarmManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_add_or_update);
		dao = ((MainApplication)getApplication()).getDao();
		alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

		isUpdate = getIntent().getBooleanExtra("isUpdate", false);
		
		if(isUpdate)
		{
			schedule = (Schedule) getIntent().getSerializableExtra("schedule");
			title.setText(schedule.getTitle());
			content.setText(schedule.getContent());

			startTime.setText(sdf.format(schedule.getStartDate()));
			startTime.setTag(schedule.getStartDate());
			endTime.setText(sdf.format(schedule.getEndDate()));
			endTime.setTag(schedule.getEndDate());
			remindTime.setText(sdf.format(schedule.getRemindDate()));
			remindTime.setTag(schedule.getRemindDate());
			
		}else
		{
			schedule = new Schedule();
		}
		
		startTime.setOnClickListener(this);
		endTime.setOnClickListener(this);
		remindTime.setOnClickListener(this);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		
		
		
	}
	


	private void choiceTime(final Button button)
	{
		final Calendar calendar = Calendar.getInstance();
		
		new DatePickerDialog(this, new OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
			{

				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				
				new TimePickerDialog(ScheduleAddOrUpdateActivity.this, new OnTimeSetListener()
				{
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute)
					{
						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						
						button.setTag(calendar.getTime());
						button.setText(sdf.format(calendar.getTime()));
						
					}
				}, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
				.show();
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
		.show();
	}
	
	public void addOrUpdate()
	{
		
		if(StringUtil.isEmptyOrNull(title.getText().toString()))
		{
			ViewUtil.showMesssageDialog(this, "标题不能为空");
			return;
		}
		schedule.setTitle(title.getText().toString().trim());
		schedule.setContent(content.getText().toString());
		
		if(startTime.getTag() == null)
		{
			ViewUtil.showMesssageDialog(this, "请选择开始时间");
			return;
		}
		schedule.setStartDate((Date) startTime.getTag());
		
		if(endTime.getTag() == null)
		{
			ViewUtil.showMesssageDialog(this, "请选择结束时间");
			return;
		}
		schedule.setEndDate((Date) endTime.getTag());
		
		if(remindTime.getTag() == null)
		{
			ViewUtil.showMesssageDialog(this, "请选择提醒时间");
			return;
		}
		schedule.setRemindDate((Date) remindTime.getTag());
		
		
		
		new AsyncTask<Void, Void, Boolean>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(ScheduleAddOrUpdateActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("请稍后");
				if(isUpdate)
				{
					dialog.setMessage("正在修改日程...");
					
				}else
				{
					dialog.setMessage("正在添加日程...");
					
				}
				dialog.show();
			};
			
			@Override
			protected Boolean doInBackground(Void... params)
			{
				boolean flag = false;
				try
				{

					if(isUpdate)
					{
						dao.update(schedule);
						
					}else
					{
						dao.save(schedule);
					}
					
					
					flag = true;
					
				} catch (Exception e)
				{
					e.printStackTrace();
					flag = false;
				}
				
				return flag;
			}
			
			protected void onPostExecute(Boolean flag) 
			{
				startService(new Intent(ScheduleAddOrUpdateActivity.this, SettingScheduleService.class));
				
				if(flag)
				{
					if(isUpdate)
					{
						toast("修改成功");
					
					}else
					{
						toast("添加成功");
					}
					finish();
					
				}else
				{
					if(isUpdate)
					{
						messageBox("修改失败");
					
					}else
					{
						messageBox("添加失败");
					}
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}

	@Override
	public void onClick(View v)
	{
		if(v == cancelButton)
		{
			finish();
			
		}else if(v == okButton)
		{
			addOrUpdate();
			
		}else if(v == startTime)
		{
			choiceTime((Button) v);
			
		}else if(v == endTime)
		{
			choiceTime((Button) v);
			
		}else if(v == remindTime)
		{
			choiceTime((Button) v);
		}
	}
}
