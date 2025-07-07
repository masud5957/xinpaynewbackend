package com.xinpay.backend.service;

import com.xinpay.backend.model.UsdtWallet;
import com.xinpay.backend.repository.UsdtWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsdtWalletService {

    @Autowired
    private UsdtWalletRepository walletRepository;

    public String getLatestTrc20Address() {
        UsdtWallet wallet = walletRepository.findTopByOrderByIdDesc();
        return wallet != null ? wallet.getTrc20Address() : null;
    }

    public String updateTrc20Address(String newAddress) {
        UsdtWallet wallet = new UsdtWallet(newAddress);
        walletRepository.save(wallet);
        return "TRC20 address updated successfully";
    }
}
