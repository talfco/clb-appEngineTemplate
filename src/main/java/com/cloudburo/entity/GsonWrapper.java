package com.cloudburo.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class GsonWrapper {
	
	private static final Logger logger = Logger.getLogger(GsonWrapper.class.getCanonicalName());
	private GsonBuilder gsonBuilder;
	private Gson gson;
	
	private class DateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
		  @Override
		  public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context) {
		    return new JsonPrimitive(src.toString());
		  }
		  @Override
		  public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
		      throws JsonParseException {
		    return new DateTime(json.getAsString());
		  }
	}
	
	private class LocalDateTimeTypeConverter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
		  @Override
		  public JsonElement serialize(LocalDateTime src, Type srcType, JsonSerializationContext context) {
			DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
			return new JsonPrimitive(fmt.print(src));
		  }
		  @Override
		  public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
		      throws JsonParseException {
		    return new LocalDateTime(json.getAsString());
		  }
	}
	
	private class DateTypeConverter implements JsonSerializer<Date>, JsonDeserializer<Date> {
		  @Override
		  public JsonElement serialize(Date src, Type srcType, JsonSerializationContext context) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			DateTime dt = DateTime.parse(df.format(src));
		    //return new JsonPrimitive(src.toString());
			return new JsonPrimitive(dt.toString());
		  }
		  @Override
		  public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context)
		      throws JsonParseException {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			Date dt= null;
			try {
			 dt = df.parse(json.getAsString());

			} catch(ParseException ex) { throw new JsonParseException("util.Date conversion failed");}
			 return dt;
		  }
	}
    
    public GsonWrapper() {
    	gsonBuilder = new GsonBuilder();
    	gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());
    	gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeConverter());
    	gsonBuilder.registerTypeAdapter(Date.class, new DateTypeConverter());
    	gson = gsonBuilder.create();
    }
    
    public Gson getGson() {
    	return gson;
    }

}
