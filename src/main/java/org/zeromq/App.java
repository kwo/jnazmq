package org.zeromq;

public class App {

	public static void main(final String[] args) throws Exception {

		System.out.printf("ZeroMQ version: %s.%s.%s%n", Zmq.ZMQ_VERSION_MAJOR, Zmq.ZMQ_VERSION_MINOR,
				Zmq.ZMQ_VERSION_PATCH);

		final Package p = App.class.getPackage();
		final String progname = p.getSpecificationTitle();
		final String version = p.getImplementationVersion();

		System.out.printf("%s version: %s%n", progname, version);

	}
}
