package com.spongehah.admin.mapper;

import com.spongehah.admin.bean.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {


    Account getAcct(@Param("id") Long id);
}
