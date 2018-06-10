# Zookeeper 服务器启动

1、启动ZooKeeper服务器

执行以下命令
```markdown
$ bin/zkServer.sh start
```

执行此命令后，你将收到以下响应
```markdown
$ JMX enabled by default
$ Using config: /Users/../zookeeper-3.4.6/bin/../conf/zoo.cfg
$ Starting zookeeper ... STARTED
```

2、启动CLI

键入以下命令
```markdown
$ bin/zkCli.sh
```

键入上述命令后，将连接到ZooKeeper服务器，你应该得到以下响应。
```markdown
Connecting to localhost:2181
................
................
................
Welcome to ZooKeeper!
................
................
WATCHER::
WatchedEvent state:SyncConnected type: None path:null
[zk: localhost:2181(CONNECTED) 0]
```

3、停止ZooKeeper服务器

连接服务器并执行所有操作后，可以使用以下命令停止zookeeper服务器。
```markdown
$ bin/zkServer.sh stop
```

# Zookeeper API

ZooKeeper有一个绑定Java和C的官方API。Zookeeper社区为大多数语言（.NET，python等）提供非官方API。使用ZooKeeper API，应用程序可以连接，交互，操作数据，协调，最后断开与ZooKeeper集合的连接。

ZooKeeper API具有丰富的功能，以简单和安全的方式获得ZooKeeper集合的所有功能。ZooKeeper API提供同步和异步方法。

ZooKeeper集合和ZooKeeper API在各个方面都完全相辅相成，对开发人员有很大的帮助。让我们在本章讨论Java绑定。

## ZooKeeper API的基础知识

与ZooKeeper集合进行交互的应用程序称为 **ZooKeeper客户端**或简称**客户端**。

Znode是ZooKeeper集合的核心组件，ZooKeeper API提供了一小组方法使用ZooKeeper集合来操纵znode的所有细节。

客户端应该遵循以步骤，与ZooKeeper集合进行清晰和干净的交互。

- 连接到ZooKeeper集合。ZooKeeper集合为客户端分配会话ID。
- 定期向服务器发送心跳。否则，ZooKeeper集合将过期会话ID，客户端需要重新连接。
- 只要会话ID处于活动状态，就可以获取/设置znode。  
- 所有任务完成后，断开与ZooKeeper集合的连接。如果客户端长时间不活动，则ZooKeeper集合将自动断开客户端。

## Java绑定

让我们来了解本章中最重要的一组ZooKeeper API。ZooKeeper API的核心部分是**ZooKeeper类**。它提供了在其构造函数中连接ZooKeeper集合的选项，并具有以下方法：

- **connect** - 连接到ZooKeeper集合
- **create**- 创建znode
- **exists**- 检查znode是否存在及其信息
- **getData** - 从特定的znode获取数据
- **setData** - 在特定的znode中设置数据
- **getChildren** - 获取特定znode中的所有子节点
- **delete** - 删除特定的znode及其所有子项
- **close** - 关闭连接

## 连接到ZooKeeper集合

ZooKeeper类通过其构造函数提供connect功能。构造函数的签名如下 :
```
ZooKeeper(String connectionString, int sessionTimeout, Watcher watcher)
```
- **connectionString** - ZooKeeper集合主机。
- **sessionTimeout** - 会话超时（以毫秒为单位）。
- **watcher** - 实现“监视器”界面的对象。ZooKeeper集合通过监视器对象返回连接状态。

让我们创建一个新的帮助类 **ZooKeeperConnection** ，并添加一个方法 **connect** 。 **connect** 方法创建一个ZooKeeper对象，连接到ZooKeeper集合，然后返回对象。

这里 **CountDownLatch** 用于停止（等待）主进程，直到客户端与ZooKeeper集合连接。

ZooKeeper集合通过监视器回调来回复连接状态。一旦客户端与ZooKeeper集合连接，监视器回调就会被调用，并且监视器回调函数调用**CountDownLatch**的**countDown**方法来释放锁，在主进程中**await**。

## 创建Znode

ZooKeeper类提供了在ZooKeeper集合中创建一个新的znode的**create**方法。 **create** 方法的签名如下：

```
create(String path, byte[] data, List<ACL> acl, CreateMode createMode)
```

- **path** - Znode路径。例如，/myapp1，/myapp2，/myapp1/mydata1，myapp2/mydata1/myanothersubdata
- **data** - 要存储在指定znode路径中的数据
- **acl** - 要创建的节点的访问控制列表。ZooKeeper API提供了一个静态接口 ZooDefs.Ids 来获取一些基本的acl列表。例如，ZooDefs.Ids.OPEN_ACL_UNSAFE返回打开znode的acl列表。
- **createMode** - 节点的类型，即临时，顺序或两者。这是一个枚举。

让我们创建一个新的Java应用程序来检查ZooKeeper API的 **create** 功能。创建文件 **ZKCreate.java** 。在main方法中，创建一个类型为 **ZooKeeperConnection** 的对象，并调用 **connect** 方法连接到ZooKeeper集合。

connect方法将返回ZooKeeper对象 **zk** 。现在，请使用自定义**path**和**data**调用 **zk** 对象的 **create** 方法。

一旦编译和执行应用程序，将在ZooKeeper集合中创建具有指定数据的znode。你可以使用ZooKeeper CLI zkCli.sh 进行检查。

```markdown
cd /path/to/zookeeper
bin/zkCli.sh
>>> get /MyFirstZnode
```

## Exists - 检查Znode的存在

ZooKeeper类提供了 **exists** 方法来检查znode的存在。如果指定的znode存在，则返回一个znode的元数据。**exists** 方法的签名如下：

```markdown
exists(String path, boolean watcher)
```

- **path** - Znode路径
- **watcher** - 布尔值，用于指定是否监视指定的znode

让我们创建一个新的Java应用程序来检查ZooKeeper API的“exists”功能。创建文件“ZKExists.java”。在main方法中，使用“ZooKeeperConnection”对象创建ZooKeeper对象“zk”。然后，使用自定义“path”调用“zk”对象的“exists”方法。

一旦编译和执行应用程序，你将获得以下输出。
```markdown
Node exists and the node version is 1.
```

## getData方法

ZooKeeper类提供 **getData** 方法来获取附加在指定znode中的数据及其状态。 **getData** 方法的签名如下：

```markdown
getData(String path, Watcher watcher, Stat stat)
```
- **path** - Znode路径。
- **watcher** - 监视器类型的回调函数。当指定的znode的数据改变时，ZooKeeper集合将通过监视器回调进行通知。这是一次性通知。
- **stat** - 返回znode的元数据。

让我们创建一个新的Java应用程序来了解ZooKeeper API的 **getData** 功能。创建文件 **ZKGetData.java** 。在main方法中，使用 **ZooKeeperConnection** 对象创建一个ZooKeeper对象 **zk** 。然后，使用自定义路径调用zk对象的 **getData** 方法。

一旦编译和执行应用程序，你将获得以下输出
```markdown
My first zookeeper app
```

应用程序将等待ZooKeeper集合的进一步通知。使用ZooKeeper CLI zkCli.sh 更改指定znode的数据。
```markdown
cd /path/to/zookeeper
bin/zkCli.sh
>>> set /MyFirstZnode Hello
```

现在，应用程序将打印以下输出并退出。
```markdown
Hello
```

## setData方法

ZooKeeper类提供 **setData** 方法来修改指定znode中附加的数据。 **setData** 方法的签名如下：

```markdown
setData(String path, byte[] data, int version)
```

- **path** - Znode路径
- **data** - 要存储在指定znode路径中的数据。
- **version**- znode的当前版本。每当数据更改时，ZooKeeper会更新znode的版本号。

现在让我们创建一个新的Java应用程序来了解ZooKeeper API的 **setData** 功能。创建文件 **ZKSetData.java** 。在main方法中，使用 **ZooKeeperConnection** 对象创建一个ZooKeeper对象 **zk** 。然后，使用指定的路径，新数据和节点版本调用 **zk** 对象的 **setData** 方法。

编译并执行应用程序后，指定的znode的数据将被改变，并且可以使用ZooKeeper CLI zkCli.sh 进行检查。

```markdown
cd /path/to/zookeeper
bin/zkCli.sh
>>> get /MyFirstZnode
```

## getChildren方法
ZooKeeper类提供 **getChildren** 方法来获取特定znode的所有子节点。 **getChildren** 方法的签名如下：

```markdown
getChildren(String path, Watcher watcher)
```

- **path** - Znode路径。
- **watcher** - 监视器类型的回调函数。当指定的znode被删除或znode下的子节点被创建/删除时，ZooKeeper集合将进行通知。这是一次性通知。

在运行程序之前，让我们使用ZooKeeper CLI zkCli.sh 为 /MyFirstZnode 创建两个子节点。
```markdown
cd /path/to/zookeeper
bin/zkCli.sh
>>> create /MyFirstZnode/myfirstsubnode Hi
>>> create /MyFirstZnode/mysecondsubmode Hi
```

现在，编译和运行程序将输出上面创建的znode。
```markdown
myfirstsubnode
mysecondsubnode
```

## 删除Znode
ZooKeeper类提供了 **delete** 方法来删除指定的znode。 **delete** 方法的签名如下：
```markdown
delete(String path, int version)
```

- **path** - Znode路径。
- **version** - znode的当前版本。

让我们创建一个新的Java应用程序来了解ZooKeeper API的 **delete** 功能。创建文件 **ZKDelete.java** 。在main方法中，使用 **ZooKeeperConnection** 对象创建一个ZooKeeper对象 **zk** 。然后，使用指定的路径和版本号调用 **zk** 对象的 **delete** 方法。
   
