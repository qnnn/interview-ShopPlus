package com.kimi.myshop.plus.cloud.feign.fallback;

import com.kimi.myshop.plus.cloud.dto.AdminLoginLogDTO;
import com.kimi.myshop.plus.cloud.feign.MessageFeign;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.commons.utils.MapperUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageFeignFallback implements MessageFeign {
    @Override
    public String sendAdminLoginLog(AdminLoginLogDTO adminLoginLogDTO) {
        try {
            return MapperUtils.obj2json(new ResponseResult<Void>(ResponseResult.CodeStatus.BREAKING,"网络出错！请重试"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
