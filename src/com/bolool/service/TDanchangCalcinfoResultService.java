package com.bolool.service;

import com.bolool.mybatis.TDanchangCalcinfoResultMapper;

public class TDanchangCalcinfoResultService extends BaseService<TDanchangCalcinfoResultMapper> {
	public TDanchangCalcinfoResultService() {

	}

	public TDanchangCalcinfoResultService(boolean autoCommit) {
		super.autoCommit = autoCommit;
	}
}
