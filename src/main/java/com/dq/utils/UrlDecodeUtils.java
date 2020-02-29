package com.dq.utils;

/**
 * For special notation Solving 
 * @author DaiQing
 *
 */
public class UrlDecodeUtils {
	
	public static String decodeData(String rawdata){
		String retString = rawdata.replaceAll("%2B", " ");
		retString = retString.replaceAll("%2F", "/");
		return retString;
	}
	
}
