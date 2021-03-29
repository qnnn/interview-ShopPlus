package com.kimi.myshop.plus.provider.mapStruct;

import com.kimi.myshop.plus.base.BaseMapper;
import com.kimi.myshop.plus.provider.domain.Menu;
import com.kimi.myshop.plus.provider.dto.MenuDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * @author 郭富城
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends BaseMapper<MenuDto, Menu> {
}
