import com.rpc.common.scanner.ClassCanner;
import com.rpc.common.scanner.referencescanner.RpcReferenceScanner;
import com.rpc.common.scanner.servicescanner.RpcServiceScanner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author xcx
 * @date
 */
public class test {
    public static void main(String[] args) throws IOException {
        List<String> classNames = new ClassCanner().getClassNames("com.rpc.demo");
        //System.out.println(classNames);

        RpcServiceScanner rpcServiceScanner = new RpcServiceScanner();
        RpcReferenceScanner rpcReferenceScanner = new RpcReferenceScanner();

        Map<String, Object> classesByRpcService = rpcServiceScanner.getClassesByRpcService("com.rpc.demo");




    }
}
