package org.shaotang.thrift.impl;

import org.apache.thrift.TException;
import org.shaotang.thrift.Hello;

public class HelloServiceImpl implements Hello.Iface {

    @Override
    public String helloString(String param) throws TException {
        System.out.println("in helloString：" + param);
        return "hello: " + param;
    }
}