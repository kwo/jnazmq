package org.zeromq;

import org.zeromq.ZMQ.SocketType;

import com.sun.jna.Pointer;

public class Context {

	final Pointer handle;
	private final ZMQLibrary zmq;

	Context(final ZMQLibrary zmq, final int ioThreads) {
		this.zmq = zmq;
		this.handle = zmq.zmq_init(ioThreads);
	}

	public Socket socket(final SocketType type) {
		return new Socket(this.zmq, this, type);
	}

	public void term() {
		check(this.zmq.zmq_term(this.handle));
	}

	private void check(final int rc) {
		if (rc != 0) {
			final int err = this.zmq.zmq_errno();
			throw new ZMQException(this.zmq.zmq_strerror(err), err);
		}
	}

}