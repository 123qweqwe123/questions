package com.bdcor.pip.client.main;

import com.bdcor.pip.client.core.sys.ISysLoadData;
import com.bdcor.pip.client.jetty.JettyServer;
import com.bdcor.pip.client.util.DatLogger;
import com.bdcor.pip.client.vo.dict.handler.PatientHandler;


public class StartJetty implements ISysLoadData {

	@Override
	public void loadData() {

		//启动jetty服务
		DatLogger.logSysStartDown(DatClientMain.class,"开始jetty启动");
		JettyServer.startJettyServer(null);
		DatLogger.logSysStartDown(DatClientMain.class,"完成jetty启动");
	}

	

}
