package org.xianghui.activity;

import java.util.ArrayList;
import java.util.List;
import org.xianghui.adapter.*;

public class MenuUtils {
	public static final int MENU_APPLY=1;
	//public static final int MENU_EXIT=2;
	//public static final int MENU_ABOUT=3;
	private static List<MenuInfo> initMenu(){
		List<MenuInfo> list=new ArrayList<MenuInfo>();
		list.add(new MenuInfo(MENU_APPLY,"Ӧ��",R.drawable.menu_ic_setting,false));
		//list.add(new MenuInfo(MENU_EXIT,"�˳�",R.drawable.menu_ic_exit,false));
		//list.add(new MenuInfo(MENU_ABOUT,"����",R.drawable.menu_ic_about,false));
		return list;
	}
	
	/**
	 * ��ȡ��ǰ�˵��б�
	 * @return
	 */
	public static List<MenuInfo> getMenuList(){
		List<MenuInfo> list=initMenu();		

		return list;
	}
	
}
