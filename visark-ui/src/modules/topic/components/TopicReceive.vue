<template>
  <div>
    <el-dialog
        :show-close="false"
        width="60%"
        v-if="isViewRowDetail"
        :visible.sync="isViewRowDetail"
    >
      <div>
        <div style="text-align: right">
          <el-select v-model="format" @change="onFormatChange">
            <el-option v-for="type in formats" :value="type.value" :label="type.label" :key="type.value"></el-option>
          </el-select>
          <el-button plain icon="el-icon-document-copy" @click="copy"></el-button>
        </div>
        <div>
          <div v-if="format==='string'">
            <el-input v-model="row.message" :autosize="{minRows:4}" type="textarea"></el-input>
          </div>
          <div v-if="format==='json'" style="overflow: auto;">
            <el-input v-model="row.json" :autosize="{minRows:4}" type="textarea"></el-input>
          </div>
        </div>

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="isViewRowDetail=false">关 闭</el-button>
      </span>
    </el-dialog>
    <div class="header">
      <div>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-video-play" :disabled="isReceiving" title="启动"
                   @click="start"></el-button>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-video-pause" :disabled="!isReceiving" title="停止"
                   @click="stop"></el-button>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-search" title="搜索" @click="search"></el-button>
        <el-button type="text" style="font-size: 16pt" icon="el-icon-circle-close" title="清空"
                   @click="clear"></el-button>
      </div>
      <div style="flex-grow: 1!important"></div>
      <div>
        <el-input placeholder="过滤..." v-model="filterText" style="width: 180px"></el-input>
        <el-input-number style="width: 120px" v-model="size" :min="1" controls-position="right"
                         placeholder="数量"></el-input-number>
        <el-select v-model="offset">
          <el-option value="earliest" label="earliest"></el-option>
          <el-option value="latest" label="latest"></el-option>
        </el-select>
      </div>
    </div>
    <div class="body">
      <ux-grid
          v-loading="loading"
          ref="table"
          row-id="offset"
          :data="list"
          size="mini"
          :height="height"
          :row-height="10"
          use-virtual
          showBodyOverflow="title"
          showHeaderOverflow="title"
          @filter-change="onFilterChange"
          @row-dblclick="onRowDblClick"
          border>
        <ux-table-column
            key="partition"
            field="partition"
            sortable
            title="partition"
            align="center"
            width="100px"/>
        <ux-table-column
            key="offset"
            sortable
            field="offset"
            title="offset"
            align="center"
            width="100px"/>
        <ux-table-column
            key="message"
            :filters="[{ label: 'NULL', value: '' },]"
            :filter-method="filterMessageMethod"
            show-overflow-tooltip
            field="message"
            title="消息"/>
        <ux-table-column
            key="timestamp"
            sortable
            field="timestamp"
            title="时间"
            align="center"
            width="180px"/>
      </ux-grid>
    </div>
  </div>
</template>

<script>

import server from '@/config'
import adminApi from '../../admin/api/admin'
import api from "@/modules/cluster/api/cluster";

export default {
  name: "TopicReceive",
  props: {cluster: String, topic: String},
  data() {
    return {
      filters: [],
      loading: false,
      size: 100,
      height:500,
      filterText: "",
      format: "text",
      offset: "latest",
      formats: [
        {
          value: "string",
          label: "string"
        },
        {
          value: "json",
          label: "json"
        },
        {
          value: "xml",
          label: "xml"
        }
      ],
      isViewRowDetail: false,
      row: {},
      isReceiving: false,
      socket: null,
      list: []
    }
  },
  watch: {
    filterText(val) {
      const column = this.$refs.table.getColumnByField('message');
      let filters = [];
      if (val) {
        filters.push({label: val, value: val, checked: true});
      }
      this.$refs.table.setFilter(column, filters)
      this.$refs.table.updateData()
    }
  },
  mounted(){
    let screenHeight = window.innerHeight;
    this.height=screenHeight-230;
    window.onresize = () => {
      return (() => {
        let screenHeight = window.innerHeight;
        this.height=screenHeight-230;
        console.log(this.height)
      })();
    };
  },
  computed: {
    tableHeight() {
      let innerHeight = window.innerHeight;
      return innerHeight-150;
    }
  }, methods: {
    onFilterChange({column, property, values, datas, filters, $event}) {
      if (property === 'message' && values.length === 0) {
        this.filterText = "";
      }
    },
    filterMessageMethod({value, row, column}) {
      if (value) {
        let result = row.message.indexOf(value) > -1;
        return result;
      }
      return true;
    },
    search() {
      if (this.isReceiving) {
        this.$confirm("正在接收实时数据,是否停止?", "提示").then(() => {
          this.stop();
          this.searchData();
        });
      } else {
        this.searchData();
      }
    },
    searchData() {
      this.loading = true;
      adminApi.getData({
        cluster: this.cluster,
        topic: this.topic,
        offset: this.offset,
        size: this.size,
      }).then(res => {
        this.list = res.data;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      })
    },
    start() {
      this.startTask();
    },
    startTask() {
      this.clear();
      let cluster = this.cluster;
      let topic = this.topic;
      this.isReceiving = true;
      let url = server.url;
      let offset = this.offset;
      let endpoint = `${location.protocol === 'https' ? 'wss' : 'ws'}://${process.env.NODE_ENV === 'production' ? location.host : url}/receive?cluster=${cluster}&topic=${topic}&offset=${offset}`;
      if (window.WebSocket) {
        this.socket = new WebSocket(endpoint);
        this.socket.onclose = () => {
          this.isReceiving = false;
        }
        this.socket.onerror = (e) => {
          this.$message.error("实时数据接收出错")
        }
        this.socket.onmessage = (messageEvent) => {
          if (messageEvent.isTrusted) {
            let data = JSON.parse(messageEvent.data);
            if (data) {
              this.list.unshift(data)
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
    stopTask() {
      if (this.socket != null && WebSocket.OPEN === this.socket.readyState) {
        this.socket.close();
      }
    },
    stop() {
      this.stopTask();
    },
    clear() {
      this.list = [];
    },
    onRowDblClick(row) {
      Object.assign(this.row, row);
      this.format = 'string';
      this.isViewRowDetail = true;
    },
    onFormatChange(format) {
      if (format === 'json') {
        try {
          let message = JSON.parse(this.row.message);
          this.row.json = JSON.stringify(message, null, 4);
        } catch (e) {
          console.log(e);
          this.$message.error("格式化失败");
          this.format = 'string';
        }
      }
    }, copy() {
      let message = this.row.message;
      if (this.format === 'json') {
        message = this.row.json;
      }
      adminApi.copy(message).then(res => {
        this.$message.success("复制成功");
      })
    }
  },created() {

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