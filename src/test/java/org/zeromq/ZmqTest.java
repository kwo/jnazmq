package org.zeromq;

import java.util.Arrays;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

public class ZmqTest {

	@Test
	public void testContext() {

		final ZmqContext ctx = Zmq.getContext(1);
		Assert.assertNotNull(ctx);
		ctx.term();

	}

	@Test
	public void testLinger() {

		final ZmqContext ctx = Zmq.getContext(1);
		final ZmqSocket s = ctx.getSocket(ZmqSocket.Type.ZMQ_PUB);
		s.setLinger(0);
		s.connect("tcp://localhost:44444");

		s.close();

		ctx.term();

	}

	@Test
	public void testSocket() {

		final ZmqContext ctx = Zmq.getContext(1);

		final byte[] id1 = UUID.randomUUID().toString().getBytes();

		final ZmqSocket s = ctx.getSocket(ZmqSocket.Type.ZMQ_PUB);
		s.setIdentity(id1);
		s.connect("tcp://localhost:44444");
		final byte[] id2 = s.getIdentity();
		Assert.assertEquals(ZmqSocket.Type.ZMQ_PUB, s.getType());

		s.send("hello world".getBytes());

		s.setLinger(0);
		s.close();
		ctx.term();

		Assert.assertEquals(id1.length, id2.length);
		Assert.assertTrue(Arrays.equals(id1, id2));

	}

	@Test
	public void testVerison() {

		final int[] version = Zmq.version();
		Assert.assertNotNull(version);
		Assert.assertEquals(3, version.length);
		Assert.assertEquals(2, version[0]);
		Assert.assertEquals(1, version[1]);
		Assert.assertEquals(6, version[2]);

		Assert.assertEquals(Zmq.VERSION, Zmq.make_version(2, 1, 6));

	}

}
