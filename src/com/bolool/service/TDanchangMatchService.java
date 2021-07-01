package com.bolool.service;

import com.bolool.mybatis.TDanchangMatchMapper;

public class TDanchangMatchService extends BaseService<TDanchangMatchMapper>  {

	public TDanchangMatchService(){
		
	}
	
	public TDanchangMatchService(boolean autoCommit){
		super.autoCommit = autoCommit;
	}
}
