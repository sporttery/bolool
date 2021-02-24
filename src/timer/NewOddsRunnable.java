package timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bolool.util.Const;
import com.bolool.util.DBHelper;
import com.bolool.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NewOddsRunnable implements Runnable {
	private static final Log log = LogFactory.getLog(NewOddsRunnable.class);
	private String sql = "select matchId from t_match_odds where (s = 0 and p = 0 and f = 0) and (h = 0 and pan = '0' and a = 0 ) limit 100";

	@Override
	public void run() {
		List<String> list = DBHelper.selectListSql(sql);
		if (list != null && list.size() > 0) {
			StringBuilder ids = new StringBuilder("");
			list.forEach(mId -> {
				ids.append(",").append(mId);
			});
			HashMap<String,String[]> oddsResult = new HashMap<String,String[]>();
			String url = "/ajax/?method=data.match.odds";
			String args = "bettingTypeId=1&providerId=27&matchIds=" + ids.substring(1);
			String odds = HttpUtil.getFromOkooo(url, "POST", args);
			if (odds != null) {
				java.lang.reflect.Type type = new TypeToken<Map<String, String[]>>() {
				}.getType();
				Map<String, String[]> map = new Gson().fromJson(odds, type);
				map.forEach((key, value) -> {
					
					oddsResult.put(key, new String[] {value[0],value[1],value[2],"0","0","0"});
//					int result = DBHelper.insertOrUpdatePre(Const.MATCH_ODDS_EUROPE_UPDATE, Float.valueOf(value[0]),
//							Float.valueOf(value[1]), Float.valueOf(value[2]), Integer.valueOf(key));
//					log.info("欧盘  matchId:" + key + " 入库 " + result);
				});
			}

			args = "bettingTypeId=2&providerId=27&matchIds=" + ids.substring(1);
			odds = HttpUtil.getFromOkooo(url, "POST", args);
			if (odds != null) {
				java.lang.reflect.Type type = new TypeToken<Map<String, String[]>>() {
				}.getType();
				Map<String, String[]> map = new Gson().fromJson(odds, type);
				map.forEach((key, value) -> {
//					int result = DBHelper.insertOrUpdatePre(Const.MATCH_ODDS_ASIA_UPDATE, Float.valueOf(value[0]),
//							value[1], Float.valueOf(value[2]), Integer.valueOf(key));
//					log.info("亚盘  matchId:" + key + " 入库 " + result);
					String arr[] = oddsResult.get(key);
					if(arr==null) {
						arr = new String[] {"0","0","0",value[0],value[1],value[2]};
						oddsResult.put(key, arr);
					}else {
						arr[3]=value[0];
						arr[4]=value[1];
						arr[5]=value[2];
					}
				});
			}
			
			if(oddsResult.size()>0) {
				oddsResult.forEach((key,value)->{
					int result = DBHelper.insertOrUpdatePre(Const.MATCH_ODDS_UPDATE, Float.valueOf(value[0]),
							Float.valueOf(value[1]), Float.valueOf(value[2]),Float.valueOf(value[3]), value[4], Float.valueOf(value[5]), Integer.valueOf(key));
					log.info(" matchId:" + key + " 入库 " + result);
				});
			}
		}
	}

}
