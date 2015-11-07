/**
 * Copyright 2013-present beyond constraint.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beyondconstraint.sqlking.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author samkirton
 */
public class ReflectionUtils {
	private static final String METHOD_GET = "get";
	private static final String METHOD_SET = "set";
	
	/**
	 * Create a new object instance based on the class provided
	 * @param	c	Create a new object instance based on this class
	 * @return	A new instance of the class
	 */
	public static Object newInstance(Class<?> c) {
		Object object = null;
		
		try {
			object = c.newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		
		return object;
	}
	
	/**
	 * Loop through all declared methods and find the method that matches
	 * the methodName (if it exists)
	 * @param	c	The class to get the declared methods from
	 * @param	methodName	The methodName that the declared method should be returned for
	 * @return	The method declaration that matches the methodName
	 */
	public static Method getMethod(Class<?> c, String methodName) {
		Method[] declaredMethods = c.getDeclaredMethods();
		Method returnMethod = null;
		
		for (Method method : declaredMethods) {
			if (method.getName().equals(methodName)) {
				returnMethod = method;
				break;
			}
		}
		
		return returnMethod;
	}
	
	/**
	 * @para	c	The class to get the setter methods from
	 * @return	A HashMap of getter method name/method declaration pairs
	 */
	public static HashMap<String,Method> getGetterMethods(Class<?> c) {
		HashMap<String,Method> setterMethods = new HashMap<String,Method>();
		Method[] declaredMethods = c.getDeclaredMethods();
		
		for (int i = 0; i < declaredMethods.length; i++) {
			Method declaredMethod = declaredMethods[i];
			String methodName = declaredMethod.getName();
			
			if (methodName.startsWith(METHOD_GET)) {
				setterMethods.put(StringUtils.removePrefixFromMethod(methodName, METHOD_GET),declaredMethod);
			}
		}
		
		return setterMethods;
	}
	
	/**
	 * @para	c	The class to get the setter methods from
	 * @return	A HashMap of setter method name/method declaration pairs
	 */
	public static HashMap<String,Method> getSetterMethods(Class<?> c) {
		HashMap<String,Method> setterMethods = new HashMap<String,Method>();
		Method[] declaredMethods = c.getDeclaredMethods();
		
		for (int i = 0; i < declaredMethods.length; i++) {
			Method declaredMethod = declaredMethods[i];
			String methodName = declaredMethod.getName();
			
			if (methodName.startsWith(METHOD_SET)) {
				setterMethods.put(StringUtils.removePrefixFromMethod(methodName, METHOD_SET),declaredMethod);
			}
		}
		
		return setterMethods;
	}
	
	/**
	 * Get the class name of the provided object
	 * @param	object	The object to get the class name for
	 * @return	The class name of the provided object
	 */
	public static String getClassName(Object object) {
		String className = null;
		
		Class<?> enclosingClass = object.getClass().getEnclosingClass();
		if (enclosingClass != null) {
			className = enclosingClass.getSimpleName();
		} else {
			className = object.getClass().getSimpleName();
		}
		
		return className;
	}
	
	/**
	 * Retrieve a list of method names with their get and set prefix removed
	 * @param	methods	The methods to create the names for
	 * @return	A list of method names
	 */
	public static String[] getMethodNamesFromMethods(Method[] methods) {
		String[] methodNames = null;
		if (methods != null && methods.length > 0) {
			methodNames = new String[methods.length];
			for (int i = 0; i < methods.length; i++) {
				String methodName = StringUtils.removeGetOrSetFromMethodName(methods[i].getName());
				methodNames[i] = methodName;
			}
		}
		
		return methodNames;
	}
	
	/**
	 * invoke reflection method
	 * @param	object	The object to invoke the method on
	 * @param	method	The method to invoke
	 * @param	byteValue	The value to pass into the method
	 */
	public static void invokeMethod(Object object, Method method, byte[] byteValue) {
		try {
			method.invoke(object, byteValue);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}
	
	/**
	 * invoke reflection method
	 * @param	object	The object to invoke the method on
	 * @param	method	The method to invoke
	 * @param	floatValue	The value to pass into the method
	 */
	public static void invokeMethod(Object object, Method method, float floatValue) {
		try {
			method.invoke(object, floatValue);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}
	
	/**
	 * invoke reflection method
	 * @param	object	The object to invoke the method on
	 * @param	method	The method to invoke
	 * @param	longValue	The value to pass into the method
	 */
	public static void invokeMethod(Object object, Method method, long longValue) {
		try {
			method.invoke(object, longValue);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}
	
	/**
	 * invoke reflection method
	 * @param	object	The object to invoke the method on
	 * @param	method	The method to invoke
	 * @param	intValue	The value to pass into the method
	 */
	public static void invokeMethod(Object object, Method method, int intValue) {
		try {
			method.invoke(object, intValue);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}
	
	/**
	 * invoke reflection method
	 * @param <T>
	 * @param	object	The object to invoke the method on
	 * @param	method	The method to invoke
	 * @param	value	The value to pass into the method
	 */
	public static <T> void invokeMethod(Object object, Method method, T value) {
		try {
			method.invoke(object, value);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}
	
	/**
	 * invoke reflection method for a response
	 * @param	object	The object to invoke the method on
	 * @param	method	The method to invoke
	 * @return	The response of the method
	 */
	public static <T> Object invokeMethod(Object object, Method method) {
		Object responseObject = null;
		
		try {
			responseObject = method.invoke(object);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		
		return responseObject;
	}
}