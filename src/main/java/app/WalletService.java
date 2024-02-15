package app;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class WalletService {

    private Web3j web3j;

    public WalletService() {
        this.web3j = Web3j.build(new HttpService("http://127.0.0.1:7545/"));
    }

    public void sendTransaction(String toAddress, BigDecimal amount) throws IOException, InterruptedException {
        String fromAddress = "0x4a17d765a9fcbb92411fc6396a0990572a1b36f89a36ad0fd91d10bdda7edace";
        Credentials credentials = Credentials.create(fromAddress);

        EthGetTransactionCount ethGetTransactionCount = web3j
                .ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

        // Gas Parameter
        BigInteger gasLimit = BigInteger.valueOf(21000);
        BigInteger gasPrice = Convert.toWei("1", Convert.Unit.GWEI).toBigInteger();

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit,
                toAddress, value);

        byte[] signedTransaction = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedTransaction);

        // Send transaction
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        //get TransactionHash
        String transactionHash = ethSendTransaction.getTransactionHash();

        Optional<TransactionReceipt> transactionReceipt = null;
        do {
            EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j.ethGetTransactionReceipt(transactionHash)
                    .send();
            transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
            Thread.sleep(3000); // Wait for 3 sec
        } while (!transactionReceipt.isPresent());
    }

    private String getPrivateKey() {
        // TODO: Implement this method to get the private key from a secure storage
        return "0x4a17d765a9fcbb92411fc6396a0990572a1b36f89a36ad0fd91d10bdda7edace";
    }
    public BigDecimal getBalance(String address) throws IOException {
        BigInteger balanceWei = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        return new BigDecimal(balanceWei).divide(BigDecimal.TEN.pow(18)); // Перевод в эфиры
    }

    private BigInteger getNonce(String address) throws IOException {
        return web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();
    }

}
