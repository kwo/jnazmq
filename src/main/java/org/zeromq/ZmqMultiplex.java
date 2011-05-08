package org.zeromq;

public enum ZmqMultiplex {

	ZMQ_POLLERR(ZmqLibrary.ZMQ_POLLERR),
	ZMQ_POLLIN(ZmqLibrary.ZMQ_POLLIN),
	ZMQ_POLLOUT(ZmqLibrary.ZMQ_POLLOUT);

	public static ZmqMultiplex findByCode(final int code) {
		for (final ZmqMultiplex x : values())
			if (code == x.code)
				return x;
		throw new IllegalArgumentException("Unknown " + ZmqMultiplex.class.getSimpleName() + ": " + code);
	}

	public final int code;

	ZmqMultiplex(final int code) {
		this.code = code;
	}

}