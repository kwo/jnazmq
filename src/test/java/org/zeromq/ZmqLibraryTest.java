package org.zeromq;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class ZmqLibraryTest {

	private static ZmqLibrary zmqlib = null;

	@BeforeClass
	public static void setup() {
		zmqlib = (ZmqLibrary) Native.loadLibrary("zmq", ZmqLibrary.class);
	}

	@AfterClass
	public static void teardown() {
		zmqlib = null;
	}

	@Test
	public void testContext() {
		final Pointer ctx = zmqlib.zmq_init(1);
		Assert.assertNotNull(ctx);
		zmqlib.zmq_term(ctx);
	}

	@Test
	public void testSocket() {

		int rc = 0;

		final Pointer ctx = zmqlib.zmq_init(1);
		Assert.assertNotNull(ctx);

		final Pointer s = zmqlib.zmq_socket(ctx, ZmqLibrary.ZMQ_PULL);
		Assert.assertNotNull(s);
		rc = zmqlib.zmq_close(s);
		Assert.assertEquals(0, rc);

		rc = zmqlib.zmq_term(ctx);
		Assert.assertEquals(0, rc);

	}

	@Test
	public void testVersion() {
		Assert.assertNotNull(zmqlib);
		final int[] major = new int[1];
		final int[] minor = new int[1];
		final int[] patch = new int[1];
		zmqlib.zmq_version(major, minor, patch);
		Assert.assertEquals(2, major[0]);
		Assert.assertEquals(1, minor[0]);
		Assert.assertTrue(patch[0] >= 0);
	}

}
