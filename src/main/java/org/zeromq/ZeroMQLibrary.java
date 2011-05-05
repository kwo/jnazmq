package org.zeromq;

import com.sun.jna.Library;

public interface ZeroMQLibrary extends Library {

	void zmq_version(int[] major, int[] minor, int[] patch);

}
