package org.zeromq;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class ZMQLibraryTest {

	private static ZMQLibrary ZMQ = null;

	@BeforeClass
	public static void setup() {
		ZMQ = (ZMQLibrary) Native.loadLibrary("zmq", ZMQLibrary.class);
	}

	@AfterClass
	public static void teardown() {
		ZMQ = null;
	}

	@Test
	public void testContext() {
		final Pointer ctx = ZMQ.zmq_init(1);
		Assert.assertNotNull(ctx);
		ZMQ.zmq_term(ctx);
	}

	@Test
	public void testSocket() {

		int rc = 0;

		final Pointer ctx = ZMQ.zmq_init(1);
		Assert.assertNotNull(ctx);

		final Pointer s = ZMQ.zmq_socket(ctx, ZMQLibrary.ZMQ_PULL);
		Assert.assertNotNull(s);
		rc = ZMQ.zmq_close(s);
		Assert.assertEquals(0, rc);

		rc = ZMQ.zmq_term(ctx);
		Assert.assertEquals(0, rc);

	}

	@Test
	public void testVersion() {
		Assert.assertNotNull(ZMQ);
		final int[] major = new int[1];
		final int[] minor = new int[1];
		final int[] patch = new int[1];
		ZMQ.zmq_version(major, minor, patch);
		Assert.assertEquals(2, major[0]);
		Assert.assertEquals(1, minor[0]);
		Assert.assertEquals(6, patch[0]);
	}

}
