package rpc.framework.test;

import rpc.framework.SimpleRPCFramework;

public class CalculatorClient {

    public static void main(String[] args) {
        SimpleRPCFramework rpc = new SimpleRPCFramework();

        Calculator calc = rpc.getProxy(Calculator.class);

        for (int i = 0; i < 10; i++) {
            int result = calc.multiply(i, (i + 1));
            System.out.println(result);
        }
    }
}
