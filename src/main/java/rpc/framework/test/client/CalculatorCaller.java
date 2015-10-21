package rpc.framework.test.client;

import rpc.framework.SimpleRPCFramework;
import rpc.framework.test.api.Calculator;

public class CalculatorCaller {

    public static void main(String[] args) {
        SimpleRPCFramework rpc = new SimpleRPCFramework();

        Calculator calc = rpc.getProxy(Calculator.class);

        for (int i = 0; i < 10; i++) {
            int result = calc.multiply(i, (i + 1));
            System.out.println(result);
        }
    }
}
