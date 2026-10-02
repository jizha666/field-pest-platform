<script setup>
import { ref, onMounted } from "vue";
import { queryPageApi } from "@/api/pest";

const pestList = ref([]);

onMounted(() => {
  search();
});

const searchPest = ref({
  name: "",
  date: "",
});

const search = async () => {
  console.log("Search:", searchPest.value);
  const result = await queryPageApi(
    searchPest.value.name,
    searchPest.value.date,
    currentPage.value,
    pageSize.value
  );
  if(result.code){
    pestList.value = result.data.rows
    total.value = result.data.total
  }
};

const clear = () => {
  searchPest.value = {
    name: "",
    date: "",
  };
  search();
};

// 分页配置
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 分页处理
const handleSizeChange = (val) => {
  search();
};
const handleCurrentChange = (val) => {
  search();
};
</script>

<template>
  <h1>虫情查询</h1>
  <br />

  <el-form :inline="true" :model="searchPest">
    <el-form-item label="田地名称">
      <el-input
        v-model="searchPest.name"
        placeholder="请输入田地名称"
      ></el-input>
    </el-form-item>

    <!-- <el-form-item label="市/区">
      <el-input v-model="searchLand.city" placeholder="请输入市/区"></el-input>
    </el-form-item> -->

    <el-form-item label="选择日期">
      <el-date-picker
        v-model="searchPest.date"
        type="date"
        placeholder="开始日期"
        value-format="YYYY-MM-DD"
      ></el-date-picker>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button @click="clear">清空</el-button>
    </el-form-item>
  </el-form>

  <br /><br />

  <!-- 表格 -->
  <el-table :data="pestList" border style="width: 100%">
    <el-table-column
      prop="province"
      label="省份"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="city"
      label="市/区"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="name"
      label="田地名称"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="number"
      label="稻纵卷叶螟数量"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="temperature"
      label="温度"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="date"
      label="日期"
      align="center"
    ></el-table-column>
  </el-table>

  <br />

  <el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    v-model:current-page="currentPage"
    v-model:page-size="pageSize"
    :page-sizes="[10, 20, 30, 40]"
    layout="total, sizes, prev, pager, next, jumper"
    :total="total"
  >
  </el-pagination>
</template>

<style scoped>
.avatar {
  height: 40px;
}
</style>