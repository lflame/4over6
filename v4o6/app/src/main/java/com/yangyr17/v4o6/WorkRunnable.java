package com.yangyr17.v4o6;

import android.content.Context;
import android.os.Looper;
import android.os.Message;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.LogRecord;

public class WorkRunnable implements Runnable {
    private class Msg {
        int length;
        byte type;
        String data;
    };

    WorkRunnable(WorkHandler handler, String ipFifoPath) {
        super();
        this.handler = handler;
        this.ipFifoPath = ipFifoPath;
    }

    int byteToInt(byte[] buf) {
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        DataInputStream dis = new DataInputStream(bais);
        try {
            return dis.readInt();
        } catch (IOException e) {
            Log.e("byteToInt", e.toString());
            return -1;
        }
    }

    boolean readMsg(File fifo, byte[] buf, int len, Msg ret) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fifo);
            BufferedInputStream in = new BufferedInputStream(fileInputStream);
            // length
            int readLen = in.read(buf, 0, 4);
            if (readLen < 4) {
                Log.e("readMsg", "读入 length 失败");
                return false;
            }
            ret.length = byteToInt(buf);
            // type
            readLen = in.read(buf, 0, 1);
            if (readLen < 1) {
                Log.e("readMsg", "读入 type 失败");
                return false;
            }
            ret.type = buf[0];
            // data
            int expectLen = ret.length - 5;
            if (expectLen > 0) {
                readLen = in.read(buf, 0, expectLen);
                if (readLen < expectLen) {
                    Log.e("readMsg", "读入 data 失败");
                    return false;
                }
                byte[] tmp = new byte[expectLen];
                tmp = buf;
                ret.data = new String(tmp);
            }
            in.close();
            Log.i("fifo", "Suc to read, len: " + ret.length + ", type: "
                    + ret.type + ", data: " + ret.data);
            return true;
        } catch (FileNotFoundException e) {
            Log.e("fifo", "FileNotFoundException");
        } catch (IOException e) {
            Log.e("fifo", "IOException");
        } catch (Exception e) {
            Log.e("fifo", e.toString());
        }
        return false;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            // 暂停 1s
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.w("WorkThread", "Interrupted Exception while sleeping.");
            }
            // 读 ip 管道
            File ipFifoFile = new File(ipFifoPath);
            if (!ipFifoFile.exists()) {
                Log.i("worker", "ip 管道暂不存在");
            } else {
                // 读取 ip 管道

            }
//            Message message = Message.obtain();
//            handler.sendMessage(message);
        }
    }

    private WorkHandler handler;
    private String ipFifoPath;
    private byte []buffer = new byte[4200];
}
