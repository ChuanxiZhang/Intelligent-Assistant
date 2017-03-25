 package org.xianghui.adapter;

import java.util.ArrayList;
import java.util.List;

import org.xianghui.activity.R;
import org.xianghui.model.Contact;
import org.xianghui.model.ContactType;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
 /**
 * 类说明:
 * @version 创建时间：2012-5-15 上午11:54:04
 */
public class ContactAdapter extends BaseAdapter
{
	private static final String tag = "IntelligentAssistant";
	
	private List<Contact> contacts = new ArrayList<Contact>();
	private List<Contact> selectContacts = new ArrayList<Contact>();
	private LayoutInflater inflater = null;
	
	public ContactAdapter(List<Contact> contacts,LayoutInflater inflater)
	{
		if(contacts!=null)
		{
			this.contacts = contacts;
		}
		
		this.inflater = inflater;
	}

	@Override
	public int getCount()
	{
		return contacts.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return contacts.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup group)
	{
		if(view==null)
		{
			view = inflater.inflate(R.layout.black_list_item, null);
		}

		TextView name = (TextView)view.findViewById(R.id.name);
		TextView number = (TextView)view.findViewById(R.id.number);
		CheckBox box = (CheckBox)view.findViewById(R.id.checkBox);
		
		name.setText(contacts.get(position).getName());
		number.setText(contacts.get(position).getNumber());
		box.setTag(contacts.get(position));
		
		if(contacts.get(position).getType()!=ContactType.TYPE_NORMAL)
		{
			Log.v(tag, "selectedItem "+contacts.get(position).getName()+" "+contacts.get(position).getNumber()+" "+contacts.get(position).getType());
			
			box.setChecked(true);
			selectContacts.remove(contacts.get(position));
			selectContacts.add(contacts.get(position));
			
		}else
		{
			box.setChecked(false);
		}
		
		box.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				Contact c = (Contact) buttonView.getTag();
				
				if(isChecked)
				{
					Log.v(tag, "add "+c.getName());
					selectContacts.remove(c);
					selectContacts.add(c);
					
				}else
				{
					selectContacts.remove(c);
				}
				Log.v(tag, "选中数量:"+selectContacts.size());
			}
		});
		
		return view;
	}

	public List<Contact> getSelectContacts()
	{
		return selectContacts;
	}
}
