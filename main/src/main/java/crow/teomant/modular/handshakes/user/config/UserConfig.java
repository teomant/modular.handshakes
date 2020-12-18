package crow.teomant.modular.handshakes.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("crow.teomant.modular.handshakes.user.persistance.repository")
public class UserConfig {
}
