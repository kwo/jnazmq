# jnazmq: Java JNA bindings for ZeroMQ
****
Java [JNA](http://jna.java.net/) bindings for [ZeroMQ](http://zeromq.org/).
Differs from the [jzmq](https://github.com/zeromq/jzmq) project which uses [JNI](http://en.wikipedia.org/wiki/Java_Native_Interface) bindings.

## Building and Installing

### Requirements
 - Java 1.6 JDK
 - Maven 2.0 + 

Build as follows:

                cd jnazmq
                mvn clean install

A JAR file will be generated in the jnazmq/target directory named jnazmq-VERSION.jar. The JAR is self-contained so it be moved and renamed if desired or left right where it is.


## Roadmap
Preliminary benchmarks indicated a 200x performance decrease over JNI. 
No further work is planned and the code is offered as a proof of concept. 
