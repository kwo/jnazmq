package org.zeromq;

import junit.framework.Assert;

import org.junit.Test;
import org.zeromq.Zmq.SocketType;

public class ZMQTest {

	@Test
	public void testContext() {

		final ZmqContext ctx = Zmq.context(1);
		Assert.assertNotNull(ctx);
		ctx.term();

	}

	@Test
	public void testSocket() {

		final ZmqContext ctx = Zmq.context(1);

		final ZmqSocket s = ctx.socket(SocketType.ZMQ_PULL);
		s.connect("tcp://localhost:44444");
		s.close();

		ctx.term();

	}

	@Test
	public void testVerison() {

		final int[] version = Zmq.version();
		Assert.assertNotNull(version);
		Assert.assertEquals(3, version.length);
		Assert.assertEquals(2, version[0]);
		Assert.assertEquals(1, version[1]);
		Assert.assertEquals(6, version[2]);

	}

}
