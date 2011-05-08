package org.zeromq;

public enum ZmqMessageType {

	ZMQ_DELIMITER(ZmqLibrary.ZMQ_DELIMITER),
	ZMQ_VSM(ZmqLibrary.ZMQ_VSM);

	public static ZmqMessageType findByCode(final int code) {
		for (final ZmqMessageType x : values())
			if (code == x.code)
				return x;
		throw new IllegalArgumentException("Unknown " + ZmqMessageType.class.getSimpleName() + ": " + code);
	}

	public final int code;

	ZmqMessageType(final int code) {
		this.code = code;
	}

}