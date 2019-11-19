package jc.mybatis.extension.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * util of reflection
 * @author JC
 * @Date 2019年11月17日
 * @since 1.0.0
 */
public class ReflectionUtil {

	/**
	 * 反射取得字段的值
	 * @param target 要操作的对象
	 * @param fieldName 字段名
	 * @return
	 */
	public static Object getFieldValue(Object target,String fieldName) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(target);
		} catch (Exception e) {
			
		}
		return null;
	}
	
	/**
	 * 反射设置字段值
	 * @param target
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static void setFieldValue(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(target, value);
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 反射执行方法
	 * @param object 操作的对象
	 * @param methodName 方法名
	 * @param parameterTypes 反射获取对象时的方法参数
	 * @param parameters 反射执行方法时的参数
	 * @return
	 * @throws InvocationTargetException
	 */
    public static Object invoke(Object object, String methodName, Class<?> [] parameterTypes,  
            Object [] parameters) throws InvocationTargetException{  
          
        Method method = getDeclaredMethod(object, methodName, parameterTypes);  
          
        if(method == null){  
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");  
        }  
          
        method.setAccessible(true);  
          
        try {  
            return method.invoke(object, parameters);  
        } catch(IllegalAccessException e) {  
  
        }   
          
        return null;  
    }  
    /**
     * 反射获得方法
     * @param object 操作的对象
     * @param methodname 获取的方法名
     * @param parameterTypes 参数
     * @return
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes){
    	//如果子类中没有,就去他的父类里面找,直到知道最高级的父类Object
    	for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
    		try {
    			//每找一次就判断一次.如果有,循环结束,返回方法,如果没有,看下面
    			return superClass.getDeclaredMethod(methodName, parameterTypes); 
    		} catch (NoSuchMethodException e) {	
    			//这里会捉到异常,但是没有做处理,继续循环,找父类的父类,嗯如果有的话
    		}
    	}
    	return null;
    }
}
