package com.rpc.protocal.handler;

import com.rpc.common.constant.RpcConstants;
import com.rpc.common.utils.SerializationUtils;
import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.base.RpcMessage;
import com.rpc.protocal.enumeration.Messagetype;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.protocal.message.ResponseMessage;
import com.rpc.serializatiion.Serialization;
import com.rpc.serializatiion.SerializationFactory;
import com.rpc.serializatiion.exeception.SerializationException;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * 通信协议的解码与编码器
 * @author xcx
 * @date
 */
public class RpcCode extends ByteToMessageCodec<RpcProtocal<Object>> implements Code {
    private static Logger LOGGER = LoggerFactory.getLogger(RpcCode.class);
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocal msg, ByteBuf out) throws Exception {
        RpcHeader header = msg.getHeader();
        out.writeShort(header.getMagic()) ;
        out.writeByte(header.getMsgType());
        out.writeByte(header.getStatus());
        out.writeLong(header.getRequestId());

        String serializationType = header.getSerializationType();

        //todo 扩展序列化方式
        Serialization serialization = SerializationFactory.getSerializationByType(serializationType);
        if(serialization == null){
            LOGGER.error("not found serialization type: " + serializationType);

            serialization = SerializationFactory.getSerializationByType("jdk");
        }
        Object body = msg.getMessage();

        serializationType = SerializationUtils.paddingString(serializationType);
        out.writeBytes(serializationType.getBytes(StandardCharsets.UTF_8));

        byte[] data = serialization.serialize(body);
        out.writeInt(data.length);
        out.writeBytes(data);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in == null || in.readableBytes() < RpcConstants.FIXED_MAX_HEADER_LENGTH){
            return;
        }

        short magic = in.readShort();
        if(magic != RpcConstants.MAGIC){
            throw new IllegalAccessException("magic numbser is, " + magic);
        }

        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();

        ByteBuf serializationTypeBuf = in.readBytes(SerializationUtils.MAX_SERIALIZATION_TYPE_COUNR);
        String serializationType = SerializationUtils.removeZero(serializationTypeBuf.toString(StandardCharsets.UTF_8));

        //todo 需要扩展更多序列化方式
        Serialization serialization = SerializationFactory.getSerializationByType(serializationType);
        if(serialization == null){
            LOGGER.error("not found serialization type: " + serializationType);
            serialization = SerializationFactory.getSerializationByType("jdk");
        }

        int dataLen = in.readInt();
        if(in.readableBytes() < dataLen){
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLen];
        in.readBytes(data);

        Messagetype msgTypeEnum = Messagetype.findByType(msgType);
        if(msgTypeEnum == null){
            throw new Exception("no dound message type: " + msgType);
        }

        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setMagic(magic);
        rpcHeader.setMsgType(msgType);
        rpcHeader.setStatus(status);
        rpcHeader.setRequestId(requestId);
        rpcHeader.setSerializationType(serializationType);
        rpcHeader.setMsgLen(dataLen);

        switch (msgTypeEnum){
            case REQUEST:
                RequestMessage requestMessage = serialization.dserialize(data, RequestMessage.class);
                if(requestMessage != null){
                    RpcProtocal<RequestMessage> protocal = new RpcProtocal<>();
                    protocal.setHeader(rpcHeader);
                    protocal.setMessage(requestMessage);

                    out.add(protocal);
                }
                break;
            case RESPONSE:
                ResponseMessage responseMessage = serialization.dserialize(data, ResponseMessage.class);
                if(responseMessage != null){
                    RpcProtocal<ResponseMessage> protocal = new RpcProtocal<>();
                    protocal.setHeader(rpcHeader);
                    protocal.setMessage(responseMessage);

                    out.add(protocal);
                }
                break;
            default:
                break;
        }
    }
}
