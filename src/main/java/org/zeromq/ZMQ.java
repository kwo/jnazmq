package org.zeromq;

import com.sun.jna.Native;

public class ZMQ {

	public static enum Device {

		ZMQ_FORWARDER(ZMQLibrary.ZMQ_FORWARDER),
		ZMQ_QUEUE(ZMQLibrary.ZMQ_QUEUE),
		ZMQ_STREAMER(ZMQLibrary.ZMQ_STREAMER);

		public static Device findByCode(final int code) {
			for (final Device x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + Device.class.getSimpleName() + ": " + code);
		}

		public final int code;

		Device(final int code) {
			this.code = code;
		}

	}

	public static enum Error {

		EADDRINUSE(ZMQLibrary.EADDRINUSE),
		EADDRNOTAVAIL(ZMQLibrary.EADDRNOTAVAIL),
		ECONNREFUSED(ZMQLibrary.ECONNREFUSED),
		EFSM(ZMQLibrary.EFSM),
		EINPROGRESS(ZMQLibrary.EINPROGRESS),
		EMTHREAD(ZMQLibrary.EMTHREAD),
		ENETDOWN(ZMQLibrary.ENETDOWN),
		ENOBUFS(ZMQLibrary.ENOBUFS),
		ENOCOMPATPROTO(ZMQLibrary.ENOCOMPATPROTO),
		ENOTSUP(ZMQLibrary.ENOTSUP),
		EPROTONOSUPPORT(ZMQLibrary.EPROTONOSUPPORT),
		ETERM(ZMQLibrary.ETERM);

		public static Error findByCode(final int code) {
			for (final Error x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + Error.class.getSimpleName() + ": " + code);
		}

		public final int code;

		Error(final int code) {
			this.code = code;
		}

	}

	public static enum MessageFlag {

		ZMQ_MSG_MASK(ZMQLibrary.ZMQ_MSG_MASK),
		ZMQ_MSG_MORE(ZMQLibrary.ZMQ_MSG_MORE),
		ZMQ_MSG_SHARED(ZMQLibrary.ZMQ_MSG_SHARED);

		public static MessageFlag findByCode(final int code) {
			for (final MessageFlag x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + MessageFlag.class.getSimpleName() + ": " + code);
		}

		public final int code;

		MessageFlag(final int code) {
			this.code = code;
		}

	}

	public static enum MessageType {

		ZMQ_DELIMITER(ZMQLibrary.ZMQ_DELIMITER),
		ZMQ_VSM(ZMQLibrary.ZMQ_VSM);

		public static MessageType findByCode(final int code) {
			for (final MessageType x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + MessageType.class.getSimpleName() + ": " + code);
		}

		public final int code;

		MessageType(final int code) {
			this.code = code;
		}

	}

	public static enum Multiplex {

		ZMQ_POLLERR(ZMQLibrary.ZMQ_POLLERR),
		ZMQ_POLLIN(ZMQLibrary.ZMQ_POLLIN),
		ZMQ_POLLOUT(ZMQLibrary.ZMQ_POLLOUT);

		public static Multiplex findByCode(final int code) {
			for (final Multiplex x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + Multiplex.class.getSimpleName() + ": " + code);
		}

		public final int code;

		Multiplex(final int code) {
			this.code = code;
		}

	}

	public static enum SocketOption {

		ZMQ_AFFINITY(ZMQLibrary.ZMQ_AFFINITY),
		ZMQ_BACKLOG(ZMQLibrary.ZMQ_BACKLOG),
		ZMQ_EVENTS(ZMQLibrary.ZMQ_EVENTS),
		ZMQ_FD(ZMQLibrary.ZMQ_FD),
		ZMQ_HWM(ZMQLibrary.ZMQ_HWM),
		ZMQ_IDENTITY(ZMQLibrary.ZMQ_IDENTITY),
		ZMQ_LINGER(ZMQLibrary.ZMQ_LINGER),
		ZMQ_MCAST_LOOP(ZMQLibrary.ZMQ_MCAST_LOOP),
		ZMQ_RATE(ZMQLibrary.ZMQ_RATE),
		ZMQ_RCVBUF(ZMQLibrary.ZMQ_RCVBUF),
		ZMQ_RCVMORE(ZMQLibrary.ZMQ_RCVMORE),
		ZMQ_RECONNECT_IVL(ZMQLibrary.ZMQ_RECONNECT_IVL),
		ZMQ_RECONNECT_IVL_MAX(ZMQLibrary.ZMQ_RECONNECT_IVL_MAX),
		ZMQ_RECOVERY_IVL(ZMQLibrary.ZMQ_RECOVERY_IVL),
		ZMQ_RECOVERY_IVL_MSEC(ZMQLibrary.ZMQ_RECOVERY_IVL_MSEC),
		ZMQ_SNDBUF(ZMQLibrary.ZMQ_SNDBUF),
		ZMQ_SUBSCRIBE(ZMQLibrary.ZMQ_SUBSCRIBE),
		ZMQ_SWAP(ZMQLibrary.ZMQ_SWAP),
		ZMQ_TYPE(ZMQLibrary.ZMQ_TYPE),
		ZMQ_UNSUBSCRIBE(ZMQLibrary.ZMQ_UNSUBSCRIBE);

		public static SocketOption findByCode(final int code) {
			for (final SocketOption x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + SocketOption.class.getSimpleName() + ": " + code);
		}

		public final int code;

		SocketOption(final int code) {
			this.code = code;
		}

	}

	public static enum SocketSendRecvOption {

		ZMQ_NOBLOCK(ZMQLibrary.ZMQ_NOBLOCK),
		ZMQ_SNDMORE(ZMQLibrary.ZMQ_SNDMORE);

		public static SocketSendRecvOption findByCode(final int code) {
			for (final SocketSendRecvOption x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + SocketSendRecvOption.class.getSimpleName() + ": " + code);
		}

		public final int code;

		SocketSendRecvOption(final int code) {
			this.code = code;
		}

	}

	public static enum SocketType {

		@Deprecated
		DOWNSTREAM(ZMQLibrary.ZMQ_PUSH),
		ZMQ_DEALER(ZMQLibrary.ZMQ_DEALER),
		ZMQ_PAIR(ZMQLibrary.ZMQ_PAIR),
		ZMQ_PUB(ZMQLibrary.ZMQ_PUB),
		ZMQ_PULL(ZMQLibrary.ZMQ_PULL),
		ZMQ_PUSH(ZMQLibrary.ZMQ_PUSH),
		ZMQ_REP(ZMQLibrary.ZMQ_REP),
		ZMQ_REQ(ZMQLibrary.ZMQ_REQ),
		ZMQ_ROUTER(ZMQLibrary.ZMQ_ROUTER),
		ZMQ_SUB(ZMQLibrary.ZMQ_SUB),
		@Deprecated
		ZMQ_UPSTREAM(ZMQLibrary.ZMQ_PULL),
		ZMQ_XPUB(ZMQLibrary.ZMQ_XPUB),
		@Deprecated
		ZMQ_XREP(ZMQLibrary.ZMQ_ROUTER),
		@Deprecated
		ZMQ_XREQ(ZMQLibrary.ZMQ_DEALER),
		ZMQ_XSUB(ZMQLibrary.ZMQ_XSUB);

		public static SocketType findByCode(final int code) {
			for (final SocketType x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + SocketType.class.getSimpleName() + ": " + code);
		}

		public final int code;

		SocketType(final int code) {
			this.code = code;
		}

	}

	public static final int VERSION;

	private static final ZMQLibrary zmq;

	static {
		zmq = (ZMQLibrary) Native.loadLibrary("zmq", ZMQLibrary.class);
		VERSION = make_version(version());
	}

	public static Context context(final int ioThreads) {
		return new Context(zmq, ioThreads);
	}

	public static int make_version(final int major, final int minor, final int patch) {
		return (major * 10000) + (minor * 100) + patch;
	}

	public static int make_version(final int[] version) {
		return make_version(version[0], version[1], version[2]);
	}

	public static int[] version() {
		final int[] major = new int[1];
		final int[] minor = new int[1];
		final int[] patch = new int[1];
		zmq.zmq_version(major, minor, patch);
		return new int[] { major[0], minor[0], patch[0] };
	}

	private ZMQ() {
	}

}
