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
 * ��˵��:������
 * @version ����ʱ�䣺2012-5-15 ����11:18:36
 */
public class AppMainActivity extends CommonActivity
{
	/** Called when the activity is first created. */
	//Context con1;

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
	
	//����������������񲼾�
	private GridView gridView = null;
	//������Ÿ�������
	private List<Item> items = new ArrayList<Item>();
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        //con1=this;

		gridView = (GridView)findViewById(R.id.gridView);

		items.add(new Item("�α�����", R.drawable.ic_launcher));
		items.add(new Item("��Ҫ��ϵ��", R.drawable.addressbook_icon));
		items.add(new Item("������", R.drawable.block_icon));
		items.add(new Item("�ճ̰���", R.drawable.ic_launcher));
		items.add(new Item("ѧ������", R.drawable.student_icon));
		items.add(new Item("��ʦ��", R.drawable.teacher_icon));

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
					Toast.makeText(AppMainActivity.this, "�����ܻ��ڿ�����", Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
		initPopuWindows();
	}
	
	
	@Override
	/*Menu�˵���ť*/
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//menu.add(0, 1, 1, R.string.exit).setIcon(R.drawable.exit);//item id=1
		//menu.add(0, 2, 2, R.string.about).setIcon(R.drawable.info);//item id=2
		//return super.onCreateOptionsMenu(menu);
		return true;
	}
	
	
	/**
	 * ����PopupWindows
	 */
    private void initPopuWindows() {
    	//��ʼ��gridview
		menuGridView=(GridView)View.inflate(this, R.layout.gridview_menu2, null);
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
		return false;// ����Ϊtrue ����ʾϵͳmenu
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
		    b.setTitle("��Ȩ��Ϣ").setMessage("��Ȩ��Ϣ");
				  android.content.DialogInterface.OnClickListener dr =  new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				};
				    b.setPositiveButton("ȷ��", dr);
				    AlertDialog ad = b.create();
				   ad.show();
				   Toast.makeText(AppMainActivity.this, "Test toast", Toast.LENGTH_SHORT).show();

		}
		return super.onOptionsItemSelected(item);
	}


	private class ImageAdapter extends BaseAdapter
	{

		// ��ȡͼƬ�ĸ���
		public int getCount()
		{
			return items.size();
		}

		// ��ȡͼƬ�ڿ��е�λ��
		public Object getItem(int position)
		{
			return items.get(position);
		}


		// ��ȡͼƬID
		public long getItemId(int position)
		{
			return position;
		}

		//��ȡ�����������������ϵ�ͼ��
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
	//û̫���ô�
	public GridView getGridView()
	{
		return gridView;
	}

	public void setGridView(GridView gridView)
	{
		this.gridView = gridView;
	}
}
