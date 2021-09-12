<template>
  <div>
    <el-descriptions border :column="2">
      <el-descriptions-item label="名称">{{ info.name }}</el-descriptions-item>
      <el-descriptions-item label="是否内置">{{ info.internal ? "是" : "否" }}</el-descriptions-item>
    </el-descriptions>
    <el-table :data="info.partitions" stripe>
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-row>
            <el-col :span="20" :offset="4">
              <el-tabs>
                <el-tab-pane label="replicas">
                  <el-table :data="scope.row.replicas">
                    <el-table-column prop="id" label="id" width="50px"></el-table-column>
                    <el-table-column prop="host" label="主机"></el-table-column>
                    <el-table-column prop="port" label="端口"></el-table-column>
                  </el-table>
                </el-tab-pane>
                <el-tab-pane label="isr">
                  <el-table :data="scope.row.isr">
                    <el-table-column prop="id" label="id" width="50px"></el-table-column>
                    <el-table-column prop="host" label="主机"></el-table-column>
                    <el-table-column prop="port" label="端口"></el-table-column>
                  </el-table>
                </el-tab-pane>
              </el-tabs>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column prop="partition" label="partition" width="120px"></el-table-column>
      <el-table-column prop="leader" label="leader">
        <template slot-scope="scope">
          {{ scope.row.leader.host + ':' + scope.row.leader.port }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import api from '../api/admin'

export default {
  name: "Topic",
  props: {cluster: String, topic: String},
  data() {
    return {
      info: {
        name: "",
        internal: false,
        partitions: []
      }
    }
  }, methods: {
    getTopic() {
      api.getTopic(this.cluster, this.topic).then(res => {
        this.info = res.data;
      })
    }
  }, created() {
    this.getTopic();
  }
}
</script>

<style scoped>

</style>