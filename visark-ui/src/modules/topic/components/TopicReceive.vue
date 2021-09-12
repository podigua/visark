<template>
  <div>
    <div class="header" >
      <div>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-video-play" :disabled="isReceiving" title="启动" @click="start"></el-button>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-video-pause" :disabled="!isReceiving" title="停止" @click="stop"></el-button>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-search" title="搜索"></el-button>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-circle-close" title="清空" @click="clear"></el-button>
      </div>
      <div style="flex-grow: 1!important"></div>
      <div>
        <el-input placeholder="过滤..." style="width: 180px"></el-input>
        <el-input-number style="width: 100px" :min="1" controls-position="right" placeholder="数量"></el-input-number>
      </div>
    </div>
    <div class="body">
      <el-table :data="list" stripe>
        <el-table-column prop="partition" label="partition" width="80px"></el-table-column>
        <el-table-column prop="offset" label="offset" width="80px"></el-table-column>
        <el-table-column prop="message" label="message" show-overflow-tooltip></el-table-column>
        <el-table-column prop="timestamp" label="timestamp" width="120px"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "TopicReceive",
  props:{cluster:String,topic:String},
  data(){
    return{
      isReceiving:false,
      source:null,
      list:[]
    }
  },methods:{
    start(){
      let cluster=this.cluster;
      let topic=this.topic;
      this.isReceiving=true;
      this.source=new EventSource(`/data/${cluster}/${topic}/receive`,{withCredentials:true});
      this.source.addEventListener("open", () => {
        console.log("open");
      });
      this.source.addEventListener("message", (e) => {
        this.list.push(JSON.parse(e.data));
        console.log(e.data);
      });
      this.source.addEventListener("error", (e) => {
        console.log("error",e)
        this.stop();
      });
      this.source.addEventListener("close", () => {
        console.log("close")
        this.stop();
      });
    },
    stop(){
      if(this.source!=null){
        this.isReceiving=false;
        this.source.close();
      }
    },
    clear(){
      this.list=[];
    }
  }
}
</script>

<style scoped>
.header{
  display: flex;
  flex-flow: row;
  height: 38px;
  line-height: 38px;
}
</style>