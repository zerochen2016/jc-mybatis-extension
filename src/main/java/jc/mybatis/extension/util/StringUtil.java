package jc.mybatis.extension.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Util Class of String
 * @author JC
 * @Date 2019年11月17日
 * @since 1.0.0
 */
public class StringUtil{

	public static boolean isEmpty(String str) {
		return (str == null || "".equals(str));
	}
	
	public static boolean anyEmpty(String ...args) {
		for(String arg : args) {
			if(isEmpty(arg)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * count occurance of Character
	 * @param string
	 * @param character
	 * @return times
	 */
	public static int countOccuranceOfChar(String string, Character character) {
		Map<Character,Integer> map = new HashMap<Character,Integer>();
		char[] arr = string.toCharArray();
	        
		for (char ch : arr) {
			if (map.containsKey(ch)) {
				Integer old = map.get(ch);
				map.put(ch, old + 1);
			} else {
				map.put(ch,1);
			}
		}
		return map.containsKey(character) ? map.get(character) : 0;
	}
	
	/**
	 * index of character
	 * @param string
	 * @param character
	 * @param occuranceTimes
	 * @return
	 */
	public static int indexOfChar(String string, Character character, int occuranceTimes) {
		if(occuranceTimes == 0) {
			return -1;
		}
		char[] arr = string.toCharArray();
	    int index = -1; 
	    int appearTimes = 0;
		for (int i = 0; i<arr.length; i++) {
			index++;
			char ch = arr[i];
			if(character.equals(ch)) {
				appearTimes++;
			}
			if(occuranceTimes == appearTimes) {
				return index;
			}
			
		}
		return index;
	}
	
	private static final String UNDERLINE = "_";
	
	public static String camelToUnderline(String name) {
		StringBuffer under = new StringBuffer(); 
		char[] array = name.toCharArray();
		for(char c: array) {
			if(Character.isUpperCase(c)) {
				under.append("_").append(Character.toLowerCase(c));
			}else {
				under.append(c);
			}
		}
		return under.toString();
	}
	
	public static String underlineToCamel(String name) {
		int index = name.indexOf(UNDERLINE);
		if(index < 0 || index >= name.length() - 1) {
			return name;
		}else {
			 return underlineToCamel(new StringBuffer(name.substring(0, index))
					 .append(name.substring(index + 1, index + 2).toUpperCase())
					 .append(name.substring(index + 2, name.length())).toString());
		}
	}
	
	public static String underlineToCamelFirstLetterUpper(String name) {
		return firstLetterUpper(underlineToCamel(name));
	}
	
	public static String firstLetterUpper(String name) {
		return name.substring(0, 1).toUpperCase() + name.subSequence(1, name.length());
	}

	public static String firstLetterLower(String name) {
		return name.substring(0, 1).toLowerCase() + name.subSequence(1, name.length());
	}
}
