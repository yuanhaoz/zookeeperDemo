package com.zyh;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * getData方法
 * ZooKeeper类提供 getData 方法来获取附加在指定znode中的数据及其状态。 getData 方法的签名如下：
 * getData(String path, Watcher watcher, Stat stat)
 *
 * path - Znode路径。
 * watcher - 监视器类型的回调函数。当指定的znode的数据改变时，ZooKeeper集合将通过监视器回调进行通知。这是一次性通知。
 * stat - 返回znode的元数据。
 *
 * 让我们创建一个新的Java应用程序来了解ZooKeeper API的 getData 功能。创建文件 ZKGetData.java 。在main方法中，使用 ZooKeeperConnection 对象创建一个ZooKeeper对象 zk 。然后，使用自定义路径调用zk对象的 getData 方法。
 * 下面是从指定节点获取数据的完整程序代码：
 *
 * Created by yuanhao on 6/6/18.
 */
public class ZKGetData {

    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;
    public static Stat znode_exists(String path) throws KeeperException, InterruptedException {
        return zk.exists(path, true);
    }

    public static void main(String[] args) {
        String path = "/MyFirstZnode";
        final CountDownLatch connectedSignal = new CountDownLatch(1);

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("localhost");
            Stat stat = znode_exists(path);

            if (stat != null) {
                byte[] b = zk.getData(path, new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if (event.getType() == Event.EventType.None) {
                            switch (event.getState()) { // 客户端会话id过期，countDown减1，执行主程序
                                case Expired:
                                    connectedSignal.countDown();
                                    break;
                            }
                        } else { // 数据做了修改后重新读取数据，countDown减1，执行主程序
                            String path = "/MyFirstZnode";
                            try {
                                byte[] bn = zk.getData(path, false, null);
                                String data = new String(bn, "utf-8");
                                System.out.println(data);
                                connectedSignal.countDown();
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                }, null);

                String data = new String(b, "utf-8");
                System.out.println(data);
                connectedSignal.await();

            } else {
                System.out.println("Node does not exists");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
