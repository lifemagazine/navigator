package com.lifemagazine.navigator.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lifemagazine.navigator.transport.ITransport;

public class Utility {

	public static void main2(String[] args) throws ParseException {

		System.out.println(convertHour2Day(17));
		System.out.println(convertHour2Day(27));
		System.out.println(convertHour2Day(49));

		//		int distance = (int)Math.round(getDistanceBetween(37.31, 126.56, 37.27, 126.37));
		//		System.out.println(distance);

		String fromDate = "2016-12-08";
		//		String[] temp = fromDate.split("-");
		Calendar cal = Calendar.getInstance();    

		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");

		cal.setTime(dateFormat.parse(fromDate));

		System.out.println(dateFormat.format(cal.getTime()));

		Calendar newCal = carculateDate(cal, 5);

		dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(dateFormat.format(newCal.getTime()));
	}
	
	public static void main(String[] args) throws ParseException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 4);
		map.put("b", 1);
		map.put("c", 3);
		map.put("d", 2);
		
		Map<String, Integer> sortedMap = sortByValue(map);
		Iterator<Map.Entry<String, Integer>> entries = sortedMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    Integer value = (Integer)entry.getValue();
		    System.out.println("Key = " + key + ", Value = " + value);
		}
	}

	public static double getDistanceBetween(double lat1, double lon1, double lat2, double lon2) {

		double EARTH_RADIUS = 6371;

		double dLat = toRadians(lat2 - lat1);
		double dLon = toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = EARTH_RADIUS * c;
		return Math.round(d);
	}

	private static double toRadians(double degrees) {
		return degrees * (Math.PI / 180);
	}

	public static String convertBitSet2Str(BitSet bitSet) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<7; i++) {
			if (bitSet.get(i))
				sb.append("1");
			else
				sb.append("0");

		}
		return sb.toString();
	}

	public static BitSet convertStr2BitSet(String str) {
		BitSet cycle = new BitSet(7);
		for (int i=0; i<7; i++) {
			if (str.substring(i, i+1).equals("1"))
				cycle.set(i, true);
			else
				cycle.set(i, false);
		}
		return cycle;
	}

	public static List<String> convertStr2List(String scheduleListStr) {
		List<String> list = new ArrayList<String>();
		String[] temp = scheduleListStr.split(";");
		for (int i=0; i<temp.length; i++)
			list.add(temp[i]);
		return list;
	}

	public static String doRoundN2Str(double d) {
		long i = (long)Math.round(d);
		return "" + i;
	}

	public static Calendar convert2Calendar(String fromDateStr) throws ParseException {
		Calendar cal = Calendar.getInstance();    
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		cal.setTime(dateFormat.parse(fromDateStr));
		return cal;
	}

	public static Calendar carculateDate(Calendar cal, int days) {
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal;
	}

	public static String convert2Str(Calendar cal) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(cal.getTime());
	}

	public static double convertHour2Day(double hour) {
		return Math.ceil(hour / 24);
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		return map.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(
						Map.Entry::getKey, 
						Map.Entry::getValue, 
						(e1, e2) -> e1, 
						LinkedHashMap::new
						));
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortReverseByValue(Map<K, V> map) {
		return map.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.collect(Collectors.toMap(
						Map.Entry::getKey, 
						Map.Entry::getValue, 
						(e1, e2) -> e1, 
						LinkedHashMap::new
						));
	}
}