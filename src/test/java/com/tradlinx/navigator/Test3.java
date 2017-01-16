package com.tradlinx.navigator;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Test3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("a",3);
        map.put("b",12);
        map.put("c",54);
        map.put("d",51);
        map.put("e",8);
         
        System.out.println("------------sort 전 -------------");
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String temp = (String) iterator.next();
            System.out.println(temp + " = " + map.get(temp));
        }
         
        Iterator it = sortByValue(map).iterator();
         
        System.out.println("------------sort 후 -------------");
        while(it.hasNext()) {
            String temp = (String) it.next();
            System.out.println(temp + " = " + map.get(temp));
        }
	}

	private static List sortByValue(final Map map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());
         
        Collections.sort(list,new Comparator() {
             
            public int compare(Object o1,Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                 
                return ((Comparable) v2).compareTo(v1);
            }
             
        });
        Collections.reverse(list); // 주석시 오름차순
        return list;
    }
}
