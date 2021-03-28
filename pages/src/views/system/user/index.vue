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
        <el-dialog
          append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU"
                   :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="570px">
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="66px">
            <el-input v-model="form.id" type="hidden" :disabled="true"/>
            <el-form-item v-if="crud.status.title==='编辑用户'" style="padding-left: 35%">
              <icon-operation
                :username="form.username"
                :icon="form.icon+''"
                :icon-width=300
                :icon-height=300
              />
            </el-form-item>
            <el-form-item label="用户名" prop="username" style="align-content: center">
              <el-input v-model="form.username" :disabled="crud.status.title!=='新增用户'"/>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email"/>
            </el-form-item>
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="form.nickName"/>
            </el-form-item>
            <el-form-item label="备注" prop="note">
              <el-input v-model="form.note"/>
            </el-form-item>
            <el-form-item label="启用" prop="status">
              <el-radio-group v-model="form.status" :disabled="form.username===user.name">
                <el-radio :label="0">禁用</el-radio>
                <el-radio :label="1">启用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item style="margin-bottom: 0;" label="角色" prop="roles">
              <el-select
                v-model="roleDatas"
                style="width: 437px"
                multiple
                placeholder="请选择"
                @remove-tag="deleteTag"
                @change="changeRole"
              >
                <el-option
                  v-for="item in roles"
                  :key="item.name"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
          </div>
        </el-dialog>
        <!--表格渲染-->
        <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;"
                  @selection-change="crud.selectionChangeHandler">
          <el-table-column :selectable="checkboxT" type="selection" width="55"/>
          <el-table-column :show-overflow-tooltip="true" width="200" prop="username" label="用户名"/>
          <el-table-column :show-overflow-tooltip="true" prop="nickName" width="200" label="昵称"/>
          <el-table-column :show-overflow-tooltip="true" prop="icon" width="135" label="头像">
            <template slot-scope="scope">
              <div class="avatar-wrapper">
                <img :src="scope.row.icon" width="35" height="35" alt="头像">
              </div>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="roles" label="角色">
            <template slot-scope="scope">
              <div v-for="item in scope.row.roles"
                   :key="item.name">
                {{item.name}}
              </div>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="email" label="邮箱"/>
          <el-table-column :show-overflow-tooltip="true" prop="note" label="标注"/>
          <el-table-column label="状态" align="center" width="115" prop="status">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status===1"
                :disabled="user.name === scope.row.username"
                active-color="#13ce66"
                inactive-color="#F56C6C"
                @change="changeEnabled(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="createTime" width="230" label="创建日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="loginTime" width="230" label="登录时间">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.loginTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="135"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <udOperation
                :data="scope.row"
                :permission="{
                  add: ['admin', 'user:add'],
                  edit: ['admin', 'user:edit'],
                  del: ['admin', 'user:del']
                }"
                :disabled-dle="scope.row.username === user.name"
              />
            </template>
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <pagination/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import crudUser from '@/api/system/user'
  import CRUD, {presenter, header, form, crud} from '@/components/CRUD/crud'
  import {getAll} from '@/api/system/role'
  import rrOperation from '@/components/CRUD/RR.operation'
  import crudOperation from '@/components/CRUD/CRUD.operation'
  import udOperation from '@/components/CRUD/UD.operation'
  import iconOperation from '@/components/CRUD/UICON.operation'
  import pagination from '@/components/CRUD/Pagination'
  import DateRangePicker from '@/components/DateRangePicker'
  import {mapGetters} from 'vuex'
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import {isvalidPhone} from '@/utils/validate'

  let userRoles = []
  const defaultForm = {
    id: null,
    icon: null,
    username: null,
    email: null,
    nickName: null,
    note: null,
    status: 0,
    roles: []
  }
  export default {
    name: 'UserInfo',
    components: {Treeselect, crudOperation, rrOperation, udOperation, iconOperation, pagination, DateRangePicker},
    cruds() {
      return CRUD({title: '用户', url: 'system/user', crudMethod: {...crudUser}})
    },
    mixins: [presenter(), header(), form(defaultForm), crud()],
    // 数据字典
    dicts: ['user_status'],
    data() {
      // 自定义验证
      const validPhone = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入电话号码'))
        } else if (!isvalidPhone(value)) {
          callback(new Error('请输入正确的11位手机号码'))
        } else {
          callback()
        }
      }
      return {
        height: document.documentElement.clientHeight - 180 + 'px;',
        roles: [],
        roleDatas: [], // 多选时使用
        permission: {
          add: ['admin', 'user:add'],
          edit: ['admin', 'user:edit'],
          del: ['admin', 'user:del']
        },
        enabledTypeOptions: [
          {key: 'true', display_name: '激活'},
          {key: 'false', display_name: '锁定'}
        ],
        rules: {
          username: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
          ],
          nickName: [
            {required: true, message: '请输入用户昵称', trigger: 'blur'},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
          ],
          email: [
            {required: true, message: '请输入邮箱地址', trigger: 'blur'},
            {type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur'}
          ]
        }
      }
    },
    computed: {
      ...mapGetters([
        'user',
        'name'
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
      changeRole(value) {
        userRoles = []
        value.forEach(function (data, index) {
          const role = {id: data}
          userRoles.push(role)
        })
      },
      deleteTag(value) {
        userRoles.forEach(function (data, index) {
          if (data.id === value) {
            userRoles.splice(index, value)
          }
        })
      },
      // 新增与编辑前做的操作
      [CRUD.HOOK.afterToCU](crud, form) {
        this.getRoles()
      },
      // 提交前做的操作
      [CRUD.HOOK.afterValidateCU](crud) {
        crud.form.roles = userRoles
        // crud.form.jobs = userJobs
        return true
      },
      // 新增前将多选的值设置为空
      [CRUD.HOOK.beforeToAdd]() {
        this.roleDatas = []
      },

      // 初始化编辑时候的角色与岗位
      [CRUD.HOOK.beforeToEdit](crud, form) {
        // this.getJobs(this.form.dept.id)
        // this.jobDatas = []
        this.roleDatas = []
        userRoles = []
        const _this = this
        form.roles.forEach(function (role, index) {
          _this.roleDatas.push(role.id)
          const rol = {id: role.id}
          userRoles.push(rol)
        })
      },
      // 改变状态
      changeEnabled(data) {
        this.$confirm('此操作将' + (data.status === 0 ? '激活用户' : '冻结用户') + data.username + ', 是否继续？', '提示', {
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
      // 获取弹窗内角色数据
      getRoles() {
        getAll().then(res => {
          this.roles = res.data.content
        }).catch(() => {
        })
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
