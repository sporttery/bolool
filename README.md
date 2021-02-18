# bolool 菠萝指数第2代
## 数据库设计： 
### t_bolool 指数表，计算菠萝指数，参考
. 1
### t_match 比赛表，从okooo 上获取，使用id 自增长的方式，获取每一场比赛信息, 数据大约从2014年7月份开始
### t_match_odds 赔率表 根据比赛id 从okooo 上获取bet365的赔率， companyId = 27 即为bet365的，支持其它公司的赔率 ，暂时没有入库
### t_match_history 主客队近99场历史数据，t_bolool 指数从历史数据计算得来 
. 2
