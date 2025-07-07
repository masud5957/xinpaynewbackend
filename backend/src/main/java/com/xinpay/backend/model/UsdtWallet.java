package com.xinpay.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usdt_wallet")
public class UsdtWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trc20_address", nullable = false)
    private String trc20Address;

    public UsdtWallet() {}

    public UsdtWallet(String trc20Address) {
        this.trc20Address = trc20Address;
    }

    public Long getId() {
        return id;
    }

    public String getTrc20Address() {
        return trc20Address;
    }

    public void setTrc20Address(String trc20Address) {
        this.trc20Address = trc20Address;
    }
}
