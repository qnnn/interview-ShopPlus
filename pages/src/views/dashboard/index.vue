<template>
  <div class="dashboard-container">
    <div class="dashboard-text">欢迎 {{ name }}</div>
    <el-button type="primary" @click=testBuildMenu>测试</el-button>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { buildMenus } from '@/api/system/menu'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'permission_routers',
      'name'
    ])
  },
  methods: {
    testBuildMenu(){
      console.log(this.permission_routers)
      for (const permissionRoutersKey in this.permission_routers) {
        console.log(permissionRoutersKey.path)
      }
    }

  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
