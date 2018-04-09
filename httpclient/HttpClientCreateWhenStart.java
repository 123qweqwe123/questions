package com.bdcor.pip.client.util.httpclient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdcor.pip.client.core.sys.ISysLoadData;
import com.bdcor.pip.client.core.sys.ISysSaveData;

public class HttpClientCreateWhenStart implements ISysLoadData ,ISysSaveData{
	private static final Logger log = LoggerFactory.getLogger(HttpClientCreateWhenStart.class);

	@Override
	public void saveData() {
		HttpClientAthentication.closeHttpClient();
		log.debug("销毁httpclient");
	}

	@Override
	public void loadData() throws IOException {
		log.debug("开始 创建httpclient单例");
		HttpClientAthentication.getInstance();
		log.debug("创建httpclient单例");
		
	}

}
