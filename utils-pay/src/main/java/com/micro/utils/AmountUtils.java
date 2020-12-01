package com.micro.utils;

import com.micro.common.Contanst;

import java.math.BigDecimal;

public final class AmountUtils {

    public static String getAmount(Integer months){
        if (months==null){
            throw new RuntimeException("购买月份有问题");
        }
        /**
         *  1月份 10块钱
         *  1季度 25块钱
         *  1年份 90块钱
         */
        if (months.equals(Contanst.ONE_MONTH)){
            return BigDecimal.valueOf(10.00).toString();
        }else if (months.equals(Contanst.ONE_QUARTER)){
            return BigDecimal.valueOf(25.00).toString();
        }else if (months.equals(Contanst.ONE_YEAR)){
            return BigDecimal.valueOf(90.00).toString();
        }
        return BigDecimal.valueOf(00.00).toString();
    }

}
