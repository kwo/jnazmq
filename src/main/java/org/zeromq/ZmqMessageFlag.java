package org.zeromq;

public enum ZmqMessageFlag {

	ZMQ_MSG_MASK(ZmqLibrary.ZMQ_MSG_MASK),
	ZMQ_MSG_MORE(ZmqLibrary.ZMQ_MSG_MORE),
	ZMQ_MSG_SHARED(ZmqLibrary.ZMQ_MSG_SHARED);

	public static ZmqMessageFlag findByCode(final int code) {
		for (final ZmqMessageFlag x : values())
			if (code == x.code)
				return x;
		throw new IllegalArgumentException("Unknown " + ZmqMessageFlag.class.getSimpleName() + ": " + code);
	}

	public final int code;

	ZmqMessageFlag(final int code) {
		this.code = code;
	}

}