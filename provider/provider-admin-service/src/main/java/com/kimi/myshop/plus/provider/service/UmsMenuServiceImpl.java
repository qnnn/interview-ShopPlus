package com.kimi.myshop.plus.provider.service;

import cn.hutool.core.util.ObjectUtil;
import com.kimi.myshop.plus.commons.utils.PageUtil;
import com.kimi.myshop.plus.commons.utils.QueryHelp;
import com.kimi.myshop.plus.provider.api.UmsMenuService;
import com.kimi.myshop.plus.provider.domain.Menu;
import com.kimi.myshop.plus.provider.dto.MenuDto;
import com.kimi.myshop.plus.provider.dto.MenuQueryCriteria;
import com.kimi.myshop.plus.provider.mapStruct.MenuMapper;
import com.kimi.myshop.plus.provider.repository.UmsMenuRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.by;

/**
 * @author 郭富城
 */
@Service(version = "1.0.0")
public class UmsMenuServiceImpl implements UmsMenuService {
    @Resource
    UmsMenuRepository umsMenuRepository;

    @Resource
    MenuMapper menuMapper;

    @Override
    public Object selectAll(MenuQueryCriteria criteria) throws IllegalAccessException {
        // 查询 Sort.Direction.ASC, "sort"
        // 父类默认为空，方便模糊查询
        criteria.setPidIsNull(true);
        List<Field> allFields = QueryHelp.getAllFields(criteria.getClass(), new ArrayList<>());
        for (Field allField : allFields) {
            // 添加访问权限，才能访问私有属性， 不然会报错
            allField.setAccessible(true);
            Object o = allField.get(criteria);
            if ("pidIsNull".equals(allField.getName())){
                continue;
            }
            if (ObjectUtil.isNotNull(o)){
                criteria.setPidIsNull(null);
                break;
            }
        }
        List<Menu> result = umsMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),Sort.by(Sort.Direction.ASC,"sort"));
        List<MenuDto> menuDtos = menuMapper.toDto(result);
        return PageUtil.toPage(menuDtos,menuDtos.size());
    }


    @Override
    public void save(Menu menu) {
        umsMenuRepository.save(menu);
    }

    @Override
    public void deleteMulti(Set<Long> ids) {

    }

    @Override
    public MenuDto findById(Long id) {
        // 通过id查找，若为空则新建一个
        Menu menu = umsMenuRepository.findById(id).orElseGet(Menu::new);
        return menuMapper.toDto(menu);
    }

    @Override
    public List<MenuDto> getParent(MenuDto menuDto, List<Menu> menus) {
        // pid为空，载入所有pid为空的数据
        if (menuDto.getPid() == null) {
            menus.addAll(umsMenuRepository.findByPidIsNull());
            return menuMapper.toDto(menus);
        }
        // pid不为空，载入所有菜单中父级类目为pid的数据
        menus.addAll(umsMenuRepository.findByPid(menuDto.getPid()));
        return getParent(findById(menuDto.getPid()), menus);
    }

    @Override
    public List<MenuDto> getTree(List<MenuDto> menuDtos) {
        List<MenuDto> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuDto menuDto : menuDtos) {
            // 顶级结点入队列
            if (menuDto.getPid() == null) {
                trees.add(menuDto);
            }
            for (MenuDto dto : menuDtos) {
                // 查找子结点
                if (menuDto.getId().equals(dto.getPid())) {
                    if (menuDto.getChildren() == null) {
                        menuDto.setChildren(new ArrayList<>());
                    }
                    // 孩子增加
                    menuDto.getChildren().add(dto);
                    ids.add(dto.getId());
                }
            }
        }
        // 模糊查询结果不包括父级结点的数据
        if (trees.size() == 0) {
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    @Override
    public List<MenuDto> getMenus(Long pid) {
        List<Menu> menus;
        if (pid != null && !pid.equals(0L)) {
            menus = umsMenuRepository.findByPid(pid);
        } else {
            menus = umsMenuRepository.findByPidIsNull();
        }
        return menuMapper.toDto(menus);
    }


}
