package org.zeromq;

public class ZmqException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int err;

	ZmqException(final String msg, final int err) {
		super(msg);
		this.err = err;
	}

	public int getErrorCode() {
		return this.err;
	}

}
