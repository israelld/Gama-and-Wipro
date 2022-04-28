package dev.israelld.baseBank.service;

import java.util.List;
import java.util.Optional;

import dev.israelld.baseBank.model.*;
import dev.israelld.baseBank.repository.HistoricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricService {

	@Autowired
	private HistoricRepository historicRepository;

	public Historic findById(Long id) {
        Optional<Historic> obj = historicRepository.findById(id);
        return obj.orElse(null);
    }
	public List<Historic> findAll() {
        return historicRepository.findAll();
    }

	public Historic create(Historic obj) {
        return historicRepository.save(obj);
    }
    public void instantiateWithdrawToAccountSpecial(double movedValue, AccountSpecial account){
        create(new Historic(HistoricType.SAQUE,movedValue, account));
    }
    public void instantiateWithdrawToAccountCurrent(double movedValue, AccountCurrent account){
        create(new Historic(HistoricType.SAQUE,movedValue, account));
    }
    public void instantiateDepositToAccountSpecial(double movedValue, AccountSpecial account){
        create(new Historic(HistoricType.DEPÓSITO,movedValue, account));
    }
    public void instantiateDepositToAccountCurrent(double movedValue, AccountCurrent account){
        create(new Historic(HistoricType.DEPÓSITO,movedValue, account));
    }
    public void instantiateTransferFromAccountSpecial(double movedValue, Long accountNumberDestiny, AccountSpecial account){
        create(new Historic(accountNumberDestiny, HistoricType.TRANSFERÊNCIA_ENVIADA,movedValue, account));
    }
    public void instantiateTransferFromAccountCurrent(double movedValue, Long accountNumberDestiny, AccountCurrent account){
        create(new Historic(accountNumberDestiny, HistoricType.TRANSFERÊNCIA_ENVIADA,movedValue, account));
    }
    public void instantiateTransferToAccountSpecial(double movedValue, Long accountNumberOrigin, AccountSpecial account){
        create(new Historic(HistoricType.TRANSFERÊNCIA_RECEBIDA,movedValue, account, accountNumberOrigin));
    }
    public void instantiateTransferToAccountCurrent(double movedValue, Long accountNumberOrigin, AccountCurrent account){
        create(new Historic(HistoricType.TRANSFERÊNCIA_RECEBIDA,movedValue, account, accountNumberOrigin));
    }

}
