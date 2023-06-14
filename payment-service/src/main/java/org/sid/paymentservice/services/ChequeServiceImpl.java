package org.sid.paymentservice.services;

import lombok.AllArgsConstructor;
import org.sid.paymentservice.entities.Cheque;
import org.sid.paymentservice.repositories.ChequeRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChequeServiceImpl implements ChequeService{
    private ChequeRepository chequeRepository;

    @Override
    public Cheque saveCheque(Cheque cheque) {
        cheque.setDate(new Date());
        return chequeRepository.save(cheque);
    }

    @Override
    public Cheque updateCheque(Long id, Cheque cheque) {
        Optional<Cheque> optionalcheque = chequeRepository.findById(id);

        Cheque existingcheque = optionalcheque.get();

        existingcheque.setMontant(cheque.getMontant());
        existingcheque.setDate(new Date());
        existingcheque.setPaymentProcess(cheque.getPaymentProcess());
        existingcheque.setIdStudent(cheque.getIdStudent());


        Cheque updatedCheque = chequeRepository.save(existingcheque);

        return updatedCheque;
    }

    @Override
    public List<Cheque> getCheques() {
        return chequeRepository.findAll();
    }

    @Override
    public Cheque getChequeById(Long id) {
        return chequeRepository.findById(id).get();
    }

    @Override
    public void deleteCheque(Long id) {
        Cheque cheque=new Cheque();
        cheque.setId(id);
        chequeRepository.delete(cheque);
    }

    @Override
    public Cheque validateCheque(Long id, Boolean isvalide) {
        Optional<Cheque> optionalCheque = chequeRepository.findById(id);

        Cheque existingCheque = optionalCheque.get();

        existingCheque.setIsValid(isvalide);

        Cheque updatedCheque = chequeRepository.save(existingCheque);

        return updatedCheque;
    }
}
