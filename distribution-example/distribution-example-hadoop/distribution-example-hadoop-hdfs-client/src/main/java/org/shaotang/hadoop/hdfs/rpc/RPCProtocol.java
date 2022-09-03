package org.shaotang.hadoop.hdfs.rpc;

public interface RPCProtocol {

    long versionID = 666;

    void mkdirs(String path);
}
