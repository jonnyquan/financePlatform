package com.sunlights.core.service;

import com.sunlights.core.models.Bank;
import com.sunlights.core.vo.BankCardVo;
import com.sunlights.core.vo.BankVo;
import com.sunlights.common.page.Pager;

import java.util.List;

/**
 * <p>Project: fsp</p>
 * <p>Title: BankService.java</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2014 Sunlights.cc</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:zhencai.yuan@sunlights.cc">yuanzhencai</a>
 */
public interface BankService {
    public List<BankVo> findBanksBy(Pager pager);

    public BankVo findBankByBankCardNo(String bankCardNo);
    public Bank findBankByBankCode(String bankCode);

    public boolean validateBankCard(String token, BankCardVo bankCardVo);
}