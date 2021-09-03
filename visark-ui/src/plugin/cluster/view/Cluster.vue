<template>
  <div>
    <el-dialog
        title="维护"
        :modal="false"
        width="70%"
        :close-on-click-modal="false"
        :visible.sync="isEdit"
    >
      <el-form :model="form" :rules="formRule" ref="formRef" label-width="60px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="地址" prop="bootstrapServers">
              <el-input v-model="form.bootstrapServers" placeholder="多个地址使用,分割"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" type="textarea" :rows="4"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="isEdit=false">取 消</el-button>
        <el-button type="primary" @click="save" :loading="loading">确 定</el-button>
      </span>
    </el-dialog>

    <el-form :inline="true" :model="queryItem" label-width="80px">
      <el-form-item label="名称">
        <el-input v-model="queryItem.name"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button-group>
          <el-button type="primary" plain @click="query4Num(1)">查询</el-button>
          <el-button plain @click="resetQuery()">重置</el-button>
          <el-button plain type="primary" @click="add">新增</el-button>
        </el-button-group>
      </el-form-item>
    </el-form>


    <el-table :data="list" height="445px">
      <el-table-column
          type="index"
          width="55">
      </el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="bootstrapServers" label="地址" show-overflow-tooltip></el-table-column>
      <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button-group>
            <el-button type="primary" plain @click="edit(scope.row)">编辑</el-button>
            <el-button type="danger" plain @click="deleteById(scope.row.id)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        background
        @size-change="query4Size"
        @current-change="query4Num"
        :page-size="page.pageSize"
        :current-page="page.pageNum"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total">
    </el-pagination>
  </div>


</template>

<script>
import api from '../api/cluster'

export default {
  name: "Cluster",
  data() {
    return {
      isEdit: false,
      loading: false,
      list: [],
      dQueryItem: {},
      queryItem: {
        name: "",
        bootstrapServers: "",
        description: ""
      },
      page: {
        pageSize: 10,
        pageNum: 1,
        total: 0
      },
      dForm: {},
      form: {
        id: "",
        name: "",
        bootstrapServers: "",
        description: ""
      },
      formRule: {
        name: [
          {required: true, message: "名称为必填项", trigger: ["blur", "change"]},
          {max: 128, message: "名称长度不能大于128", trigger: ["blur", "change"]}
        ],
        bootstrapServers: [
          {required: true, message: "地址为必填项", trigger: ["blur", "change"]},
          {max: 128, message: "地址长度不能大于128", trigger: ["blur", "change"]}
        ]

      }
    }
  }, methods: {
    query() {
      api.query4Page({
        name: this.queryItem.name,
        bootstrapServers: this.queryItem.bootstrapServers,
        description: this.queryItem.description,
        pageNum: this.page.pageNum,
        pageSize: this.page.pageSize
      }).then(result => {
        this.page.total = result.data.total;
        this.list = result.data.list;
      });
    },
    query4Num(pageNum) {
      this.page.pageNum = pageNum;
      this.query();
    },
    query4Size(pageSize) {
      this.page.pageSize = pageSize;
      this.page.pageNum = 1;
      this.query();
    },
    resetQuery() {
      this.queryItem = Object.assign({}, this.dQueryItem);
      this.query4Num(1);
    },
    add() {
      this.form = Object.assign({}, this.dForm);
      this.isEdit = true;
      this.$nextTick(() => {
        this.$refs.formRef.clearValidate([]);
      });
    },
    edit(row) {
      Object.assign(this.form, row);
      this.isEdit = true;
      this.$nextTick(() => {
        this.$refs.formRef.clearValidate([]);
      });
    },
    save() {
      this.$refs["formRef"].validate((v) => {
        if (v) {
          this.loading = true;
          api.save(this.form).then(() => {
            this.$message.success("保存成功");
            this.query();
            this.loading = false;
            this.isEdit = false;
            this.$emit('update');
          }).catch(() => {
            this.loading = false;
          });
        }
      });

    },
    deleteById(id) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示").then(() => {
        api.deleteById(id).then(() => {
          this.$message.success("删除成功");
          this.query();
        });
      });

    }
  }, created() {
    Object.assign(this.form, this.dForm);
    this.query();
  }
}
</script>

<style scoped>

</style>