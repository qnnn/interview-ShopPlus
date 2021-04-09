<template>
  <elFrame :src="url" />
</template>
<script>
  import elFrame from '@/components/Iframe/index'
  export default {
    name: 'Nacos',
    components: { elFrame },
    data(){
      return{
        url: "http://192.168.229.150:8848/nacos/#/configurationManagement?dataId=&group=&appName=&namespace=&pageSize=&pageNo="
      }
    }
  }
</script>
