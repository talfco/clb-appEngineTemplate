##App Engine Java Application Template for a REST Backend

This is a skeleton application for a Google App Engine Java REST backend.

It provides sample code for a standardized REST API which is used within Cloudburo. 

It will integrate essential libraries and frameworks which are simplifying the building as well as testing of the Cloudburo REST API layer.

The API specification can be found here

[http://cloudburo.github.com/docs/opensource/jsonapispec/](http://cloudburo.github.com/docs/opensource/jsonapispec)

The sample will implement a REST API for a _Customer_ domain object, which is based upon the following frameworks

* __com.google.appengine__: The Google App Engine Java Framework
* __com.google.objectify__: Objectify is a Java data access API specifically designed for the Google App Engine datastore.
* __com.google.gson__: Gson is a Java library that can be used to convert Java Objects into their JSON representation
* __org.joda-time__: Joda-Time provides a quality replacement for the Java date and time classes.

For testing purposes the following frameworks are intgerated.

* __junit__: Is a simple framework to write repeatable tests
* __org.mockito__: Mockito is a mocking framework that tastes really good. It lets you write beautiful tests with clean & simple API.
* __com.cloudburo.clb-test__: Are our testing libraries which mainly are managing test data loads from csv files.

### Release Information

The Skeleton is actually work in progress and used as base for testing out quickly REST interface extensions. The template is used as base for our Acceleo based code generators, which will automate the REST backend implementation.

### References to other Cloudburo Open Source Work

* The template is making use of the Cloudburo testing utilities [https://github.com/talfco/clb-test](https://github.com/talfco/clb-test).

### Base of Skeleton

The Cloudburo skeleton is based on the Skeleton Application of Google, which is released under Apache License Version 2:

-------------------------------------------------

App Engine Java Application
Copyright (C) 2010-2012 Google Inc.

Skeleton application for use with App Engine Java.

Requires [Apache Maven](http://maven.apache.org) 3.0 or greater, and JDK 6+ in order to run.

To build, run

    mvn package

Building will run the tests, but to explicitly run tests you can use the test target

    mvn test

To start the app, use the [App Engine Maven Plugin](http://code.google.com/p/appengine-maven-plugin/) that is already included in this demo.  Just run the command.

    mvn appengine:devserver

For further information, consult the [Java App Engine](https://developers.google.com/appengine/docs/java/overview) documentation.

To see all the available goals for the App Engine plugin, run

    mvn help:describe -Dplugin=appengine
