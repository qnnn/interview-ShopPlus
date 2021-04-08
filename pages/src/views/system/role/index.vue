<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.blurry" size="small" clearable placeholder="输入名称或者描述搜索" style="width: 200px;"
                  class="filter-item" @keyup.enter.native="crud.toQuery"/>
        <date-range-picker v-model="query.createTime" class="date-item"/>
        <rrOperation/>
      </div>
      <crudOperation :permission="permission"/>
    </div>
    <!-- 表单渲染 -->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU"
               :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="520px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" style="width: 380px;"/>
        </el-form-item>
        <el-form-item label="描述信息" prop="description">
          <el-input v-model="form.description" style="width: 380px;" rows="5" type="textarea"/>
        </el-form-item>
        <el-form-item label="启用" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">启用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色排序" prop="menuSort" style="padding-right: 20px">
          <el-input-number v-model.number="form.sort" :min="0" :max="999" controls-position="right"
                           style="width: 178px;"/>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
    <el-row :gutter="15">
      <!--角色管理-->
      <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="17" style="margin-bottom: 10px">
        <el-table ref="table" v-loading="crud.loading" highlight-current-row style="width: 100%;" :data="crud.data"
                  @selection-change="crud.selectionChangeHandler" @current-change="handleCurrentChange">
          <el-table-column :selectable="checkboxT" type="selection" width="55"/>
          <el-table-column prop="name" label="名称"/>
          <el-table-column prop="sort" label="排序"/>
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status===1"
                active-color="#13ce66"
                inactive-color="#F56C6C"
                @change="changeEnabled(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="description" label="描述"/>
          <el-table-column :show-overflow-tooltip="true" width="200px" prop="createTime" label="创建日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="135px" align="center"
                           fixed="right">
            <template slot-scope="scope">
              <udOperation
                :data="scope.row"
                :permission="permission"
              />
            </template>
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <pagination/>
      </el-col>
      <!-- 菜单授权 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="7">


        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <el-tooltip class="item" effect="dark" content="选择指定角色分配菜单" placement="top">
              <span class="role-span">菜单分配</span>
            </el-tooltip>
            <el-button
              :disabled="!showButton"
              :loading="menuLoading"
              icon="el-icon-check"
              size="mini"
              style="float: right; padding: 6px 9px"
              type="primary"
              @click="saveMenu"
            >保存
            </el-button>
          </div>
          <el-tree
            ref="menu"
            lazy
            :data="menus"
            :default-checked-keys="menuIds"
            :load="getMenuDatas"
            :props="defaultProps"
            check-strictly
            accordion
            show-checkbox
            node-key="id"
            @check="menuChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import crudRoles from '@/api/system/role'
  import {getMenusTree, getChild} from '@/api/system/menu'
  import CRUD, {presenter, header, form, crud} from '@/components/CRUD/crud'
  import rrOperation from '@/components/CRUD/RR.operation'
  import crudOperation from '@/components/CRUD/CRUD.operation'
  import udOperation from '@/components/CRUD/UD.operation'
  import pagination from '@/components/CRUD/Pagination'
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import {LOAD_CHILDREN_OPTIONS} from '@riophae/vue-treeselect'
  import DateRangePicker from '@/components/DateRangePicker'
  import crudUser from "@/api/system/user";

  const defaultForm = {id: null, name: null, description: null, status: 0, sort: 999}
  export default {
    name: 'RoleInfo',
    components: {Treeselect, pagination, crudOperation, rrOperation, udOperation, DateRangePicker},
    cruds() {
      return CRUD({title: '角色', url: 'system/role', sort: 'sort,asc', crudMethod: {...crudRoles}})
    },
    mixins: [presenter(), header(), form(defaultForm), crud()],
    data() {
      return {
        defaultProps: {children: 'children', label: 'label', isLeaf: 'leaf'},
        currentId: 0, menuLoading: false, showButton: false,
        menus: [], menuIds: [],  // 多选时使用
        permission: {
          add: ['admin', 'roles:add'],
          edit: ['admin', 'roles:edit'],
          del: ['admin', 'roles:del']
        },
        rules: {},
        interDialog: false
      }
    },
    created() {

    },
    methods: {
      getMenuDatas(node, resolve) {
        setTimeout(() => {
          getMenusTree(node.data.id ? node.data.id : 0).then(res => {
            resolve(res.data)
          })
        }, 100)
      },
      [CRUD.HOOK.afterRefresh]() {
        this.$refs.menu.setCheckedKeys([])
      },

      // 提交前做的操作
      // [CRUD.HOOK.afterValidateCU](crud) {
      //   // if (crud.form.dataScope === '自定义' && this.deptDatas.length === 0) {
      //   //   this.$message({
      //   //     message: '自定义数据权限不能为空',
      //   //     type: 'warning'
      //   //   })
      //   //   return false
      //   // } else if (crud.form.dataScope === '自定义') {
      //   //   const depts = []
      //   //   this.deptDatas.forEach(function (data) {
      //   //     const dept = {id: data}
      //   //     depts.push(dept)
      //   //   })
      //   //   crud.form.depts = depts
      //   // } else {
      //   //   crud.form.depts = []
      //   // }
      //   // return true
      // },

      // 触发单选
      handleCurrentChange(val) {
        if (val) {
          const _this = this
          // 清空菜单的选中
          this.$refs.menu.setCheckedKeys([])
          // 保存当前的角色id
          this.currentId = val.id
          // 初始化默认选中的key
          this.menuIds = []
          val.menus.forEach(function (data) {
            _this.menuIds.push(data.id)
          })
          this.showButton = true
        }
      },
      menuChange(menu) {
        if (this.menuIds.indexOf(menu.id) !== -1) {
          const index = this.menuIds.indexOf(menu.id)
          if (index !== -1) {
            this.menuIds.splice(index, 1)
          }
        } else {
          this.menuIds.push(menu.id)
        }
        this.$refs.menu.setCheckedKeys(this.menuIds)

        // 获取该节点的所有子节点，id 包含自身
        // getChild(menu.id).then(childIds => {
        //   // 判断是否在 menuIds 中，如果存在则删除，否则添加
        //   if (this.menuIds.indexOf(menu.id) !== -1) {
        //     for (let i = 0; i < childIds.length; i++) {
        //       const index = this.menuIds.indexOf(childIds[i])
        //       if (index !== -1) {
        //         this.menuIds.splice(index, 1)
        //       }
        //     }
        //   } else {
        //     for (let i = 0; i < childIds.length; i++) {
        //       this.menuIds.push(childIds[i])
        //     }
        //   }
        //   this.$refs.menu.setCheckedKeys(this.menuIds)
        // })
      },
      // 保存菜单
      saveMenu() {
        this.menuLoading = true
        const role = {id: this.currentId, menus: []}
        // 得到已选中的 key 值
        this.menuIds.forEach(function (id) {
          const menu = {id: id}
          role.menus.push(menu)
        })
        crudRoles.editMenu(role).then(() => {
          this.crud.notify('保存成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
          this.menuLoading = false
          this.update()
        }).catch(err => {
          this.menuLoading = false
          console.log(err.response.data.message)
        })
      },
      // 改变数据
      update() {
        // 无刷新更新 表格数据
        crudRoles.get(this.currentId).then(res => {
          for (let i = 0; i < this.crud.data.length; i++) {
            if (res.data.id === this.crud.data[i].id) {
              this.crud.data[i] = res
              break
            }
          }
        })
      },
      // 改变状态
      changeEnabled(data) {
        this.$confirm('此操作将' + (data.status === 0 ? '激活角色' : '冻结角色') + data.name + ', 是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          data.status = (data.status + 1) % 2
          crudUser.edit(data).then(res => {
            this.crud.notify('成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
          }).catch(() => {
            data.status = (data.status + 1) % 2
          })
        }).catch(() => {

        })
      },

      checkboxT(row) {
        return row.id !== 5
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
  .role-span {
    font-weight: bold;
    color: #303133;
    font-size: 15px;
  }
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
  ::v-deep .el-input-number .el-input__inner {
    text-align: left;
  }

  ::v-deep .vue-treeselect__multi-value {
    margin-bottom: 0;
  }

  ::v-deep .vue-treeselect__multi-value-item {
    border: 0;
    padding: 0;
  }
</style>
