package com.bolool.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CombineUtil {

	private List combList = new ArrayList();

	/**
	 * 组合
	 * 
	 * @param array
	 * @param n
	 * @return
	 */
	public List<String> mn(String[] array, int n) {
		List alist = new ArrayList();
		alist = this.mn(array, n, alist);
		this.combList = alist;
		return alist;
	}

	public List<String[]> mn4Arr(String[] array, int m) {
		List<String[]> list = new ArrayList<String[]>();
		int n = array.length;
		if (n < m)
			throw new IllegalArgumentException("Error   n   <   m");
		BitSet bs = new BitSet(n);
		for (int i = 0; i < m; i++) {
			bs.set(i, true);
		}
		do {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < n; i++)
				if (bs.get(i)) {
					sb.append(array[i]).append(',');
				}
			if (sb.length() > 0) {// zw edit
				sb.setLength(sb.length() - 1);
			}
			list.add(sb.toString().split(","));
		} while (moveNext(bs, n));
		return list;
	}

	public List<double[]> mn4Arr(double[] array, int m) {
		List<double[]> list = new ArrayList<double[]>();
		int n = array.length;
		if (n < m)
			throw new IllegalArgumentException("Error   n   <   m");
		BitSet bs = new BitSet(n);
		for (int i = 0; i < m; i++) {
			bs.set(i, true);
		}
		do {
			double[] dd = new double[m];
			int idx = 0;
			for (int i = 0; i < n; i++)
				if (bs.get(i)) {
					dd[idx++] = array[i];
					// sb.append(array[i]).append(',');
				}
			list.add(dd);
			// if(sb.length()>0){//zw edit
			// sb.setLength(sb.length() - 1);
			// }
			// list.add(sb.toString().split(","));
		} while (moveNext(bs, n));
		return list;
	}
	

	public List<int[]> mn4Arr(int[] array, int m) {
		List<int[]> list = new ArrayList<int[]>();
		int n = array.length;
		if (n < m)
			throw new IllegalArgumentException("Error   n   <   m");
		BitSet bs = new BitSet(n);
		for (int i = 0; i < m; i++) {
			bs.set(i, true);
		}
		do {
			int[] dd = new int[m];
			int idx = 0;
			for (int i = 0; i < n; i++)
				if (bs.get(i)) {
					dd[idx++] = array[i];
					// sb.append(array[i]).append(',');
				}
			list.add(dd);
			// if(sb.length()>0){//zw edit
			// sb.setLength(sb.length() - 1);
			// }
			// list.add(sb.toString().split(","));
		} while (moveNext(bs, n));
		return list;
	}

	/**
	 * 从数组的n个元素中取m个元素的组合取法
	 * 
	 * @param array
	 * @param m
	 * @param list
	 * @return["1,2,3","2,3,4"]
	 */
	private List mn(String[] array, int m, List list) {
		int n = array.length;
		if (n < m)
			throw new IllegalArgumentException("Error   n   <   m");
		BitSet bs = new BitSet(n);
		for (int i = 0; i < m; i++) {
			bs.set(i, true);
		}
		do {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < n; i++)
				if (bs.get(i)) {
					sb.append(array[i]).append(',');
				}
			if (sb.length() > 0) {// zw edit
				sb.setLength(sb.length() - 1);
			}
			list.add(sb.toString());
		} while (moveNext(bs, n));
		return list;
	}

	/**
	 * 1、start 第一个true片段的起始位，end截止位 2、把第一个true片段都置false
	 * 3、数组从0下标起始到以第一个true片段元素数量减一为下标的位置都置true 4、把第一个true片段end截止位置true
	 * 
	 * @param bs
	 *            数组是否显示的标志位
	 * @param n
	 *            数组长度
	 * @return boolean 是否还有其他组合
	 */
	private boolean moveNext(BitSet bs, int n) {
		int start = -1;
		while (start < n)
			if (bs.get(++start))
				break;
		if (start >= n)
			return false;

		int end = start;
		while (end < n)
			if (!bs.get(++end))
				break;
		if (end >= n)
			return false;
		for (int i = start; i < end; i++)
			bs.set(i, false);
		for (int i = 0; i < end - start - 1; i++)
			bs.set(i);
		bs.set(end);
		return true;
	}

	/**
	 * 输出生成的组合结果
	 * 
	 * @param array
	 *            数组
	 * @param bs
	 *            数组元素是否显示的标志位集合
	 */
	private void printAll(String[] array, BitSet bs) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++)
			if (bs.get(i)) {
				sb.append(array[i]).append(',');
			}
		sb.setLength(sb.length() - 1);
		combList.add(sb.toString());
	}

	/**
	 * 获取组合后的数组
	 * **/
	public List getCombineData() {
		// //System.out.println(combList.size());
		return combList;
	}

	/**
	 * 从n个元素中取出m个元素的组和数
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public int getCombineCount(int m, int n) {
		if (m < 0 || n < 0 || n < m) {
			return 0;
		} // 当m小于0时返回0 by:liaoyuding
		if (m == 0 || n == 0)
			return 1;// 当m为0或者n 为 0 时,返回 1 by :zw
		int n1 = 1, n2 = 1;
		for (int i = n, j = 1; j <= m; n1 *= i--, n2 *= j++)
			;
		return n1 / n2;
	}

	/**
	 * 全排列
	 * @param orginal
	 * @return
	 */

	public static String[] permutation(String orginal) {
		ArrayList<String> list = new ArrayList<String>();
		if (orginal.length() == 1) {
			return new String[] { orginal };
		} else {
			for (int i = 0; i < orginal.length(); i++) {
				String s = orginal.charAt(i) + "";
				String result = "";
				String resultA = result + s;
				String leftS = orginal.substring(0, i) + orginal.substring(i + 1, orginal.length());
				for (String element : permutation(leftS)) {
					result = resultA + element;
					list.add(result);
				}
			}
			return (String[]) list.toArray(new String[list.size()]);
		}
	}

	/**
	 * 获取组合注数
	 * */

	public static void main(String[] args) throws Exception {
		CombineUtil comb = new CombineUtil();
		// System.out.println(comb.getCombineCount(0, 10));
		// BitSet bs = new BitSet(5);
		// bs.set(0,true);
		// bs.set(1,true);
		// while(comb.moveNext(bs,5));
		// //System.out.println(comb.getCombineCount(3, 10));
		// //System.out.println(comb.mn(new String[] { "10", "11",
		// "12","13","14" }, 2));
		String []source = "0124536789abcdefgfij".split("|");
//		String [] arr = permutation("0124536789abcdefgfij");
		List<String> list = comb.mn(source,9);
//		System.out.println(StringUtils.join(list,"\n"));
		System.out.println(list.size());
	}
}
