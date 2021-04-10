<template>
  <elFrame :src="url" />
</template>
<script>
  import elFrame from '@/components/Iframe/index'
  export default {
    name: 'RockMq',
    components: { elFrame },
    data(){
      return{
        url: "http://192.168.229.150:8080/#/topic"
      }
    }
  }
</script>
