import app.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;

@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class, org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration.class})
@ComponentScan(basePackages = {"app"})
public class WalletApplication {


    @Autowired
    private WalletService walletService;

    public WalletApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
    }


/*    public void run(String... args) throws Exception {
        try {
            walletService.sendTransaction("0x4c03ca81B46E1C26dF36f207942C367e54178ADB", BigDecimal.valueOf(1.0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
