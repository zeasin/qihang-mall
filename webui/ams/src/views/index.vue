<template>
  <div class="dashboard-editor-container">

<!--    <panel-group :chart-data="report" @handleSetLineChartData="handleSetLineChartData" />-->

<!--    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">-->
<!--      <line-chart :chart-data="lineChartData" />-->
<!--    </el-row>-->
<!--    <el-row>-->
<!--      <el-table-->
<!--        :data="tableData"-->
<!--        style="width: 100%">-->
<!--        <el-table-column-->
<!--          prop="date"-->
<!--          label="日期"-->
<!--          width="180">-->
<!--        </el-table-column>-->
<!--        <el-table-column-->
<!--          prop="name"-->
<!--          label="姓名"-->
<!--          width="180">-->
<!--        </el-table-column>-->
<!--        <el-table-column-->
<!--          prop="address"-->
<!--          label="地址">-->
<!--        </el-table-column>-->
<!--      </el-table>-->
<!--    </el-row>-->
<!--    <el-row :gutter="32">-->
<!--&lt;!&ndash;      <el-col :xs="24" :sm="24" :lg="8">&ndash;&gt;-->
<!--&lt;!&ndash;        <div class="chart-wrapper">&ndash;&gt;-->
<!--&lt;!&ndash;          <raddar-chart />&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-col>&ndash;&gt;-->
<!--      <el-col :xs="24" :sm="24" :lg="24">-->
<!--        <div class="chart-wrapper">-->
<!--          <pie-chart :chart-data="skuTopData" />-->
<!--        </div>-->
<!--      </el-col>-->
<!--&lt;!&ndash;      <el-col :xs="24" :sm="24" :lg="12">&ndash;&gt;-->
<!--&lt;!&ndash;        <div class="chart-wrapper">&ndash;&gt;-->
<!--&lt;!&ndash;          <bar-chart />&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-col>&ndash;&gt;-->
<!--    </el-row>-->


  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'

const lineChartData = {
  // newVisitis: {
  //   expectedData: [100, 120, 161, 134, 105, 160, 165],
  //   actualData: [120, 82, 91, 154, 162, 140, 145]
  // },
  // messages: {
  //   expectedData: [200, 192, 120, 144, 160, 130, 140],
  //   actualData: [180, 160, 151, 106, 145, 150, 130]
  // },
  // purchases: {
  //   expectedData: [80, 100, 121, 104, 105, 90, 100],
  //   actualData: [120, 90, 100, 138, 142, 130, 130]
  // },
  // shoppings: {
  //   expectedData: [130, 140, 141, 142, 145, 150, 160],
  //   actualData: [120, 82, 91, 154, 162, 140, 130]
  // }
  salesDaily: {
    date:['09-15','09-16','09-17','09-18','09-19','09-20','09-21'],
    salesVolume: [130, 140, 141, 142, 145, 150, 160],
    salesOrder: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'Index',
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart
  },
  data() {
    return {
      lineChartData: {
        date:[],
        salesVolume:[],
        salesOrder:[]
      },
      skuTopData:{
        data:[
          // { value: 320, name: 'AAA' },
          // { value: 240, name: 'BBB' },
          // { value: 149, name: 'CC' },
          // { value: 100, name: 'DD' },
          // { value: 59, name: 'DEEE' }
        ]
      },
      report:{
        inventory:50,
        salesVolume:5989.98,
        orderCount:302,
        shopCount:8
      }
    }
  },
  mounted() {

  },
  methods: {
    handleSetLineChartData(type) {
      // this.lineChartData = lineChartData[type]
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
