/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
