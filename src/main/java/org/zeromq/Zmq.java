/*
    Copyright (c) 2007-2011 iMatix Corporation
    Copyright (c) 2007-2011 Other contributors as noted in the AUTHORS file

    This file is part of 0MQ.

    0MQ is free software; you can redistribute it and/or modify it under
    the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    0MQ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.zeromq;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;

import com.sun.jna.ptr.LongByReference;

/**
 * 
 * ZeroMQ JNA direct mapping, taken directly from zmq.h.
 * 
 * Special thanks to Tim Clark whose <a href="http://www.lshift.net/blog/2010/10/30/jna-wrapper-for-zmq">blog post</a>
 * provided the catalyst for this wrapper.
 * 
 * @author Karl Ostendorf <karl@ostendorf.com>
 *
 */
public class Zmq {

	static {
		Native.register("zmq");
	}

	/******************************************************************************/
	/*  0MQ versioning support.                                                   */
	/******************************************************************************/

	/*  Version macros for compile-time API version detection                     */
	public static final int ZMQ_VERSION_MAJOR;
	public static final int ZMQ_VERSION_MINOR;
	public static final int ZMQ_VERSION_PATCH;
	public static final int ZMQ_VERSION;

	public static int ZMQ_MAKE_VERSION(final int major, final int minor, final int patch) {
		return (major * 10000) + (minor * 100) + patch;
	}

	/*  Run-time API version detection                                            */
	public static native void zmq_version(int[] major, int[] minor, int[] patch);

	static {
		final int[] major = new int[1];
		final int[] minor = new int[1];
		final int[] patch = new int[1];
		zmq_version(major, minor, patch);
		ZMQ_VERSION_MAJOR = major[0];
		ZMQ_VERSION_MINOR = minor[0];
		ZMQ_VERSION_PATCH = patch[0];
		ZMQ_VERSION = ZMQ_MAKE_VERSION(ZMQ_VERSION_MAJOR, ZMQ_VERSION_MINOR, ZMQ_VERSION_PATCH);
	}

	/******************************************************************************/
	/*  0MQ errors.                                                               */
	/******************************************************************************/

	/*  A number random enough not to collide with different errno ranges on      */
	/*  different OSes. The assumption is that error_t is at least 32-bit type.   */
	public static final int ZMQ_HAUSNUMERO = 156384712;

	/*  On Windows platform some of the standard POSIX errnos are not defined.    */
	public static final int ENOTSUP = (ZMQ_HAUSNUMERO + 1);
	public static final int EPROTONOSUPPORT = (ZMQ_HAUSNUMERO + 2);
	public static final int ENOBUFS = (ZMQ_HAUSNUMERO + 3);
	public static final int ENETDOWN = (ZMQ_HAUSNUMERO + 4);
	public static final int EADDRINUSE = (ZMQ_HAUSNUMERO + 5);
	public static final int EADDRNOTAVAIL = (ZMQ_HAUSNUMERO + 6);
	public static final int ECONNREFUSED = (ZMQ_HAUSNUMERO + 7);
	public static final int EINPROGRESS = (ZMQ_HAUSNUMERO + 8);

	/*  Native 0MQ error codes.                                                   */
	public static final int EFSM = (ZMQ_HAUSNUMERO + 51);
	public static final int ENOCOMPATPROTO = (ZMQ_HAUSNUMERO + 52);
	public static final int ETERM = (ZMQ_HAUSNUMERO + 53);
	public static final int EMTHREAD = (ZMQ_HAUSNUMERO + 54);

	/*  This function retrieves the errno as it is known to 0MQ library. The goal */
	/*  of this function is to make the code 100% portable, including where 0MQ   */
	/*  compiled with certain CRT library (on Windows) is linked to an            */
	/*  application that uses different CRT library.                              */
	public static native int zmq_errno ();

	/*  Resolves system errors and 0MQ errors to human-readable string.           */
	public static native String zmq_strerror (int errnum);

	/******************************************************************************/
	/*  0MQ message definition.                                                   */
	/******************************************************************************/

	/*  Maximal size of "Very Small Message". VSMs are passed by value            */
	/*  to avoid excessive memory allocation/deallocation.                        */
	/*  If VMSs larger than 255 bytes are required, type of 'vsm_size'            */
	/*  field in zmq_msg_t structure should be modified accordingly.              */
	public static final int ZMQ_MAX_VSM_SIZE = 30;

	/*  Message types. These integers may be stored in 'content' member of the    */
	/*  message instead of regular pointer to the data.                           */
	public static final int ZMQ_DELIMITER = 31;
	public static final int ZMQ_VSM = 32;

	/*  Message flags. ZMQ_MSG_SHARED is strictly speaking not a message flag     */
	/*  (it has no equivalent in the wire format), however, making  it a flag     */
	/*  allows us to pack the stucture tigher and thus improve performance.       */
	public static final int ZMQ_MSG_MORE = 1;
	public static final int ZMQ_MSG_SHARED = 128;
	public static final int ZMQ_MSG_MASK = 129; /* Merges all the flags */

	/*  A message. Note that 'content' is not a pointer to the raw data.          */
	/*  Rather it is pointer to zmq::msg_content_t structure                      */
	/*  (see src/msg_content.hpp for its definition).                             */
	public static class zmq_msg_t extends Structure {
		public Pointer content;
		public byte flags;
		public byte vsm_size;
		public byte[] vsm_data = new byte[ZMQ_MAX_VSM_SIZE];
		public static class ByReference extends zmq_msg_t implements Structure.ByReference {
		}
		public static class ByValue extends zmq_msg_t implements Structure.ByValue {
		}
	}

	public static class zmq_free_fn extends PointerType {
		public zmq_free_fn() {
		}
		public zmq_free_fn(final Pointer address) {
			super(address);
		}
	}

	public static native int zmq_msg_init (zmq_msg_t msg);
	public static native int zmq_msg_init_size (zmq_msg_t msg, NativeLong size);
	public static native int zmq_msg_init_data (zmq_msg_t msg, Pointer data, NativeLong size, zmq_free_fn ffn, Pointer hint);
	public static native int zmq_msg_close (zmq_msg_t msg);
	public static native int zmq_msg_move (zmq_msg_t dest, zmq_msg_t src);
	public static native int zmq_msg_copy (zmq_msg_t dest, zmq_msg_t src);
	public static native Pointer zmq_msg_data (zmq_msg_t msg);
	public static native NativeLong zmq_msg_size (zmq_msg_t msg);

	/******************************************************************************/
	/*  0MQ infrastructure (a.k.a. context) initialisation & termination.         */
	/******************************************************************************/

	public static native Pointer zmq_init (int io_threads);
	public static native int zmq_term (Pointer context);

	/******************************************************************************/
	/*  0MQ socket definition.                                                    */
	/******************************************************************************/

	/*  Socket types.                                                             */
	public static final int ZMQ_PAIR = 0;
	public static final int ZMQ_PUB = 1;
	public static final int ZMQ_SUB = 2;
	public static final int ZMQ_REQ = 3;
	public static final int ZMQ_REP = 4;
	public static final int ZMQ_DEALER = 5;
	public static final int ZMQ_ROUTER = 6;
	public static final int ZMQ_PULL = 7;
	public static final int ZMQ_PUSH = 8;
	public static final int ZMQ_XPUB = 9;
	public static final int ZMQ_XSUB = 10;
	@Deprecated public static final int ZMQ_XREQ = ZMQ_DEALER;        /*  Old alias, remove in 3.x               */
	@Deprecated public static final int ZMQ_XREP = ZMQ_ROUTER;        /*  Old alias, remove in 3.x               */
	@Deprecated public static final int ZMQ_UPSTREAM = ZMQ_PULL;      /*  Old alias, remove in 3.x               */
	@Deprecated public static final int ZMQ_DOWNSTREAM = ZMQ_PUSH;    /*  Old alias, remove in 3.x               */

	/*  Socket options.                                                           */
	public static final int ZMQ_HWM = 1;
	public static final int ZMQ_SWAP = 3;
	public static final int ZMQ_AFFINITY = 4;
	public static final int ZMQ_IDENTITY = 5;
	public static final int ZMQ_SUBSCRIBE = 6;
	public static final int ZMQ_UNSUBSCRIBE = 7;
	public static final int ZMQ_RATE = 8;
	public static final int ZMQ_RECOVERY_IVL = 9;
	public static final int ZMQ_MCAST_LOOP = 10;
	public static final int ZMQ_SNDBUF = 11;
	public static final int ZMQ_RCVBUF = 12;
	public static final int ZMQ_RCVMORE = 13;
	public static final int ZMQ_FD = 14;
	public static final int ZMQ_EVENTS = 15;
	public static final int ZMQ_TYPE = 16;
	public static final int ZMQ_LINGER = 17;
	public static final int ZMQ_RECONNECT_IVL = 18;
	public static final int ZMQ_BACKLOG = 19;
	public static final int ZMQ_RECOVERY_IVL_MSEC = 20;   /*  opt. recovery time, reconcile in 3.x   */
	public static final int ZMQ_RECONNECT_IVL_MAX = 21;

	/*  Send/recv options.                                                        */
	public static final int ZMQ_NOBLOCK = 1;
	public static final int ZMQ_SNDMORE = 2;

	public static native Pointer zmq_socket (Pointer context, int type);
	public static native int zmq_close (Pointer s);
	public static native int zmq_setsockopt (Pointer s, int option, Pointer optval, NativeLong optvallen);
	public static native int zmq_getsockopt (Pointer s, int option, Pointer optval, LongByReference optvallen);
	public static native int zmq_bind (Pointer s, String addr);
	public static native int zmq_connect (Pointer s, String addr);
	public static native int zmq_send (Pointer s, zmq_msg_t msg, int flags);
	public static native int zmq_recv (Pointer s, zmq_msg_t msg, int flags);

	/******************************************************************************/
	/*  I/O multiplexing.                                                         */
	/******************************************************************************/

	public static final int ZMQ_POLLIN = 1;
	public static final int ZMQ_POLLOUT = 2;
	public static final int ZMQ_POLLERR = 4;

	public static class zmq_pollitem_t extends Structure {
		Pointer socket;
		int fd;
		short events;
		short revents;
	}

	public static native int zmq_poll (zmq_pollitem_t items, int nitems, long timeout);

	/******************************************************************************/
	/*  Built-in devices                                                          */
	/******************************************************************************/

	public static final int ZMQ_STREAMER = 1;
	public static final int ZMQ_FORWARDER = 2;
	public static final int ZMQ_QUEUE = 3;

	public static native int zmq_device (int device, Pointer insocket, Pointer outsocket);

	private Zmq() {}

}
