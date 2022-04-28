package dev.israelld.baseBank.service;

import dev.israelld.baseBank.controller.form.TransferForm;
import dev.israelld.baseBank.model.Account;
import dev.israelld.baseBank.model.AccountCurrent;
import dev.israelld.baseBank.model.AccountSpecial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransactionService {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountCurrentService accountCurrentService;
    @Autowired
    private AccountSpecialService accountSpecialService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private HistoricService historicService;

    public String deposit(Long id,Account obj) {
        Account account = accountService.findById(id);
        String typeAccount = account.getTipo();
        if (typeAccount.equals("S")) {
            AccountSpecial newAccountSpecial = accountSpecialService.findById(id);
            newAccountSpecial.setBalance(obj.getBalance() + newAccountSpecial.getBalance());
            accountSpecialService.update(id, newAccountSpecial);
            historicService.instantiateDepositToAccountSpecial(obj.getBalance(), newAccountSpecial);
            return String.format("Saldo atual: %.2f\nLimite especial atual: %.2f", newAccountSpecial.getBalance(), newAccountSpecial.getLimitValue());
        } else if (typeAccount.equals("C")) {
            AccountCurrent newAccountCurrent = accountCurrentService.findById(id);
            newAccountCurrent.setBalance(obj.getBalance() + newAccountCurrent.getBalance());
            accountCurrentService.update(id, newAccountCurrent);
            historicService.instantiateDepositToAccountCurrent(obj.getBalance(), newAccountCurrent);
            return String.format("Saldo atual: %.2f", newAccountCurrent.getBalance());
        } else {
            return "Ocorreu um erro!\n Tente novamente mais tarde.";
        }
    }

    public String withdraw(Long id, Account obj) {
        Account account = accountService.findById(id);
        if (account.getTipo().equals("S")) {
            AccountSpecial newAccountSpecial = accountSpecialService.findById(id);
            if (newAccountSpecial.getBalance() - obj.getBalance() < 0) {
                if (newAccountSpecial.getBalance() - obj.getBalance() + newAccountSpecial.getLimitValue() >= 0) {
                    newAccountSpecial.setLimitValue((newAccountSpecial.getBalance() + newAccountSpecial.getLimitValue()) - obj.getBalance());
                    newAccountSpecial.setBalance(0);
                    accountSpecialService.update(id, newAccountSpecial);
                    historicService.instantiateWithdrawToAccountSpecial(obj.getBalance(), newAccountSpecial);
                    return String.format("Saldo atual: %.2f\nLimite especial atual: %.2f", newAccountSpecial.getBalance(), newAccountSpecial.getLimitValue());
                }
                else {
                    return String.format("Saldo insuficiente!\nSaldo atual: %.2f\nLimite especial atual: %.2f", newAccountSpecial.getBalance(), newAccountSpecial.getLimitValue());
                }
            } else {
                newAccountSpecial.setBalance(newAccountSpecial.getBalance() - obj.getBalance());
                accountSpecialService.update(id, newAccountSpecial);
                historicService.instantiateWithdrawToAccountSpecial(obj.getBalance(), newAccountSpecial);
                return String.format("Saldo atual: %.2f\nLimite especial atual: %.2f", newAccountSpecial.getBalance(), newAccountSpecial.getLimitValue());
            }
        } else if (account.getTipo().equals("C")) {
            AccountCurrent newAccountCurrent = accountCurrentService.findById(id);
            if (newAccountCurrent.getBalance() - obj.getBalance() < 0) {

                return String.format("Saldo insuficiente!\nSaldo atual: %.2f", newAccountCurrent.getBalance());
            }
            else {
                newAccountCurrent.setBalance(newAccountCurrent.getBalance() - obj.getBalance());
                accountCurrentService.update(id, newAccountCurrent);
                historicService.instantiateWithdrawToAccountCurrent(obj.getBalance(), newAccountCurrent);
                return String.format("Saldo atual: %.2f", newAccountCurrent.getBalance());
            }
        } else {
            return "Ocorreu um erro!\n Tente novamente mais tarde.";
        }
    }

    public String transfer(Long id, TransferForm form) {

        Account accountDestination = accountService.findById(form.getAccountDestination());
        Account accountOrigin = accountService.findById(id);

            if (accountOrigin.getBalance() - form.getValueToTransfer() < 0) {
                return String.format("Saldo insuficiente!\nSaldo atual: %.2f", accountOrigin.getBalance());
            }else {
                if (accountDestination.getTipo().equals("C")) {
                    AccountCurrent newAccountCurrent = accountCurrentService.findById(accountDestination.getId());
                    newAccountCurrent.setBalance(form.getValueToTransfer() + newAccountCurrent.getBalance());
                    accountCurrentService.update(newAccountCurrent.getId(), newAccountCurrent);
                    historicService.instantiateTransferFromAccountCurrent(form.getValueToTransfer(), newAccountCurrent.getId(), newAccountCurrent);
                }else if (accountDestination.getTipo().equals("S")) {
                    AccountSpecial newAccountSpecial = accountSpecialService.findById(accountDestination.getId());
                    newAccountSpecial.setBalance(form.getValueToTransfer() + newAccountSpecial.getBalance());
                    accountSpecialService.update(newAccountSpecial.getId(), newAccountSpecial);
                    historicService.instantiateTransferFromAccountSpecial(form.getValueToTransfer(), newAccountSpecial.getId(), newAccountSpecial);
                }
                if (accountOrigin.getTipo().equals("C")) {
                    AccountCurrent newAccountCurrent = accountCurrentService.findById(accountOrigin.getId());
                    newAccountCurrent.setBalance(form.getValueToTransfer() + newAccountCurrent.getBalance());
                    newAccountCurrent.setBalance(accountOrigin.getBalance() - form.getValueToTransfer());
                    accountCurrentService.update(newAccountCurrent.getId(), newAccountCurrent);
                    historicService.instantiateTransferToAccountCurrent(form.getValueToTransfer(), newAccountCurrent.getId(), newAccountCurrent);
                }else if (accountOrigin.getTipo().equals("S")) {
                    System.err.println("Or S");
                    AccountSpecial newAccountSpecial = accountSpecialService.findById(accountOrigin.getId());
                    newAccountSpecial.setBalance(accountOrigin.getBalance() - form.getValueToTransfer());
                    accountSpecialService.update(newAccountSpecial.getId(), newAccountSpecial);
                    historicService.instantiateTransferToAccountSpecial(form.getValueToTransfer(), newAccountSpecial.getId(), newAccountSpecial);
                }
                return String.format("Transferência realizada com sucesso!\n Saldo atual: %.2f", accountOrigin.getBalance());
            }
    }
}
