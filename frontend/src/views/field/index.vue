<script setup>
import { ref, onMounted, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  queryPageApi_a,
  addApi,
  updateApi,
  queryProvince,
  queryCity,
  queryById,
  deleteApi,
} from "@/api/field";

const searchField = ref({
  province: "",
  city: "",
  name: "",
});

const fieldList = ref([]);

const provinces = ref([]);

const cities = ref([]);

onMounted(async () => {
  const response = await queryProvince();
  provinces.value = response.data;
  search();
});

//新增/修改表单
const fieldFormRef = ref(null);
const field = ref({
  id: "",
  name: "",
  province: "",
  city: "",
});

// 省份变更处理
watch(
  () => field.value.province,
  async (newValue, oldValue) => {
    if (newValue.length) {
      const result = await queryCity(newValue);
      cities.value = result.data;
    }
  }
);

const search = async () => {
  console.log("Search:", searchField.value);
  const result = await queryPageApi_a(
    searchField.value.name,
    searchField.value.province,
    searchField.value.city,
    currentPage.value,
    pageSize.value
  );
  if (result.code) {
    fieldList.value = result.data.rows;
    total.value = result.data.total;
  }
};

const clear = () => {
  searchField.value = {
    province: "",
    city: "",
    name: "",
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

const addField = () => {
  dialogVisible.value = true;
  dialogTitle.value = "新增试验田";

  field.value = {
    name: "",
    province: "",
    city: "",
  };
  if (fieldFormRef.value) {
    fieldFormRef.value.resetFields();
  }
};

//表单校验规则
// 验证规则
const rules = ref({
  name: [
    { required: true, message: "请输入试验田名", trigger: "blur" },
    {
      min: 2,
      max: 20,
      message: "试验田名应在2到20个字符之间",
      trigger: "blur",
    },
  ],
  province: [{ required: true, message: "请选择省份", trigger: "blur" }],
  city: [{ required: true, message: "请选择市/区", trigger: "blur" }],
});

const dialogVisible = ref(false);
const dialogTitle = ref("新增地区");

const cancel = () => {
  dialogVisible.value = false;
  field.value = {
    name: "",
    province: "",
    city: "",
  };
};

const save = async () => {
  fieldFormRef.value.validate(async (valid) => {
    if (valid) {
      let result;
      if (field.value.id) {
        //修改
        result = await updateApi(field.value);
      } else {
        //新增
        result = await addApi(field.value);
      }

      if (result.code) {
        //成功
        ElMessage.success("保存成功");
        dialogVisible.value = false;
        search();
      } else {
        //失败了
        ElMessage.error(result.msg);
      }
    } else {
      //不通过
      ElMessage.error("表单校验不通过");
    }
  });
};

const edit = async (id) => {
  const result = await queryById(id);
  if (result.code) {
    dialogVisible.value = true;
    dialogTitle.value = "修改试验田";
    field.value = result.data;
  }

  if (fieldFormRef.value) {
    fieldFormRef.value.resetFields();
  }

  // dialogVisible.value = true;
  // dialogTitle.value = "修改试验田";
};

const deleteById = (id) => {
  //弹出确认框
  ElMessageBox.confirm("您确认删除该试验田吗？", "提示", {
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
  <h1>试验田管理</h1>

  <br />

  <el-form :inline="true" :model="searchField">
    <el-form-item label="省份">
      <el-input
        v-model="searchField.province"
        placeholder="请输入省份名"
      ></el-input>
    </el-form-item>

    <el-form-item label="市/区">
      <el-input
        v-model="searchField.city"
        placeholder="请输入市/区名"
      ></el-input>
    </el-form-item>

    <el-form-item label="试验田名称">
      <el-input
        v-model="searchField.name"
        placeholder="请输入试验田名称"
      ></el-input>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button @click="clear">清空</el-button>
    </el-form-item>
  </el-form>

  <el-button type="primary" @click="addField"> + 新增试验田</el-button>

  <br /><br />

  <!-- 表格 -->
  <el-table :data="fieldList" border style="width: 100%">
    <el-table-column type="index" label="序号" width="100" align="center" />
    <el-table-column
      prop="name"
      label="试验田名称"
      width="300"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="province"
      label="省份名称"
      width="250"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="city"
      label="市/区名称"
      width="300"
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

  <!-- 新增/修改地区的对话框 -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="fieldFormRef"
      :model="field"
      :rules="rules"
      label-width="100px"
    >
      <!-- 基本信息 -->

      <el-form-item label="试验田名：" prop="name">
        <el-input
          v-model="field.name"
          placeholder="请输入试验田名称，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="省份：" prop="province">
        <el-select
          v-model="field.province"
          placeholder="请选择省份"
          style="width: 100%"
        >
          <el-option
            v-for="province in provinces"
            :key="province.id"
            :label="province.name"
            :value="province.name"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="市/区：" prop="city">
        <el-select
          v-model="field.city"
          placeholder="请先选择省份"
          :disabled="!field.province"
          style="width: 100%"
        >
          <el-option
            v-for="city in cities"
            :key="city.id"
            :label="city.name"
            :value="city.name"
          />
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
.avatar {
  height: 40px;
}
</style>