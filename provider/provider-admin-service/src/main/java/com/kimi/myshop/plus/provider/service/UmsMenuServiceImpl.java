package com.kimi.myshop.plus.provider.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kimi.myshop.plus.business.BadRequestException;
import com.kimi.myshop.plus.commons.utils.PageUtil;
import com.kimi.myshop.plus.commons.utils.QueryHelp;
import com.kimi.myshop.plus.provider.api.UmsMenuService;
import com.kimi.myshop.plus.provider.api.UmsRoleService;
import com.kimi.myshop.plus.provider.domain.Menu;
import com.kimi.myshop.plus.provider.dto.MenuDto;
import com.kimi.myshop.plus.provider.dto.MenuQueryCriteria;
import com.kimi.myshop.plus.provider.dto.RoleDto;
import com.kimi.myshop.plus.provider.mapStruct.MenuMapper;
import com.kimi.myshop.plus.provider.repository.UmsMenuRepository;
import com.kimi.myshop.plus.provider.vo.MenuMetaVo;
import com.kimi.myshop.plus.provider.vo.MenuVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityExistsException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author 郭富城
 */
@Service(version = "1.0.0")
public class UmsMenuServiceImpl implements UmsMenuService {
    @Resource
    UmsMenuRepository umsMenuRepository;

    @Resource
    UmsRoleService roleService;

    @Resource
    MenuMapper menuMapper;

    @Override
    public Object selectAll(MenuQueryCriteria criteria) throws IllegalAccessException {
        // 查询 Sort.Direction.ASC, "sort"
        // 父类默认为空，方便模糊查询和懒加载展开
        criteria.setPidIsNull(true);
        // 通过反射得到对应数据，若为模糊查询和展开子菜单则令其pid为null
        List<Field> allFields = QueryHelp.getAllFields(criteria.getClass(), new ArrayList<>());
        for (Field allField : allFields) {
            // 添加访问权限，才能访问私有属性， 不然会报错
            allField.setAccessible(true);
            Object o = allField.get(criteria);
            if ("pidIsNull".equals(allField.getName())) {
                continue;
            }
            if (ObjectUtil.isNotNull(o)) {
                criteria.setPidIsNull(null);
                break;
            }
        }
        List<Menu> result = umsMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), Sort.by(Sort.Direction.ASC, "sort"));
        List<MenuDto> menuDtos = menuMapper.toDto(result);
        return PageUtil.toPage(menuDtos, menuDtos.size());
    }

    @Override
    public List<MenuDto> findByUser(String currentUsername) {
        List<RoleDto> roles = roleService.findByUsersName(currentUsername);
        Set<Long> roleIds = roles.stream().map(RoleDto::getId).collect(Collectors.toSet());
        LinkedHashSet<Menu> menus = umsMenuRepository.findByRoleIds(roleIds);
        return menus.stream().map(menuMapper::toDto).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Menu menu) {
        // 已存在相同标题
        if (umsMenuRepository.findByName(menu.getName()) != null) {
            throw new EntityExistsException("创建菜单失败，已存在相同名称的菜单!");
        }
        if (umsMenuRepository.findByUri(menu.getUri()) != null) {
            throw new EntityExistsException("创建菜单失败，已存在相同路径!");
        }
        if (menu.getPid().equals(0L)) {
            menu.setPid(null);
            // 新增的都不含子菜单
            menu.setIsParent(false);
            umsMenuRepository.save(menu);
        } else {
            Menu parent = umsMenuRepository.findById(menu.getPid()).orElseGet(Menu::new);
            if (!parent.getIsParent()) {
                parent.setIsParent(true);
                umsMenuRepository.save(parent);
            }
            menu.setIsParent(false);
            umsMenuRepository.save(menu);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Menu menu) {
        if (menu.getId().equals(menu.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Menu byName = umsMenuRepository.findByName(menu.getName());
        if (byName != null && !byName.getId().equals(menu.getId())) {
            throw new EntityExistsException("更新菜单失败，已存在相同名称标题!");
        }
        Menu byUri = umsMenuRepository.findByUri(menu.getUri());
        if (byUri != null && !byUri.getId().equals(menu.getId())) {
            throw new EntityExistsException("更新菜单失败，已存在相同路径!");
        }
        if (menu.getPid()!=null && menu.getPid().equals(0L)) {
            menu.setPid(null);
        }

        // 旧数据
        Menu oldMenu = umsMenuRepository.findById(menu.getId()).orElseGet(Menu::new);
        // 旧数据父级不为空修改
        if (oldMenu.getPid() != null) {
            // 旧数据的父节点
            Menu parent = umsMenuRepository.findById(oldMenu.getPid()).orElseGet(Menu::new);
            List<Menu> allChild = umsMenuRepository.findByPid(oldMenu.getPid());
            // 长度为1表示父级类目只含此结点，若修改父级则重置isParent
            boolean flag = (allChild.size() == 1) && (menu.getPid() == null || !oldMenu.getPid().equals(menu.getPid()));
            if (flag) {
                parent.setIsParent(false);
                umsMenuRepository.save(parent);
            }
        }
        // 新数据父级不为空
        if (menu.getPid() != null) {
            // 新数据的父结点
            Menu newParent = umsMenuRepository.findById(menu.getPid()).orElseGet(Menu::new);
            List<Menu> allChild1 = umsMenuRepository.findByPid(menu.getPid());
            // 长度为0表示该新父级菜单不含子菜单
            boolean flag = (allChild1.size() == 0) && (oldMenu.getPid() == null || !oldMenu.getPid().equals(menu.getPid()));
            if (flag) {
                newParent.setIsParent(true);
                umsMenuRepository.save(newParent);
            }
        }

        umsMenuRepository.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMulti(Set<Long> ids) {
        for (Long id : ids) {
            Menu menu = umsMenuRepository.findById(id).orElseGet(Menu::new);
            if (menu.getId() == null) {
                continue;
            }
            List<Menu> sons = umsMenuRepository.findByPid(id);
            recursionDelete(sons);

            // 若父级结点不为空，则更新父级结点 isParent属性
            if (menu.getPid()!=null&&!menu.getPid().equals(0L)){
                Menu parent = umsMenuRepository.findById(menu.getPid()).orElseGet(Menu::new);
                List<Menu> parentLength = umsMenuRepository.findByPid(menu.getPid());
                boolean flag = (parentLength.size() == 1 && parent != null);
                if (flag){
                    parent.setIsParent(false);
                }
            }
            umsMenuRepository.deleteRoleMap(menu.getId());
            umsMenuRepository.delete(menu);
        }
    }

    /**
     *  递归删除数据
     * @param menus 菜单
     */
    private void recursionDelete(List<Menu> menus) {
        if (!menus.isEmpty()) {
            for (Menu menu : menus) {
                List<Menu> sons = umsMenuRepository.findByPid(menu.getId());
                recursionDelete(sons);
            }
        }
        for (Menu menu : menus) {
            umsMenuRepository.deleteRoleMap(menu.getId());
            umsMenuRepository.delete(menu);
        }
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

    @Override
    public List<MenuVo> buildMenus(List<MenuDto> menuDtos) {
        List<MenuVo> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO!=null){
                        List<MenuDto> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getRouterName())  ? menuDTO.getRouterName() : menuDTO.getName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getUri() :menuDTO.getUri());
                        menuVo.setHidden(menuDTO.getStatus().equals(0));
                        // 非外链
                        if (!menuDTO.getExternalLink()){
                            if(menuDTO.getPid() == null){
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getValue())?"Layout":menuDTO.getValue());
                            }else if(!StrUtil.isEmpty(menuDTO.getValue())){
                                menuVo.setComponent(menuDTO.getValue());
                            }
                        }

                        menuVo.setMeta(new MenuMetaVo(menuDTO.getName(),menuDTO.getIcon()));
                        if(menuDtoList !=null && menuDtoList.size()!=0){
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        }
                        else if(menuDTO.getPid() == null){
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if(!menuDTO.getExternalLink()){
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getUri());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }


}
