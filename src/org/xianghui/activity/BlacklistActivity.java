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
 * ��˵��:�������б�
 * @version ����ʱ�䣺2012-5-15 ����11:38:41
 */
public class BlacklistActivity extends ContactsBaseActivity
{	
	
	/**
	 * ����popupwindow
	 */	
	private PopupWindow popup;
	/**
	 * ����������
	 */
	private MenuAdapter menuAdapter;
	//�˵����б�
	private List<MenuInfo> menulists;
	//����gridview
	private GridView menuGridView;
	
	
	//������ZMQ���
	
	
	//�����־�ı�ǩ
	private static final String tag = "IntelligentAssistant";
	//��ʶ������������menu
	private static final int MENU_APPLY = 1;
	
	//���ñ�ʶ������������ĸ������õ�Activity���صĽ��
	private static final int REQUEST_CODE_ADD = 1;
	
	
	@Component
	private ListView listView = null;
	//�������ݿ�
	private CommonDao dao = null;
	//������ϵ��
	private List<Contact> contacts = null;
	//������
	private ContactAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		dao = ((MainApplication)getApplication()).getDao();
		//���غ�����
		loadBlackList();
		initPopuWindows();
	}
	//���ɲ˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//menu.add(MENU_APPLY,MENU_APPLY,MENU_APPLY,"Ӧ������");
		//return super.onCreateOptionsMenu(menu);
		return true;
	}
	
	
	
	/**
	 * ����PopupWindows
	 */
    private void initPopuWindows() {
    	//��ʼ��gridview
		menuGridView=(GridView)View.inflate(this, R.layout.gridview_menu, null);
		//��ʼ��PopupWindow,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT������ʾ
		popup = new PopupWindow(menuGridView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// ����menu�˵�����
		popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_background));
		// menu�˵���ý��� ���û�л�ý���menu�˵��еĿؼ��¼��޷���Ӧ
		popup.setFocusable(true);
		//������ʾ�����صĶ���
		popup.setAnimationStyle(R.style.menushow);
		popup.update();
		//���ô�����ȡ����
		menuGridView.setFocusableInTouchMode(true);
		//���ü����¼�,������²˵��������ز˵�
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
		//��Ӳ˵���ť�¼�
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
		return false;// ����Ϊtrue ����ʾϵͳmenu
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("menu");
		return super.onPrepareOptionsMenu(menu);
	}
    
	
	
	
	
	
	
	
	
	//�����Ӧ�����á��󴥷�
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
		//�첽���ȽϺ�ʱ�������첽
		new AsyncTask<Void, Void, List<Contact>>()
		{
			
			private ProgressDialog dialog = new ProgressDialog(BlacklistActivity.this);

			protected void onPreExecute() 
			{
				dialog.setTitle("���Ժ�");
				dialog.setMessage("���ڼ��غ������б�...");
				dialog.show();
			};
			
			@Override
			protected List<Contact> doInBackground(Void... params)
			{
				//���Һ�����
				List<Contact> blacklist = dao.queryForList(Contact.class, "select * from Contact where type=?",new String[]{ContactType.TYPE_BLACK+""});
				Log.v(tag, "����������:"+blacklist.size());
				//�Ȱ�������ϵ�˵ĺ�������ʶ����
				contacts = loadContacts(blacklist,ContactType.TYPE_BLACK);
				//�������򣬰Ѻ���������ǰ��
				Collections.sort(contacts,new Comparator<Contact>()
				{
					@Override
					public int compare(Contact c1, Contact c2)
					{
						//�������ı�ʶ������3���������ǰ��
						return c2.getType() - c1.getType();
					}
				});
				return contacts;
			}
			//��������ʾ����
			protected void onPostExecute(List<Contact> result) 
			{
				
				if(result == null || result.size()<=0)
				{
					toast("���޺������б�");
					
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
				dialog.setTitle("���Ժ�");
				dialog.setMessage("����Ӧ������");
				dialog.show();
			};
			
			@Override
			protected Boolean doInBackground(Void... params)
			{
				boolean flag = false;
				dao.execute(new SQLiteExecution()
				{
					//��ԭ�ȵĺ�����ɾ��
					@Override
					public void onExecute(SQLiteDatabase db)
					{
						db.execSQL("delete from Contact where type=?",new String[]{ContactType.TYPE_BLACK+""});
					}
				});
				//��ʼ����
				dao.beginTransaction();
				
				try
				{
					//��ȡ����ѡ����ϵ��
					List<Contact> list = adapter.getSelectContacts();
					Log.v(tag, "����������:"+list.size());
					for(Contact c:list)
					{
						c.setType(ContactType.TYPE_BLACK);
						dao.save(c);
					}
					//�ύ����
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
			//����һ���������ڲ�ͬ���̣߳�result���Ϻ������ؽ�����Ϻ������ܸ��½��棬�����
			protected void onPostExecute(Boolean result) 
			{
				
				if(result)
				{
					toast("Ӧ�óɹ�");
					finish();
					
				}else
				{
					messageBox("Ӧ�ó����쳣");
				}
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
			};
			
		}.execute();
	}
	
}
