<template>
  <div>
    <el-dialog
        title="方案"
        :modal="false"
        width="500px"
        :close-on-click-modal="false"
        :visible.sync="isEdit"
    >
      <el-form :model="programme" :rules="programmeRule" ref="programmeRef" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="方案名称" prop="name">
              <el-input v-model="programme.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button-group>
          <el-button @click="isEdit=false">取 消</el-button>
          <el-button type="primary" @click="save" :loading="loading">确 定</el-button>
        </el-button-group>
      </span>
    </el-dialog>
    <el-drawer
        title="方案列表"
        :visible.sync="isProgramme">
      <el-table :data="programmes" stripe @row-dblclick="onProgrammesDblclick">
        <el-table-column label="方案名称" prop="name"></el-table-column>
        <el-table-column label="操作" prop="action" width="80px">
          <template slot-scope="scope">
            <el-button icon="el-icon-delete" plain @click="deleteProgramme(scope.row.id,scope.row.index)"
                       title="删除"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-drawer>
    <el-drawer
        title="表达式"
        :visible.sync="isDrawer"
        ref="expressionDrawerRef"
        :before-close="handleExpressionClose">
      <div style="width: 500px">
        <el-form :model="expression" ref="expressionRef" label-width="0px">
          <div>
            <el-button icon="el-icon-plus" @click="addExpression" title="新增"></el-button>
          </div>
          <el-table :data="expression.expressions" stripe>
            <el-table-column label="键" width="180px">
              <template slot-scope="scope">
                <el-form-item label-width="0px"
                              :prop="'expressions['+scope.$index+'].code'"
                              :rules="{ required: true, message: '键不能为空', trigger: ['blur','change']}">
                  <el-input v-model="scope.row.code"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="表达式">
              <template slot-scope="scope">
                <el-form-item label-width="0px"
                              :prop="'expressions['+scope.$index+'].expression'"
                              :rules="{ required: true, message: '表达式不能为空', trigger: ['blur','change']}">
                  <el-input v-model="scope.row.expression">
                    <el-dropdown slot="append" trigger="click"
                                 @command="(command)=>{handleCommand(command,scope.$index)}">
                              <span class="el-dropdown-link">
                                 <el-button type="text" icon="el-icon-more-outline"></el-button>
                              </span>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-for="(exp,index) in expressions" :command="exp.value" :key="index">
                          {{ exp.label }}
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                  </el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80px">
              <template slot-scope="scope">
                <el-form-item label-width="0px">
                  <el-button icon="el-icon-delete" plain @click="deleteExpression(scope.$index)"
                             title="删除"></el-button>
                </el-form-item>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </div>
      <div style="text-align: right;margin: 5px;">
        <el-button @click="isDrawer=false">取 消</el-button>
        <el-button type="primary" @click="saveExpression">确 定</el-button>
      </div>
    </el-drawer>

    <div style="padding: 5px 5px">
      <i class="iconfont icon-gaojibiaodashi" style="color:#008CD2;font-size:16pt;cursor: pointer;margin-right: 10px"
         @click="showExpression" title="表达式"></i>
      <i class="iconfont icon-fangan" style="color:#008CD2;font-size:16pt;cursor: pointer;margin-right: 10px"
         @click="showProgramme" title="方案列表"></i>
      <i class="el-icon-plus" style="color:#008CD2;font-size:16pt;cursor: pointer;margin-right: 10px"
         @click="onTabAdd" title="新增"></i>
    </div>
    <el-tabs v-model="tab" type="border-card" closable @tab-remove="onTabRemove">
      <el-tab-pane v-for="t in tabs" :label="t.name" :name="t.id" :key="t.id" closable>
        <div :style="{height:getTabHeight}" v-if="tab===t.id">
          <el-form label-width="50px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="key" prop="contentKey">
                  <el-input v-model="t.contentKey" type="textarea" :rows="4" :spellcheck="false"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="value" prop="contentValue">
                  <el-input type="textarea" :autosize="{minRows:8,maxRows:15}" :spellcheck="false"
                            v-model="t.contentValue"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <div style="text-align: right">
              <el-button type="primary" @click="saveProgramme" :loading="loading">保存方案</el-button>
              <el-button type="primary">发送</el-button>
            </div>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import expressionApi from "../api/expression";
import programmeApi from '../api/programme'

export default {
  name: "TopicProducer",
  props: {cluster: String, topic: String},
  data() {
    return {
      loading: false,
      isDrawer: false,
      isProgramme: false,
      isEdit: false,
      expressions: [],
      programmes: [],
      actives: ["data"],
      tab: "",
      tabs: [],
      expression: {
        expressions: [],
      },
      programme: {
        name: "",
      },
      programmeRule: {
        name: [
          {required: true, message: "方案名称为必填项", trigger: ["blur", "change"]}
        ]
      },
    }
  }, computed: {
    getTabHeight() {
      return `calc(100vh - 300px)`
    }
  }, methods: {
    addExpression() {
      console.log(this.expression)
      this.expression.expressions.push({
        code: "",
        expression: ""
      })
    },
    deleteExpression(index) {
      this.expression.expressions.splice(index, 1);
    },
    getExpression() {
      expressionApi.getFunctions().then(res => {
        this.expressions = res.data;
      })
    },
    handleCommand(name, index) {
      this.expression.expressions[index].expression = name;
    },
    showProgramme() {
      this.isProgramme = true;
    },
    showExpression() {
      let node = this.tabs.find(data => data.id === this.tab);
      this.expression.expressions = [];
      this.expression.expressions.push(...node.expressions);
      this.isDrawer = true;
    },
    handleExpressionClose(done) {
      this.$refs.expressionRef.validate(v => {
        if (v) {
          let node = this.tabs.find(data => data.id === this.tab);
          node.expressions = [];
          node.expressions.push(...this.expression.expressions);
          done()
        }
      })
    },
    saveExpression() {
      this.$refs.expressionDrawerRef.closeDrawer();
    },
    getProgrammeList() {
      programmeApi.query4List(this.cluster, this.topic).then(res => {
        this.programmes = res.data;
      })
    },
    saveProgramme() {
      let node = this.tabs.find(data => data.id === this.tab);
      this.programme.name = node.name;
      this.isEdit = true;
    },
    save() {
      this.$refs.programmeRef.validate(v => {
        if (v) {
          console.log("tabs:", this.tabs)
          console.log("tab:", this.tab)
          let node = this.tabs.find(data => data.id === this.tab);
          let remove = false;
          let index = this.tabs.findIndex(data => data.id === this.tab);
          let params = {};
          params.name = this.programme.name;
          params.clusterId = this.cluster;
          params.topic = this.topic;
          params.id = node.id;
          params.contentKey = node.contentKey;
          params.contentValue = node.contentValue;
          if (params.id.startsWith('temp')) {
            remove = true;
            params.id = "";
          }
          params.expressions = node.expressions;
          programmeApi.save(params).then(res => {
            this.$message.success("保存成功");
            this.tabs[index].name = res.data.name;
            if (remove) {
              this.tabs.splice(index, 1);
              this.tabs.splice(index, 0, res.data);
              this.$nextTick(() => {
                this.tab = res.data.id;
              })
            }
            this.getProgrammeList();
            this.isEdit = false;
          })
        }
      })
    },
    deleteProgramme(id, index) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示").then(() => {
        programmeApi.deleteById(id).then(() => {
          this.$message.success("删除成功");
          this.programmes.splice(index, 1);
          let node = this.tabs.find(data => data.id === id);
          let tabIndex = this.tabs.findIndex(data => data.id === id);
          if (tabIndex > -1) {
            this.tabs.splice(tabIndex, 1);
          }
        })
      });
    }, onProgrammesDblclick(row) {
      let tab = this.tabs.find(data => data.id === row.id);
      if (!tab) {
        this.tabs.push(row);
      }
      this.tab = row.id;
      this.isProgramme = false;
    },
    onTabAdd() {
      let uid = "temp" + this.$uuid.v4();
      let id = uid.replace(/-/g, '');
      console.log(id)
      this.tabs.push({
        id: id,
        name: "new",
        clusterId: this.cluster,
        topic: this.topic,
        contentKey: "",
        contentValue: "",
        expressions: [],
      });
      this.$nextTick(() => {
        this.tab = id;
      })
    },
    onTabRemove(id) {
      let index = this.tabs.findIndex(data => data.id === id);
      this.tabs.splice(index, 1);
      if (this.tabs.length > 0) {
        this.tab = this.tabs[this.tabs.length - 1].id;
      }
    },
  }, created() {
    this.getExpression();
    this.getProgrammeList();
    this.onTabAdd();
  }
}
</script>

<style scoped>

</style>