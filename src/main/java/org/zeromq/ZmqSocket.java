package org.zeromq;

import org.zeromq.ZmqLibrary.zmq_msg_t;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

public class ZmqSocket {

	public static enum Option {

		AFFINITY(ZmqLibrary.ZMQ_AFFINITY),
		BACKLOG(ZmqLibrary.ZMQ_BACKLOG),
		EVENTS(ZmqLibrary.ZMQ_EVENTS),
		FD(ZmqLibrary.ZMQ_FD),
		HWM(ZmqLibrary.ZMQ_HWM),
		IDENTITY(ZmqLibrary.ZMQ_IDENTITY),
		LINGER(ZmqLibrary.ZMQ_LINGER),
		MCAST_LOOP(ZmqLibrary.ZMQ_MCAST_LOOP),
		RATE(ZmqLibrary.ZMQ_RATE),
		RCVBUF(ZmqLibrary.ZMQ_RCVBUF),
		RCVMORE(ZmqLibrary.ZMQ_RCVMORE),
		RECONNECT_IVL(ZmqLibrary.ZMQ_RECONNECT_IVL),
		RECONNECT_IVL_MAX(ZmqLibrary.ZMQ_RECONNECT_IVL_MAX),
		RECOVERY_IVL(ZmqLibrary.ZMQ_RECOVERY_IVL),
		RECOVERY_IVL_MSEC(ZmqLibrary.ZMQ_RECOVERY_IVL_MSEC),
		SNDBUF(ZmqLibrary.ZMQ_SNDBUF),
		SUBSCRIBE(ZmqLibrary.ZMQ_SUBSCRIBE),
		SWAP(ZmqLibrary.ZMQ_SWAP),
		TYPE(ZmqLibrary.ZMQ_TYPE),
		UNSUBSCRIBE(ZmqLibrary.ZMQ_UNSUBSCRIBE);

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

		NOBLOCK(ZmqLibrary.ZMQ_NOBLOCK),
		SNDMORE(ZmqLibrary.ZMQ_SNDMORE);

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

		DEALER(ZmqLibrary.ZMQ_DEALER),
		@Deprecated
		DOWNSTREAM(ZmqLibrary.ZMQ_PUSH),
		PAIR(ZmqLibrary.ZMQ_PAIR),
		PUB(ZmqLibrary.ZMQ_PUB),
		PULL(ZmqLibrary.ZMQ_PULL),
		PUSH(ZmqLibrary.ZMQ_PUSH),
		REP(ZmqLibrary.ZMQ_REP),
		REQ(ZmqLibrary.ZMQ_REQ),
		ROUTER(ZmqLibrary.ZMQ_ROUTER),
		SUB(ZmqLibrary.ZMQ_SUB),
		@Deprecated
		UPSTREAM(ZmqLibrary.ZMQ_PULL),
		XPUB(ZmqLibrary.ZMQ_XPUB),
		@Deprecated
		XREP(ZmqLibrary.ZMQ_ROUTER),
		@Deprecated
		XREQ(ZmqLibrary.ZMQ_DEALER),
		XSUB(ZmqLibrary.ZMQ_XSUB);

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

	public void addSubscription(final byte[] filter) {
		setOption(Option.SUBSCRIBE, filter);
	}

	public void bind(final String address) {
		check(zmqlib.zmq_bind(this.handle, address));
	}

	public void close() {
		check(zmqlib.zmq_close(this.handle));
	}

	public void connect(final String address) {
		check(zmqlib.zmq_connect(this.handle, address));
	}

	public long getAffinity() {
		return getOptionLong(Option.AFFINITY);
	}

	public byte[] getIdentity() {
		return getOptionByteArray(Option.IDENTITY, 1024);
	}

	public int getLinger() {
		return getOptionInt(Option.LINGER);
	}

	public ZmqSocket.Type getType() {
		return Type.findByCode((int) getOptionLong(Option.TYPE));
	}

	public byte[] recv(final SendRecvOption... opts) {

		int flags = 0;
		for (final SendRecvOption opt : opts)
			flags += opt.code;

		final zmq_msg_t msg = new zmq_msg_t();

		try {

			check(zmqlib.zmq_msg_init(msg));
			check(zmqlib.zmq_recv(this.handle, msg, flags));

			final int size = zmqlib.zmq_msg_size(msg).intValue();
			final Pointer buffer = zmqlib.zmq_msg_data(msg);

			final byte[] data = new byte[size];
			buffer.read(0, data, 0, size);
			return data;

		} finally {
			check(zmqlib.zmq_msg_close(msg));
		}

	}

	public void removeSubscription(final byte[] filter) {
		setOption(Option.UNSUBSCRIBE, filter);
	}

	public void send(final byte[] data, final SendRecvOption... opts) {

		int flags = 0;
		for (final SendRecvOption opt : opts)
			flags += opt.code;

		final Memory m = new Memory(data.length);
		m.write(0, data, 0, data.length);

		final zmq_msg_t msg = new zmq_msg_t();
		try {
			check(zmqlib.zmq_msg_init_data(msg, m, new NativeLong(data.length), null, null));
			check(zmqlib.zmq_send(this.handle, msg, flags));
		} finally {
			check(zmqlib.zmq_msg_close(msg));
		}

	}

	public void setAffinity(final long value) {
		setOption(Option.AFFINITY, value);
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
		setOption(Option.IDENTITY, id);
	}

	public void setLinger(final int value) {
		setOption(Option.LINGER, value);
	}

	private void check(final int rc) {
		if (rc != 0) {
			final int err = zmqlib.zmq_errno();
			throw new ZmqException(zmqlib.zmq_strerror(err), err);
		}
	}

	private byte[] getOptionByteArray(final Option opt, final int capacity) {
		final Memory data = new Memory(capacity);
		data.clear(capacity);
		final LongByReference size = new LongByReference(capacity);
		check(zmqlib.zmq_getsockopt(this.handle, opt.code, data, size));
		return data.getByteArray(0, (int) size.getValue());
	}

	private int getOptionInt(final Option opt) {
		final int capacity = 4;
		final Memory data = new Memory(capacity);
		data.clear(capacity);
		final LongByReference size = new LongByReference(capacity);
		check(zmqlib.zmq_getsockopt(this.handle, opt.code, data, size));
		return data.getInt(0);
	}

	private long getOptionLong(final Option opt) {
		final int capacity = 8;
		final Memory data = new Memory(capacity);
		data.clear(capacity);
		final LongByReference size = new LongByReference(capacity);
		check(zmqlib.zmq_getsockopt(this.handle, opt.code, data, size));
		return data.getLong(0);
	}

	private void setOption(final Option opt, final byte[] value) {
		final Memory data = new Memory(value.length);
		data.write(0, value, 0, value.length);
		check(zmqlib.zmq_setsockopt(this.handle, opt.code, data, new NativeLong(data.getSize())));
	}

	private void setOption(final Option opt, final int value) {
		final Memory data = new Memory(4);
		data.setInt(0, value);
		check(zmqlib.zmq_setsockopt(this.handle, opt.code, data, new NativeLong(data.getSize())));
	}

	private void setOption(final Option opt, final long value) {
		final Memory data = new Memory(8);
		data.setLong(0, value);
		check(zmqlib.zmq_setsockopt(this.handle, opt.code, data, new NativeLong(data.getSize())));
	}

}