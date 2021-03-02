package com.kimi.myshop.plus.business.feign.fallback;

import com.kimi.myshop.plus.business.dto.UmsAdminDto;
import com.kimi.myshop.plus.business.dto.params.IconParam;
import com.kimi.myshop.plus.business.dto.params.ProfileParam;
import com.kimi.myshop.plus.business.feign.ProfileFeign;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.commons.utils.MapperUtils;
import org.springframework.stereotype.Component;

@Component
public class ProfileFeignFallback implements ProfileFeign {

    public static final String BREAKING_MESSAGE = "网络出现问题！请检查";

    @Override
    public String info(String username) {
        try {
            return MapperUtils.obj2json(new ResponseResult<Void>(ResponseResult.CodeStatus.BREAKING,BREAKING_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String update(ProfileParam profileParam) {
        try {
            return MapperUtils.obj2json(new ResponseResult<UmsAdminDto>(ResponseResult.CodeStatus.BREAKING,BREAKING_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String modifyIcon(IconParam iconParam) {
        try {
            return MapperUtils.obj2json(new ResponseResult<UmsAdminDto>(ResponseResult.CodeStatus.BREAKING,BREAKING_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
