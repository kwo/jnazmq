package org.zeromq;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jna.Native;

public class ZeroMQLibraryTest {

	private static ZeroMQLibrary ZMQ = null;

	@BeforeClass
	public static void setup() {
		ZMQ = (ZeroMQLibrary) Native.loadLibrary("zmq", ZeroMQLibrary.class);
	}

	@AfterClass
	public static void teardown() {
		ZMQ = null;
	}

	@Test
	public void testVersion() {
		Assert.assertNotNull(ZMQ);
		int[] major = new int[1];
		int[] minor = new int[1];
		int[] patch = new int[1];
		ZMQ.zmq_version(major, minor, patch);
		Assert.assertEquals(2, major[0]);
		Assert.assertEquals(1, minor[0]);
		Assert.assertEquals(4, patch[0]);
	}

}
