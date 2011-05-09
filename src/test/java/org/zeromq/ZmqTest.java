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
	public void testSocket() {

		final ZmqContext ctx = Zmq.getContext(1);
		final ZmqSocket s = ctx.getSocket(ZmqSocket.Type.ZMQ_PUB);

		final byte[] id = UUID.randomUUID().toString().getBytes();
		s.setAffinity(1);
		s.setLinger(2000);
		s.setIdentity(id);

		s.connect("tcp://localhost:44444");

		Assert.assertEquals(ZmqSocket.Type.ZMQ_PUB, s.getType());
		Assert.assertTrue(Arrays.equals(id, s.getIdentity()));
		Assert.assertEquals(2000, s.getLinger());
		Assert.assertEquals(1, s.getAffinity());

		s.send("hello world".getBytes());

		s.setLinger(0);
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

		Assert.assertEquals(Zmq.VERSION, Zmq.make_version(2, 1, 6));

	}

}
