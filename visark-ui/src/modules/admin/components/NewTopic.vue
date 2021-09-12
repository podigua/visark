<template>
  <div>
    <el-form :model="form" :rules="formRule" ref="formRef" label-width="120px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="分区数" prop="partitions">
            <el-input-number v-model="form.partitions" controls-position="right" style="width: 100%;"
                             :min="-1"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="副本数" prop="replication">
            <el-input-number v-model="form.replication" controls-position="right" style="width: 100%;"
                             :min="-1"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <!--      <el-row>-->
      <!--        <el-col :span="24">-->
      <!--          <el-form-item label="配置" prop="config">-->
      <!--            <el-button @click="form.configs.push({})">添加</el-button>-->
      <!--            <el-table :data="form.configs">-->
      <!--              -->
      <!--            </el-table>-->
      <!--          </el-form-item>-->
      <!--        </el-col>-->
      <!--      </el-row>-->
    </el-form>
  </div>
</template>

<script>
import api from '../api/admin'

export default {
  name: "NewTopic",
  props: {
    id: String,
  },
  data() {
    return {
      topics: [],
      form: {
        name: "",
        partitions: 1,
        replication: 1,
        configs: {}
      },
      formRule: {
        name: [
          {required: true, message: "名称为必填项", trigger: ["blur", "change"]}
        ],
        partitions: [
          {required: true, type: 'number', message: "分区数为必填项", trigger: ["blur", "change"]},
        ],
        replication: [
          {required: true, type: 'number', message: "副本数为必填项", trigger: ["blur", "change"]},
        ]
      }
    }
  }, methods: {
    createTopic() {
      let topic = this.topics.find(data => data.value === this.form.name);
      if (topic) {
        this.$emit('error');
        this.$message.error("Topic已存在");
        return;
      }
      this.$refs.formRef.validate(v => {
        if (v) {
          api.createTopic(this.id, this.form).then(() => {
            this.$message.success("创建成功");
            this.$emit('close');
          }).catch(() => {
            this.$emit('error');
            this.$message.error("创建失败");
          })
        }
      })
    }
  }, created() {
    api.getTopics(this.id).then(res => {
      this.topics = res.data;
    })
  }
}
</script>

<style scoped>

</style>