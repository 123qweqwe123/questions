package com.lmbx.csp.web.common.service.impl;

import com.google.common.collect.Maps;
import com.lmbx.csp.core.constant.AppConsts;
import com.lmbx.csp.core.exception.ServiceException;
import com.lmbx.csp.data.sys.domain.SysParameter;
import com.lmbx.csp.data.sys.domain.SysParameterExample;
import com.lmbx.csp.data.sys.mapper.SysParameterMapper;
import com.lmbx.csp.thirdservice.fdfs.FileService;
import com.lmbx.csp.web.common.qo.AutoCompleteQO;
import com.lmbx.csp.web.common.service.CommonService;
import com.lmbx.csp.web.common.vo.AutoCompleteVO;
import com.lmbx.csp.web.common.vo.SelectVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.csource.common.NameValuePair;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * 
 * <pre>
 * 模块共通插件调用
 * </pre>
 * 
 * @author javahuang
 * @since 2017/11/24 上午10:11
 */
@Service
public class CommonServiceImpl implements CommonService {

    Logger                                     logger                = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private SysParameterMapper                 sysParameterMapper;

    @Autowired
    private FileService                        fileService;
    @Override
    public String upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extName = StringUtils.substringAfterLast(fileName, ".");
        try {
            NameValuePair[] pairs = new NameValuePair[1];
            pairs[0] = new NameValuePair("name", fileName);
            return fileService.uploadFile(file.getBytes(), extName, pairs);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件上传失败，文件名：{}", fileName);
            throw new ServiceException("上传失败，请联系管理员!");
        }
    }

    @Override
    public byte[] download(String fileId) {
        return fileService.downloadFile(fileId);
    }

    @Override
    public Map<String, Object> downloadWithMetaData(String fileId) {
        return fileService.downloadFileWithMetaData(fileId);
    }
}
