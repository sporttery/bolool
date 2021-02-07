var EuroOddsToAsia = {
	/**
	 * -0.25，-1.25，-2.25，-3.25盘口
	 * 
	 * @param money
	 *            计划总投注额
	 * @param pk
	 *            盘口
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	oddsToHan1 : function(money, pk, s, p, f, rs, rp, rf) {
		var otbMatchAsiaOdd = {};
		otbMatchAsiaOdd["pk"]=pk;
		otbMatchAsiaOdd["had"]={"h":s,"d":p,"a":f};
		otbMatchAsiaOdd["hhad"]={"h":rs,"d":rp,"a":rf};
		
		if (pk < 0) {
			if (pk + 0.25 == 0) {
				x, y, z, sp, xp;
				y = money / (2 * p);
				x = money - y;
				sp = (s * x - money) / money;
				otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
				console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔胜投入金额：" + Math.round(x, 4) + "，欧赔平投入金额：" + Math.round(y, 4));
				y = money * (f + 1) / (f + 2 * p);
				z = money - y;
				xp = f * z / money - 1;
				otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
				console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "。投注下盘时，欧赔平投入金额：" + Math.round(y, 4) + "，欧赔负投入金额：" + Math.round(z, 4));
			} else if (pk + 0.25 < 0) {
				x, y, z, sp, xp;
				y = money / (2 * rp);
				x = money - y;
				sp = (rs * x - money) / money;
				otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
				console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔让球胜投入金额：" + Math.round(x, 4) + "，欧赔让球平投入金额：" + Math.round(y, 4));
				y = money * (rf + 1) / (rf + 2 * rp);
				z = money - y;
				xp = rf * z / money - 1;
				otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
				console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "。投注下盘时，欧赔让球平投入金额：" + Math.round(y, 4) + "，欧赔让球负投入金额：" + Math.round(z, 4));
			}
		} else if (pk > 0) {
			if (pk - 0.25 == 0) {
				x, y, z, sp, xp;
				y = money / (2 * p);
				x = money - y;
				sp = (f * x - money) / money;
				otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
				console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔负投入金额：" + Math.round(x, 4) + "，欧赔平投入金额：" + Math.round(y, 4));
				y = money * (s + 1) / (s + 2 * p);
				z = money - y;
				xp = f * z / money - 1;
				otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
				console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "。投注下盘时，欧赔平投入金额：" + Math.round(y, 4) + "，欧赔胜投入金额：" + Math.round(z, 4));
			} else if (pk - 0.25 > 0) {
				x, y, z, sp, xp;
				y = money / (2 * rp);
				x = money - y;
				sp = (rf * x - money) / money;
				otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
				console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔让球负投入金额：" + Math.round(x, 4) + "，欧赔让球平投入金额：" + Math.round(y, 4));
				y = money * (rs + 1) / (rs + 2 * rp);
				z = money - y;
				xp = rs * z / money - 1;
				otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
				console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "。投注下盘时，欧赔让球平投入金额：" + Math.round(y, 4) + "，欧赔让球胜投入金额：" + Math.round(z, 4));
			}
		}
		return otbMatchAsiaOdd;
	},

	/**
	 * -0.75，-1.75，-2.75，-3.75盘口
	 * 
	 * @param money
	 *            计划总投注额
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 * @throws Exception
	 */
	oddsToHan2 : function(money, pk, rs, rp, rf) {
		var otbMatchAsiaOdd = {};
		otbMatchAsiaOdd["pk"]=pk;
		otbMatchAsiaOdd["hhad"]={"h":rs,"d":rp,"a":rf};
		
		if (pk < 0) {
			x, y, z, sp, xp;
			y = money * ((1 + rs) / (2 * rp + rs));
			x = money - y;
			sp = rs * x / money - 1;
			otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
			console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔让球胜投入金额：" + Math.round(x, 4) + "，欧赔让球平投入金额：" + Math.round(y, 4));
			xp = (1 - 1 / (2 * rp)) * rf - 1;
			z = money * (xp + 1) / rf;
			y = money - z;
			otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
			console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "。投注下盘时，欧赔让球平投入金额：" + Math.round(y, 4) + "，欧赔让球负投入金额：" + Math.round(z, 4));
		} else if (pk > 0) {
			x, y, z, sp, xp;
			y = money * ((1 + rf) / (2 * rp + rf));
			x = money - y;
			sp = rf * x / money - 1;
			otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
			console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔让球负投入金额：" + Math.round(x, 4) + "，欧赔让球平投入金额：" + Math.round(y, 4));
			xp = (1 - 1 / (2 * rp)) * rs - 1;
			z = money * (xp + 1) / rs;
			y = money - z;
			otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
			console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "。投注下盘时，欧赔让球平投入金额：" + Math.round(y, 4) + "，欧赔让球胜投入金额：" + Math.round(z, 4));
		}
		return otbMatchAsiaOdd;
	},
	/**
	 * 1.5,0.5,-0.5，-1.5，-2.5，-3.5盘口
	 * 
	 * @param money
	 *            计划总投注额
	 * @param pk
	 *            盘口
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	oddsToHan3 : function(money, pk, s, p, f, rs, rp, rf) {
		var x, y, z, sp, xp;
		var otbMatchAsiaOdd = {};
		otbMatchAsiaOdd["pk"]=pk;
		otbMatchAsiaOdd["had"]={"h":s,"d":p,"a":f};
		otbMatchAsiaOdd["hhad"]={"h":rs,"d":rp,"a":rf};
		if (pk < 0) {
			if (pk + 0.5 == 0) {
				x = money;
				sp = s - 1;
				otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
				console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔胜投入金额：" + Math.round(x, 4));
				z = money / (f / p + 1);
				y = money - z;
				xp = (p * y - money) / money;
				otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
				console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "。投注下盘时，欧赔让球平投入金额：" + Math.round(y, 4) + "，欧赔让球负投入金额：" + Math.round(z, 4));
			} else if (pk + 0.5 < 0) {
				x = money;
				sp = rs - 1;
				otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
				console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔让球胜投入金额：" + Math.round(x, 4));
				z = money / (rf / rp + 1);
				y = money - z;
				xp = (rp * y - money) / money;
				otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
				console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "，欧赔让球负投入金额：" + Math.round(z, 4));
			}
		} else {
			if (pk - 0.5 == 0) {
				x = money / (s / p + 1);
				y = money - x;
				sp = (p * y - money) / money;
				otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
				console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔胜投入金额：" + Math.round(x, 4) + "，欧赔平投入金额：" + Math.round(y, 4));
				z = money;
				xp = f - 1;
				otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
				console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "，欧赔负投入金额：" + Math.round(z, 4));
			} else if (pk - 0.5 > 0) {
				x = money / (rs / rp + 1);
				y = money - x;
				sp = (rp * y - money) / money;
				otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
				console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔让球胜投入金额：" + Math.round(x, 4) + "，欧赔让球平投入金额：" + Math.round(y, 4));
				z = money;
				xp = rf - 1;
				otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
				console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "，欧赔让球胜投入金额：" + Math.round(z, 4));
			}
		}
		return otbMatchAsiaOdd;
	},

	/**
	 * 2,1,0,-1,-2盘口
	 * 
	 * @param money
	 *            计划总投注额
	 * @param pk
	 *            盘口
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	oddsToHan4 : function(money, pk, s, p, f, rs, rp, rf) {
		var x, y, z, sp, xp;
		var otbMatchAsiaOdd = {};
		otbMatchAsiaOdd["pk"]=pk;
		otbMatchAsiaOdd["had"]={"h":s,"d":p,"a":f};
		otbMatchAsiaOdd["hhad"]={"h":rs,"d":rp,"a":rf};
		if (pk == 0) {
			x = money;
			y = money / p;
			sp = s - 1;
			console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "，欧赔胜投入金额：" + Math.round(x, 4) + "。投注走盘时，欧赔平投入金额：" + Math.round(y, 4));
			otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
			z = money;
			xp = f - 1;
			console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "，欧赔让球负投入金额：" + Math.round(z, 4) + "。投注走盘时，欧赔平投入金额：" + Math.round(y, 4));
			otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
		} else {
			x = money;
			y = money / rp;
			sp = rs - 1;
			otbMatchAsiaOdd["sp"]=Math.round(sp, 4);
			console.log(pk + "盘口上盘水位：" + Math.round(sp, 4) + "。投注上盘时，欧赔胜投入金额：" + Math.round(x, 4));
			z = money;
			xp = rf - 1;
			console.log(pk + "盘口下盘水位：" + Math.round(xp, 4) + "。投注走盘时，欧赔让球平投入金额：" + Math.round(y, 4) + "，欧赔让球负投入金额：" + Math.round(z, 4));
			otbMatchAsiaOdd["xp"]=Math.round(xp, 4);
		}
		return otbMatchAsiaOdd;
	},
	/**
	 * 2,1,0,-1,-2盘口
	 * 
	 * @param money
	 *            计划总投注额
	 * @param bonus
	 *            计划得到奖金
	 * @param pk
	 *            盘口
	 * @param sp
	 *            亚赔上盘水位
	 * @param xp
	 *            亚赔下盘水位
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	compareEuroAndAsiaForMoney4 : function(money, pk, sp, xp, s, p, f, rs, rp, rf) {
		var x, y, z, ybonuss, ybonusx;
		if (pk == 0) {
			// 上盘
			// 先计算固定投注的亚盘奖金
			ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
			// 在计算欧赔得到这么奖金需要投注多少金额
			x = ybonuss / s;
			y = money / p;
			/*
			 * //计算固定奖金情况下的亚盘投注额 sypt = bonus/sp; //计算此时欧赔需要的投注额 bx = bonus / s; by = sypt / p;
			 */
			console.log(pk + "上盘时,投注" + money + "亚盘得到奖金为" + ybonuss + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(y, 4));
			if (x + y > money) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
			console.log("=========================================================================================");
			// 下盘
			ybonusx = money * xp + money;
			z = ybonusx / f;
			console.log("下盘时,投注" + money + "亚盘得到奖金为" + ybonusx + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(y, 4));
			if (y + z > money) {// 表示欧赔所需本金大于亚赔
				console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
		} else {
			ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
			// 在计算欧赔得到这么奖金需要投注多少金额
			x = ybonuss / rs;
			y = money / rp;
			console.log(pk + "上盘时,投注" + money + "亚盘得到奖金为" + ybonuss + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(y, 4));
			if (x + y > money) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
			console.log("=========================================================================================");
			// 下盘
			ybonusx = money * xp + money;
			z = ybonusx / rf;
			console.log("下盘时,投注" + money + "亚盘得到奖金为" + ybonusx + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(y, 4));
			if (y + z > money) {// 表示欧赔所需本金大于亚赔
				console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
		}
	},

	/**
	 * 2,1,0,-1,-2盘口
	 * 
	 * @param bonus
	 *            计划得到奖金
	 * @param pk
	 *            盘口
	 * @param sp
	 *            亚赔上盘水位
	 * @param xp
	 *            亚赔下盘水位
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	compareEuroAndAsiaForBonus4 : function(bonus, pk, sp, xp, s, p, f, rs, rp, rf) {
		var x, sy, xy, z, sypt, xypt;
		if (pk == 0) {
			// 上盘
			// 计算固定奖金情况下的亚盘投注额
			sypt = bonus / sp;
			// 计算此时欧赔需要的投注额
			x = bonus / s;
			sy = sypt / p;
			console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(sy, 4));
			if (x + sy > sypt) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
			console.log("=========================================================================================");
			// 下盘
			xypt = bonus / xp;
			// 计算此时欧赔需要的投注额
			z = bonus / f;
			xy = xypt / p;
			console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(xy, 4));
			if (x + xy > xypt) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
		} else {
			// 上盘
			sypt = bonus / sp;
			x = bonus / rs;
			sy = sypt / rp;
			console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(sy, 4));
			if (x + sy > sypt) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
			console.log("=========================================================================================");
			// 下盘
			xypt = bonus / xp;
			// 计算此时欧赔需要的投注额
			z = bonus / rf;
			xy = xypt / rp;
			console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(xy, 4));
			if (x + xy > xypt) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
		}
	},
	/**
	 * 1.5,0.5,-0.5，-1.5，-2.5，-3.5盘口
	 * 
	 * @param money
	 *            计划总投注额
	 * @param bonus
	 *            计划得到奖金
	 * @param pk
	 *            盘口
	 * @param sp
	 *            亚赔上盘水位
	 * @param xp
	 *            亚赔下盘水位
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	compareEuroAndAsiaForMoney3 : function(money, pk, sp, xp, s, p, f, rs, rp, rf) {
		var x, y, z, ybonuss, ybonusx;
		if (pk < 0) {
			if (pk + 0.5 == 0) {
				// 上盘
				// 先计算固定投注的亚盘奖金
				ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
				// 在计算欧赔得到这么奖金需要投注多少金额
				x = ybonuss / s;
				console.log(pk + "上盘时,投注" + money + "亚盘得到奖金为" + ybonuss + ",欧赔胜需投注" + Math.round(x, 4));
				if (x > money) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				ybonusx = money * xp + money;
				z = ybonusx / f;
				y = ybonusx / p;
				console.log("下盘时,投注" + money + "亚盘得到奖金为" + ybonusx + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(y, 4));
				if (y + z > money) {// 表示欧赔所需本金大于亚赔
					console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			} else if (pk + 0.5 < 0) {
				// 上盘
				// 先计算固定投注的亚盘奖金
				ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
				// 在计算欧赔得到这么奖金需要投注多少金额
				x = ybonuss / rs;
				console.log(pk + "上盘时,投注" + money + "亚盘得到奖金为" + ybonuss + ",欧赔胜需投注" + Math.round(x, 4));
				if (x > money) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				ybonusx = money * xp + money;
				z = ybonusx / rf;
				y = ybonusx / rp;
				console.log("下盘时,投注" + money + "亚盘得到奖金为" + ybonusx + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(y, 4));
				if (y + z > money) {// 表示欧赔所需本金大于亚赔
					console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			}
		} else {
			if (pk - 0.5 == 0) {
				// 上盘
				// 先计算固定投注的亚盘奖金
				ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
				// 在计算欧赔得到这么奖金需要投注多少金额
				x = ybonuss / s;
				y = ybonuss / p;
				console.log(pk + "上盘时,投注" + money + "亚盘得到奖金为" + ybonuss + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(y, 4));
				if (x + y > money) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				ybonusx = money * xp + money;
				z = ybonusx / f;
				console.log("下盘时,投注" + money + "亚盘得到奖金为" + ybonusx + ",欧赔负需投注" + Math.round(z, 4));
				if (z > money) {// 表示欧赔所需本金大于亚赔
					console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			} else if (pk - 0.5 > 0) {
				// 上盘
				// 先计算固定投注的亚盘奖金
				ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
				// 在计算欧赔得到这么奖金需要投注多少金额
				x = ybonuss / rs;
				y = ybonuss / rp;
				console.log(pk + "上盘时,投注" + money + "亚盘得到奖金为" + ybonuss + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(y, 4));
				if (x + y > money) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				ybonusx = money * xp + money;
				z = ybonusx / rf;
				console.log("下盘时,投注" + money + "亚盘得到奖金为" + ybonusx + ",欧赔让负需投注" + Math.round(z, 4));
				if (z > money) {// 表示欧赔所需本金大于亚赔
					console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			}
		}
	},

	/**
	 * 1.5,0.5,-0.5，-1.5，-2.5，-3.5盘口
	 * 
	 * @param bonus
	 *            计划得到奖金
	 * @param pk
	 *            盘口
	 * @param sp
	 *            亚赔上盘水位
	 * @param xp
	 *            亚赔下盘水位
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	compareEuroAndAsiaForBonus3 : function(bonus, pk, sp, xp, s, p, f, rs, rp, rf) {
		var x, y, z, sypt, xypt;
		if (pk < 0) {
			if (pk + 0.5 == 0) {
				// 上盘
				// 计算固定奖金情况下的亚盘投注额
				sypt = bonus / sp;
				// 计算此时欧赔需要的投注额
				x = bonus / s;
				console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔胜需投注" + Math.round(x, 4));
				if (x > sypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				xypt = bonus / xp;
				// 计算此时欧赔需要的投注额
				z = bonus / f;
				y = bonus / p;
				console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(y, 4));
				if (z + y > xypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			} else if (pk + 0.5 < 0) {
				// 上盘
				// 计算固定奖金情况下的亚盘投注额
				sypt = bonus / sp;
				// 计算此时欧赔需要的投注额
				x = bonus / rs;
				console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔让胜需投注" + Math.round(x, 4));
				if (x > sypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				xypt = bonus / xp;
				// 计算此时欧赔需要的投注额
				z = bonus / rf;
				y = bonus / rp;
				console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(y, 4));
				if (z + y > xypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			}

		} else {
			if (pk - 0.5 == 0) {
				// 上盘
				// 计算固定奖金情况下的亚盘投注额
				sypt = bonus / sp;
				// 计算此时欧赔需要的投注额
				x = bonus / s;
				y = bonus / p;
				console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(y, 4));
				if (x + y > sypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				xypt = bonus / xp;
				// 计算此时欧赔需要的投注额
				z = bonus / f;
				console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔负需投注" + Math.round(z, 4));
				if (z > xypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			} else if (pk - 0.5 > 0) {
				// 上盘
				// 计算固定奖金情况下的亚盘投注额
				sypt = bonus / sp;
				// 计算此时欧赔需要的投注额
				x = bonus / rs;
				y = bonus / rp;
				console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(y, 4));
				if (x + y > sypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				xypt = bonus / xp;
				// 计算此时欧赔需要的投注额
				z = bonus / rf;
				console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔负需投注" + Math.round(z, 4));
				if (z > xypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			}
		}
	},

	/**
	 * -0.25，-1.25，-2.25，-3.25盘口
	 * 
	 * @param money
	 *            计划总投注额
	 * @param bonus
	 *            计划得到奖金
	 * @param pk
	 *            盘口
	 * @param sp
	 *            亚赔上盘水位
	 * @param xp
	 *            亚赔下盘水位
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	compareEuroAndAsiaForMoney1 : function(money, pk, sp, xp, s, p, f, rs, rp, rf) {
		var x, sy, xy, z, ybonuss, ybonusx, ybonussHalf, ybonusxHalf;
		if (pk < 0) {
			if (pk + 0.25 == 0) {
				// 上盘
				// 先计算固定投注的亚盘奖金
				ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
				// 输一半的情况下
				ybonussHalf = money / 2;
				// 在计算欧赔得到这么奖金需要投注多少金额
				x = ybonuss / s;
				sy = ybonussHalf / p;
				console.log(pk + "上盘时,投注" + money + "亚盘得到最大奖金为" + ybonuss + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(sy, 4));
				if (x + sy > money) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				ybonusx = money * xp + money;
				// 赢一半
				ybonusxHalf = money * xp / 2 + money;
				z = ybonusx / f;
				xy = ybonusxHalf / p;
				console.log("下盘时,投注" + money + "亚盘得到最大奖金为" + ybonusx + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(xy, 4));
				if (z + xy > money) {// 表示欧赔所需本金大于亚赔
					console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			} else if (pk + 0.25 < 0) {
				// 上盘
				// 先计算固定投注的亚盘奖金
				ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
				// 输一半的情况下
				ybonussHalf = money / 2;
				// 在计算欧赔得到这么奖金需要投注多少金额
				x = ybonuss / rs;
				sy = ybonussHalf / rp;
				console.log(pk + "上盘时,投注" + money + "亚盘得到最大奖金为" + ybonuss + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(sy, 4));
				if (x + sy > money) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				ybonusx = money * xp + money;
				// 赢一半
				ybonusxHalf = money * xp / 2 + money;
				z = ybonusx / rf;
				xy = ybonusxHalf / rp;
				console.log("下盘时,投注" + money + "亚盘得到最大奖金为" + ybonusx + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(xy, 4));
				if (z + xy > money) {// 表示欧赔所需本金大于亚赔
					console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			}
		} else {
			if (pk - 0.25 == 0) {
				// 上盘
				// 先计算固定投注的亚盘奖金
				ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
				// 赢一半的情况下
				ybonussHalf = money * sp / 2 + money;
				// 在计算欧赔得到这么奖金需要投注多少金额
				x = ybonuss / s;
				sy = ybonussHalf / p;
				console.log(pk + "上盘时,投注" + money + "亚盘得到最大奖金为" + ybonuss + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(sy, 4));
				if (x + sy > money) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				ybonusx = money * xp + money;
				ybonusxHalf = money / 2;
				z = ybonusx / f;
				xy = ybonusxHalf / p;
				console.log("下盘时,投注" + money + "亚盘得到最大奖金为" + ybonusx + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(xy, 4));
				if (z + xy > money) {// 表示欧赔所需本金大于亚赔
					console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			} else if (pk - 0.5 > 0) {
				// 上盘
				// 先计算固定投注的亚盘奖金
				ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
				// 赢一半的情况下
				ybonussHalf = money * sp / 2 + money;
				// 在计算欧赔得到这么奖金需要投注多少金额
				x = ybonuss / rs;
				sy = ybonussHalf / rp;
				console.log(pk + "上盘时,投注" + money + "亚盘得到最大奖金为" + ybonuss + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(sy, 4));
				if (x + sy > money) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				ybonusx = money * xp + money;
				ybonusxHalf = money / 2;
				z = ybonusx / rf;
				xy = ybonusxHalf / rp;
				console.log("下盘时,投注" + money + "亚盘得到最大奖金为" + ybonusx + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(xy, 4));
				if (z + xy > money) {// 表示欧赔所需本金大于亚赔
					console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			}
		}
	},

	/**
	 * -0.25，-1.25，-2.25，-3.25盘口
	 * 
	 * @param bonus
	 *            计划得到奖金
	 * @param pk
	 *            盘口
	 * @param sp
	 *            亚赔上盘水位
	 * @param xp
	 *            亚赔下盘水位
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	compareEuroAndAsiaForBonus1 : function(bonus, pk, sp, xp, s, p, f, rs, rp, rf) {
		var x, sy, xy, z, sypt, xypt, bonussHalf, bonusxHalf;
		if (pk < 0) {
			if (pk + 0.25 == 0) {
				// 上盘
				// 计算固定奖金情况下的亚盘投注额
				sypt = bonus / sp;
				bonussHalf = sypt / 2;
				// 计算此时欧赔需要的投注额
				x = bonus / s;
				sy = bonussHalf / p;
				console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(sy, 4));
				if (x + sy > sypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				xypt = bonus / xp;
				bonusxHalf = xypt * xp / 2 + xypt;
				// 计算此时欧赔需要的投注额
				z = bonus / f;
				xy = bonusxHalf / p;
				console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(xy, 4));
				if (z + xy > xypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			} else if (pk + 0.25 < 0) {
				// 上盘
				// 计算固定奖金情况下的亚盘投注额
				sypt = bonus / sp;
				bonussHalf = sypt / 2;
				// 计算此时欧赔需要的投注额
				x = bonus / rs;
				sy = bonussHalf / rp;
				console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(sy, 4));
				if (x + sy > sypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				xypt = bonus / xp;
				bonusxHalf = xypt * xp / 2 + xypt;
				// 计算此时欧赔需要的投注额
				z = bonus / rf;
				xy = bonusxHalf / rp;
				console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(xy, 4));
				if (z + xy > xypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			}

		} else {
			if (pk - 0.5 == 0) {
				// 上盘
				// 计算固定奖金情况下的亚盘投注额
				sypt = bonus / sp;
				bonussHalf = sypt * sp / 2 + sypt;
				// 计算此时欧赔需要的投注额
				x = bonus / s;
				sy = bonussHalf / p;
				console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔胜需投注" + Math.round(x, 4) + ",欧赔平需投注" + Math.round(sy, 4));
				if (x + sy > sypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				xypt = bonus / xp;
				bonusxHalf = xypt / 2;
				// 计算此时欧赔需要的投注额
				z = bonus / f;
				xy = bonusxHalf / p;
				console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔负需投注" + Math.round(z, 4) + ",欧赔平需投注" + Math.round(xy, 4));
				if (z + xy > xypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			} else if (pk - 0.5 > 0) {
				// 上盘
				// 计算固定奖金情况下的亚盘投注额
				sypt = bonus / sp;
				bonussHalf = sypt * sp / 2 + sypt;
				// 计算此时欧赔需要的投注额
				x = bonus / rs;
				sy = bonussHalf / rp;
				console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(sy, 4));
				if (x + sy > sypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
				console.log("=========================================================================================");
				// 下盘
				xypt = bonus / xp;
				bonusxHalf = xypt / 2;
				// 计算此时欧赔需要的投注额
				z = bonus / rf;
				xy = bonusxHalf / rp;
				console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(xy, 4));
				if (z + xy > xypt) {// 表示欧赔所需本金大于亚赔
					console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
				} else {
					console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
				}
			}
		}
	},

	/**
	 * -0.75，-1.75，-2.75，-3.75盘口
	 * 
	 * @param money
	 *            计划总投注额
	 * @param bonus
	 *            计划得到奖金
	 * @param pk
	 *            盘口
	 * @param sp
	 *            亚赔上盘水位
	 * @param xp
	 *            亚赔下盘水位
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	compareEuroAndAsiaForMoney2 : function(money, pk, sp, xp, rs, rp, rf) {
		var x, sy, xy, z, ybonuss, ybonusx, ybonussHalf, ybonusxHalf;
		if (pk < 0) {
			// 上盘
			// 先计算固定投注的亚盘奖金
			ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
			// 赢一半的情况下
			ybonussHalf = money * sp / 2 + money;
			// 在计算欧赔得到这么奖金需要投注多少金额
			x = ybonuss / rs;
			sy = ybonussHalf / rp;
			console.log(pk + "上盘时,投注" + money + "亚盘得到最大奖金为" + ybonuss + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(sy, 4));
			if (x + sy > money) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
			console.log("=========================================================================================");
			// 下盘
			ybonusx = money * xp + money;
			ybonusxHalf = money / 2;
			z = ybonusx / rf;
			xy = ybonusxHalf / rp;
			console.log("下盘时,投注" + money + "亚盘得到最大奖金为" + ybonusx + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(xy, 4));
			if (z + xy > money) {// 表示欧赔所需本金大于亚赔
				console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
		} else {
			// 上盘
			// 先计算固定投注的亚盘奖金
			ybonuss = money * sp + money;// 投中上盘所得奖金,走盘得到本金moneny为奖金
			// 赢一半的情况下
			ybonussHalf = money / 2;
			// 在计算欧赔得到这么奖金需要投注多少金额
			x = ybonuss / rs;
			sy = ybonussHalf / rp;
			console.log(pk + "上盘时,投注" + money + "亚盘得到最大奖金为" + ybonuss + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(sy, 4));
			if (x + sy > money) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
			console.log("=========================================================================================");
			// 下盘
			ybonusx = money * xp + money;
			ybonusxHalf = money * xp / 2 + money;
			z = ybonusx / rf;
			xy = ybonusxHalf / rp;
			console.log("下盘时,投注" + money + "亚盘得到最大奖金为" + ybonusx + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(xy, 4));
			if (z + xy > money) {// 表示欧赔所需本金大于亚赔
				console.log("下盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("下盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
		}
	},

	/**
	 * -0.75，-1.75，-2.75，-3.75盘口
	 * 
	 * @param bonus
	 *            计划得到奖金
	 * @param pk
	 *            盘口
	 * @param sp
	 *            亚赔上盘水位
	 * @param xp
	 *            亚赔下盘水位
	 * @param s
	 *            欧赔胜赔率
	 * @param p
	 *            欧赔平赔率
	 * @param f
	 *            欧赔负赔率
	 * @param rs
	 *            欧赔让球胜赔率
	 * @param rp
	 *            欧赔让球平赔率
	 * @param rf
	 *            欧赔让球负赔率
	 */
	compareEuroAndAsiaForBonus2 : function(bonus, pk, sp, xp, rs, rp, rf) {
		var x, sy, xy, z, sypt, xypt, bonussHalf, bonusxHalf;
		if (pk < 0) {
			// 上盘
			// 计算固定奖金情况下的亚盘投注额
			sypt = bonus / sp;
			bonussHalf = sypt * sp / 2 + sypt;
			// 计算此时欧赔需要的投注额
			x = bonus / rs;
			sy = bonussHalf / rp;
			console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(sy, 4));
			if (x + sy > sypt) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
			console.log("=========================================================================================");
			// 下盘
			xypt = bonus / xp;
			bonusxHalf = xypt / 2;
			// 计算此时欧赔需要的投注额
			z = bonus / rf;
			xy = bonusxHalf / rp;
			console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(xy, 4));
			if (z + xy > xypt) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}

		} else {
			// 上盘
			// 计算固定奖金情况下的亚盘投注额
			sypt = bonus / sp;
			bonussHalf = sypt / 2;
			// 计算此时欧赔需要的投注额
			x = bonus / rs;
			sy = bonussHalf / rp;
			console.log(pk + "上盘时,固定奖金为" + bonus + ",欧赔让胜需投注" + Math.round(x, 4) + ",欧赔让平需投注" + Math.round(sy, 4));
			if (x + sy > sypt) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
			console.log("=========================================================================================");
			// 下盘
			xypt = bonus / xp;
			bonusxHalf = xypt * xp / 2 + xypt;
			// 计算此时欧赔需要的投注额
			z = bonus / rf;
			xy = bonusxHalf / rp;
			console.log(pk + "下盘时,固定奖金为" + bonus + ",欧赔让负需投注" + Math.round(z, 4) + ",欧赔让平需投注" + Math.round(xy, 4));
			if (z + xy > xypt) {// 表示欧赔所需本金大于亚赔
				console.log("上盘时,亚赔本金低于欧赔,建议投注亚赔");
			} else {
				console.log("上盘时,亚赔本金高于欧赔,建议投注欧赔");
			}
		}
	},
	/**
	 * 欧赔转换亚盘
	 * @param list
	 * @return
	 * @throws Exception
	 */
	getAsiaOdds : function(list){
		var asiaOddsList=[];
		
		if(list!=null&&list.length>0){
			for(var i  in list){
				var otbMatchEuropeOdd = list[i];
				var otbMatchAsiaOdd1=null;//整数盘口
				var otbMatchAsiaOdd2=null;//0.25盘口 
				var otbMatchAsiaOdd3=null;//0.5盘口
				var otbMatchAsiaOdd4=null;//0.75盘口
				var hodds=parseFloat(otbMatchEuropeOdd["had"]["h"]);
				var dodds=parseFloat(otbMatchEuropeOdd["had"]["d"]);
				var aodds=parseFloat(otbMatchEuropeOdd["had"]["a"]);
				var rqHodds=parseFloat(otbMatchEuropeOdd["hhad"]["h"]);
				var rqDodds=parseFloat(otbMatchEuropeOdd["hhad"]["d"]);
				var rqAodds=parseFloat(otbMatchEuropeOdd["hhad"]["a"]);
				var letPoint=parseInt(otbMatchEuropeOdd["goalline"]);
				
				switch (letPoint) {
				case -1:
					otbMatchAsiaOdd1=oddsToHan4(1000, 0, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd2=oddsToHan1(1000, -0.25, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd3=oddsToHan3(1000, -0.5, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd4=oddsToHan2(1000, -0.75,rqHodds, rqDodds,rqAodds);
					break;
				case 1:
					otbMatchAsiaOdd1=oddsToHan4(1000, 0, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd2=oddsToHan1(1000, 0.25, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd3=oddsToHan3(1000, 0.5, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd4=oddsToHan2(1000, 0.75,rqHodds, rqDodds,rqAodds);
					break;
				case -2:
					otbMatchAsiaOdd1=oddsToHan4(1000, -1, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd2=oddsToHan1(1000, -1.25, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd3=oddsToHan3(1000, -1.5, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd4=oddsToHan2(1000, -1.75,rqHodds, rqDodds,rqAodds);
					break;
				case 2:
					otbMatchAsiaOdd1=oddsToHan4(1000, 1, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd2=oddsToHan1(1000, 1.25, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd3=oddsToHan3(1000, 1.5, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd4=oddsToHan2(1000, 1.75,rqHodds, rqDodds,rqAodds);
					break;
				case -3:
					otbMatchAsiaOdd1=oddsToHan4(1000, -2, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd2=oddsToHan1(1000, -2.25, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd3=oddsToHan3(1000, -2.5, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd4=oddsToHan2(1000, -2.75,rqHodds, rqDodds,rqAodds);
					break;
				case 3:
					otbMatchAsiaOdd1=oddsToHan4(1000, 2, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd2=oddsToHan1(1000, 2.25, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd3=oddsToHan3(1000, 2.5, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd4=oddsToHan2(1000, 2.75,rqHodds, rqDodds,rqAodds);
					break;
				case -4:
					otbMatchAsiaOdd1=oddsToHan4(1000, -3, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd2=oddsToHan1(1000, -3.25, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd3=oddsToHan3(1000, -3.5, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd4=oddsToHan2(1000, -3.75,rqHodds, rqDodds,rqAodds);
					break;
				case 4:
					otbMatchAsiaOdd1=oddsToHan4(1000, 3, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd2=oddsToHan1(1000, 3.25, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd3=oddsToHan3(1000, 3.5, hodds, dodds,aodds, rqHodds, rqDodds,rqAodds);
					otbMatchAsiaOdd4=oddsToHan2(1000, 3.75,rqHodds, rqDodds,rqAodds);
					break;
				}

				//otbMatchAsiaOdd1盘口
				if(otbMatchAsiaOdd1!=null){
					otbMatchAsiaOdd1["num"]=otbMatchEuropeOdd["num"];//赛事编号
					otbMatchAsiaOdd1["id"]=otbMatchEuropeOdd["id"];//赛事id
					asiaOddsList.push(otbMatchAsiaOdd1);	
				}
				
				
				//otbMatchAsiaOdd2盘口
				if(otbMatchAsiaOdd2!=null){
					otbMatchAsiaOdd2["num"]=otbMatchEuropeOdd["num"];//赛事编号
					otbMatchAsiaOdd2["id"]=otbMatchEuropeOdd["id"];//赛事id
					asiaOddsList.push(otbMatchAsiaOdd2);	
				}
				
				//otbMatchAsiaOdd3盘口
				if(otbMatchAsiaOdd3!=null){
					otbMatchAsiaOdd3["num"]=otbMatchEuropeOdd["num"];//赛事编号
					otbMatchAsiaOdd3["id"]=otbMatchEuropeOdd["id"];//赛事id
					asiaOddsList.push(otbMatchAsiaOdd3);	
				}
				
				//otbMatchAsiaOdd4盘口
				if(otbMatchAsiaOdd4!=null){
					otbMatchAsiaOdd4["num"]=otbMatchEuropeOdd["num"];//赛事编号
					otbMatchAsiaOdd4["id"]=otbMatchEuropeOdd["id"];//赛事id
					asiaOddsList.push(otbMatchAsiaOdd4);	
				}
			}
		}
		return asiaOddsList;
	}
};