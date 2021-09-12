<template>
  <el-tabs v-model="current" >
    <el-tab-pane label="配置" name="option">
      <TopicOption :cluster="cluster" :topic="topic" ></TopicOption>
    </el-tab-pane>
    <el-tab-pane label="消费" name="receive">
      <TopicReceive :cluster="cluster" :topic="topic" ></TopicReceive>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import TopicOption from "@/modules/topic/components/TopicOption";
import TopicReceive from "@/modules/topic/components/TopicReceive";
export default {
  name: "TopicData",
  components: {TopicReceive, TopicOption},
  props:{cluster:String,topic:String},
  data(){
    return{
      current:"receive"
    }
  }
}
</script>

<style scoped>

</style>