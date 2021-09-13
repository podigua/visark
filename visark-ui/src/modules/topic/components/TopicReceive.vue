<template>
  <div>
    <div class="header">
      <div>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-video-play" :disabled="isReceiving" title="启动"
                   @click="start"></el-button>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-video-pause" :disabled="!isReceiving" title="停止"
                   @click="stop"></el-button>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-search" title="搜索"></el-button>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-circle-close" title="清空"
                   @click="clear"></el-button>
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
        <el-table-column prop="message" label="消息" show-overflow-tooltip></el-table-column>
        <el-table-column prop="timestamp" label="时间" width="150px"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "TopicReceive",
  props: {cluster: String, topic: String},
  data() {
    return {
      isReceiving: false,
      socket: null,
      list: []
    }
  }, methods: {
    start() {
      this.clear();
      let cluster = this.cluster;
      let topic = this.topic;
      this.isReceiving = true;
      let endpoint = `${location.protocol === 'https' ? 'wss' : 'ws'}://${process.env.NODE_ENV === 'production'? location.host: 'localhost:8082'}/receive?cluster=${cluster}&topic=${topic}`;
      if (window.WebSocket) {
        this.socket = new WebSocket(endpoint);
        this.socket.onclose = () => {
          console.log("close")
          this.isReceiving=false;
        }
        this.socket.onerror = (e) => {
          console.log("error",e)
          this.$message.error("实时数据接收出错")
        }
        this.socket.onmessage = (messageEvent) => {
          if (messageEvent.isTrusted) {
            console.log(messageEvent.data)
            let data = JSON.parse(messageEvent.data);
            if (data) {
              this.list.push(data)
            } else {
              this.$message.error(data.message);
            }
          }
        }
        this.socket.onopen = () => {
          console.log("open")
        }
      }
    },
    stop() {
      if (this.socket != null && WebSocket.OPEN===this.socket.readyState) {
        this.socket.close();
      }
    },
    clear() {
      this.list = [];
    }
  }
}
</script>

<style scoped>
.header {
  display: flex;
  flex-flow: row;
  height: 38px;
  line-height: 38px;
}
</style>