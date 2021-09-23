<template>
  <div>
    <el-form :model="form" :rules="formRule" ref="formRef" label-width="120px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="当前分片数" prop="count">
            <el-input-number v-model="current" :controls="false" style="width: 100%;" disabled></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="增加到" prop="count">
            <el-input-number v-model="form.count" controls-position="right" style="width: 100%;"
                             :min="last"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import admin from "@/modules/admin/api/admin";

export default {
  name: "NewPartitions",
  props: {cluster: String, topic: String},
  data() {
    return {
      current: 1,
      last: 1,
      form: {
        count: 1
      },
      formRule: {
        count: [
          {required: true, type: 'number', message: "分片数为必填项", trigger: ["blur", "change"]}
        ]
      }
    }
  }, methods: {
    getLast() {
      admin.getTopic(this.cluster, this.topic).then(res => {
        if (res.data && res.data.partitions && res.data.partitions.length > 0) {
          this.current = res.data.partitions.length
          this.last = res.data.partitions.length + 1
          this.form.count = this.last;
        }
      })
    },
    newPartitions() {
      this.$refs.formRef.validate(v => {
        if (v) {
          admin.newPartitions(this.cluster, this.topic, this.form.count).then(res => {
            this.$message.success("更新成功");
            this.$emit('close');
          }).catch(() => {
            this.$message.error("更新失败")
            this.$emit('error');
          })
        }
      })
    }
  }, created() {
    this.getLast();
  }
}
</script>

<style scoped>

</style>