package consume;

import interfaces.TestInterface;

import com.rpc.annotation.RpcService;
import com.rpc.annotation.RpcReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Resource;

/**
 * @author xcx
 * @date
 */
@SpringBootApplication
public class ConsumeApplication {
    @RpcReference
    @Resource
    private TestInterface testInterface;

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ConsumeApplication.class);

        ConsumeApplication consumeApplication = (ConsumeApplication) run.getBean("consumeApplication");

        System.out.println(consumeApplication.testInterface.test("wt"));
    }
}
