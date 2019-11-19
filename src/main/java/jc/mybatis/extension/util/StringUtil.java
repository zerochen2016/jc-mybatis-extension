package jc.mybatis.extension.util;


public class StringUtil {

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
