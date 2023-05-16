package TeamProject.Project.Component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static TeamProject.Project.Contorller.CrawlingController.setChromeDriver;

@Component
public class StartUp implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        setChromeDriver();
    }
}
