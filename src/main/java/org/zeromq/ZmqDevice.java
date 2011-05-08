package org.zeromq;

public enum ZmqDevice {

	ZMQ_FORWARDER(ZmqLibrary.ZMQ_FORWARDER),
	ZMQ_QUEUE(ZmqLibrary.ZMQ_QUEUE),
	ZMQ_STREAMER(ZmqLibrary.ZMQ_STREAMER);

	public static ZmqDevice findByCode(final int code) {
		for (final ZmqDevice x : values())
			if (code == x.code)
				return x;
		throw new IllegalArgumentException("Unknown " + ZmqDevice.class.getSimpleName() + ": " + code);
	}

	public final int code;

	ZmqDevice(final int code) {
		this.code = code;
	}

}