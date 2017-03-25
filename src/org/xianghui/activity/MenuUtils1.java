package org.xianghui.activity;

import java.util.ArrayList;
import java.util.List;
import org.xianghui.adapter.*;

public class MenuUtils1 {
	public static final int MENU_EXIT=2;
	public static final int MENU_ABOUT=1;
	private static List<MenuInfo> initMenu(){
		List<MenuInfo> list=new ArrayList<MenuInfo>();
		list.add(new MenuInfo(MENU_EXIT,"退出",R.drawable.menu_ic_exit,false));
		list.add(new MenuInfo(MENU_ABOUT,"关于",R.drawable.menu_ic_about,false));
		return list;
	}
	
	/**
	 * 获取当前菜单列表
	 * @return
	 */
	public static List<MenuInfo> getMenuList(){
		List<MenuInfo> list=initMenu();		

		return list;
	}
	
}
