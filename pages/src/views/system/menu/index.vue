<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-col :xs="24" :sm="24" :md="24" :lg="20" :xl="24">
        <!--工具栏-->
        <div class="head-container">
          <div v-if="crud.props.searchToggle">
            <!-- 搜索 -->
            <el-input
              v-model="query.blurry"
              clearable
              size="small"
              placeholder="输入用户名或昵称搜索"
              style="width: 300px; padding-right: 10px"
              class="filter-item"
              @keyup.enter.native="crud.toQuery"
            />
            <date-range-picker v-model="query.createTime" class="date-item"/>
            <rrOperation style="padding-left: 10px"/>
          </div>
          <crudOperation show="" :permission="permission"/>
        </div>
        <!--表单渲染-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU"
                   :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="580px">
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
            <el-form-item label="菜单类型" prop="type">
              <el-radio-group v-model="form.type" size="mini" style="width: 178px">
                <el-radio-button label="0">目录</el-radio-button>
                <el-radio-button label="1">菜单</el-radio-button>
                <el-radio-button label="2">按钮</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="菜单图标" prop="icon">
              <el-popover
                placement="bottom-start"
                width="450"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="selected"/>
                <el-input slot="reference" v-model="form.icon" style="width: 450px;" placeholder="点击选择图标" readonly>
                  <svg-icon v-if="form.icon" slot="prefix" :icon-class="form.icon" class="el-input__icon"
                            style="height: 32px;width: 16px;"/>
                  <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                </el-input>
              </el-popover>
            </el-form-item>
            <el-form-item label="启用" prop="status">
              <el-radio-group v-model="form.status" :disabled="form.name==='菜单管理'">
                <el-radio :label="0">禁用</el-radio>
                <el-radio :label="1">启用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="菜单标题" prop="name">
              <el-input v-model="form.name" placeholder="菜单标题"/>
            </el-form-item>
            <el-form-item label="路由地址" prop="uri">
              <el-input v-model="form.uri" placeholder="路由地址" style="width: 178px;"/>
            </el-form-item>
            <el-form-item label="菜单排序" prop="menuSort">
              <el-input-number v-model.number="form.sort" :min="0" :max="999" controls-position="right"
                               style="width: 178px;"/>
            </el-form-item>
            <el-form-item label="上级类目" prop="pid">
              <treeselect
                v-model="form.pid"
                :options="menus"
                :load-options="loadMenus"
                style="width: 450px;"
                placeholder="选择上级类目"
              />
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
          </div>
        </el-dialog>
        <!--表格渲染-->
        <el-table
          ref="table"
          v-loading="crud.loading"
          lazy
          :load="getMenus"
          :data="crud.data"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
          row-key="id"
          @select="crud.selectChange"
          @select-all="crud.selectAllChange"
          @selection-change="crud.selectionChangeHandler"
        >
          <el-table-column type="selection" width="55"/>
          <el-input v-model="form.id" type="hidden" :disabled="true"/>
          <el-table-column :show-overflow-tooltip="true" label="菜单标题" width="125px" prop="name"/>
          <el-table-column prop="icon" label="图标" align="center" width="60px">
            <template slot-scope="scope">
              <svg-icon :icon-class="scope.row.icon ? scope.row.icon : ''"/>
            </template>
          </el-table-column>
          <el-table-column prop="sort" align="center" label="排序">
            <template slot-scope="scope">
              {{ scope.row.sort }}
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="uri" label="组件路径"/>
          <el-table-column label="状态" align="center" width="115" prop="status">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status===1"
                active-color="#13ce66"
                inactive-color="#F56C6C"
                @change="changeEnabled(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建日期" width="205px">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="130px" align="center" fixed="right">
            <template slot-scope="scope">
              <udOperation
                :data="scope.row"
                :permission="permission"
                msg="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import crudMenu from '@/api/system/menu'
  import CRUD, {presenter, header, form, crud} from '@/components/CRUD/crud'
  import rrOperation from '@/components/CRUD/RR.operation'
  import crudOperation from '@/components/CRUD/CRUD.operation'
  import udOperation from '@/components/CRUD/UD.operation'
  import DateRangePicker from '@/components/DateRangePicker'
  import {mapGetters} from 'vuex'
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import IconSelect from '@/components/IconSelect'
  import { LOAD_CHILDREN_OPTIONS } from '@riophae/vue-treeselect'

  let userRoles = []
  const defaultForm = {
    id: null,
    name: null,
    sort: 999,
    uri: null,
    roles: [],
    pid: 0,
    icon: null,
    cache: false,
    hidden: false,
    type: 0,
    permission: null
  }
  export default {
    name: 'MenuInfo',
    components: {Treeselect, crudOperation, rrOperation, udOperation, DateRangePicker, IconSelect},
    cruds() {
      return CRUD({title: '菜单', url: 'system/menu', crudMethod: {...crudMenu}})
    },
    mixins: [presenter(), header(), form(defaultForm), crud()],
    data() {
      return {
        height: document.documentElement.clientHeight - 180 + 'px;',
        permission: {
          add: ['admin', 'user:add'],
          edit: ['admin', 'user:edit'],
          del: ['admin', 'user:del']
        },
        enabledTypeOptions: [
          {key: 'true', display_name: '激活'},
          {key: 'false', display_name: '锁定'}
        ],
        menus: [],
        rules: {}
      }
    },
    computed: {
      ...mapGetters([
        'user'
      ])
    },
    created() {
      this.crud.msg.add = '新增成功，默认密码：123456'
    },
    mounted: function () {
      const that = this
      window.onresize = function temp() {
        that.height = document.documentElement.clientHeight - 180 + 'px;'
      }
    },
    methods: {
      // 新增与编辑前做的操作
      [CRUD.HOOK.afterToCU](crud, form) {
        this.menus = []
        if (form.id != null) {
          if (form.pid === null) {
            form.pid = 0
          }
          console.log(form.pid)
          this.getSupDepts(form.id)
        } else {
          this.menus.push({id: 0, label: '顶级类目', children: null})
        }
      },
      // 改变状态
      changeEnabled(data) {
        this.$confirm('此操作将' + (data.status === 0 ? '激活菜单' : '冻结菜单') + data.username + ', 是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          data.status = (data.status + 1) % 2
          crudMenu.edit(data).then(res => {
            this.crud.notify('成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
          }).catch(() => {
            data.status = (data.status + 1) % 2
          })
        }).catch(() => {

        })
      },

      getMenus(tree, treeNode, resolve) {

        console.log("生效")
        const params = {pid: tree.id}
        setTimeout(() => {
          crudMenu.getMenus(params).then(res => {
            resolve(res.data.content)
          })
        }, 100)
      },
      getSupDepts(id) {
        crudMenu.getMenuParent(id).then(res => {
          const children = res.data.map(function (obj) {
            if (!obj.isParent && !obj.children) {
              obj.children = null
            }
            return obj
          })
          console.log(children)
          this.menus = [{id: 0, label: '顶级类目', children: children}]
        })
      },

      loadMenus({action, parentNode, callback}) {
        if (action === LOAD_CHILDREN_OPTIONS) {
          crudMenu.getMenusTree(parentNode.id).then(res => {
            parentNode.children = res.data.map(function (obj) {
              if (!obj.isParent) {
                obj.children = null
              }
              obj.hasChildren=true;
              return obj
            })
            setTimeout(() => {
              callback()
            }, 100)
          })
        }
      },

      // 选中图标
      selected(name) {
        this.form.icon = name
      },
      checkboxT(row, rowIndex) {
        return row.id !== this.user.id
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  ::v-deep .vue-treeselect__control, ::v-deep .vue-treeselect__placeholder, ::v-deep .vue-treeselect__single-value {
    height: 30px;
    line-height: 30px;
  }
</style>
