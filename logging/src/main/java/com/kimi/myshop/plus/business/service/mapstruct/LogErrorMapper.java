package com.kimi.myshop.plus.business.service.mapstruct;

import com.kimi.myshop.plus.base.BaseMapper;
import com.kimi.myshop.plus.business.domain.Log;
import com.kimi.myshop.plus.business.service.dto.LogErrorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * DTO 与 Entity互相转换代码生成器
 * @author 郭富城
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogErrorMapper extends BaseMapper<LogErrorDTO, Log> {

}
