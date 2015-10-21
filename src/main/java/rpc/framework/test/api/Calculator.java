package rpc.framework.test.api;

public interface Calculator {

    /**
     * @return a * b
     */
    int multiply(int a, int b);

    /**
     * @return a / b
     */
    int devide(int a, int b);
}
