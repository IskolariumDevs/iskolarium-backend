package iskolarium_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class BackenApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackenApplication.class, args);
	}
@SpringBootApplication
@EnableScheduling // <-- FLIP THE SWITCH ON!
public class IskolariumBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(IskolariumBackendApplication.class, args);
    }
}
}
