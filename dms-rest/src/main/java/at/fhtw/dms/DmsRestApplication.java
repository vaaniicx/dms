package at.fhtw.dms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DmsRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmsRestApplication.class, args);
    }
}
