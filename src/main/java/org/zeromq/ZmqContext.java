package org.zeromq;

import org.zeromq.Zmq.SocketType;

import com.sun.jna.Pointer;

public class ZmqContext {

	private final Pointer handle;
	private final ZmqLibrary zmq;

	ZmqContext(final ZmqLibrary zmq, final int ioThreads) {
		this.zmq = zmq;
		this.handle = zmq.zmq_init(ioThreads);
	}

	public ZmqSocket socket(final SocketType type) {
		return new ZmqSocket(this.zmq, this, type);
	}

	public void term() {
		check(this.zmq.zmq_term(this.handle));
	}

	Pointer getHandle() {
		return this.handle;
	}

	private void check(final int rc) {
		if (rc != 0) {
			final int err = this.zmq.zmq_errno();
			throw new ZmqException(this.zmq.zmq_strerror(err), err);
		}
	}

}