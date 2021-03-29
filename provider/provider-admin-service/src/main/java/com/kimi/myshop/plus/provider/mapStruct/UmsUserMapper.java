package com.kimi.myshop.plus.provider.mapStruct;

import com.kimi.myshop.plus.base.BaseMapper;
import com.kimi.myshop.plus.provider.domain.User;
import com.kimi.myshop.plus.provider.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * @author 郭富城
 */
@Mapper(componentModel = "spring",uses = {RoleMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UmsUserMapper extends BaseMapper<UserDto, User> {

}
