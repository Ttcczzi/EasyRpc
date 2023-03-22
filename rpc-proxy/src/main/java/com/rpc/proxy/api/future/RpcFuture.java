package com.rpc.proxy.api.future;

import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.protocal.message.ResponseMessage;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.threadpool.CallBackThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 *
 * @author xcx
 * @date
 */
public class RpcFuture extends CompletableFuture<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcFuture.class);
    private Sync sync;
    private RpcProtocal<RequestMessage> requestRrotocal;
    private RpcProtocal<ResponseMessage> responseRrotocal;
    private long startTime;
    private long responseTimeThrshould = 5000;
    private ArrayList<AsyncCallback> callbackList;

    private ReentrantLock lock = new ReentrantLock();


    public RpcFuture(RpcProtocal<RequestMessage> requestRrotocal) {
        this.sync = new Sync();
        this.requestRrotocal = requestRrotocal;
        this.startTime = System.currentTimeMillis();
    }

    public RpcFuture(RpcProtocal<RequestMessage> requestRrotocal, AsyncCallback callback) {
        this(requestRrotocal);
        callbackList = new ArrayList<>();
        addCallBack(callback);
    }

    public void addCallBack(AsyncCallback callback){
        lock.lock();
        try {
            if(callbackList == null){
                callbackList = new ArrayList<>();
            }
            if(callback != null){
                callbackList.add(callback);
            }
        }finally {
            lock.unlock();
        }
    }

    public void runCallBack(final AsyncCallback callback) {
        lock.lock();
        try {
            CallBackThreadPool.submit(() -> {
                if (responseRrotocal.getMessage().isError()) {
                    callback.defeat(new RuntimeException("consume defeat," + responseRrotocal.getMessage().getError()));
                }else{
                    callback.success(responseRrotocal);
                }
                    }
            );

        } finally {
            lock.unlock();
        }
    }

    public void invokeCallBack() {
        lock.lock();
        try {
            if(callbackList != null){
                for (AsyncCallback callback : callbackList) {
                    runCallBack(callback);
                }
            }
        } finally {
            lock.unlock();
        }

    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        return get0();
    }


    private Object get0() {
        if (responseRrotocal != null) {
            return responseRrotocal.getMessage().getResult();
        }
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            return get0();
        } else {
            throw new RuntimeException("time out, RequestId: " + requestRrotocal.getHeader().getRequestId()
                    + "interfaceName: " + requestRrotocal.getMessage().getInterfaceName()
                    + "methodName: " + requestRrotocal.getMessage().getMethodName());
        }
    }

    public void Done(RpcProtocal<ResponseMessage> responseRrotocal) {
        this.responseRrotocal = responseRrotocal;
        sync.release(1);
        invokeCallBack();
        long processTime = System.currentTimeMillis() - startTime;
        if (processTime > responseTimeThrshould) {
            LOGGER.warn("time out: " + requestRrotocal.getMessage().getInterfaceName() + "," + requestRrotocal.getMessage().getMethodName());
        }
    }

    //该aqs没有作为锁来用，只是利用其特性来实现future的功能
    static class Sync extends AbstractQueuedSynchronizer {

        private final int done = 1;
        private final int pedding = 0;

        //若此时状态已经是done，说明已经获得结果，返回true，使得线程不阻塞
        //若返回false，则会阻塞线程
        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == done;
        }

        //若此时状态还是pedding，说明结果还未获取到，通过cas将状态改变，并将阻塞的线程释放
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == pedding) {
                if (compareAndSetState(pedding, done)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isDone() {
            //todo 为什么要多getState() 一下
            getState();
            return getState() == done;
        }
    }
}
