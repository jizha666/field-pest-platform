<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { queryPageApi_a, addApi, updateApi, queryById, deleteApi } from "@/api/area";

const searchLand = ref({
  province: "",
  city: "",
});

const landList = ref([
  // {
  //   id: 11,
  //   province: "上海市",
  //   city: "崇明区",
  //   number: 2,
  // },
  // {
  //   id: 22,
  //   province: "上海市",
  //   city: "松江区",
  //   number: 3,
  // },
]);

onMounted(() => {//打开页面时自动执行
  search();
});

const search = async () => {
  console.log("Search:", searchLand.value);//控制台输出
  const result = await queryPageApi_a(
    searchLand.value.province,
    searchLand.value.city,
    currentPage.value,
    pageSize.value
  );
  if(result.code){
    landList.value = result.data.rows
    total.value = result.data.total
  }
};

const clear = () => {
  searchLand.value = {
    province: "",
    city: "",
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

//新增/修改表单
const landFormRef = ref(null);
const land = ref({
  id: "",
  province: "",
  city: "",
});

const addLand = () => {
  dialogVisible.value = true;
  dialogTitle.value = "新增地区";

  land.value = {
    province: "",
    city: "",
  };
  if (landFormRef.value) {
    landFormRef.value.resetFields();
  }
};

//表单校验规则
// 验证规则
const rules = ref({
  province: [
    { required: true, message: "请输入省份", trigger: "blur" },
    { min: 2, max: 20, message: "省份名应在2到20个字符之间", trigger: "blur" },
  ],
  city: [
    { required: true, message: "请输入市/区", trigger: "blur" },
    { min: 2, max: 20, message: "市/区名应在2到20个字符之间", trigger: "blur" },
  ],
});

const dialogVisible = ref(false);
const dialogTitle = ref("新增地区");

const cancel = () => {
  dialogVisible.value = false;
  land.value = {
    province: "",
    city: "",
  };
};

const save = async () => {
  landFormRef.value.validate(async (valid) => {
    if (valid) { //通过

      let result;
      if(land.value.id){ //修改
        result = await updateApi(land.value);
      }else { //新增
        result = await addApi(land.value);
      }

      if(result.code) { //成功
        ElMessage.success('保存成功');
        dialogVisible.value = false;
        search();
      }else { //失败了
        ElMessage.error(result.msg);
      }
    }else { //不通过
      ElMessage.error('表单校验不通过');
    }
  });
};

const edit = async (id) => {
  //console.log(id);
  const result = await queryById(id);
  if(result.code){
    dialogVisible.value = true;
    dialogTitle.value = "修改地区";
    land.value = result.data;
  }


  // if (landFormRef.value) {
  //   landFormRef.value.resetFields();
  // }
  // land.value.province = id;
  // land.value.city = id;
};

const deleteById = (id) => {
  //弹出确认框
  ElMessageBox.confirm("您确认删除该地区吗？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      const result = await deleteApi(id);
      if(result.code){
        ElMessage.success("删除成功");
        search();
      }else{
        ElMessage.error(result.msg);
      }
    })
    .catch(() => {
      ElMessage.info("您已取消删除");
    });
};
</script>

<template>
  <h1>地区管理</h1>
  <!-- {{ searchLand }} -->
  <br />
  <el-form :inline="true" :model="searchLand">
    <el-form-item label="省份">
      <el-input
        v-model="searchLand.province"
        placeholder="请输入省份"
      ></el-input>
    </el-form-item>

    <el-form-item label="市/区">
      <el-input v-model="searchLand.city" placeholder="请输入市/区"></el-input>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button @click="clear">清空</el-button>
    </el-form-item>
  </el-form>

  <el-button type="primary" @click="addLand"> + 新增地区</el-button>
  <br /><br />

  <!-- 表格 -->
  <el-table :data="landList" border style="width: 100%">
    <el-table-column type="index" label="序号" width="100" align="center" />
    <el-table-column
      prop="province"
      label="省份"
      width="250"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="city"
      label="市/区"
      width="300"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="number"
      label="田地数量"
      width="250"
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

  <el-pagination
    v-model:current-page="currentPage"
    v-model:page-size="pageSize"
    :page-sizes="[5, 10, 20, 30, 50, 75, 100]"
    layout="total, sizes, prev, pager, next, jumper"
    :total="total"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  />

  <!-- 新增/修改地区的对话框 -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form ref="landFormRef" :model="land" :rules="rules" label-width="80px">
      <!-- 基本信息 -->
      <!-- 第一行 -->

      <el-form-item label="省份名" prop="province">
        <el-input
          v-model="land.province"
          placeholder="请输入省份名，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="区/市名" prop="city">
        <el-input
          v-model="land.city"
          placeholder="请输入区/市名，2-20个字"
        ></el-input>
      </el-form-item>
    </el-form>

    <!-- 底部按钮 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.avatar {
  height: 40px;
}
</style>