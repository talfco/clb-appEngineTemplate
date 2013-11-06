package com.cloudburo.entity;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.LocalDateTranslatorFactory;
import com.googlecode.objectify.impl.translate.opt.joda.LocalDateTimeTranslatorFactory;


public class OfyService {
	static {
		// Must be entered before any entity is registered 
		ofy().factory().getTranslators().add(new LocalDateTranslatorFactory());
		ofy().factory().getTranslators().add(new LocalDateTimeTranslatorFactory());
        ofy().factory().register(Customer.class);
    }	

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
	
}
