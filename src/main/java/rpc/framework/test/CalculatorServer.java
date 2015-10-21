package rpc.framework.test;

import java.io.IOException;

import rpc.framework.SimpleRPCFramework;

public class CalculatorServer {

    public static void main(String[] args) throws IOException {
        //实例化helloWorld
        CalculatorImpl calculator = new CalculatorImpl();
        //创建rpc框架实例
        SimpleRPCFramework rpc = new SimpleRPCFramework();
        //注册接口和对应实现
        rpc.registerImplements(Calculator.class, calculator);
        //暴露服务接口，监听服务
        rpc.expose();
    }
}
