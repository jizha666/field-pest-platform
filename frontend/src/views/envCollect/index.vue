<script setup>
import { ref, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";

const searchEnv = ref({
  name: "",
  category: "",
  date: [],
  begin: "",
  end: "",
});

const envList = ref([
  {
    id: 11,
    name: "崇明一号田",
    category: "温度",
    value: "23℃",
    createTime: "2024-09-01T23:06:29",
    updateTime: "2024-09-01T23:06:29",
  },
  {
    id: 22,
    name: "崇明二号田",
    category: "湿度",
    value: "50%",
    createTime: "2024-09-01T23:06:29",
    updateTime: "2024-09-01T23:06:29",
  },
]);

//新增/修改表单变量
const envFormRef = ref(null);
const env = ref({
  name: "",
  category: "",
  value: "",
});

//新增/修改表单可视化
const addDialogVisible = ref(false);
const addDialogTitle = ref("新增记录");
const editDialogVisible = ref(false);
const editDialogTitle = ref("修改记录");

//侦听searchEnv中的date属性
watch(
  () => searchEnv.value.date,
  (newValue, oldValue) => {
    if (newValue.length == 2) {
      searchEnv.value.begin = newValue[0];
      searchEnv.value.end = newValue[1];
    } else {
      searchEnv.value.begin = "";
      searchEnv.value.end = "";
    }
  }
);

const search = () => {
  console.log("Search: ", searchEnv.value);
};

const clear = () => {
  searchEnv.value = {
    name: "",
    category: "",
    date: [],
    begin: "",
    end: "",
  };
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

const addEnv = () => {
  addDialogVisible.value = true;

  env.value = {
    name: "",
    category: "",
    value: "",
  };

  if (envFormRef.value) {
    envFormRef.value.resetFields();
  }
};

//表单校验规则
// 验证规则
const rules = ref({
  name: [
    { required: true, message: "请输入田地名称", trigger: "blur" },
    {
      min: 2,
      max: 20,
      message: "田地名称应在2到20个字符之间",
      trigger: "blur",
    },
  ],
  category: [
    { required: true, message: "请输入数据类别", trigger: "blur" },
    { min: 2, max: 20, message: "数据类别应在2到20字符之间", trigger: "blur" },
  ],
  value: [
    { required: true, message: "请输入数值", trigger: "blur" },
    { min: 2, max: 20, message: "数值应在2到20字符之间", trigger: "blur" },
  ],
});

const cancel = () => {
  addDialogVisible.value = false;
  editDialogVisible.value = false;
  env.value = {
    name: "",
    category: "",
    value: "",
  };
  if (envFormRef.value) {
    envFormRef.value.resetFields();
  }
};

const save = async () => {
  envFormRef.value.validate(async (valid) => {
    if (valid) {
      ElMessage.success("保存成功");
      addDialogVisible.value = false;
      editDialogVisible.value = false;
      console.log(env.value);
      search();
    }
  });
};

const edit = async (id) => {
  editDialogVisible.value = true;

  if (envFormRef.value) {
    envFormRef.value.resetFields();
  }

  env.value.name = id;
  env.value.category = id;
};

const deleteById = (id) => {
  //弹出确认框
  ElMessageBox.confirm("您确认删除该记录吗？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      ElMessage.success("删除成功");
      search();
    })
    .catch(() => {
      ElMessage.info("您已取消删除");
    });
};
</script>

<template>
  <h1>环境数据采集</h1>
  <br />

  <el-form :inline="true" :model="searchEnv">
    <el-form-item label="田地名称">
      <el-input
        v-model="searchEnv.name"
        placeholder="请输入田地名称"
      ></el-input>
    </el-form-item>

    <el-form-item label="数据类别">
      <el-input
        v-model="searchEnv.category"
        placeholder="请输入数据类别"
      ></el-input>
    </el-form-item>

    <el-form-item label="查询范围">
      <el-date-picker
        v-model="searchEnv.date"
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

  <el-button type="primary" @click="addEnv"> + 新增记录</el-button>
  <br /><br />

  <!-- 表格 -->
  <el-table :data="envList" border style="width: 100%">
    <el-table-column
      prop="name"
      label="田地名称"
      width="250"
      align="center"
    ></el-table-column>

    <el-table-column
      prop="category"
      label="数据类别"
      width="180"
      align="center"
    ></el-table-column>

    <el-table-column
      prop="value"
      label="数值"
      width="180"
      align="center"
    ></el-table-column>

    <el-table-column
      prop="createTime"
      label="创建时间"
      width="280"
      align="center"
    ></el-table-column>

    <el-table-column
      prop="updateTime"
      label="最后修改时间"
      width="280"
      align="center"
    ></el-table-column>

    <el-table-column label="操作" fixed="right" align="center">
      <template #default="scope">
        <el-button size="small" type="primary" @click="edit(scope.row.id)"
          >编辑</el-button
        >
        <el-button size="small" type="danger" @click="deleteById(scope.row.id)"
          >删除</el-button
        >
      </template>
    </el-table-column>
  </el-table>

  <br />

  <!-- 分页 -->
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

  <!-- 新增记录对话框 -->
  <el-dialog v-model="addDialogVisible" :title="addDialogTitle">
    <el-form ref="envFormRef" :model="env" :rules="rules" label-width="100px">
      <!-- 基本信息 -->

      <el-form-item label="田地名称：" prop="name">
        <el-input
          v-model="env.name"
          placeholder="请输入田地名称，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="数据类别" prop="category">
        <el-input
          v-model="env.category"
          placeholder="请输入更新的数据类别，2-20个字"
        ></el-input>
      </el-form-item>
    </el-form>

    <!-- 底部按钮 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="save">更新</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 编辑记录对话框 -->
  <el-dialog v-model="editDialogVisible" :title="editDialogTitle">
    <el-form ref="envFormRef" :model="env" :rules="rules" label-width="100px">
      <!-- 基本信息 -->

      <el-form-item label="田地名称：" prop="name">
        <el-input
          v-model="env.name"
          placeholder="请输入田地名称，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="数据类别" prop="category">
        <el-input
          v-model="env.category"
          placeholder="请输入数据类别，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="数值" prop="value">
        <el-input
          v-model="env.value"
          placeholder="请输入数值，2-20个字"
        ></el-input>
      </el-form-item>
    </el-form>

    <!-- 底部按钮 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="save">新增记录</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
</style>