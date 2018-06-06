package com.zyh;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

/**
 * 创建Znode
 * ZooKeeper类提供了在ZooKeeper集合中创建一个新的znode的create方法。 create 方法的签名如下：
 * create(String path, byte[] data, List<ACL> acl, CreateMode createMode)
 *
 * path - Znode路径。例如，/myapp1，/myapp2，/myapp1/mydata1，myapp2/mydata1/myanothersubdata
 * data - 要存储在指定znode路径中的数据
 * acl - 要创建的节点的访问控制列表。ZooKeeper API提供了一个静态接口 ZooDefs.Ids 来获取一些基本的acl列表。例如，ZooDefs.Ids.OPEN_ACL_UNSAFE返回打开znode的acl列表。
 * createMode - 节点的类型，即临时，顺序或两者。这是一个枚举。
 *
 * 让我们创建一个新的Java应用程序来检查ZooKeeper API的 create 功能。创建文件 ZKCreate.java 。在main方法中，创建一个类型为 ZooKeeperConnection 的对象，并调用 connect 方法连接到ZooKeeper集合。
 * connect方法将返回ZooKeeper对象 zk 。现在，请使用自定义path和data调用 zk 对象的 create 方法。
 *
 * Created by yuanhao on 6/5/18.
 */
public class ZKCreate {

    // create static instance for zookeeper class.
    private static ZooKeeper zk;

    // create static instance for ZooKeeperConnection class.
    private static ZooKeeperConnection conn;

    // Method to create znode in zookeeper ensemble
    public static void create(String path, byte[] data) throws KeeperException, InterruptedException {
        zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public static void main(String[] args) {

        // znode path
        String path = "/MySecondZnode"; // Assign path to znode

        // data in byte array
        byte[] data = "My second zookeeper app".getBytes(); // Declare data

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("localhost");
            create(path, data); // Create the data to the specified path
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Catch error message
//            e.printStackTrace();
        }

    }

}
