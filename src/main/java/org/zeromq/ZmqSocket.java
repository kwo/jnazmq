package org.zeromq;

import com.sun.jna.Pointer;

public class ZmqSocket {

	public static enum Option {

		ZMQ_AFFINITY(ZmqLibrary.ZMQ_AFFINITY),
		ZMQ_BACKLOG(ZmqLibrary.ZMQ_BACKLOG),
		ZMQ_EVENTS(ZmqLibrary.ZMQ_EVENTS),
		ZMQ_FD(ZmqLibrary.ZMQ_FD),
		ZMQ_HWM(ZmqLibrary.ZMQ_HWM),
		ZMQ_IDENTITY(ZmqLibrary.ZMQ_IDENTITY),
		ZMQ_LINGER(ZmqLibrary.ZMQ_LINGER),
		ZMQ_MCAST_LOOP(ZmqLibrary.ZMQ_MCAST_LOOP),
		ZMQ_RATE(ZmqLibrary.ZMQ_RATE),
		ZMQ_RCVBUF(ZmqLibrary.ZMQ_RCVBUF),
		ZMQ_RCVMORE(ZmqLibrary.ZMQ_RCVMORE),
		ZMQ_RECONNECT_IVL(ZmqLibrary.ZMQ_RECONNECT_IVL),
		ZMQ_RECONNECT_IVL_MAX(ZmqLibrary.ZMQ_RECONNECT_IVL_MAX),
		ZMQ_RECOVERY_IVL(ZmqLibrary.ZMQ_RECOVERY_IVL),
		ZMQ_RECOVERY_IVL_MSEC(ZmqLibrary.ZMQ_RECOVERY_IVL_MSEC),
		ZMQ_SNDBUF(ZmqLibrary.ZMQ_SNDBUF),
		ZMQ_SUBSCRIBE(ZmqLibrary.ZMQ_SUBSCRIBE),
		ZMQ_SWAP(ZmqLibrary.ZMQ_SWAP),
		ZMQ_TYPE(ZmqLibrary.ZMQ_TYPE),
		ZMQ_UNSUBSCRIBE(ZmqLibrary.ZMQ_UNSUBSCRIBE);

		public static Option findByCode(final int code) {
			for (final Option x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + Option.class.getSimpleName() + ": " + code);
		}

		public final int code;

		Option(final int code) {
			this.code = code;
		}

	}

	public static enum SendRecvOption {

		ZMQ_NOBLOCK(ZmqLibrary.ZMQ_NOBLOCK),
		ZMQ_SNDMORE(ZmqLibrary.ZMQ_SNDMORE);

		public static SendRecvOption findByCode(final int code) {
			for (final SendRecvOption x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + SendRecvOption.class.getSimpleName() + ": " + code);
		}

		public final int code;

		SendRecvOption(final int code) {
			this.code = code;
		}

	}

	public static enum Type {

		@Deprecated
		DOWNSTREAM(ZmqLibrary.ZMQ_PUSH),
		ZMQ_DEALER(ZmqLibrary.ZMQ_DEALER),
		ZMQ_PAIR(ZmqLibrary.ZMQ_PAIR),
		ZMQ_PUB(ZmqLibrary.ZMQ_PUB),
		ZMQ_PULL(ZmqLibrary.ZMQ_PULL),
		ZMQ_PUSH(ZmqLibrary.ZMQ_PUSH),
		ZMQ_REP(ZmqLibrary.ZMQ_REP),
		ZMQ_REQ(ZmqLibrary.ZMQ_REQ),
		ZMQ_ROUTER(ZmqLibrary.ZMQ_ROUTER),
		ZMQ_SUB(ZmqLibrary.ZMQ_SUB),
		@Deprecated
		ZMQ_UPSTREAM(ZmqLibrary.ZMQ_PULL),
		ZMQ_XPUB(ZmqLibrary.ZMQ_XPUB),
		@Deprecated
		ZMQ_XREP(ZmqLibrary.ZMQ_ROUTER),
		@Deprecated
		ZMQ_XREQ(ZmqLibrary.ZMQ_DEALER),
		ZMQ_XSUB(ZmqLibrary.ZMQ_XSUB);

		public static Type findByCode(final int code) {
			for (final Type x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + Type.class.getSimpleName() + ": " + code);
		}

		public final int code;

		Type(final int code) {
			this.code = code;
		}

	}

	private static final ZmqLibrary zmq;
	static {
		zmq = Zmq.getLibrary();
	}

	private final Pointer handle;

	ZmqSocket(final ZmqContext ctx, final ZmqSocket.Type type) {

		this.handle = zmq.zmq_socket(ctx.getHandle(), type.code);
	}

	public void close() {
		check(zmq.zmq_close(this.handle));
	}

	public void connect(final String address) {
		check(zmq.zmq_connect(this.handle, address));
	}

	public byte[] getIdentity() {
		return null;
	}

	public ZmqSocket.Type getType() {
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
			final int err = zmq.zmq_errno();
			throw new ZmqException(zmq.zmq_strerror(err), err);
		}
	}

}