package com.javaprophet.unixsocket;

import java.io.IOException;

public class Test {
	public static void main(String[] args) {
		CLib.INSTANCE.umask(077);
		Thread server = new Thread() {
			public void run() {
				try {
					UnixServerSocket server = new UnixServerSocket("/etc/testsock2");
					server.bind();
					System.out.println("Server listening!");
					UnixSocket sock = server.accept();
					System.out.println("Server received client!");
					UnixOutputStream out = sock.getOutputStream();
					out.flush();
					UnixInputStream in = sock.getInputStream();
					out.write("i am the server. - server".getBytes());
					out.flush();
					byte[] test = new byte[1024];
					int i = in.read(test);
					System.out.println("Server received " + i + " bytes!");
					System.out.println(new String(test, 0, i));
					sock.close();
					server.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		server.start();
		try {
			Thread.sleep(1000L);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			UnixSocket sock = new UnixSocket("/etc/testsock2");
			sock.connect();
			System.out.println("Client connected!");
			UnixOutputStream out = sock.getOutputStream();
			out.flush();
			UnixInputStream in = sock.getInputStream();
			out.write("i am the client. - client".getBytes());
			out.flush();
			System.out.println("Client wrote!");
			try {
				Thread.sleep(100L);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(in.available() + " available at client!");
			byte[] test = new byte[1024];
			int i = in.read(test);
			System.out.println("Client read " + i + " bytes!");
			System.out.println(new String(test, 0, i));
			sock.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		// NativeLibrary.addSearchPath("socket", "/usr/include/sys/");
		// File f = new File("/etc/socktest1");
		// f.delete();
		// int sfd = SocketLib.INSTANCE.socket(1, 1, 0);
		// System.out.println(sfd);
		// SocketLib.sockaddr_un local = new SocketLib.sockaddr_un();
		// local.sunfamily = 1;
		// local.sunpath = "/etc/socktest1".getBytes();
		// int bind = SocketLib.INSTANCE.bind(sfd, local, local.sunpath.length + 2);
		// System.out.println(bind);
		// int listen = SocketLib.INSTANCE.listen(sfd, 50);
		// System.out.println(listen);
		// SocketLib.sockaddr_un remote = new SocketLib.sockaddr_un();
		// IntByReference ibr = new IntByReference(110);
		// System.out.println("running accept");
		// int accept = SocketLib.INSTANCE.accept(sfd, remote, ibr);
		// System.out.println(accept);
	}
}
