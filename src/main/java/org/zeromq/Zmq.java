package org.zeromq;

import com.sun.jna.Native;

public class Zmq {

	public static enum Device {

		ZMQ_FORWARDER(ZmqLibrary.ZMQ_FORWARDER),
		ZMQ_QUEUE(ZmqLibrary.ZMQ_QUEUE),
		ZMQ_STREAMER(ZmqLibrary.ZMQ_STREAMER);

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

		EADDRINUSE(ZmqLibrary.EADDRINUSE),
		EADDRNOTAVAIL(ZmqLibrary.EADDRNOTAVAIL),
		ECONNREFUSED(ZmqLibrary.ECONNREFUSED),
		EFSM(ZmqLibrary.EFSM),
		EINPROGRESS(ZmqLibrary.EINPROGRESS),
		EMTHREAD(ZmqLibrary.EMTHREAD),
		ENETDOWN(ZmqLibrary.ENETDOWN),
		ENOBUFS(ZmqLibrary.ENOBUFS),
		ENOCOMPATPROTO(ZmqLibrary.ENOCOMPATPROTO),
		ENOTSUP(ZmqLibrary.ENOTSUP),
		EPROTONOSUPPORT(ZmqLibrary.EPROTONOSUPPORT),
		ETERM(ZmqLibrary.ETERM);

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

		ZMQ_MSG_MASK(ZmqLibrary.ZMQ_MSG_MASK),
		ZMQ_MSG_MORE(ZmqLibrary.ZMQ_MSG_MORE),
		ZMQ_MSG_SHARED(ZmqLibrary.ZMQ_MSG_SHARED);

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

		ZMQ_DELIMITER(ZmqLibrary.ZMQ_DELIMITER),
		ZMQ_VSM(ZmqLibrary.ZMQ_VSM);

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

		ZMQ_POLLERR(ZmqLibrary.ZMQ_POLLERR),
		ZMQ_POLLIN(ZmqLibrary.ZMQ_POLLIN),
		ZMQ_POLLOUT(ZmqLibrary.ZMQ_POLLOUT);

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

		ZMQ_NOBLOCK(ZmqLibrary.ZMQ_NOBLOCK),
		ZMQ_SNDMORE(ZmqLibrary.ZMQ_SNDMORE);

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

	private static final ZmqLibrary zmq;

	static {
		zmq = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);
		VERSION = make_version(version());
	}

	public static ZmqContext context(final int ioThreads) {
		return new ZmqContext(zmq, ioThreads);
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

	private Zmq() {
	}

}
