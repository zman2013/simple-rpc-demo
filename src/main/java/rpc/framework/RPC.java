package rpc.framework;

import java.io.IOException;

public interface RPC {
    
    /**
     * 服务器端注册接口与相应实现。
     *
     * @param inteface
     * @param instance
     */
    void registerImplements(Class<?> inteface, Object instance);
    
    /**
     * 服务器端暴露端口，并监听调用请求
     */
    void expose() throws IOException;
    
    /**
     * 调用端获取接口的代理
     */
    <T> T getProxy(final Class<T> inteface);
}
