package com.bdcor.pip.web.spem.dao;

import com.bdcor.pip.web.spem.domain.PipScmTransProcMasterMail;
import com.bdcor.pip.web.spem.domain.PipScmTransProcMasterMailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PipScmTransProcMasterMailMapper {
    long countByExample(PipScmTransProcMasterMailExample example);

    int deleteByExample(PipScmTransProcMasterMailExample example);

    int deleteByPrimaryKey(String id);

    int insert(PipScmTransProcMasterMail record);

    int insertSelective(PipScmTransProcMasterMail record);

    List<PipScmTransProcMasterMail> selectByExample(PipScmTransProcMasterMailExample example);

    PipScmTransProcMasterMail selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") PipScmTransProcMasterMail record, @Param("example") PipScmTransProcMasterMailExample example);

    int updateByExample(@Param("record") PipScmTransProcMasterMail record, @Param("example") PipScmTransProcMasterMailExample example);

    int updateByPrimaryKeySelective(PipScmTransProcMasterMail record);

    int updateByPrimaryKey(PipScmTransProcMasterMail record);
}