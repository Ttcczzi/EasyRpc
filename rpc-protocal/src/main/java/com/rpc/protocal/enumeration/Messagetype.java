package com.rpc.protocal.enumeration;

/**
 * @author xcx
 * @date
 */
public enum Messagetype {
    //请求消息
    REQUEST(1),
    //响应消息
    RESPONSE(2),

    HEARTBEAT(3),

    ULTRALIMIT(4);

    private final int type;

    Messagetype(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static Messagetype findByType(int type) {
        for (Messagetype messagetype : Messagetype.values()) {
            if (messagetype.getType() == type) {
                return messagetype;
            }
        }
        return null;
    }
}
