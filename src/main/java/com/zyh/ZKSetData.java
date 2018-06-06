package com.zyh;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * setData方法
 * ZooKeeper类提供 setData 方法来修改指定znode中附加的数据。 setData 方法的签名如下：
 * setData(String path, byte[] data, int version)
 *
 * path- Znode路径
 * data - 要存储在指定znode路径中的数据。
 * version- znode的当前版本。每当数据更改时，ZooKeeper会更新znode的版本号。
 *
 * 现在让我们创建一个新的Java应用程序来了解ZooKeeper API的 setData 功能。创建文件 ZKSetData.java 。在main方法中，使用 ZooKeeperConnection 对象创建一个ZooKeeper对象 zk 。然后，使用指定的路径，新数据和节点版本调用 zk 对象的 setData 方法。
 *
 * 以下是修改附加在指定znode中的数据的完整程序代码。
 *
 * Created by yuanhao on 6/6/18.
 */
public class ZKSetData {

    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;

    // Method to update the data in a znode. Similar to getData but without watcher.
    public static void update(String path, byte[] data) throws KeeperException, InterruptedException {
        zk.setData(path, data, zk.exists(path, true).getVersion());
    }

    public static void main(String[] args) {
        String path = "/MyFirstZnode";
        byte[] data = "Success".getBytes(); // Assign data which is to be updated.

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("localhost");
            update(path, data); // Update znode data to the specified path
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
