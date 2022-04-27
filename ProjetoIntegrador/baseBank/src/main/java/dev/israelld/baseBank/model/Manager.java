package dev.israelld.baseBank.model;

import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

@Entity
public class Manager extends Person{

    @OneToOne
    private Agency agency;

    public Agency getAgency() {
        return agency;
    }
    public void setAgency(Agency agency) {
        this.agency = agency;
    }

}
