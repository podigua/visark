<template>
  <div>
    <el-table :data="list" stripe>
      <el-table-column prop="id" label="id" width="50px"></el-table-column>
      <el-table-column prop="host" label="主机" width="120px"></el-table-column>
      <el-table-column prop="port" label="端口" width="50px"></el-table-column>
      <el-table-column prop="rack" label="机架信息" show-overflow-tooltip></el-table-column>
    </el-table>
  </div>
</template>

<script>
import api from '../api/admin'
export default {
  name: "NodeList",
  props:{cluster:String},
  data(){
    return{
      list:[]
    }
  },methods:{
    getNodes(){
      api.getNodes(this.cluster).then(res=>{
        this.list=res.data;
      })
    }
  },created() {
    this.getNodes();
  }
}
</script>

<style scoped>

</style>