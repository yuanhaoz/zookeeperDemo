package com.zyh;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Exists - 检查Znode的存在
 * ZooKeeper类提供了 exists 方法来检查znode的存在。如果指定的znode存在，则返回一个znode的元数据。exists方法的签名如下：
 * exists(String path, boolean watcher)
 *
 * path- Znode路径
 * watcher - 布尔值，用于指定是否监视指定的znode
 *
 * 让我们创建一个新的Java应用程序来检查ZooKeeper API的“exists”功能。创建文件“ZKExists.java”。在main方法中，使用“ZooKeeperConnection”对象创建ZooKeeper对象“zk”。然后，使用自定义“path”调用“zk”对象的“exists”方法。完整的列表如下：
 *
 * Created by yuanhao on 6/6/18.
 */
public class ZKExists {

    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;

    // Method to check existance of znode and its status, if znode is available.
    public static Stat znode_exists(String path) throws KeeperException, InterruptedException {
        return zk.exists(path, true);
    }

    public static void main(String[] args) {
        String path = "/FirstZnode"; // Assign znode to the specified path

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("localhost");
            Stat stat = znode_exists(path); // Stat checks the path of the znode

            if (stat != null) {
                System.out.println("Node exists and the node version is " + stat.getVersion());
            } else {
                System.out.println("Node does not exists");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage()); // Catches error messages
        }
    }

}
