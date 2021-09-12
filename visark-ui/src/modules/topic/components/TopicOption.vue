<template>
  <div>
    <el-form :model="option" :rules="optionRule" ref="optionRef" label-width="120px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="Key反序列化" prop="keyDeserializer">
            <el-select v-model="option.keyDeserializer" style="width: 100%;">
              <el-option v-for="deserializer in deserializers" :value="deserializer.value" :label="deserializer.label"
                         :key="deserializer.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="Value反序列化" prop="valueDeserializer">
            <el-select v-model="option.valueDeserializer" style="width: 100%;">
              <el-option v-for="deserializer in deserializers" :value="deserializer.value" :label="deserializer.label"
                         :key="deserializer.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24" style="text-align: right">
          <el-button type="primary" @click="save" :loading="loading">保存</el-button>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import api from '../api/topic-option'
import optionApi from '@/modules/option/api/option'

export default {
  name: "TopicOption",
  props: {cluster: String, topic: String},
  data() {
    return {
      loading: false,
      deserializers: [],
      option: {
        id: "",
        clusterId: this.cluster,
        topic: this.topic,
        keyDeserializer: "",
        valueDeserializer: ""
      },
      optionRule: {
        keyDeserializer: [
          {required: true, timeout: "Key反序列化为必填项", trigger: ["change"]},
        ],
        valueDeserializer: [
          {required: true, timeout: "Value反序列化为必填项", trigger: ["change"]},
        ]
      }
    }
  },
  methods: {
    initDeserializers() {
      optionApi.getDeserializers().then(res => {
        this.deserializers = res.data;
      })
    },
    get() {
      api.get(this.cluster, this.topic).then(res => {
        this.option = res.data;
      })
    },
    save() {
      this.$refs.optionRef.validate(v => {
        if (v) {
          this.loading = true;
          api.save(this.option).then(res => {
            this.$message.success("保存成功");
            this.get();
            this.loading = false;
          }).catch(() => {
            this.$message.error("保存失败");
            this.loading = false;
          })
        }
      })
    }

  }, created() {
    this.initDeserializers();
    this.get();
  }
}
</script>

<style scoped>

</style>