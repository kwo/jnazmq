package org.zeromq;

import org.zeromq.Zmq.SocketType;

import com.sun.jna.Pointer;

public class ZmqSocket {

	private final Pointer handle;
	private final ZmqLibrary zmq;

	protected ZmqSocket(final ZmqLibrary zmq, final ZmqContext ctx, final SocketType type) {
		this.zmq = zmq;
		this.handle = zmq.zmq_socket(ctx.getHandle(), type.code);
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
			throw new ZmqException(this.zmq.zmq_strerror(err), err);
		}
	}

}