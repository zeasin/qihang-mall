<template>
  <div class="app-container">


    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shopList" >
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="小组名称" align="center" prop="name" />
      <el-table-column label="负责人" align="center" prop="manageUserId" >
        <template slot-scope="scope">
          <el-tag >{{userList.find(x=>x.userId == scope.row.manageUserId)?userList.find(x=>x.userId == scope.row.manageUserId).nickName:''}}</el-tag>
        </template>
      </el-table-column>

<!--      <el-table-column label="描述" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.id !== 500 && scope.row.id !== 999"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['shop:shop:edit']"
          >修改</el-button>

<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-location"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['shop:shop:edit']"-->
<!--          >省市区地址库</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改店铺对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="小组名称" prop="name">
          <el-input v-model="form.name" placeholder="国家/地区" />
        </el-form-item>
        <el-form-item label="负责人" prop="manageUserId">
          <el-select v-model="form.manageUserId" placeholder="请选择负责人" >
            <el-option
              v-for="item in userList"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listGroup, updateGroup, getGroup,addGroup} from "@/api/shop/group";
import { listUser} from "@/api/system/user";
export default {
  name: "ShopGroup",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 店铺表格数据
      shopList: [],
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      isEdit: true,
      // 查询参数
      queryParams: {},
      // 表单参数
      form: {
        exchangeRate:null
      },
      // 表单校验
      rules: {
        name: [{ required: true, message: "不能为空", trigger: "blur" }],
        manageUserId: [{ required: true, message: "不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    listUser().then(response => {
        this.userList = response.rows.filter(item => item.userId >= 100);
        this.getList();
      });

  },
  methods: {
    /** 查询店铺列表 */
    getList() {
      this.loading = true;
      listGroup().then(response => {
        this.shopList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.apiOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.isEdit=true
      this.form = {
        id: null,
        name: null,
        num: null,
        exchangeRate: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 角色状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.name + '"平台吗？').then(function() {
        return changeStatus(row.id, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getGroup(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改";
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.isEdit = false
      this.title = "添加";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateGroup(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          }else{
            // 新增
            addGroup(this.form).then(response => {
              this.$modal.msgSuccess("添加成功");
              this.open = false;
              this.isEdit = true;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
