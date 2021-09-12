<template>
  <div>
    <el-dialog
        title="集群"
        :modal="false"
        width="500px"
        :close-on-click-modal="false"
        :visible.sync="isEdit"
    >
      <el-form :model="form" :rules="formRule" ref="formRef" label-width="80px">
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
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button-group>
          <el-button @click="isEdit=false">取 消</el-button>
          <el-button type="primary" @click="save" :loading="loading">确 定</el-button>
        </el-button-group>
      </span>
    </el-dialog>

    <div style="text-align: right">
      <el-input v-model="queryItem.name" style="width: 180px" placeholder="过滤..."></el-input>
      <el-button-group>
        <el-button plain icon="el-icon-search " @click="query4Num(1)" title="查询"></el-button>
        <el-button plain icon="el-icon-plus" @click="add" title="新增"></el-button>
      </el-button-group>
    </div>

    <el-table :data="list" stripe>
      <el-table-column prop="name" label="名称" width="120px" show-overflow-tooltip></el-table-column>
      <el-table-column prop="bootstrapServers" label="地址" show-overflow-tooltip></el-table-column>
      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button-group>
            <el-button icon="el-icon-edit" plain @click="edit(scope.row)" title="编辑"></el-button>
            <el-button icon="el-icon-delete" plain @click="deleteById(scope.row.id)" title="删除"></el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        background
        :page-sizes="[5,10,20]"
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
        pageSize: 5,
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
            if(!this.form.id){
              this.$emit('update');
            }
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
          this.$emit('delete', id);
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