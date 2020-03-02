package com.ajaxstartstopjersey.AjaxStartStopJersey;

import com.google.gson.*;

public class JsonUtility{
	static Gson gson;
	static { 
		gson=new GsonBuilder().setPrettyPrinting().create();
	}
	public static String convertToJSON(Readings object) {
		String jsonResult=null;
		try {
			jsonResult=gson.toJson(object);
		}
		catch(Exception e) {
			System.out.println("Message");
		}
		return jsonResult;
	}
	public static Readings convertToJavaObject(String jsonResult) {
		gson = new Gson();
		Readings readings= gson.fromJson(jsonResult, Readings.class);
		return(readings);
	}
}
