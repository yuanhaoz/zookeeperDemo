package com.zyh;

// import java classes
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

// import zookeeper classes
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.data.Stat;

/**
 * 连接到ZooKeeper集合
 * ZooKeeper类通过其构造函数提供connect功能。构造函数的签名如下 :
 * ZooKeeper(String connectionString, int sessionTimeout, Watcher watcher)
 * connectionString - ZooKeeper集合主机。
 * sessionTimeout - 会话超时（以毫秒为单位）。
 * watcher - 实现“监视器”界面的对象。ZooKeeper集合通过监视器对象返回连接状态。
 * 让我们创建一个新的帮助类 ZooKeeperConnection ，并添加一个方法 connect 。 connect 方法创建一个ZooKeeper对象，连接到ZooKeeper集合，然后返回对象。
 * 这里 CountDownLatch 用于停止（等待）主进程，直到客户端与ZooKeeper集合连接。
 * ZooKeeper集合通过监视器回调来回复连接状态。一旦客户端与ZooKeeper集合连接，监视器回调就会被调用，并且监视器回调函数调用CountDownLatch的countDown方法来释放锁，在主进程中await。
 * Created by yuanhao on 6/5/18.
 */
public class ZooKeeperConnection {

    // declare zookeeper instance to access ZooKeeper ensemble
    private ZooKeeper zoo;
    // CountDownLatch的构造函数接收一个int类型的参数作为计数器，等待n个点完成
    final CountDownLatch connectedSignal = new CountDownLatch(1);

    // Method to connect zookeeper ensemble.
    public ZooKeeper connect(String host) throws IOException,InterruptedException {

        zoo = new ZooKeeper(host,5000,new Watcher() {

            public void process(WatchedEvent we) {

                if (we.getState() == KeeperState.SyncConnected) {
                    connectedSignal.countDown(); // countDown函数会使得n减1
                }
            }
        });

        // await函数阻塞当前线程，直到n变成0。n个点可以是n个线程，也可以是1个线程里的n个执行步骤
        connectedSignal.await();
        return zoo;
    }

    // Method to disconnect from zookeeper server
    public void close() throws InterruptedException {
        zoo.close();
    }

}
