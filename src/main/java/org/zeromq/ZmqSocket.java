package org.zeromq;

import org.zeromq.ZmqLibrary.zmq_msg_t;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

public class ZmqSocket {

	public static enum Option {

		AFFINITY(ZmqLibrary.ZMQ_AFFINITY),
		BACKLOG(ZmqLibrary.ZMQ_BACKLOG),
		EVENTS(ZmqLibrary.ZMQ_EVENTS),
		FD(ZmqLibrary.ZMQ_FD),
		HWM(ZmqLibrary.ZMQ_HWM),
		IDENTITY(ZmqLibrary.ZMQ_IDENTITY),
		LINGER(ZmqLibrary.ZMQ_LINGER),
		MCAST_LOOP(ZmqLibrary.ZMQ_MCAST_LOOP),
		RATE(ZmqLibrary.ZMQ_RATE),
		RCVBUF(ZmqLibrary.ZMQ_RCVBUF),
		RCVMORE(ZmqLibrary.ZMQ_RCVMORE),
		RECONNECT_IVL(ZmqLibrary.ZMQ_RECONNECT_IVL),
		RECONNECT_IVL_MAX(ZmqLibrary.ZMQ_RECONNECT_IVL_MAX),
		RECOVERY_IVL(ZmqLibrary.ZMQ_RECOVERY_IVL),
		RECOVERY_IVL_MSEC(ZmqLibrary.ZMQ_RECOVERY_IVL_MSEC),
		SNDBUF(ZmqLibrary.ZMQ_SNDBUF),
		SUBSCRIBE(ZmqLibrary.ZMQ_SUBSCRIBE),
		SWAP(ZmqLibrary.ZMQ_SWAP),
		TYPE(ZmqLibrary.ZMQ_TYPE),
		UNSUBSCRIBE(ZmqLibrary.ZMQ_UNSUBSCRIBE);

		public static Option findByCode(final int code) {
			for (final Option x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + Option.class.getSimpleName() + ": " + code);
		}

		public final int code;

		Option(final int code) {
			this.code = code;
		}

	}

	public static enum RecvFlag {

		NOBLOCK(ZmqLibrary.ZMQ_NOBLOCK);

		public static RecvFlag findByCode(final int code) {
			for (final RecvFlag x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + RecvFlag.class.getSimpleName() + ": " + code);
		}

		public final int code;

		RecvFlag(final int code) {
			this.code = code;
		}

	}

	public static enum SendFlag {

		NOBLOCK(ZmqLibrary.ZMQ_NOBLOCK),
		SNDMORE(ZmqLibrary.ZMQ_SNDMORE);

		public static SendFlag findByCode(final int code) {
			for (final SendFlag x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + SendFlag.class.getSimpleName() + ": " + code);
		}

		public final int code;

		SendFlag(final int code) {
			this.code = code;
		}

	}

	public static enum Type {

		DEALER(ZmqLibrary.ZMQ_DEALER),
		@Deprecated
		DOWNSTREAM(ZmqLibrary.ZMQ_PUSH),
		PAIR(ZmqLibrary.ZMQ_PAIR),
		PUB(ZmqLibrary.ZMQ_PUB),
		PULL(ZmqLibrary.ZMQ_PULL),
		PUSH(ZmqLibrary.ZMQ_PUSH),
		REP(ZmqLibrary.ZMQ_REP),
		REQ(ZmqLibrary.ZMQ_REQ),
		ROUTER(ZmqLibrary.ZMQ_ROUTER),
		SUB(ZmqLibrary.ZMQ_SUB),
		@Deprecated
		UPSTREAM(ZmqLibrary.ZMQ_PULL),
		XPUB(ZmqLibrary.ZMQ_XPUB),
		@Deprecated
		XREP(ZmqLibrary.ZMQ_ROUTER),
		@Deprecated
		XREQ(ZmqLibrary.ZMQ_DEALER),
		XSUB(ZmqLibrary.ZMQ_XSUB);

		public static Type findByCode(final int code) {
			for (final Type x : values())
				if (code == x.code)
					return x;
			throw new IllegalArgumentException("Unknown " + Type.class.getSimpleName() + ": " + code);
		}

		public final int code;

		Type(final int code) {
			this.code = code;
		}

	}

	private static final ZmqLibrary zmqlib;

	static {
		zmqlib = Zmq.getLibrary();
	}

	private final Pointer handle;

	ZmqSocket(final ZmqContext ctx, final ZmqSocket.Type type) {
		this.handle = zmqlib.zmq_socket(ctx.getHandle(), type.code);
	}

	/**
	 * Establish a message filter.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_SUBSCRIBE option shall establish a new message filter on a
	 * ZMQ_SUB socket. Newly created ZMQ_SUB sockets shall filter out all
	 * incoming messages, therefore you should call this option to establish an
	 * initial message filter.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * An empty option_value of length zero shall subscribe to all incoming
	 * messages. A non-empty option_value shall subscribe to all messages
	 * beginning with the specified prefix. Multiple filters may be attached to
	 * a single ZMQ_SUB socket, in which case a message shall be accepted if it
	 * matches at least one filter.
	 * 
	 * @param filter
	 *            the message filter
	 * 
	 * @see #removeSubscription(byte[])
	 * 
	 */
	public void addSubscription(final byte[] filter) {
		// TODO: allow for zero-length subscription
		setOption(Option.SUBSCRIBE, filter);
	}

	/**
	 * Accept connections on a socket. The zmq_bind() function shall create an
	 * endpoint for accepting connections and bind it to the socket referenced
	 * by the socket argument.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * With the exception of ZMQ_PAIR sockets, a single socket may be connected
	 * to multiple endpoints using zmq_connect(), while simultaneously accepting
	 * incoming connections from multiple endpoints bound to the socket using
	 * zmq_bind(). Refer to zmq_socket(3) for a description of the exact
	 * semantics involved when connecting or binding a socket to multiple
	 * endpoints.
	 * 
	 * @param endpoint
	 * 
	 *            The endpoint argument is a string consisting of two parts as
	 *            follows: transport://address. The transport part specifies the
	 *            underlying transport protocol to use. The meaning of the
	 *            address part is specific to the underlying transport protocol
	 *            selected.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 *            The following transports are defined:
	 * 
	 *            <ul>
	 * 
	 *            <li>inproc: local in-process (inter-thread) communication
	 *            transport, see zmq_inproc(7)</li>
	 * 
	 *            <li>ipc: local inter-process communication transport, see
	 *            zmq_ipc(7)</li>
	 * 
	 *            <li>tcp: unicast transport using TCP, see zmq_tcp(7)</li>
	 * 
	 *            <li>pgm, epgm: reliable multicast transport using PGM, see
	 *            zmq_pgm(7)</li>
	 * 
	 *            </ul>
	 * 
	 * @see #connect(String)
	 * 
	 */
	public void bind(final String endpoint) {
		check(zmqlib.zmq_bind(this.handle, endpoint));
	}

	/**
	 * Close a socket. The zmq_close() function shall destroy the socket
	 * referenced by the socket argument. Any outstanding messages physically
	 * received from the network but not yet received by the application with
	 * zmq_recv() shall be discarded. The behaviour for discarding messages sent
	 * by the application with zmq_send() but not yet physically transferred to
	 * the network depends on the value of the ZMQ_LINGER socket option for the
	 * specified socket.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Note. The default setting of ZMQ_LINGER does not discard unsent messages;
	 * this behaviour may cause the application to block when calling
	 * zmq_term(). For details refer to zmq_setsockopt(3) and zmq_term(3).
	 * 
	 */
	public void close() {
		check(zmqlib.zmq_close(this.handle));
	}

	/**
	 * Connect a socket. The zmq_connect() function shall connect the socket
	 * referenced by the socket argument to the endpoint specified by the
	 * endpoint argument.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * With the exception of ZMQ_PAIR sockets, a single socket may be connected
	 * to multiple endpoints using zmq_connect(), while simultaneously accepting
	 * incoming connections from multiple endpoints bound to the socket using
	 * zmq_bind(). Refer to zmq_socket(3) for a description of the exact
	 * semantics involved when connecting or binding a socket to multiple
	 * endpoints.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Note. The connection will not be performed immediately but as needed by
	 * 0MQ. Thus a successful invocation of zmq_connect() does not indicate that
	 * a physical connection was or can actually be established.
	 * 
	 * @param endpoint
	 * 
	 *            The endpoint argument is a string consisting of two parts as
	 *            follows: transport://address. The transport part specifies the
	 *            underlying transport protocol to use. The meaning of the
	 *            address part is specific to the underlying transport protocol
	 *            selected.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 *            The following transports are defined:
	 * 
	 *            <ul>
	 * 
	 *            <li>inproc: local in-process (inter-thread) communication
	 *            transport, see zmq_inproc(7)</li>
	 * 
	 *            <li>ipc: local inter-process communication transport, see
	 *            zmq_ipc(7)</li>
	 * 
	 *            <li>tcp: unicast transport using TCP, see zmq_tcp(7)</li>
	 * 
	 *            <li>pgm, epgm: reliable multicast transport using PGM, see
	 *            zmq_pgm(7)</li>
	 * 
	 *            </ul>
	 * 
	 * @see #bind(String)
	 * 
	 */
	public void connect(final String endpoint) {
		check(zmqlib.zmq_connect(this.handle, endpoint));
	}

	/**
	 * Return the I/O thread affinity. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_AFFINITY option shall set the I/O thread affinity for newly
	 * created connections on the specified socket.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Affinity determines which threads from the 0MQ I/O thread pool associated
	 * with the socket's context shall handle newly created connections. A value
	 * of zero specifies no affinity, meaning that work shall be distributed
	 * fairly among all 0MQ I/O threads in the thread pool. For non-zero values,
	 * the lowest bit corresponds to thread 1, second lowest bit to thread 2 and
	 * so on. For example, a value of 3 specifies that subsequent connections on
	 * socket shall be handled exclusively by I/O threads 1 and 2.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * See also zmq_init(3) for details on allocating the number of I/O threads
	 * for a specific context. *
	 * 
	 * @return the I/O thread affinity.
	 * 
	 * @see Zmq#getContext(int)
	 * 
	 */
	public long getAffinity() {
		return getOptionLong(Option.AFFINITY);
	}

	/**
	 * Set the maximum length of the queue of outstanding connections. Default:
	 * 100.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_BACKLOG option shall set the maximum length of the queue of
	 * outstanding peer connections for the specified socket; this only applies
	 * to connection-oriented transports. For details refer to your operating
	 * system documentation for the listen function.
	 * 
	 * @return the maximum length of the queue of outstanding connections
	 */
	public int getBacklog() {
		return getOptionInt(Option.BACKLOG);
	}

	/**
	 * Return the high water mark in number of messages. The default value of 0
	 * indicates no limit.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_HWM option shall set the high water mark for the specified
	 * socket. The high water mark is a hard limit on the maximum number of
	 * outstanding messages 0MQ shall queue in memory for any single peer that
	 * the specified socket is communicating with.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * If this limit has been reached the socket shall enter an exceptional
	 * state and depending on the socket type, 0MQ shall take appropriate action
	 * such as blocking or dropping sent messages. Refer to the individual
	 * socket descriptions in zmq_socket(3) for details on the exact action
	 * taken for each socket type.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The default ZMQ_HWM value of zero means "no limit".
	 * 
	 * @return the high water mark in number of messages
	 */
	public long getHWM() {
		return getOptionLong(Option.HWM);
	}

	/**
	 * Return the socket identifier. Default: null.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Socket identity determines if existing 0MQ infrastructure (message
	 * queues, forwarding devices) shall be identified with a specific
	 * application and persist across multiple runs of the application.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * If the socket has no identity, each run of an application is completely
	 * separate from other runs. However, with identity set the socket shall
	 * re-use any existing 0MQ infrastructure configured by the previous run(s).
	 * Thus the application may receive messages that were sent in the meantime,
	 * message queue limits shall be shared with previous run(s) and so on.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Identity should be at least one byte and at most 255 bytes long.
	 * Identities starting with binary zero are reserved for use by 0MQ
	 * infrastructure.
	 * 
	 * @return the socket identifier
	 */
	public byte[] getIdentity() {
		return getOptionByteArray(Option.IDENTITY, 1024);
	}

	/**
	 * Set the linger period for socket shutdown in milliseconds. Default -1
	 * (infinite).
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_LINGER option shall set the linger period for the specified
	 * socket. The linger period determines how long pending messages which have
	 * yet to be sent to a peer shall linger in memory after a socket is closed
	 * with zmq_close(3), and further affects the termination of the socket's
	 * context with zmq_term(3). The following outlines the different
	 * behaviours:
	 * 
	 * <ul>
	 * <li>The default value of -1 specifies an infinite linger period. Pending
	 * messages shall not be discarded after a call to zmq_close(); attempting
	 * to terminate the socket's context with zmq_term() shall block until all
	 * pending messages have been sent to a peer.</li>
	 * 
	 * <li>The value of 0 specifies no linger period. Pending messages shall be
	 * discarded immediately when the socket is closed with zmq_close().</li>
	 * 
	 * <li>Positive values specify an upper bound for the linger period in
	 * milliseconds. Pending messages shall not be discarded after a call to
	 * zmq_close(); attempting to terminate the socket's context with zmq_term()
	 * shall block until either all pending messages have been sent to a peer,
	 * or the linger period expires, after which any pending messages shall be
	 * discarded.</li>
	 * </ul>
	 * 
	 * @return linger period in milliseconds
	 * 
	 */
	public int getLinger() {
		return getOptionInt(Option.LINGER);
	}

	/**
	 * Return the multicast loop-back.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_MCAST_LOOP option shall control whether data sent via multicast
	 * transports using the specified socket can also be received by the sending
	 * host via loop-back. A value of zero disables the loop-back functionality,
	 * while the default value of 1 enables the loop-back functionality. Leaving
	 * multicast loop-back enabled when it is not required can have a negative
	 * impact on performance. Where possible, disable ZMQ_MCAST_LOOP in
	 * production environments.
	 * 
	 * @return the multicast loop back
	 */
	public long getMcastLoop() {
		return getOptionLong(Option.MCAST_LOOP);
	}

	/**
	 * Return the multicast data rate in kilobits per second. Default: 100.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RATE option shall set the maximum send or receive data rate for
	 * multicast transports such as zmq_pgm(7) using the specified socket.
	 * 
	 * @return the multicast data rate in kilobits per second
	 */
	public long getRate() {
		return getOptionLong(Option.RATE);
	}

	/**
	 * Return the kernel receive buffer size in bytes. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RCVBUF option shall set the underlying kernel receive buffer size
	 * for the socket to the specified size in bytes. A value of zero means
	 * leave the OS default unchanged. For details refer to your operating
	 * system documentation for the SO_RCVBUF socket option.
	 * 
	 * @return the kernel receive buffer size in bytes
	 * 
	 */
	public long getReceiveBuffer() {
		return getOptionLong(Option.RCVBUF);
	}

	/**
	 * Return the reconnection interval in milliseconds. Default: 100.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RECONNECT_IVL option shall set the initial reconnection interval
	 * for the specified socket. The reconnection interval is the period 0MQ
	 * shall wait between attempts to reconnect disconnected peers when using
	 * connection-oriented transports.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Note. The reconnection interval may be randomized by 0MQ to prevent
	 * reconnection storms in topologies with a large number of peers per
	 * socket.
	 * 
	 * @return the reconnection interval in milliseconds
	 */
	public int getReconnectInterval() {
		return getOptionInt(Option.RECONNECT_IVL);
	}

	/**
	 * Return the maximum reconnection interval in milliseconds. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RECONNECT_IVL_MAX option shall set the maximum reconnection
	 * interval for the specified socket. This is the maximum period 0MQ shall
	 * wait between attempts to reconnect. On each reconnect attempt, the
	 * previous interval shall be doubled untill ZMQ_RECONNECT_IVL_MAX is
	 * reached. This allows for exponential backoff strategy. Default value
	 * means no exponential backoff is performed and reconnect interval
	 * calculations are only based on ZMQ_RECONNECT_IVL.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Note. Values less than ZMQ_RECONNECT_IVL will be ignored.
	 * 
	 * @return the maximum reconnection interval in milliseconds
	 */
	public int getReconnectIntervalMax() {
		return getOptionInt(Option.RECONNECT_IVL_MAX);
	}

	/**
	 * Return the multicast recovery interval in seconds. Default: 10.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RECOVERY_IVL option shall set the recovery interval for multicast
	 * transports using the specified socket. The recovery interval determines
	 * the maximum time in seconds that a receiver can be absent from a
	 * multicast group before unrecoverable data loss will occur.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Caution. Exercise care when setting large recovery intervals as the data
	 * needed for recovery will be held in memory. For example, a 1 minute
	 * recovery interval at a data rate of 1Gbps requires a 7GB in-memory
	 * buffer.
	 * 
	 * @return the multicast recovery interval in seconds
	 */
	public long getRecoveryInterval() {
		return getOptionLong(Option.RECOVERY_IVL);
	}

	/**
	 * Return the multicast recovery interval in milliseconds. Default: -1.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RECOVERY_IVL_MSEC option shall set the recovery interval,
	 * specified in milliseconds (ms) for multicast transports using the
	 * specified socket. The recovery interval determines the maximum time in
	 * milliseconds that a receiver can be absent from a multicast group before
	 * unrecoverable data loss will occur.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * A non-zero value of the ZMQ_RECOVERY_IVL_MSEC option will take precedence
	 * over the ZMQ_RECOVERY_IVL option, but since the default for the
	 * ZMQ_RECOVERY_IVL_MSEC is -1, the default is to use the ZMQ_RECOVERY_IVL
	 * option value.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Caution. Exercise care when setting large recovery intervals as the data
	 * needed for recovery will be held in memory. For example, a 1 minute
	 * recovery interval at a data rate of 1Gbps requires a 7GB in-memory
	 * buffer.
	 * 
	 * @return the multicast recovery interval in milliseconds
	 */
	public long getRecoveryIntervalMsec() {
		return getOptionLong(Option.RECOVERY_IVL_MSEC);
	}

	/**
	 * Return the kernel transmit buffer size in bytes. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_SNDBUF option shall set the underlying kernel transmit buffer
	 * size for the socket to the specified size in bytes. A value of zero means
	 * leave the OS default unchanged. For details please refer to your
	 * operating system documentation for the SO_SNDBUF socket option.
	 * 
	 * @return the kernel transmit buffer size in bytes
	 * 
	 */
	public long getSendBuffer() {
		return getOptionLong(Option.SNDBUF);
	}

	/**
	 * Return the disk offload size in bytes. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_SWAP option shall set the disk offload (swap) size for the
	 * specified socket. A socket which has ZMQ_SWAP set to a non-zero value may
	 * exceed it's high water mark; in this case outstanding messages shall be
	 * offloaded to storage on disk rather than held in memory.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The value of ZMQ_SWAP defines the maximum size of the swap space in
	 * bytes.
	 * 
	 * @return the disk offload size in bytes
	 * 
	 * @see #getHWM()
	 * 
	 */
	public long getSwap() {
		return getOptionLong(Option.SWAP);
	}

	/**
	 * Return the socket type as set when creating the socket.
	 * 
	 * @return the socket type
	 * 
	 * @see ZmqContext#getSocket(ZmqSocket.Type)
	 * 
	 */
	public ZmqSocket.Type getType() {
		return Type.findByCode((int) getOptionLong(Option.TYPE));
	}

	/**
	 * Returns more message parts to follow.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RCVMORE option shall return a boolean value indicating if the
	 * multi-part message currently being read from the specified socket has
	 * more message parts to follow. If there are no message parts to follow or
	 * if the message currently being read is not a multi-part message a value
	 * of zero shall be returned. Otherwise, a value of 1 shall be returned.
	 * 
	 * @return more message parts to follow.
	 */
	public boolean isReceiveMore() {
		return getOptionLong(Option.RCVMORE) != 0;
	}

	/**
	 * Receive a message from a socket. The zmq_recv() function shall receive a
	 * message from the socket referenced by the socket argument and store it in
	 * the message referenced by the msg argument. Any content previously stored
	 * in msg shall be properly deallocated. If there are no messages available
	 * on the specified socket the zmq_recv() function shall block until the
	 * request can be satisfied.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Multi-part messages. A 0MQ message is composed of 1 or more message
	 * parts; each message part is an independent zmq_msg_t in its own right.
	 * 0MQ ensures atomic delivery of messages; peers shall receive either all
	 * message parts of a message or none at all.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The total number of message parts is unlimited.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * An application wishing to determine if a message is composed of multiple
	 * parts does so by retrieving the value of the ZMQ_RCVMORE socket option on
	 * the socket it is receiving the message from. If there are no message
	 * parts to follow, or if the message is not composed of multiple parts,
	 * ZMQ_RCVMORE shall report a value of zero. Otherwise, ZMQ_RCVMORE shall
	 * report a value of 1, indicating that more message parts are to follow.
	 * 
	 * 
	 * @param flags
	 * 
	 *            The optional flags argument is a combination of the flags
	 *            defined below:
	 * 
	 *            <ul>
	 * 
	 *            <li>ZMQ_NOBLOCK Specifies that the operation should be
	 *            performed in non-blocking mode. If there are no messages
	 *            available on the specified socket, the zmq_recv() function
	 *            shall fail with errno set to EAGAIN.</li>
	 * 
	 *            </ul>
	 * 
	 * 
	 * @return the message data
	 * 
	 * @see #send(byte[], SendFlag...)
	 * @see #isReceiveMore()
	 * 
	 * 
	 */
	public byte[] recv(final RecvFlag... flags) {

		int flag = 0;
		for (final RecvFlag opt : flags)
			flag += opt.code;

		final zmq_msg_t msg = new zmq_msg_t();

		try {

			check(zmqlib.zmq_msg_init(msg));
			check(zmqlib.zmq_recv(this.handle, msg, flag));

			final int size = zmqlib.zmq_msg_size(msg).intValue();
			final Pointer buffer = zmqlib.zmq_msg_data(msg);

			final byte[] data = new byte[size];
			buffer.read(0, data, 0, size);
			return data;

		} finally {
			check(zmqlib.zmq_msg_close(msg));
		}

	}

	/**
	 * Remove a message filter.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_UNSUBSCRIBE option shall remove an existing message filter on a
	 * ZMQ_SUB socket. The filter specified must match an existing filter
	 * previously established with the ZMQ_SUBSCRIBE option. If the socket has
	 * several instances of the same filter attached the ZMQ_UNSUBSCRIBE option
	 * shall remove only one instance, leaving the rest in place and functional.
	 * 
	 * @param filter
	 *            the message filter
	 * 
	 * @see #addSubscription(byte[])
	 * 
	 */
	public void removeSubscription(final byte[] filter) {
		setOption(Option.UNSUBSCRIBE, filter);
	}

	/**
	 * Send a message on a socket. The zmq_send() function shall queue the
	 * message referenced by the msg argument to be sent to the socket
	 * referenced by the socket argument.
	 * 
	 * The zmq_msg_t structure passed to zmq_send() is nullified during the
	 * call. If you want to send the same message to multiple sockets you have
	 * to copy it using (e.g. using zmq_msg_copy()).
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Note. A successful invocation of zmq_send() does not indicate that the
	 * message has been transmitted to the network, only that it has been queued
	 * on the socket and 0MQ has assumed responsibility for the message.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Multi-part messages. A 0MQ message is composed of 1 or more message
	 * parts; each message part is an independent zmq_msg_t in its own right.
	 * 0MQ ensures atomic delivery of messages; peers shall receive either all
	 * message parts of a message or none at all.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The total number of message parts is unlimited.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * An application wishing to send a multi-part message does so by specifying
	 * the ZMQ_SNDMORE flag to zmq_send(). The presence of this flag indicates
	 * to 0MQ that the message being sent is a multi-part message and that more
	 * message parts are to follow. When the application wishes to send the
	 * final message part it does so by calling zmq_send() without the
	 * ZMQ_SNDMORE flag; this indicates that no more message parts are to
	 * follow.
	 * 
	 * @param data
	 *            data to be sent
	 * @param flags
	 *            The optional flags argument is a combination of the flags
	 *            defined below:
	 * 
	 *            <ul>
	 * 
	 *            <li>ZMQ_NOBLOCK Specifies that the operation should be
	 *            performed in non-blocking mode. If the message cannot be
	 *            queued on the socket, the zmq_send() function shall fail with
	 *            errno set to EAGAIN.</li>
	 * 
	 *            <li>ZMQ_SNDMORE Specifies that the message being sent is a
	 *            multi-part message, and that further message parts are to
	 *            follow. Refer to the section regarding multi-part messages
	 *            below for a detailed description.</li>
	 * 
	 *            </ul>
	 * 
	 * @see #recv(RecvFlag...)
	 * 
	 */
	public void send(final byte[] data, final SendFlag... flags) {

		int flag = 0;
		for (final SendFlag opt : flags)
			flag += opt.code;

		final Memory m = new Memory(data.length);
		m.write(0, data, 0, data.length);

		final zmq_msg_t msg = new zmq_msg_t();
		try {
			check(zmqlib.zmq_msg_init_data(msg, m, new NativeLong(data.length), null, null));
			check(zmqlib.zmq_send(this.handle, msg, flag));
		} finally {
			check(zmqlib.zmq_msg_close(msg));
		}

	}

	/**
	 * Set the I/O thread affinity. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_AFFINITY option shall set the I/O thread affinity for newly
	 * created connections on the specified socket.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Affinity determines which threads from the 0MQ I/O thread pool associated
	 * with the socket's context shall handle newly created connections. A value
	 * of zero specifies no affinity, meaning that work shall be distributed
	 * fairly among all 0MQ I/O threads in the thread pool. For non-zero values,
	 * the lowest bit corresponds to thread 1, second lowest bit to thread 2 and
	 * so on. For example, a value of 3 specifies that subsequent connections on
	 * socket shall be handled exclusively by I/O threads 1 and 2.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * See also zmq_init(3) for details on allocating the number of I/O threads
	 * for a specific context. *
	 * 
	 * @param value
	 *            I/O thread affinity.
	 * 
	 * @see Zmq#getContext(int)
	 * 
	 */
	public void setAffinity(final long value) {
		setOption(Option.AFFINITY, value);
	}

	/**
	 * Set the maximum length of the queue of outstanding connections. Default:
	 * 100.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_BACKLOG option shall set the maximum length of the queue of
	 * outstanding peer connections for the specified socket; this only applies
	 * to connection-oriented transports. For details refer to your operating
	 * system documentation for the listen function.
	 * 
	 * @param value
	 *            the maximum length of the queue of outstanding connections
	 */
	public void setBacklog(final int value) {
		setOption(Option.BACKLOG, value);
	}

	/**
	 * Set the high water mark in number of messages. The default value of 0
	 * indicates no limit.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_HWM option shall set the high water mark for the specified
	 * socket. The high water mark is a hard limit on the maximum number of
	 * outstanding messages 0MQ shall queue in memory for any single peer that
	 * the specified socket is communicating with.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * If this limit has been reached the socket shall enter an exceptional
	 * state and depending on the socket type, 0MQ shall take appropriate action
	 * such as blocking or dropping sent messages. Refer to the individual
	 * socket descriptions in zmq_socket(3) for details on the exact action
	 * taken for each socket type.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The default ZMQ_HWM value of zero means "no limit".
	 * 
	 * @param value
	 *            high water mark in number of messages
	 */
	public void setHWM(final long value) {
		setOption(Option.HWM, value);
	}

	/**
	 * Set the socket identity. Default: null.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_IDENTITY option shall set the identity of the specified socket.
	 * Socket identity determines if existing 0MQ infrastructure (message
	 * queues, forwarding devices) shall be identified with a specific
	 * application and persist across multiple runs of the application.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * If the socket has no identity, each run of an application is completely
	 * separate from other runs. However, with identity set the socket shall
	 * re-use any existing 0MQ infrastructure configured by the previous run(s).
	 * Thus the application may receive messages that were sent in the meantime,
	 * message queue limits shall be shared with previous run(s) and so on.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Identity should be at least one byte and at most 255 bytes long.
	 * Identities starting with binary zero are reserved for use by 0MQ
	 * infrastructure.
	 * 
	 * @param id
	 *            socket identifier between 1 and 255 bytes long
	 */
	public void setIdentity(final byte[] id) {
		if (id == null || id.length == 0 || id.length > 255)
			throw new IllegalArgumentException();
		setOption(Option.IDENTITY, id);
	}

	/**
	 * Set the linger period for socket shutdown in milliseconds. Default -1
	 * (infinite).
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_LINGER option shall set the linger period for the specified
	 * socket. The linger period determines how long pending messages which have
	 * yet to be sent to a peer shall linger in memory after a socket is closed
	 * with zmq_close(3), and further affects the termination of the socket's
	 * context with zmq_term(3). The following outlines the different
	 * behaviours:
	 * 
	 * <ul>
	 * <li>The default value of -1 specifies an infinite linger period. Pending
	 * messages shall not be discarded after a call to zmq_close(); attempting
	 * to terminate the socket's context with zmq_term() shall block until all
	 * pending messages have been sent to a peer.</li>
	 * 
	 * <li>The value of 0 specifies no linger period. Pending messages shall be
	 * discarded immediately when the socket is closed with zmq_close().</li>
	 * 
	 * <li>Positive values specify an upper bound for the linger period in
	 * milliseconds. Pending messages shall not be discarded after a call to
	 * zmq_close(); attempting to terminate the socket's context with zmq_term()
	 * shall block until either all pending messages have been sent to a peer,
	 * or the linger period expires, after which any pending messages shall be
	 * discarded.</li>
	 * </ul>
	 * 
	 * @param value
	 *            linger period in milliseconds
	 */
	public void setLinger(final int value) {
		setOption(Option.LINGER, value);
	}

	/**
	 * Control multicast loop-back.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_MCAST_LOOP option shall control whether data sent via multicast
	 * transports using the specified socket can also be received by the sending
	 * host via loop-back. A value of zero disables the loop-back functionality,
	 * while the default value of 1 enables the loop-back functionality. Leaving
	 * multicast loop-back enabled when it is not required can have a negative
	 * impact on performance. Where possible, disable ZMQ_MCAST_LOOP in
	 * production environments.
	 * 
	 * @param value
	 *            the multicast loop-back
	 */
	public void setMcastLoop(final long value) {
		setOption(Option.MCAST_LOOP, value);
	}

	/**
	 * Set multicast data rate in kilobits per second. Default: 100.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RATE option shall set the maximum send or receive data rate for
	 * multicast transports such as zmq_pgm(7) using the specified socket.
	 * 
	 * @param value
	 *            multicast data rate in kilobits per second
	 */
	public void setRate(final long value) {
		setOption(Option.RATE, value);
	}

	/**
	 * Set the kernel receive buffer size in bytes. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RCVBUF option shall set the underlying kernel receive buffer size
	 * for the socket to the specified size in bytes. A value of zero means
	 * leave the OS default unchanged. For details refer to your operating
	 * system documentation for the SO_RCVBUF socket option.
	 * 
	 * @param value
	 *            the kernel receive buffer size in bytes
	 */
	public void setReceiveBuffer(final long value) {
		setOption(Option.RCVBUF, value);
	}

	/**
	 * Set the reconnection interval in milliseconds. Default: 100.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RECONNECT_IVL option shall set the initial reconnection interval
	 * for the specified socket. The reconnection interval is the period 0MQ
	 * shall wait between attempts to reconnect disconnected peers when using
	 * connection-oriented transports.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Note. The reconnection interval may be randomized by 0MQ to prevent
	 * reconnection storms in topologies with a large number of peers per
	 * socket.
	 * 
	 * @param value
	 *            the reconnection interval in milliseconds
	 */
	public void setReconnectInterval(final int value) {
		setOption(Option.RECONNECT_IVL, value);
	}

	/**
	 * Set the maximum reconnection interval in milliseconds. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RECONNECT_IVL_MAX option shall set the maximum reconnection
	 * interval for the specified socket. This is the maximum period 0MQ shall
	 * wait between attempts to reconnect. On each reconnect attempt, the
	 * previous interval shall be doubled untill ZMQ_RECONNECT_IVL_MAX is
	 * reached. This allows for exponential backoff strategy. Default value
	 * means no exponential backoff is performed and reconnect interval
	 * calculations are only based on ZMQ_RECONNECT_IVL.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Note. Values less than ZMQ_RECONNECT_IVL will be ignored.
	 * 
	 * @param value
	 *            the maximum reconnection interval in milliseconds
	 * 
	 */
	public void setReconnectIntervalMax(final int value) {
		setOption(Option.RECONNECT_IVL_MAX, value);
	}

	/**
	 * Set the multicast recovery interval in seconds. Default: 10.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RECOVERY_IVL option shall set the recovery interval for multicast
	 * transports using the specified socket. The recovery interval determines
	 * the maximum time in seconds that a receiver can be absent from a
	 * multicast group before unrecoverable data loss will occur.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Caution. Exercise care when setting large recovery intervals as the data
	 * needed for recovery will be held in memory. For example, a 1 minute
	 * recovery interval at a data rate of 1Gbps requires a 7GB in-memory
	 * buffer.
	 * 
	 * @param value
	 *            the multicast recovery interval in seconds
	 */
	public void setRecoveryInterval(final long value) {
		setOption(Option.RECOVERY_IVL, value);
	}

	/**
	 * Set the multicast recovery interval in milliseconds. Default: -1.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_RECOVERY_IVL_MSEC option shall set the recovery interval,
	 * specified in milliseconds (ms) for multicast transports using the
	 * specified socket. The recovery interval determines the maximum time in
	 * milliseconds that a receiver can be absent from a multicast group before
	 * unrecoverable data loss will occur.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * A non-zero value of the ZMQ_RECOVERY_IVL_MSEC option will take precedence
	 * over the ZMQ_RECOVERY_IVL option, but since the default for the
	 * ZMQ_RECOVERY_IVL_MSEC is -1, the default is to use the ZMQ_RECOVERY_IVL
	 * option value.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * Caution. Exercise care when setting large recovery intervals as the data
	 * needed for recovery will be held in memory. For example, a 1 minute
	 * recovery interval at a data rate of 1Gbps requires a 7GB in-memory
	 * buffer.
	 * 
	 * @param value
	 *            the multicast recovery interval in milliseconds
	 */
	public void setRecoveryIntervalMsec(final long value) {
		setOption(Option.RECOVERY_IVL_MSEC, value);
	}

	/**
	 * Set the kernel transmit buffer size in bytes. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_SNDBUF option shall set the underlying kernel transmit buffer
	 * size for the socket to the specified size in bytes. A value of zero means
	 * leave the OS default unchanged. For details please refer to your
	 * operating system documentation for the SO_SNDBUF socket option.
	 * 
	 * @param value
	 *            the kernel transmit buffer size in bytes
	 */
	public void setSendBuffer(final long value) {
		setOption(Option.SNDBUF, value);
	}

	/**
	 * Set the disk offload size in bytes. Default: 0.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The ZMQ_SWAP option shall set the disk offload (swap) size for the
	 * specified socket. A socket which has ZMQ_SWAP set to a non-zero value may
	 * exceed it's high water mark; in this case outstanding messages shall be
	 * offloaded to storage on disk rather than held in memory.
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * The value of ZMQ_SWAP defines the maximum size of the swap space in
	 * bytes.
	 * 
	 * 
	 * @param value
	 *            the disk offload size in bytes
	 * 
	 * @see #setHWM(long)
	 * 
	 */
	public void setSwap(final long value) {
		setOption(Option.SWAP, value);
	}

	private void check(final int rc) {
		if (rc != 0) {
			final int err = zmqlib.zmq_errno();
			throw new ZmqException(zmqlib.zmq_strerror(err), err);
		}
	}

	private byte[] getOptionByteArray(final Option opt, final int capacity) {
		final Memory data = new Memory(capacity);
		data.clear(capacity);
		final LongByReference size = new LongByReference(capacity);
		check(zmqlib.zmq_getsockopt(this.handle, opt.code, data, size));
		return data.getByteArray(0, (int) size.getValue());
	}

	private int getOptionInt(final Option opt) {
		final int capacity = 4;
		final Memory data = new Memory(capacity);
		data.clear(capacity);
		final LongByReference size = new LongByReference(capacity);
		check(zmqlib.zmq_getsockopt(this.handle, opt.code, data, size));
		return data.getInt(0);
	}

	private long getOptionLong(final Option opt) {
		final int capacity = 8;
		final Memory data = new Memory(capacity);
		data.clear(capacity);
		final LongByReference size = new LongByReference(capacity);
		check(zmqlib.zmq_getsockopt(this.handle, opt.code, data, size));
		return data.getLong(0);
	}

	private void setOption(final Option opt, final byte[] value) {
		final Memory data = new Memory(value.length);
		data.write(0, value, 0, value.length);
		check(zmqlib.zmq_setsockopt(this.handle, opt.code, data, new NativeLong(data.getSize())));
	}

	private void setOption(final Option opt, final int value) {
		final Memory data = new Memory(4);
		data.setInt(0, value);
		check(zmqlib.zmq_setsockopt(this.handle, opt.code, data, new NativeLong(data.getSize())));
	}

	private void setOption(final Option opt, final long value) {
		final Memory data = new Memory(8);
		data.setLong(0, value);
		check(zmqlib.zmq_setsockopt(this.handle, opt.code, data, new NativeLong(data.getSize())));
	}

}