package org.zeromq;

import com.sun.jna.Pointer;

public class ZmqContext {

	private static final ZmqLibrary zmq;

	static {
		zmq = Zmq.getLibrary();
	}

	private final Pointer handle;

	private ZmqContext(final int ioThreads) {
		this.handle = zmq.zmq_init(ioThreads);
	}

	public ZmqSocket socket(final ZmqSocket.Type type) {
		return new ZmqSocket(this, type);
	}

	public void term() {
		check(zmq.zmq_term(this.handle));
	}

	Pointer getHandle() {
		return this.handle;
	}

	private void check(final int rc) {
		if (rc != 0) {
			final int err = zmq.zmq_errno();
			throw new ZmqException(zmq.zmq_strerror(err), err);
		}
	}

}