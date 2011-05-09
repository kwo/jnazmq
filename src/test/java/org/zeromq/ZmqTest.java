package org.zeromq;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.Assert;

import org.junit.Test;
import org.zeromq.ZmqSocket.SendRecvOption;
import org.zeromq.ZmqSocket.Type;

public class ZmqTest {

	@Test
	public void testContext() {

		final ZmqContext ctx = Zmq.getContext(1);
		Assert.assertNotNull(ctx);
		ctx.term();

	}

	@Test
	public void testSendRecv() throws Exception {

		final byte[] filter = "hello".getBytes();
		final byte[] contents = "hello, world!".getBytes();
		final String addr = "tcp://127.0.0.1:44444";

		final ExecutorService executor = Executors.newCachedThreadPool();
		final CountDownLatch ready = new CountDownLatch(1);
		final CountDownLatch messages = new CountDownLatch(3);
		final CountDownLatch clean = new CountDownLatch(2);

		final Runnable publisher = new Runnable() {

			@Override
			public void run() {

				final ZmqContext ctx = Zmq.getContext(1);
				final ZmqSocket socket = ctx.getSocket(Type.PUB);
				socket.bind(addr);

				try {
					while (!Thread.currentThread().isInterrupted()) {
						socket.send(contents);
						System.out.printf("sent: %s%n", new String(contents));
						Thread.sleep(50);
					}
				} catch (final InterruptedException e) {
				}

				socket.setLinger(0);
				socket.close();
				ctx.term();

				clean.countDown();

				System.out.println("exited publisher");

			}

		};

		final Runnable subscriber = new Runnable() {

			@Override
			public void run() {

				final ZmqContext ctx = Zmq.getContext(1);
				final ZmqSocket socket = ctx.getSocket(Type.SUB);
				socket.connect(addr);
				socket.addSubscription(filter);

				ready.countDown();

				while (!Thread.currentThread().isInterrupted()) {
					try {
						final byte[] response = socket.recv(SendRecvOption.NOBLOCK);
						Assert.assertTrue(Arrays.equals(contents, response));
						System.out.printf("received: %s%n", new String(contents));
						messages.countDown();
					} catch (final ZmqException x) {
						// catch resource not available (consequence of noblock)
						if (x.getCode() == 35)
							continue;
						throw x;
					}
				}

				socket.close();
				ctx.term();
				clean.countDown();

				System.out.println("exited subscriber");

			}

		};

		executor.execute(subscriber);
		System.out.println("waiting for ready...");
		ready.await();
		executor.execute(publisher);
		System.out.println("waiting for messages...");
		messages.await();
		executor.shutdownNow();
		System.out.println("waiting for cleanup...");
		clean.await();

	}

	@Test
	public void testSocket() {

		final ZmqContext ctx = Zmq.getContext(1);
		final ZmqSocket s = ctx.getSocket(Type.PUB);

		final byte[] id = UUID.randomUUID().toString().getBytes();
		s.setAffinity(1);
		s.setLinger(2000);
		s.setIdentity(id);

		s.connect("tcp://localhost:44444");

		Assert.assertEquals(Type.PUB, s.getType());
		Assert.assertTrue(Arrays.equals(id, s.getIdentity()));
		Assert.assertEquals(2000, s.getLinger());
		Assert.assertEquals(1, s.getAffinity());

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
