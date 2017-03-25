 package org.xianghui.listener;

import org.xianghui.model.BMsg;
 /**
 * 类说明:
 * @version 创建时间：2012-5-18 上午10:58:27
 */
public interface RecieverMessageListener
{
	public void onReciever(BMsg msg);
	public void disconnnect();
}
