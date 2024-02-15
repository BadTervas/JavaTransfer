package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;


    @PostMapping("/send")
    public void sendTransaction(@RequestBody TransactionRequest request) throws IOException, InterruptedException {
        walletService.sendTransaction(request.getToAddress(), request.getAmount());
    }

}
