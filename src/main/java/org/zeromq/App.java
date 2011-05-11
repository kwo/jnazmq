package org.zeromq;

public class App {

	public static void main(final String[] args) throws Exception {

		final Package p = App.class.getPackage();
		final String appname = p.getSpecificationTitle();
		final String versionMaven = p.getSpecificationVersion();
		final String[] version = p.getImplementationVersion().split(" ", 2);

		System.out.printf("%s version:      %s.%s.%s%n", "ZeroMQ", Zmq.ZMQ_VERSION_MAJOR, Zmq.ZMQ_VERSION_MINOR,
				Zmq.ZMQ_VERSION_PATCH);

		System.out.printf("%s version:      %s%n", appname, versionMaven);
		System.out.printf("%s build time:   %s%n", appname, version[1]);
		System.out.printf("%s build commit: %s%n", appname, version[0]);

	}
}
