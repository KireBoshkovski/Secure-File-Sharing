package mk.ukim.finki.ib.filesharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class FileSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileSharingApplication.class, args);
    }
}
