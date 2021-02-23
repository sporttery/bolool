package timer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.bolool.util.Const;
import com.bolool.util.DBHelper;
import com.bolool.util.HttpUtil;

public class NewMatchRunnable implements Runnable {
	private static final Log log = LogFactory.getLog(NewMatchRunnable.class);
	@Override
	public void run() {

		Date now = new Date();
		String today = new SimpleDateFormat(Const.YMD_HMS).format(now);
		String content = HttpUtil.getFromOkooo(
				"https://www.okooo.com/livecenter/jingcai/?date=" + today.split(" ")[0], null, null);
		if(content==null || content.length() < 100) {
			return;
		}
		content = HttpUtil.safeHtml(content);
		Document doc = Jsoup.parseBodyFragment(content);
		Elements matchs = doc.select(".each_match");
		log.info(today + " 共获取比赛数据：" + matchs.size()+" 场" );
		matchs.forEach(match -> {
			String id = match.attr("matchid");
			String state = match.attr("state");
			if ("End".equals(state)) {
				String halfscore = match.attr("data-hs");
				String fullscore = match.select(".show_score").text().replaceAll("[\\r\\n\\s]", "");
				String[] scores = fullscore.split("-");
				if (scores.length == 2) {
					int h = Integer.parseInt(scores[0]);
					int a = Integer.parseInt(scores[1]);
					int goalscore = 0;
					String result = "负";
					if (h > a) {
						goalscore = 3;
						result = "胜";
					} else if (h == a) {
						goalscore = 1;
						result = "平";
					}
					int c = DBHelper.insertOrUpdatePre(Const.UPDATE_MATCH_SCORE, fullscore, halfscore,
							result, goalscore, id);
					log.info(today + " 更新赛程比分：" + id + "," + fullscore + "(" + halfscore + ") 成功" + c);
				}
			}else{
				log.info(today + " 更新赛程比分：" + id + " 没有比分 ");
			}
		});
	

	}

}
