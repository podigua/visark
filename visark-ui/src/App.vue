<template>
  <div id="app" style="margin-top: 0px;">
    <el-dialog
        :title="topic"
        :modal="false"
        width="600px"
        :close-on-click-modal="false"
        v-if="isNewPartitions"
        :visible.sync="isNewPartitions"
    >
      <NewPartitions ref="newPartitions" :cluster="cluster" :topic="topic" @close="onNewPartitionsSuccess"
                     @error="loading=false"></NewPartitions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="isNewPartitions=false">取 消</el-button>
        <el-button type="primary" @click="updateNewPartitions" :loading="loading">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="分片信息"
        width="600px"
        v-if="isViewPartition"
        :visible.sync="isViewPartition"
    >
      <Topic :cluster="cluster" :topic="topic"></Topic>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="isViewPartition=false">关 闭</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="节点信息"
        width="500px"
        v-if="isViewNodeList"
        :visible.sync="isViewNodeList"
    >
      <NodeList :cluster="cluster"></NodeList>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="isViewNodeList=false">关 闭</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="节点信息"
        width="500px"
        v-if="isViewNode"
        :visible.sync="isViewNode"
    >
      <Node :cluster="cluster" :id="node"></Node>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="isViewNode=false">关 闭</el-button>
      </span>
    </el-dialog>
    <el-dialog
        width="300px"
        center
        top="40vh"
        v-if="isConnect"
        :close-on-press-escape="false"
        :close-on-click-modal="false"
        :show-close="false"
        :visible.sync="isConnect"
    >
      <el-row v-if="isSuccess">
        <el-col :span="24" style="text-align: center">
          <el-icon class="el-icon-loading"></el-icon>
          连接中....
        </el-col>
        <el-col :span="24" style="text-align: center">
          <el-button @click="cancelConnect">取 消</el-button>
        </el-col>
      </el-row>
      <el-row v-else>
        <el-col :span="24" style="text-align: center">
          <el-icon class="el-icon-warning-outline"></el-icon>
          连接失败
        </el-col>
        <el-col :span="24" style="text-align: center">
          <el-button @click="isConnect=false">关 闭</el-button>
        </el-col>
      </el-row>
    </el-dialog>
    <el-dialog
        title="Topic"
        :modal="false"
        width="600px"
        :close-on-click-modal="false"
        v-if="isCreateTopic"
        :visible.sync="isCreateTopic"
    >
      <NewTopic ref="newTopic" :id="cluster" @close="onCreateTopicSuccess" @error="loading=false"></NewTopic>
      <span slot="footer" class="dialog-footer">
        <el-button @click="isCreateTopic=false">取 消</el-button>
        <el-button type="primary" @click="createTopic" :loading="loading">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="首选项"
        :modal="false"
        width="500px"
        :close-on-click-modal="false"
        :visible.sync="isOptionEdit"
    >
      <Option ref="optionRef" @close="isOptionEdit=false"></Option>
      <span slot="footer" class="dialog-footer">
        <el-button-group>
          <el-button @click="isOptionEdit=false">取 消</el-button>
          <el-button type="primary" @click="saveOption" :loading="loading">确 定</el-button>
        </el-button-group>
      </span>
    </el-dialog>
    <el-container>
      <el-header
          style="height: 64px;margin-top: 0px;transform: translateY(0px);left: 0px;right: 0px;background-color: #f5f5f5;">
        <div style="display: flex;flex-flow: row;height: 64px;line-height: 64px">
          <div style="margin:0px 10px;">
            <span class="iconfont icon-kafka" style="color: #008CD2;font-size: 16pt;cursor: pointer;"></span>
          </div>
          <div style="margin-right:10px;">
            <span style="cursor: pointer;">Visark</span>
          </div>
          <div style="margin-right:10px;">
            <el-select v-model="cluster" style="width: 200px" filterable clearable @change="onClusterChange">
              <el-option v-for="c in clusters" :value="c.id" :key="c.id" :label="c.name"></el-option>
            </el-select>
          </div>
          <div style="margin-right:10px;">
            <el-popover style="margin:0px 10px;"
                        placement="top-start"
                        title="集群"
                        width="500"
                        trigger="click">
              <Cluster @update="onClusterUpdate" @delete="onClusterDelete"></Cluster>
              <i slot="reference" class="iconfont icon-cluster" type="text" title="集群"
                 style="font-size: 16pt;color:#0081C8;cursor: pointer;"></i>
            </el-popover>
          </div>
          <div style="flex-grow: 1!important"></div>
          <div>
            <el-button class="el-icon-setting" type="text" title="集群"
                       style="font-size: 16pt;color:#0081C8;cursor: pointer;" @click="isOptionEdit=true"></el-button>
          </div>
        </div>
      </el-header>
      <el-container :style="{height:getContainerHeight}">
        <el-aside width="300px">
          <div v-if="isReady" style="padding-right: 10px;">
            <el-input
                placeholder="输入关键字进行过滤"
                clearable
                v-model="filterText">
            </el-input>
            <el-tree ref="tree"
                     :data="trees"
                     @node-click="onNodeClick"
                     expand-on-click-node
                     :filter-node-method="filterNode"
                     node-key="value">
               <span class="tree-node" slot-scope="{ data, node }">
                  <span style="width: 200px;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;"
                        :title="data.label">{{ data.label }}</span>
                 <span v-if="data.type!=='CONSUMER'">
                   <el-dropdown @command="(command)=>{handleCommand(command,data,node)}">
                    <el-button type="text" icon="el-icon-more-outline">
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <template v-if="data.type==='TOPIC'">
                        <el-dropdown-item icon="el-icon-plus" command="create-topic"> 新增</el-dropdown-item>
                        <el-dropdown-item icon="el-icon-refresh" command="refresh-topic"> 刷新</el-dropdown-item>
                      </template>
                      <template v-if="data.type==='TOPIC_INSTANCE'">
                        <el-dropdown-item icon="el-icon-delete" command="delete-topic"> 删除</el-dropdown-item>
                        <el-dropdown-item icon="el-icon-edit" command="new-partition"> 修改分片</el-dropdown-item>
                        <el-dropdown-item icon="el-icon-view" command="view-partition"> 查看分片</el-dropdown-item>
                        <el-dropdown-item icon="el-icon-s-data" command="topic-data"> 数据</el-dropdown-item>
                      </template>
                      <template v-if="data.type==='NODE_INSTANCE'">
                        <el-dropdown-item icon="el-icon-view" command="view-node"> 节点信息</el-dropdown-item>
                      </template>
                      <template v-if="data.type==='NODE'">
                        <el-dropdown-item icon="el-icon-view" command="view-nodes"> 节点信息</el-dropdown-item>
                      </template>
                       <template v-if="data.type==='CONSUMER_INSTANCE'">
                        <el-dropdown-item icon="el-icon-delete" command="delete-consumer"> 删除</el-dropdown-item>
                      </template>
                    </el-dropdown-menu>
                  </el-dropdown>
                 </span>
               </span>
            </el-tree>
          </div>
        </el-aside>
        <el-main style="padding:0px 5px">
          <div v-if="isReady" style="height: 99%">
            <el-tabs v-model="currentTab" @tab-remove="onTabRemove" closable type="border-card" style="height: 100%">
              <el-tab-pane v-for="tab in tabs" :label="tab" :name="tab" style="height: 100%" :key="tab">
                <TopicData :ref="tab" :cluster="cluster" :topic="tab"></TopicData>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>


import Cluster from "./modules/cluster/view/Cluster";
import Option from "./modules/option/view/Option";
import Node from "./modules/admin/components/Node"
import NodeList from "./modules/admin/components/NodeList"
import clusterApi from './modules/cluster/api/cluster'
import NewTopic from "./modules/admin/components/NewTopic";
import adminApi from './modules/admin/api/admin'
import Topic from "./modules/admin/components/Topic";
import TopicData from "@/modules/topic/components/TopicData";
import NewPartitions from "@/modules/admin/components/NewPartitions";

export default {
  name: 'App',
  components: {NewPartitions, TopicData, Topic, Option, Cluster, NewTopic, Node, NodeList},
  data() {
    return {
      tabs: [],
      current: {
        data: null,
        node: null
      },
      isReady: false,
      cluster: "",
      node: "",
      topic: "",
      currentTab: "",
      clusters: [],
      isOptionEdit: false,
      isCreateTopic: false,
      isConnect: false,
      isSuccess: true,
      isViewNode: false,
      isViewNodeList: false,
      isViewPartition: false,
      isNewPartitions: false,
      loading: false,
      filterText: "",
      trees: [],
      cancel: null,
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  computed: {
    getContainerHeight() {
      return `calc(100vh - 64px)`;
    }
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    saveOption() {
      this.$refs.optionRef.save();
    },
    onClusterUpdate() {
      clusterApi.query4List().then(res => {
        this.clusters = res.data;
      })
    },
    onClusterDelete(id) {
      if (id === this.cluster) {
        this.trees = [];
        this.tabs = [];
        this.isReady = false;
        this.cluster = "";
      }
      let index = this.clusters.findIndex(data => data.id === id);
      if (index > -1) {
        this.clusters.splice(index, 1);
      }
    },
    onClusterChange(id) {
      this.filterText = "";
      this.trees = [];
      this.tabs = [];
      this.isReady = false;
      this.isSuccess = true;
      if (!id) {
        return;
      }
      this.isConnect = true;
      let _this = this;
      let CancelToken = this.$http.CancelToken;
      this.$http.get(`/admin/${id}/trees`, {
        cancelToken: new CancelToken(executor => {
          _this.cancel = executor
        })
      }).then(res => {
        this.trees = res.data.data;
        this.isReady = true;
        this.isSuccess = true;
        this.isConnect = false;
      }).catch((e) => {
        this.cluster = "";
        if (e == 'Cancel') {
          this.isConnect = false;
          this.$message.warning("已取消");
        } else {
          this.isSuccess = false;
        }
      })
    },
    onNodeClick(data) {
      if (data.type === 'TOPIC_INSTANCE') {
        this.topicData(data);
      }
    },
    handleCommand(command, data, node) {
      this.current.data = data;
      this.current.node = node;
      if (command === 'create-topic') {
        this.isCreateTopic = true;
      } else if (command === 'refresh-topic') {
        this.refreshTopic(data)
      } else if (command === 'delete-topic') {
        this.deleteTopic(data, node);
      } else if (command === 'view-node') {
        this.viewNode(data, node);
      } else if (command === 'view-nodes') {
        this.viewNodes(data, node);
      } else if (command === 'view-partition') {
        this.viewPartition(data, node);
      } else if (command === 'topic-data') {
        this.topicData(data);
      } else if (command === 'new-partition') {
        this.newPartitions(data);
      } else if (command === 'delete-consumer') {
        this.deleteConsumer(data, node);
      }
    },
    refreshTopic(data) {
      this.filterText = "";
      data.children = [];
      adminApi.getTopics(this.cluster).then(res => {
        if (res.data && res.data.length > 0) {
          res.data.forEach(node => {
            data.children.push(node)
          })
        }
      })
    },
    deleteTopic(data, node) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示").then(() => {
        adminApi.deleteTopic(this.cluster, data.value).then(() => {
          this.$message.success("删除成功");
          const parent = node.parent;
          const children = parent.data.children || parent.data;
          const index = children.findIndex(d => d.value === data.value);
          children.splice(index, 1);
        }).catch(() => {
          this.$message.error("删除失败")
        })
      });
    },
    viewNode(data) {
      this.node = data.value;
      this.isViewNode = true;
    },
    viewNodes() {
      this.isViewNodeList = true;
    },
    viewPartition(data) {
      this.topic = data.label;
      this.isViewPartition = true;
    },
    topicData(data) {
      let tab = this.tabs.find(d => d === data.value);
      if (!tab) {
        this.tabs.push(data.value);
      }
      this.currentTab = data.value;
    },
    createTopic() {
      this.loading = true;
      this.$refs.newTopic.createTopic();
    },
    newPartitions(data) {
      this.topic = data.value;
      this.isNewPartitions = true;
    },
    deleteConsumer(data, node) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示").then(() => {
        adminApi.deleteConsumer(this.cluster, data.value).then(() => {
          this.$message.success("删除成功");
          const parent = node.parent;
          const children = parent.data.children || parent.data;
          const index = children.findIndex(d => d.value === data.value);
          children.splice(index, 1);
        }).catch((err) => {
          this.$message.error("删除失败:" + err.response.data.message);
        })
      });
    },
    onNewPartitionsSuccess() {
      this.isNewPartitions = false;
      this.loading = false;
    },
    onCreateTopicSuccess() {
      this.loading = false;
      this.isCreateTopic = false;
      this.refreshTopic(this.current.data)
    },
    updateNewPartitions() {
      this.$refs.newPartitions.newPartitions();
    },
    cancelConnect() {
      this.isConnect = false;
      if (this.cancel != null) {
        this.cancel();
        this.cancel = null;
      }
    },
    onTabRemove(name) {
      let index = this.tabs.findIndex(data => data === name);
      if (index > -1) {
        this.$refs[name][0].close();
        this.tabs.splice(index, 1);
      }
    }
  }, created() {
    clusterApi.query4List().then(res => {
      this.clusters = res.data;
    })
  }
}
</script>

<style>
#app {
  margin: 0px;
}

.tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}


::-webkit-scrollbar { /*滚动条整体样式*/
  width: 4px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 4px;
  scrollbar-arrow-color: red;

}

::-webkit-scrollbar-thumb { /*滚动条里面小方块*/
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: rgba(0, 0, 0, 0.2);
  scrollbar-arrow-color: red;
}

::-webkit-scrollbar-track { /*滚动条里面轨道*/
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 0;
  background: rgba(0, 0, 0, 0.1);
}

</style>
