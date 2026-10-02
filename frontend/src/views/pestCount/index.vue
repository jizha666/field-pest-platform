<script setup>
import { ref, watch, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import * as echarts from "echarts";
import { queryChartApi } from "@/api/pest";

// 示例数据
const pestList = ref([
  {
    name: "",
    land: "",
    values: [],
    dates: [],
  },
  {
    name: "",
    land: "",
    values: [],
    dates: [],
  },
]);

const searchPest = ref({
  name: "",
  date: [],
  begin: "",
  end: "",
});

onMounted(() => {
  //chart();
});

const chart = async () => {
  pestList.value.forEach((pest, index) => {
    const chartDom = document.getElementById(`pest-chart-${index}`);
    const chart = echarts.init(chartDom);
    chart.setOption(
      generateChartOption(
        pest.name,
        searchPest.value.name,
        pest.values,
        pest.dates
      )
    );

    // 为每个图表单独添加resize监听
    window.addEventListener("resize", () => {
      chart.resize();
    });
  });
};

//侦听searchPest中的date属性
watch(
  () => searchPest.value.date,
  (newValue, oldValue) => {
    if (newValue.length == 2) {
      searchPest.value.begin = newValue[0];
      searchPest.value.end = newValue[1];
    } else {
      searchPest.value.begin = "";
      searchPest.value.end = "";
    }
  }
);

// 生成图表配置
const generateChartOption = (name, land, data, date) => ({
  title: {
    text: land + "   " + name,
    left: "center",
    textStyle: {
      fontSize: 16,
    },
  },
  xAxis: {
    type: "category",
    data: date,
    axisLabel: {
      color: "#666",
    },
  },
  yAxis: {
    type: "value",
    axisLabel: {
      color: "#666",
    },
  },
  series: [
    {
      data: data,
      type: "line",
      smooth: true,
      itemStyle: {
        color: "#409EFF",
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: "#409EFF40" },
          { offset: 1, color: "#409EFF00" },
        ]),
      },
    },
  ],
  grid: {
    top: "15%",
    bottom: "15%",
    left: "8%",
    right: "8%",
  },
  tooltip: {
    trigger: "axis",
  },
});

const search = async () => {
  console.log("Search:", searchPest.value);
  if ((searchPest.value.name == "") | (searchPest.value.date == "")) {
    ElMessageBox.alert("请输入完整信息后查询！", "查询信息缺失", {
      // if you want to disable its autofocus
      // autofocus: false,
      confirmButtonText: "确认",
    });
    return;
  }
  const result = await queryChartApi(
    searchPest.value.name,
    searchPest.value.begin,
    searchPest.value.end
  );
  if (result.code) {
    //console.log(pestList.value);
    pestList.value = result.data;
    //console.log(result.data);
    //console.log(pestList.value);
    chart();
  }
};

const clear = async () => {
  searchPest.value = {
    name: "",
    date: [],
    begin: "",
    end: "",
  };
};
</script>

<template>
  <h1>虫情统计</h1>

  <br />

  <el-form :inline="true" :model="searchPest">
    <el-form-item label="田地名称">
      <el-input
        v-model="searchPest.name"
        placeholder="请输入田地名称"
      ></el-input>
    </el-form-item>

    <el-form-item label="查询范围">
      <el-date-picker
        v-model="searchPest.date"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="YYYY-MM-DD"
      ></el-date-picker>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button @click="clear">清空</el-button>
    </el-form-item>
  </el-form>

  <el-card
    v-for="(pest, index) in pestList"
    :key="index"
    style="margin-bottom: 20px"
  >
    <div :id="'pest-chart-' + index" style="width: 100%; height: 300px"></div>
  </el-card>
</template>

<style scoped>
.avatar {
  height: 40px;
}
</style>
