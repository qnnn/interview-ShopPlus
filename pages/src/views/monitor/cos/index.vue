<template>
  <elFrame :src="url" />
</template>
<script>
  import elFrame from '@/components/Iframe/index'
  export default {
    name: 'Cos',
    components: { elFrame },
    data(){
      return{
        url: "https://console.cloud.tencent.com/cos5/bucket/setting?type=filelist&bucketName=data-repository-1304879813&path=&region=ap-guangzhou"
      }
    }
  }
</script>
