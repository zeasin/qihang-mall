<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="售后ID" prop="aftersaleId">
        <el-input
          v-model="queryParams.aftersaleId"
          placeholder="请输入售后ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="aftersaleType">
        <el-select v-model="queryParams.aftersaleType" placeholder="请选择类型" clearable @change="handleQuery">

          <el-option label="退货退款" value="0" ></el-option>
          <el-option label="已发货仅退款" value="1"></el-option>
          <el-option label="未发货仅退款" value="2"></el-option>
          <el-option label="换货" value="3"> </el-option>
          <el-option label="维修" value="8"> </el-option>
          <el-option label="价保" value="6"> </el-option>
          <el-option label="缺货补寄" value="7"> </el-option>

        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull"
        >API拉取新退款</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-refresh"
          size="mini"
          :disabled="multiple"
          @click="handlePushOms"
        >手动推送到售后中心</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taoRefundList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="退款ID" align="left" prop="aftersaleId" width="180px">
        <template slot-scope="scope">
          {{scope.row.aftersaleId}}
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.aftersaleId" @click="copyActiveCode($event,scope.row.aftersaleId)" ></i>
          <!--          <el-button type="text" :data-clipboard-text="scope.row.orderSn" size="mini" style="float: left"  @click="copyActiveCode($event,scope.row.orderSn)"  class="tag-copy">复制</el-button>-->
          <br/>
          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="订单号" align="left" prop="relatedId" width="200px">
        <template slot-scope="scope">
          {{scope.row.relatedId}}
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.relatedId" @click="copyActiveCode($event,scope.row.relatedId)" ></i>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="aftersaleType" >

        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.aftersaleType === 0"> 退货退款</el-tag>
          <el-tag size="small" v-if="scope.row.aftersaleType === 1"> 已发货仅退款</el-tag>
          <el-tag size="small" v-if="scope.row.aftersaleType === 2"> 未发货仅退款</el-tag>
          <el-tag size="small" v-if="scope.row.aftersaleType === 3"> 换货</el-tag>
          <el-tag size="small" v-if="scope.row.aftersaleType === 6"> 价保</el-tag>
          <el-tag size="small" v-if="scope.row.aftersaleType === 7"> 补寄</el-tag>
          <el-tag size="small" v-if="scope.row.aftersaleType === 8"> 维修</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="店铺" align="center" prop="shopId" >-->
<!--        <template slot-scope="scope">-->
<!--&lt;!&ndash;          <el-tag size="small"></el-tag>&ndash;&gt;-->
<!--            {{shopList.find(x=>x.id === scope.row.shopId).name}}-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="订单号" align="center" prop="relatedId" />-->
      <el-table-column label="商品" prop="orderProductName" ></el-table-column>
      <el-table-column label="退款金额" prop="refundAmount" >
          <template slot-scope="scope">
            <span>{{ amountFormatter(null,null,scope.row.refundAmount/100,0) }}</span>
          </template>
      </el-table-column>
      <el-table-column label="订单金额" prop="orderPayAmount" >
        <template slot-scope="scope">
          <span>{{ amountFormatter(null,null,scope.row.orderPayAmount/100,0) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="售后数量" prop="aftersaleNum" ></el-table-column>


      <el-table-column label="退款状态" align="center" prop="refundStatus" >
        <template slot-scope="scope">
<!--      退款状态；1-待退款;2-退款中;3-退款成功;4-退款失败;5-追缴成功;    -->
          <el-tag size="small" v-if="scope.row.refundStatus === 1"> 待退款</el-tag>
          <el-tag size="small" v-if="scope.row.refundStatus === 2"> 退款中</el-tag>
          <el-tag size="small" v-if="scope.row.refundStatus === 3"> 退款成功</el-tag>
          <el-tag size="small" v-if="scope.row.refundStatus === 4"> 退款失败</el-tag>
          <el-tag size="small" v-if="scope.row.refundStatus === 5"> 追缴成功</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="申请时间" align="center" prop="applyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.applyTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--          v-if="scope.row.auditStatus === 0 && scope.row.afterSalesType === 1 "-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleConfirm(scope.row)"-->
<!--            v-hasPermi="['tao:taoRefund:edit']"-->
<!--          >退货确认</el-button>-->
<!--          &lt;!&ndash; <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['tao:taoRefund:remove']"-->
<!--          >删除</el-button> &ndash;&gt;-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import { listRefund,pullRefund,pushOms } from "@/api/dou/refund";
import { listShop } from "@/api/shop/shop";
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import Clipboard from "clipboard";
export default {
  name: "RefundDou",
  data() {
    return {
      // 遮罩层
      loading: true,
      pullLoading: false,
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
      // 淘宝退款订单表格数据
      taoRefundList: [],
      shopList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundId: null,
        afterSalesType: null,
        tid: null,
        oid: null,

      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        num: [
          { required: true, message: "退货数量不能为空", trigger: "blur" }
        ],
        logisticsCompany: [
          { required: true, message: "不能为空", trigger: "change" }
        ],
        logisticsCode: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        sendTime: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    listShop({type: 400}).then(response => {
      this.shopList = response.rows;
      if (this.shopList && this.shopList.length > 0) {
        this.queryParams.shopId = this.shopList[0].id
      }
      this.getList();
    });
    // this.getList();
  },
  methods: {
    copyActiveCode(event,queryParams) {
      console.log(queryParams)
      const clipboard = new Clipboard(".tag-copy")
      clipboard.on('success', e => {
        this.$message({ type: 'success', message: '复制成功' })
        // 释放内存
        clipboard.destroy()
      })
      clipboard.on('error', e => {
        // 不支持复制
        this.$message({ type: 'waning', message: '该浏览器不支持自动复制' })
        // 释放内存
        clipboard.destroy()
      })
    },
    amountFormatter(row, column, cellValue, index) {
      return '￥' + cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询退款列表 */
    getList() {
      this.loading = true;
      listRefund(this.queryParams).then(response => {
        this.taoRefundList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        refundId: null,
        afterSalesType: null,
        tid: null,
        oid: null,
        refundPhase: null
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.aftersaleId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handlePushOms(row) {
      const ids = row.aftersaleId || this.ids;
      this.$modal.confirm('是否手动推送到OMS？').then(function() {
        return pushOms({ids:ids});
      }).then(() => {
        // this.getList();
        this.$modal.msgSuccess("推送成功");
      }).catch(() => {});
    },
    handlePull() {
      if(this.queryParams.shopId){
        this.pullLoading = true
        pullRefund({shopId:this.queryParams.shopId,updType:0}).then(response => {
          console.log('拉取淘宝订单接口返回=====', response)
          if (response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', {
              confirmButtonText: '前往授权',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.$router.push({path: "/shop/shop_list", query: {type: 3}})
              // isRelogin.show = false;
              // store.dispatch('LogOut').then(() => {
              // location.href = response.data.tokenRequestUrl+'?shopId='+this.queryParams.shopId
              // })
            }).catch(() => {
              isRelogin.show = false;
            });

            // return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
          } else {
            this.$modal.msgSuccess(JSON.stringify(response));
            this.getList();

          }
          this.pullLoading = false
        })
      }else{
        this.$modal.msgSuccess("请先选择店铺");
      }

      // this.$modal.msgSuccess("请先配置API");
    }
  }
};
</script>
