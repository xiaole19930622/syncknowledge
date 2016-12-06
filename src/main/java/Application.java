import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 程序入口
 * Created by xuwen on 2015/3/23.
 */
@SpringBootApplication
@EntityScan(basePackages = "com.xuebang.o2o.business.entity")
@EnableJpaRepositories(basePackages = "com.xuebang.o2o.business.dao")
@ComponentScan(basePackages = {"com.xuebang.o2o"})
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
