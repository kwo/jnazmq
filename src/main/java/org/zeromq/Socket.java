package org.zeromq;

import org.zeromq.ZMQ.SocketType;

import com.sun.jna.Pointer;

public class Socket {

	final Pointer handle;
	private final ZMQLibrary zmq;

	protected Socket(final ZMQLibrary zmq, final Context ctx, final SocketType type) {
		this.zmq = zmq;
		this.handle = zmq.zmq_socket(ctx.handle, type.code);
	}

	public void close() {
		check(this.zmq.zmq_close(this.handle));
	}

	public void connect(final String address) {
		check(this.zmq.zmq_connect(this.handle, address));
	}

	public byte[] getIdentity() {
		return null;
	}

	public SocketType getType() {
		// return ZMQ.zmq_getsockopt(this.handle, option, opt_val, opt_len)
		return null;
	}

	public byte[] recv(final int flags) {
		return null;
	}

	public void send(final byte[] data, final int flags) {

	}

	public void setIdentity(final byte[] id) {

	}

	private void check(final int rc) {
		if (rc != 0) {
			final int err = this.zmq.zmq_errno();
			throw new ZMQException(this.zmq.zmq_strerror(err), err);
		}
	}

}