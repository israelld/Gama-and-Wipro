package dev.israelld.baseBank.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "C")
public class AccountCurrent extends Account{

    @Override
    public String toString() {
        return "AccountCurrent{} " + super.toString();
    }
}