package crow.teomant.modular.handshakes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class HandShakeGraphApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandShakeGraphApplication.class, args);
    }

}