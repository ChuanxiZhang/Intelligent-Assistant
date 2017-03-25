package org.xianghui.activity;

import java.util.ArrayList;
import java.util.List;

import org.pzn.common.activity.CommonActivity;
import org.xianghui.model.Contact;
import org.xianghui.model.ContactType;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * 类说明:多选联系人
 * 
 * @version 创建时间：2012-5-15 下午12:30:46
 */
public class ContactsBaseActivity extends CommonActivity
{
	private static final String tag = "IntelligentAssistant";

	public List<Contact> loadContacts(List<Contact> selectContacts,int type)
	{
		List<Contact> contacts = new ArrayList<Contact>();

		Cursor c = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				null,
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");

		while (c.moveToNext())
		{
			//联系人姓名
			String disPlayName = c.getString(c
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			//联系人Id，通过Id获取号码
			String contactId = c.getString(c
					.getColumnIndex(ContactsContract.Contacts._ID));
			int phoneCount = c
					.getInt(c
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (phoneCount > 0)
			{
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phones.moveToNext())
				{
					// 遍历所有的电话号码
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					
					Contact contact = new Contact();
					contact.setName(disPlayName);
					contact.setNumber(phoneNumber);
					contact.setType(ContactType.TYPE_NORMAL);
					
					for(Contact ct:selectContacts)
					{
						if(ct.getNumber().equals(contact.getNumber()))
						{
							Log.v(tag, "ct "+ct.getNumber()+"=="+contact.getNumber());
							contact.setType(type);
						}
					}
					contacts.add(contact);
				}
				phones.close();
			}

		}
		c.close();
		return contacts;
	}
}
