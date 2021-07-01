package com.bolool.service;

import com.bolool.mybatis.TDanchangCalcinfoMatchMapper;

public class TDanchangCalcinfoMatchService extends BaseService<TDanchangCalcinfoMatchMapper> {
	public TDanchangCalcinfoMatchService() {

	}

	public TDanchangCalcinfoMatchService(boolean autoCommit) {
		super.autoCommit = autoCommit;
	}
}
