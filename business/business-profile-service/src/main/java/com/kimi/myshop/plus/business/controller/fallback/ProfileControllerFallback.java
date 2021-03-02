package com.kimi.myshop.plus.business.controller.fallback;

import com.kimi.myshop.plus.business.dto.UmsAdminDto;
import com.kimi.myshop.plus.business.feign.fallback.ProfileFeignFallback;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileControllerFallback {

    public static final Logger logger = LoggerFactory.getLogger(ProfileControllerFallback.class);

    public static ResponseResult<UmsAdminDto> getInfoFallback(String username,Throwable throwable){
        logger.warn("invoke getInfoFallback" + throwable.getClass().getTypeName());

        return new ResponseResult<>(ResponseResult.CodeStatus.BREAKING, ProfileFeignFallback.BREAKING_MESSAGE);
    }
}
