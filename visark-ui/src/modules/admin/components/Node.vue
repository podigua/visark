<template>
  <div>
    <el-descriptions border :column="1">
      <el-descriptions-item label="id">{{node.id}}</el-descriptions-item>
      <el-descriptions-item label="主机">{{ node.host }}</el-descriptions-item>
      <el-descriptions-item label="端口">{{node.port}}</el-descriptions-item>
      <el-descriptions-item label="机架信息">{{ node.rack }}</el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
import api from '../api/admin'

export default {
  name: "Node",
  props: {cluster: String, id: String},
  data() {
    return {
      node: {
        id: undefined,
        host: "",
        port: undefined,
        rack: ""
      }
    }
  }, methods: {
    getNode() {
      api.getNode(this.cluster, this.id).then(res => {
        this.node = res.data;
      })
    }
  }, created() {
    this.getNode();
  }
}
</script>

<style scoped>

</style>