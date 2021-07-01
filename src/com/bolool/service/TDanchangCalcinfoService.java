package com.bolool.service;

import com.bolool.mybatis.TDanchangCalcinfoMapper;

public class TDanchangCalcinfoService extends BaseService<TDanchangCalcinfoMapper> {
	public TDanchangCalcinfoService() {

	}

	public TDanchangCalcinfoService(boolean autoCommit) {
		super.autoCommit = autoCommit;
	}
}
