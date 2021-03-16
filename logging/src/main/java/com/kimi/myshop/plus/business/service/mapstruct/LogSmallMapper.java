package com.kimi.myshop.plus.business.service.mapstruct;

import com.kimi.myshop.plus.base.BaseMapper;
import com.kimi.myshop.plus.business.domain.Log;
import com.kimi.myshop.plus.business.service.dto.LogSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;



/**
 * @author 郭富城
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogSmallMapper extends BaseMapper<LogSmallDTO, Log> {

}
