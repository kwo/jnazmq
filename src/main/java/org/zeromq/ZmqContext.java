package org.zeromq;

import com.sun.jna.Pointer;

public class ZmqContext {

	public static ZmqContext getInstance(final int ioThreads) {
		return new ZmqContext(ioThreads);
	}

	private final Pointer handle;

	ZmqContext(final int ioThreads) {
		this.handle = Zmq.zmq_init(ioThreads);
	}

	public ZmqSocket getSocket(final ZmqSocket.Type type) {
		return new ZmqSocket(this, type);
	}

	public void term() {
		check(Zmq.zmq_term(this.handle));
	}

	Pointer getHandle() {
		return this.handle;
	}

	private void check(final int rc) {
		if (rc != 0) {
			final int err = Zmq.zmq_errno();
			throw new ZmqException(Zmq.zmq_strerror(err), err);
		}
	}

}