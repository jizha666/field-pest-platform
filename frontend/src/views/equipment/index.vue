<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { queryPageApi, addApi, updateApi, queryById, deleteApi } from "@/api/equip"

const searchEquip = ref({
  name: "",
  landName: "",
  category: "",
  status: "",
});

const equipList = ref([]);

onMounted(() => {
  search();
});

//新增/修改表单
const equipFormRef = ref(null);
const equip = ref({
  name: "",
  landName: "",
  category: "",
  status: "",
});

const search = async () => {
  console.log("Search: ", searchEquip.value);
  const result = await queryPageApi(
    searchEquip.value.name,
    searchEquip.value.landName,
    searchEquip.value.category,
    searchEquip.value.status,
    currentPage.value,
    pageSize.value
  );
  if(result.code){
    equipList.value = result.data.rows
    total.value = result.data.total
  }
};

const clear = () => {
  searchEquip.value = {
    name: "",
    landName: "",
    category: "",
    status: "",
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

const addEquip = () => {
  dialogVisible.value = true;
  dialogTitle.value = "新增设备";

  equip.value = {
    name: "",
    landName: "",
    category: "",
    status: "",
  };
  if (equipFormRef.value) {
    equipFormRef.value.resetFields();
  }
};

//表单校验规则
// 验证规则
const rules = ref({
  name: [
    { required: true, message: "请输入设备名称", trigger: "blur" },
    {
      min: 2,
      max: 20,
      message: "设备名称应在2到20个字符之间",
      trigger: "blur",
    },
  ],
  landName: [
    { required: true, message: "请输入田地名称", trigger: "blur" },
    { min: 2, max: 20, message: "田地名称应在2到20字符之间" },
  ],
  category: [
    { required: true, message: "请输入设备类别", trigger: "blur" },
    { min: 2, max: 20, message: "设备类别应在2到20字符之间" },
  ],
  status: [{ required: true, message: "请选择工作状态", trigger: "blur" }],
});

const dialogVisible = ref(false);
const dialogTitle = ref("新增设备");

const cancel = () => {
  dialogVisible.value = false;
  equip.value = {
    name: "",
    landName: "",
    category: "",
    status: "",
  };
};

const save = async () => {
  equipFormRef.value.validate(async (valid) => {
    if (valid) {
      let result;
      if(equip.value.id){
        result = await updateApi(equip.value);
      }else {
        result = await addApi(equip.value);
      }

      if(result.code) {
        ElMessage.success('保存成功');
        dialogVisible.value = false;
        search();
      }else {
        ElMessage.error(result.msg);
      }
    }else {
      ElMessage.error('表单校验不通过');
    }
  });
};

const edit = async (id) => {
  const result = await queryById(id);
  if(result.code){
    dialogVisible.value = true;
    dialogTitle.value = "修改设备";
    equip.value = result.data;
    if(equip.value.status == 0){
      equip.value.status = "0"
    }else{
      equip.value.status = "1"
    }
  }

  if (equipFormRef.value) {
    equipFormRef.value.resetFields();
  }
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
  <h1>设备管理</h1>

  <br />

  <el-form :inline="true" :model="searchEquip">
    <el-form-item label="设备名称">
      <el-input
        v-model="searchEquip.name"
        placeholder="请输入设备名称"
      ></el-input>
    </el-form-item>

    <el-form-item label="所属田地">
      <el-input
        v-model="searchEquip.landName"
        placeholder="请输入所属田地名称"
      ></el-input>
    </el-form-item>

    <el-form-item label="类别">
      <el-input
        v-model="searchEquip.category"
        placeholder="请输入设备类别"
      ></el-input>
    </el-form-item>

    <el-form-item label="工作状态">
      <el-select v-model="searchEquip.status" placeholder="请选择">
        <el-option label="正常" value="0"></el-option>
        <el-option label="异常" value="1"></el-option>
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button @click="clear">清空</el-button>
    </el-form-item>
  </el-form>

  <el-button type="primary" @click="addEquip"> + 新增设备</el-button>
  <br /><br />

  <!-- 表格 -->
  <el-table :data="equipList" border style="width: 100%">
    <el-table-column type="index" label="序号" width="100" align="center" />
    <el-table-column
      prop="name"
      label="设备名称"
      width="300"
      align="center"
    ></el-table-column>

    <el-table-column
      prop="landName"
      label="所属田地名称"
      width="300"
      align="center"
    ></el-table-column>

    <el-table-column
      prop="category"
      label="设备类别"
      width="200"
      align="center"
    ></el-table-column>

    <el-table-column label="工作状态" width="200" align="center">
      <template #default="scope">
        {{ scope.row.status == 0 ? "正常" : "异常" }}
      </template>
    </el-table-column>
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

  <!-- 新增/修改地区的对话框 -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="equipFormRef"
      :model="equip"
      :rules="rules"
      label-width="100px"
    >
      <!-- 基本信息 -->
      <el-form-item label="设备名称：" prop="name">
        <el-input
          v-model="equip.name"
          placeholder="请输入设备名称，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="田地名称：" prop="landName">
        <el-input
          v-model="equip.landName"
          placeholder="请输入所属田地名称，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="设备类别：" prop="category">
        <el-input
          v-model="equip.category"
          placeholder="请输入设备类别，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="工作状态：" prop="status">
        <el-select
          v-model="equip.status"
          placeholder="请选择设备工作状态"
          style="width: 100%"
        >
          <el-option label="正常" value="0"></el-option>
          <el-option label="异常" value="1"></el-option>
        </el-select>
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
</style>