package com.zyh;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * 删除Znode
 * ZooKeeper类提供了 delete 方法来删除指定的znode。 delete 方法的签名如下：
 * delete(String path, int version)
 *
 * path - Znode路径。
 * version - znode的当前版本。
 *
 * 让我们创建一个新的Java应用程序来了解ZooKeeper API的 delete 功能。创建文件 ZKDelete.java 。在main方法中，使用 ZooKeeperConnection 对象创建一个ZooKeeper对象 zk 。然后，使用指定的路径和版本号调用 zk 对象的 delete 方法。
 * 删除znode的完整程序代码如下：
 *
 * Created by yuanhao on 6/6/18.
 */
public class ZKDelete {

    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;

    // Method to check existence of znode and its status, delete if znode is available.
    public static void delete(String path) throws KeeperException, InterruptedException {
        zk.delete(path, zk.exists(path,true).getVersion());
    }

    public static void main(String[] args) {
        String path = "/MySecondZnode"; // Assign path to the znode

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("localhost");
            delete(path); // delete the node with the specified path
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Catches error messages
        }

    }

}
