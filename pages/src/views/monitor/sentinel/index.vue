<template>
  <elFrame :src="url" />
</template>
<script>
  import elFrame from '@/components/Iframe/index'
  export default {
    name: 'Sentinel',
    components: { elFrame },
    data(){
      return{
        url: "http://localhost:8765/#/dashboard"
      }
    }
  }
</script>
