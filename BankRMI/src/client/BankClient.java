package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import interfaces.IBank;

public class BankClient {
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        IBank bank = (IBank) Naming.lookup("rmi://localhost/Bank");

        bank.createAccount(123456L, "123456");
        bank.deposit(100.10, 123456L);
        bank.deposit(33.56, 123456L);

        System.out.println(bank.statement(123456L, "123456"));
    }
}
