package org.xianghui.adapter;

public class MenuInfo {
	/**
	 * ±êÌâ
	 */
	public String title;
	public int imgsrc;
	/**
	 * ÊÇ·ñÒş²Ø
	 */
	public boolean ishide;
	/**
	 * menuId
	 */
	public int menuId;
	public MenuInfo(int menuId, String title,int imgsrc,Boolean ishide){
		this.menuId=menuId;
		this.title=title;
		this.imgsrc=imgsrc;
		this.ishide=ishide;
	}
}