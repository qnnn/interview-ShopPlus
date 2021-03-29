package com.kimi.myshop.plus.provider.mapStruct;

import com.kimi.myshop.plus.base.BaseMapper;
import com.kimi.myshop.plus.provider.domain.Role;
import com.kimi.myshop.plus.provider.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * @author 郭富城
 */
@Mapper(componentModel = "spring", uses = {MenuMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseMapper<RoleDto, Role> {

}
