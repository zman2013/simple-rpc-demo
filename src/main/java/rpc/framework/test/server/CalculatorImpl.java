package rpc.framework.test.server;

import rpc.framework.test.api.Calculator;

public class CalculatorImpl implements Calculator {

    @Override
    public int multiply(int a, int b) {
        return a * b;
    }

    @Override
    public int devide(int a, int b) {
        return a / b;
    }

}
