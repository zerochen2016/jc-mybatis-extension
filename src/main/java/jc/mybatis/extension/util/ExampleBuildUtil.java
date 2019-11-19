package jc.mybatis.extension.util;

import java.lang.reflect.Field;


import jc.mybatis.extension.util.PageModel;


/**
 * 
 * @author JC
 * @Date 2019年11月18日
 * @since 1.0.0
 */
public class ExampleBuildUtil {

	
	/**
	 * 添加分页参数
	 * @param page
	 * @param pageSize
	 * @param example
	 * @return
	 */
	public static <T>T setPageParam(PageModel<?> pageModel,T example) {
		return setPageParam(pageModel.getPage(), pageModel.getPageSize(), example);
	}
	/**
	 * 添加分页参数
	 * @param page
	 * @param pageSize
	 * @param example
	 * @return
	 */
	public static <T>T setPageParam(Integer page, Integer pageSize,T example) {
		int limitStart = (page-1)*pageSize;
		try {
			ReflectionUtil.setFieldValue(example, "limitStart", limitStart);
			ReflectionUtil.setFieldValue(example, "limitLength", pageSize);
		}catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return example;
	}

	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @param template
	 * @param example
	 * @return
	 */
	public static <T>T buildExample(Class<?> clazz, T template, T example) {
		try {
			Object criateria = ReflectionUtil.invoke(example, "createCriteria", null, null);
			Field[] fields = template.getClass().getDeclaredFields();
			for(Field field:fields) {
				field.setAccessible(true);
				Object value = ReflectionUtil.getFieldValue(template, field.getName());
				if(value != null && value.toString().length() > 0) {
					String methodName = getMethodNameEqualTo(field.getName());
					ReflectionUtil.invoke(criateria, methodName, new Class<?>[] {field.getType()}, new Object[] {value});
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return example;
	}
	/**
	 * 根据字段名获取example的equalTo方法名
	 * @param fieldName
	 * @return
	 */
	private static String getMethodNameEqualTo(String fieldName) {
		fieldName = fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1,fieldName.length());
		String methodName = "and"+fieldName+"EqualTo";
		return methodName;
	}
}
