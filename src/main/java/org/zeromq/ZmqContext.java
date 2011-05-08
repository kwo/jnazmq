package org.zeromq;

import com.sun.jna.Pointer;

public class ZmqContext {

	private static final ZmqLibrary zmqlib;

	static {
		zmqlib = Zmq.getLibrary();
	}

	private final Pointer handle;

	ZmqContext(final int ioThreads) {
		this.handle = zmqlib.zmq_init(ioThreads);
	}

	public ZmqSocket socket(final ZmqSocket.Type type) {
		return new ZmqSocket(this, type);
	}

	public void term() {
		check(zmqlib.zmq_term(this.handle));
	}

	Pointer getHandle() {
		return this.handle;
	}

	private void check(final int rc) {
		if (rc != 0) {
			final int err = zmqlib.zmq_errno();
			throw new ZmqException(zmqlib.zmq_strerror(err), err);
		}
	}

}