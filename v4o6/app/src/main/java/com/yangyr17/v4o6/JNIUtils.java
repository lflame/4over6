package com.yangyr17.v4o6;

public class JNIUtils {
    // ipv6: The ipv6 address of the server.
    // ipFifoPath: The path of fifo for obtaining ipv4 address, route and dns.
    // statFifoPath: The path of fifo for obtaining statistics information.
    static public native String startBackend(String ipv6, int port, String ipFifoPath, String tunFifoPath, String statFifoPath, String debugFifoPath, String FBFifoPath);
    static {
        System.loadLibrary("hellojni");
    }
}