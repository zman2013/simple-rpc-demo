package rpc.framework;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRPCFramework {

    /**
     * 存储接口与实现的对应关系
     */
    private final Map<Class<?>, Object> implCache = new ConcurrentHashMap<>();

    /**
     * 存储接口与代理的对应关系
     */
    private final Map<Class<?>, Object> proxyCache = new ConcurrentHashMap<>();

    public synchronized void registerImplements(Class<?> inteface, Object instance) {
        //检测接口是否已经存在
        if (implCache.containsKey(inteface)) {
            throw new RuntimeException("This interface[" + inteface.getName() + "] have already been exposed.");
        } else {
            implCache.put(inteface, instance);
        }
    }

    
    public void expose() throws IOException {
        //暴露服务端口
        @SuppressWarnings("resource")
        ServerSocket server = new ServerSocket(2008);
        while (true) {
            final Socket socket = server.accept();
            /*@formatter:off*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try (
                            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                        //获取请求数据 - 接收请求,并解码
                        Class<?> inter = (Class<?>) in.readObject();
                        String methodName = in.readUTF();
                        Class<?>[] parameterTypes = (Class<?>[]) in.readObject();
                        Object[] args = (Object[]) in.readObject();
                        //根据接口信息获得对应的接口实现 - Dispatcher
                        Object instance = implCache.get(inter);
                        //调用接口实现 - invoke
                        Method method = instance.getClass().getMethod(methodName, parameterTypes);
                        Object result = method.invoke(instance, args);
                        //返回结果 - 编码,并返回结果
                        out.writeObject(result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).run();
            /*@formatter:on*/
        }

    }

    
    @SuppressWarnings("unchecked")
    public synchronized <T> T getProxy(final Class<T> inteface) {
        //检测接口是否已经存在
        if (proxyCache.containsKey(inteface)) {
            return (T) proxyCache.get(inteface);
        }

        /*@formatter:off*/
        T proxy = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] { inteface }, new InvocationHandler() {
                    @SuppressWarnings("resource")
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = new Socket(InetAddress.getLocalHost(), 2008);
                        try(
                                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                                ObjectInputStream input = new ObjectInputStream(socket.getInputStream())
                                ){
                            //发送请求数据 - 编码,并发送请求
                            output.writeObject(inteface);
                            output.writeUTF(method.getName());
                            output.writeObject(method.getParameterTypes());
                            output.writeObject(args);
                            //接收结果 - 接受数据,并解码
                            Object result = input.readObject();

                            return result;
                        }
                    }

                });
        /*@formatter:on*/
        proxyCache.put(inteface, proxy);
        return proxy;
    }

}
