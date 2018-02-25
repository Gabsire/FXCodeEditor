package main.com.editor.utils;

public class Utils {

	public static boolean areNotNull(Object...objects) {
		for(Object object : objects){
			if(null == object){
				return false;
			}
		}
		return true;
	}
}
