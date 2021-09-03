<template>
  <div>
    <el-form :model="form" :rules="formRule" ref="formRef" label-width="120px">
      <el-tabs tab-position="left" style="height: 350px">
        <el-tab-pane label="通用配置">
          <el-form-item label="连接超时时间" prop="timeout">
            <el-input-number v-model="form.timeout" controls-position="right" style="width: 100%"
                             min="1000"></el-input-number>
          </el-form-item>
          <el-form-item label="读取超时时间" prop="readTimeout">
            <el-input-number v-model="form.readTimeout" controls-position="right" style="width: 100%"
                             min="1000"></el-input-number>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="Topic">
          <el-form-item label="Key反序列化" prop="readTimeout">
            <el-select v-model="form.keyDeserializer" style="width: 100%;">
              <el-option v-for="deserializer in deserializers" :value="deserializer.value" :label="deserializer.label"
                         :key="deserializer.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Value反序列化" prop="valueDeserializer">
            <el-select v-model="form.valueDeserializer" style="width: 100%;">
              <el-option v-for="deserializer in deserializers" :value="deserializer.value" :label="deserializer.label"
                         :key="deserializer.value"></el-option>
            </el-select>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
      <el-row>
        <el-col :span="24" style="text-align: right;">
          <el-button type="primary" @click="save" :loading="loading">保 存</el-button>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import api from '../api/option'

export default {
  name: "Option",
  data() {
    return {
      loading: false,
      deserializers: [],
      form: {
        id: "",
        timeout: undefined,
        readTimeout: undefined,
        keyDeserializer: "",
        valueDeserializer: "",
      },
      formRule: {
        timeout: [
          {required: true, type: 'number', timeout: "连接超时时间为必填项", trigger: ["blur", "change"]},
        ],
        readTimeout: [
          {required: true, type: 'number', timeout: "读超时时间为必填项", trigger: ["blur", "change"]},
        ],
        keyDeserializer: [
          {required: true, timeout: "Key反序列化为必填项", trigger: ["change"]},
        ],
        valueDeserializer: [
          {required: true, timeout: "Value反序列化为必填项", trigger: ["change"]},
        ]
      }
    }
  }, methods: {
    query() {
      api.getOption().then(res => {
        this.form = res.data;
      })
    },
    initDeserializers() {
      api.getDeserializers().then(res => {
        this.deserializers = res.data;
      })
    },
    save() {
      this.$refs.formRef.validate(v => {
        if (v) {
          api.save(this.form).then(() => {
            this.$message.success("保存成功");
            this.query();
          })
        } else {
          this.$message.error("校验失败");
        }
      })
    }
  }, created() {
    this.initDeserializers();
    this.query();
  }
}
</script>

<style scoped>

</style>