package com.yzkj.controller;


import com.yzkj.mybatis.dao.AccountMapper;
import com.yzkj.mybatis.model.Account;
import com.yzkj.service.inter.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private ILoginService loginService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Account> getAccountAll() {
        return null;
    }



    @RequestMapping(value = "/login/{code}", method = RequestMethod.GET)
    public String onLogin( @PathVariable("code") String js_code) {

        String appid = "wxaad176c2db5e6591";
        String secret = "9faed2137a322227f4d5139718eed8d3";
        String grant_type="authorization_code";
        loginService.login(appid,secret,js_code,grant_type);

        return null;
    }

    @RequestMapping(value="/add")
    public int onAdd(Account account){
        return accountMapper.insert(account);
    }
}
