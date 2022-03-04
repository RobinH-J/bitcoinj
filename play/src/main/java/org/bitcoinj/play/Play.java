package org.bitcoinj.play;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.concurrent.locks.ReentrantLock;


import org.bitcoinj.crypto.KeyCrypter;
import org.bitcoinj.crypto.KeyCrypterException;
import org.bitcoinj.utils.Threading;
import org.bitcoinj.wallet.WalletExtension;
import org.bitcoinj.wallet.WalletProtobufSerializer;
import org.bitcoinj.wallet.Wallet;
import picocli.CommandLine;

import static com.google.common.base.Preconditions.checkState;

//@CommandLine.Command(name = "pincrack", usageHelpAutoWidth = true, sortOptions = false, description = "Crack wallet's pin.")
public class Play {
//    private String wallet = null;
//    @CommandLine.Option(names = "--wallet", description = "Path to wallet file.")
//
//    private String firstPin = null;
//    @CommandLine.Option(names = "--first-pin", description = "Start crack from this pin.")

    public static void main(String args[]) throws Exception {
        System.out.println("Run with args: <filepath> [optional]<starting-pin>");
        System.out.println("max pin 999999.");
        String walletFile = "/Users/robin/Documents/Dev/Brundan/play";
//        walletFile = "";
        int pinStart = 0;
//        if (Play.wallet) {
//            walletFile = args[0];
//        }
//        if(args.length>1)
//            pinStart = Integer.parseInt(args[1]);

        String password;
        password = "1571";
        password = "1234";
        final ReentrantLock keyChainGroupLock = Threading.lock("Wallet-KeyChainGroup lock");
        System.out.println("Hello World!");
        WalletProtobufSerializer loader = new WalletProtobufSerializer();
        BufferedInputStream walletInputStream = new BufferedInputStream(new FileInputStream(walletFile));
        Wallet wallet = loader.readWallet(walletInputStream, false, (WalletExtension[])(null));
        wallet.keyChainGroupLock.lock();
        boolean isFound = false;
        for(int i=pinStart;i<=999999;i++){
            if(i%100 == 0)
                System.out.println(i);
//            password = Integer.toString(i).substring(1);
            password = Integer.toString(i);
            final KeyCrypter crypter = wallet.keyChainGroup.getKeyCrypter();

//            System.out.println(Integer.toString(i)+" : " + password);
            try {
                var aes = crypter.deriveKey(password);
                wallet.keyChainGroup.decrypt(aes);
//                wallet.decrypt(password);
            }
            catch (Exception ignored){
                continue;
            }
            isFound = true;
            System.out.println(i);
            System.exit(0);
        }
        System.out.println("Pin not found");
    }
}
