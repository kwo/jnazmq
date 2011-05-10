package org.zeromq;

import com.sun.jna.Native;

public class Zmq {

	public static final int VERSION;

	private static final Object lock = new Object();
	private static final ZmqLibrary zmqlib;

	static {
		synchronized (lock) {
			zmqlib = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);
			VERSION = make_version(version());
		}
	}

	static {

	}

	public static ZmqContext getContext(final int ioThreads) {
		return new ZmqContext(ioThreads);
	}

	public static int make_version(final int major, final int minor, final int patch) {
		return (major * 10000) + (minor * 100) + patch;
	}

	public static int make_version(final int[] version) {
		return make_version(version[0], version[1], version[2]);
	}

	public static int[] version() {
		final int[] major = new int[1];
		final int[] minor = new int[1];
		final int[] patch = new int[1];
		ZmqLibrary.zmq_version(major, minor, patch);
		return new int[] { major[0], minor[0], patch[0] };
	}

	static ZmqLibrary getLibrary() {
		synchronized (lock) {
			return zmqlib;
		}
	}

	private Zmq() {
	}

}
