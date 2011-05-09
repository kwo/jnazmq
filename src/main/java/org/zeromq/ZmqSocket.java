package org.zeromq;

import org.zeromq.ZmqLibrary.zmq_msg_t;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

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

	private static final ZmqLibrary zmqlib;
	static {
		zmqlib = Zmq.getLibrary();
	}

	private final Pointer handle;

	ZmqSocket(final ZmqContext ctx, final ZmqSocket.Type type) {
		this.handle = zmqlib.zmq_socket(ctx.getHandle(), type.code);
	}

	public void close() {
		check(zmqlib.zmq_close(this.handle));
	}

	public void connect(final String address) {
		check(zmqlib.zmq_connect(this.handle, address));
	}

	public byte[] getIdentity() {
		return getSocketOptionByteArray(Option.ZMQ_IDENTITY, 1024);
	}

	public int getLinger() {
		return getSocketOptionInt(Option.ZMQ_LINGER);
	}

	public ZmqSocket.Type getType() {
		return Type.findByCode((int) getSocketOptionLong(Option.ZMQ_TYPE));
	}

	public byte[] recv(final SendRecvOption... opts) {
		return null;
	}

	public void send(final byte[] data, final SendRecvOption... opts) {

		final Memory m = new Memory(data.length);
		m.write(0, data, 0, data.length);

		int flags = 0;
		for (final SendRecvOption opt : opts)
			flags += opt.code;

		final zmq_msg_t msg = new zmq_msg_t();
		check(zmqlib.zmq_msg_init_data(msg, m, new NativeLong(data.length), null, null));
		check(zmqlib.zmq_send(this.handle, msg, flags));
		check(zmqlib.zmq_msg_close(msg));

	}

	/**
	 * Set the socket identifier.
	 * 
	 * @param id
	 *            socket identifier between 1 and 255 bytes in size
	 */
	public void setIdentity(final byte[] id) {
		if (id == null || id.length == 0 || id.length > 255)
			throw new IllegalArgumentException();
		setSocketOption(Option.ZMQ_IDENTITY, id);
	}

	public void setLinger(final int value) {
		setSocketOption(Option.ZMQ_LINGER, value);
	}

	private void check(final int rc) {
		if (rc != 0) {
			final int err = zmqlib.zmq_errno();
			throw new ZmqException(zmqlib.zmq_strerror(err), err);
		}
	}

	private byte[] getSocketOptionByteArray(final Option opt, final int capacity) {
		final Memory data = new Memory(capacity);
		data.clear(capacity);
		final LongByReference size = new LongByReference(capacity);
		check(zmqlib.zmq_getsockopt(this.handle, opt.code, data, size));
		return data.getByteArray(0, (int) size.getValue());
	}

	private int getSocketOptionInt(final Option opt) {
		final int capacity = 4;
		final Memory data = new Memory(capacity);
		data.clear(capacity);
		final LongByReference size = new LongByReference(capacity);
		check(zmqlib.zmq_getsockopt(this.handle, opt.code, data, size));
		return data.getInt(0);
	}

	private long getSocketOptionLong(final Option opt) {
		final int capacity = 8;
		final Memory data = new Memory(capacity);
		data.clear(capacity);
		final LongByReference size = new LongByReference(capacity);
		check(zmqlib.zmq_getsockopt(this.handle, opt.code, data, size));
		return data.getLong(0);
	}

	private void setSocketOption(final Option opt, final byte[] value) {
		final Memory data = new Memory(value.length);
		data.write(0, value, 0, value.length);
		check(zmqlib.zmq_setsockopt(this.handle, opt.code, data, new NativeLong(data.getSize())));
	}

	private void setSocketOption(final Option opt, final int value) {
		final Memory data = new Memory(4);
		data.setInt(0, value);
		check(zmqlib.zmq_setsockopt(this.handle, opt.code, data, new NativeLong(data.getSize())));
	}

	private void setSocketOption(final Option opt, final long value) {
		final Memory data = new Memory(8);
		data.setLong(0, value);
		check(zmqlib.zmq_setsockopt(this.handle, opt.code, data, new NativeLong(data.getSize())));
	}

}