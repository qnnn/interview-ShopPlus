<template>
  <div class="components-container" style="align-content: center;margin: 1px">
    <pan-thumb :image="image" width="130px" height="130px" style="padding-right: 30%"/>
    <el-button
      type="primary"
      size="small"
      icon="el-icon-upload"
      style="position: relative;bottom: 10px"
      @click="toggleShow"
    >
      修改头像
    </el-button>

    <!--  field 是字段名  -->
    <image-cropper
      v-model="show"
      field="multipartFile"
      :width="300"
      :height="300"
      :url="url"
      :params="params"
      :headers="headers"
      img-format="png"
      @crop-success="cropSuccess"
      @crop-upload-success="cropUploadSuccess"
      @crop-upload-fail="cropUploadFail"
    />
  </div>
</template>


<script>
  import PanThumb from '@/components/PanThumb'
  import ImageCropper from 'vue-image-crop-upload'
  import {modifyIcon} from '@/api/profile'
  import {getToken} from "@/utils/auth"
  import CRUD, {crud} from '@/components/CRUD/crud'


  export default {
    components: {ImageCropper, PanThumb},
    mixins: [crud()],
    props: {
      username: {
        type: String,
        required: false
      },
      icon: {
        type: String,
        required: false
      },
      iconWidth: {
        type: Number,
        required: false
      },
      iconHeight: {
        type: Number,
        required: false
      },
      msg: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        url: process.env.VUE_APP_BASE_API + '/upload',
        show: false,
        image: this.icon,
        params: {
          access_token: getToken()
        },
        headers: {
          smail: '*_~'
        }
      }
    },
    methods: {
      toggleShow() {
        this.show = !this.show
      },
      cropSuccess(image, field) {
        this.image = image
      },
      /**
       * upload success
       *
       * [param] jsonData  server api return data, already json encode
       * [param] field
       */
      cropUploadSuccess(jsonData, field) {
        console.log('-------- 图片上传成功 --------')
        console.log('field: ' + field)


        modifyIcon({
          username: this.username,
          path: jsonData.data.path
        }).then(response => {
          this.$message({
            message: response.message,
            type: 'success'
          })
          this.crud.refresh();
          this.username === this.$store.getters.name ?
            this.$store.dispatch('user/resetAvatar', jsonData.data.path) :
            () => {

            }
        }).catch(() => {
          this.crud.refresh();
        })
      },
      /**
       * upload fail
       *
       * [param] status    server api return error status, like 500
       * [param] field
       */
      cropUploadFail(status, field) {
        console.log('-------- 图片上传失败 --------')
        console.log('field: ' + field)
      }
    }

  }

</script>

<style scoped>

</style>
