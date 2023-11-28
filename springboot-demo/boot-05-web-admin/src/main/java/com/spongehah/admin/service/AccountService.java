package com.spongehah.admin.service;

import com.spongehah.admin.bean.Account;
import com.spongehah.admin.mapper.AccountMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    @Autowired
    private AccountMapper accountMapper;

    private Counter counter;
    
    public AccountService(MeterRegistry meterRegistry) {
        counter = meterRegistry.counter("myservice.method.running.counter");
    }
    
    public Account getAccountById(Long id){
        counter.increment();
        return accountMapper.getAcct(id);
    }
}
