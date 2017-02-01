package com.yzkj.controller;

import com.yzkj.dao.AccountMapper;
import com.yzkj.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Gorm on 2017/2/1.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountMapper accountMapper;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Account> getAccountAll() {
        return accountMapper.selectAll();
    }
}
