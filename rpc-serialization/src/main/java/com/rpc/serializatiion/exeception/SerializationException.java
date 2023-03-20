package com.rpc.serializatiion.exeception;

/**
 * @author xcx
 * @date
 */
public class SerializationException extends Exception{
    public SerializationException(String msg){
        super(msg);
    }
    public SerializationException(Throwable throwable){
        super(throwable);
    }
}
