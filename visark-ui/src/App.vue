<template>
  <div id="app">
    <el-dialog
        title="Topic"
        :modal="false"
        width="600px"
        :close-on-click-modal="false"
        v-if="isCreateTopic"
        :visible.sync="isCreateTopic"
    >
      <NewTopic ref="newTopic" :id="cluster" @save="onCreateTopicSuccess"></NewTopic>
      <span slot="footer" class="dialog-footer">
        <el-button @click="isCreateTopic=false">取 消</el-button>
        <el-button type="primary" @click="createTopic" :loading="loading">确 定</el-button>
      </span>
    </el-dialog>
    <el-container>
      <el-header height="100" style="padding:0px">
        <el-form inline>
          <el-form-item>
            <el-select v-model="cluster" filterable @change="onClusterChange" style="width: 180px;">
              <el-option v-for="cluster in clusters" :value="cluster.id" :label="cluster.name"
                         :key="cluster.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button-group>
              <el-button icon="el-icon-refresh" @click="getCluster" title="刷新"></el-button>
              <el-button icon="iconfont icon-kafka" @click="openCluster" title="集群"></el-button>
              <el-button icon="el-icon-setting" @click="openOption" title="设置"></el-button>
            </el-button-group>
          </el-form-item>
        </el-form>
      </el-header>
      <el-container>
        <el-aside width="300px">
          <div v-if="isReady">
            <el-input
                placeholder="输入关键字进行过滤"
                v-model="filterText">
            </el-input>
            <el-tree ref="tree" :data="trees" :filter-node-method="filterNode" :style="{height:treeHeight}"
                     @node-contextmenu="onNodeContextMenu"></el-tree>
            <div
                id="rightMenu"
                v-if="visible"
                class="tree_menu"
                :style="rightMenuStyle"
                :key="(currentData && currentData.value) || '0'"
            >
              <ul v-if="currentData.type==='TOPIC'">
                <li @click="refreshTopic"><i class="el-icon-refresh"></i> 刷新</li>
                <li @click="isCreateTopic=true"><i class="el-icon-circle-plus-outline"></i> 新增</li>
              </ul>
              <ul v-if="currentData.type==='TOPIC_INSTANCE'">
                <li @click="deleteTopic"><i class="el-icon-delete"></i> 删除</li>
                <li @click="viewTopic"><i class="el-icon-view"></i> 查看</li>
              </ul>
            </div>
          </div>
        </el-aside>
        <el-main>
          <el-tabs v-model="currentTab" type="border-card" :editable="true" @tab-remove="onTabRemove">
            <el-tab-pane v-for="tab in tabs" :label="tab.label" :name="tab.name" :key="tab.name"></el-tab-pane>
          </el-tabs>
        </el-main>
      </el-container>
    </el-container>

  </div>
</template>

<script>

import api from './plugin/cluster/api/cluster'
import admin from './plugin/admin/api/admin'
import win from '@/utils/window'
import NewTopic from "@/plugin/admin/components/NewTopic";

export default {
  name: 'App',
  components: {NewTopic},
  data() {
    return {
      rightMenuStyle: "",
      currentTab: "",
      tabs:[],
      currentData: {},
      currentNode: {},
      isCreateTopic: false,
      isReady: false,
      visible: false,
      filterText: "",
      trees: [],
      cluster: "",
      clusters: []
    }
  }, watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  computed: {
    treeHeight() {
      return `calc(100vh - 150px)`;
    }
  },
  methods: {
    getCluster() {
      api.query4List().then(res => {
        this.clusters = res.data;
      })
    },
    openCluster() {
      win.open({
        "title": "集群配置",
        "path": "/cluster.html",
        "width": 850,
        "height": 550
      });
    },
    openOption() {
      win.open({
        "title": "首选项",
        "path": "/option.html",
        "width": 600,
        "height": 400
      });
    },
    onClusterChange(id) {
      this.isReady = false;
      this.trees = [];
      const loading = this.$loading({
        lock: true,
        text: '连接中',
        spinner: 'el-icon-loading',
        background: '#CCC'
      });
      admin.getTrees(id).then(res => {
        this.trees = res.data;
        this.isReady = true;
        loading.close();
      }).catch(() => {
        loading.close();
        this.cluster = "";
      })
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    onNodeContextMenu(event, data, node, $this) {
      this.getContextmenu(event, data, node);
    },
    getContextmenu(e, data,node) {
      this.rightMenuStyle =
          "top:" + e.pageY + "px; left:" + e.pageX + "px";
      this.visible = true;
      this.currentData = data;
      this.currentNode = node;
      const self = this;
      document.onclick = function (ev) {
        if (ev.target !== document.getElementById("rightMenu")) {
          self.visible = false;
        }
      };
    },
    refreshTopic() {
      admin.getTopics(this.cluster).then(res => {
        this.currentData.children = [];
        if (res.data && res.data.length > 0) {
          res.data.forEach(node => {
            this.currentData.children.push(node);
          })
        }
      })
    },
    createTopic() {
      this.$refs.newTopic.createTopic();
    },
    onCreateTopicSuccess() {
      this.isCreateTopic = false;
      this.refreshTopic();
    },
    deleteTopic(){
      this.$confirm("此操作将永久删除, 是否继续?", "提示").then(() => {
        admin.deleteTopic(this.cluster,this.currentData.value).then(() => {
          this.$message.success("删除成功");
          const parent = this.currentNode.parent;
          const children = parent.data.children || parent.data;
          const index = children.findIndex(d => d.value === this.currentData.value);
          children.splice(index, 1);
        });
      });
    },
    viewTopic(){
      let value = this.currentData.value;
      this.tabs.push({
        name:value,
        label:value
      })
    },
    onTabRemove(){

    }
  },
  created() {
    this.getCluster();
  }
}
</script>

<style>
.tree_menu {
  position: absolute;
  background-color: #fff;
  padding: 5px 0;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(187, 178, 178, 0.1)
}

.tree_menu ul {
  margin: 0;
  padding: 0;
}

.tree_menu ul li {
  list-style: none;
  margin: 0;
  padding: 0 15px;
  font-size: 14px;
  line-height: 30px;
  cursor: pointer;
}

.tree_menu ul li:hover {
  background-color: #ebeef5;
}


#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
