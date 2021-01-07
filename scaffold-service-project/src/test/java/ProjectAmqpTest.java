import com.ylwq.scaffold.service.project.ProjectApplication;
import com.ylwq.scaffold.service.project.amqp.ProjectAmqpSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 项目消息测试
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ProjectApplication.class})
public class ProjectAmqpTest {

    @Autowired
    ProjectAmqpSender projectAmqpSender;

    @Test
    public void update() {
        projectAmqpSender.projectUpdate("5");
    }

    /**
     * RabbitMQ Direct交换机测试
     *
     * @throws InterruptedException 异常
     */
    @Test
    public void direct() throws InterruptedException {
        projectAmqpSender.directExchangeSender("project.delete");
        System.out.println("==================================================================================");
        Thread.sleep(2000);
        projectAmqpSender.directExchangeSender("project.update");
        System.out.println("==================================================================================");
        Thread.sleep(2000);
        projectAmqpSender.directExchangeSender("project.add");
        System.out.println("==================================================================================");
    }

    /**
     * RabbitMQ Topic交换机测试
     *
     * @throws InterruptedException 异常
     */
    @Test
    public void topic() throws InterruptedException {
        projectAmqpSender.topicExchangeSender("company.name");
        System.out.println("==================================================================================");
        Thread.sleep(2000);
        projectAmqpSender.topicExchangeSender("project.test.name");
        System.out.println("==================================================================================");
        Thread.sleep(2000);
        projectAmqpSender.topicExchangeSender("project.change");
        System.out.println("==================================================================================");
    }

    /**
     * RabbitMQ Fanout交换机测试
     */
    @Test
    public void fanout() {
        projectAmqpSender.fanoutExchangeSender();
    }
}
