<script setup>
import { ref, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";

const searchPest = ref({
  name: "",
  landName: "",
  date: [],
  begin: "",
  end: "",
});

//样例数据
const pestList = ref([
  {
    id: 1,
    landName: "崇明一号田",
    name: "稻纵卷叶螟",
    image:
      "https://web-framework.oss-cn-hangzhou.aliyuncs.com/2022-09-02-00-27-53B.jpg",
    number: 10,
    createTime: "2024-09-01T23:06:29",
    updateTime: "2024-09-01T23:06:29",
  },
  {
    id: 2,
    landName: "崇明二号田",
    name: "稻纵卷叶螟",
    image: "https://web-zhaji.oss-cn-shanghai.aliyuncs.com/cat.jpg",
    number: 100,
    createTime: "2024-09-01T23:06:29",
    updateTime: "2024-09-01T23:06:29",
  },
]);

//新增/修改表单变量
const pestFormRef = ref(null);
const pest = ref({
  name: "",
  landName: "",
  image: "",
  number: "",
});

//新增/修改表单可视化
const addDialogVisible = ref(false);
const addDialogTitle = ref("新增记录");
const editDialogVisible = ref(false);
const editDialogTitle = ref("修改记录");

//侦听searchEnv中的date属性
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

const search = () => {
  console.log("Search: ", searchPest.value);
};

const clear = () => {
  searchPest.value = {
    name: "",
    landName: "",
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

const addPest = () => {
  addDialogVisible.value = true;

  pest.value = {
    landName: "",
    image: "",
  };

};

//文件上传
// 图片上传成功后触发
const handleAvatarSuccess = (response, uploadFile) => {
  employee.value.image = response.data;
};
// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== "image/jpeg" && rawFile.type !== "image/png") {
    ElMessage.error("只支持上传图片");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error("只能上传10M以内图片");
    return false;
  }
  return true;
};

//表单校验规则
// 验证规则
const rules = ref({
  landName: [
    { required: true, message: "请输入田地名称", trigger: "blur" },
    {
      min: 2,
      max: 20,
      message: "田地名称应在2到20个字符之间",
      trigger: "blur",
    },
  ],
  name: [
    { required: true, message: "请输入害虫名称", trigger: "blur" },
    {
      min: 2,
      max: 20,
      message: "害虫名称应在2到20个字符之间",
      trigger: "blur",
    },
  ],
  category: [
    { required: true, message: "请输入数据类别", trigger: "blur" },
    { min: 2, max: 20, message: "数据类别应在2到20字符之间", trigger: "blur" },
  ],
  number: [
    { required: true, message: "请输入数值", trigger: "blur" },
    { min: 1, max: 20, message: "数值应在1到20字符之间", trigger: "blur" },
  ],
  image: [{ required: true, message: "请上传图片", trigger: "blur" }],
});

const cancel = () => {
  addDialogVisible.value = false;
  editDialogVisible.value = false;
  pest.value = {
    name: "",
    landName: "",
    image: "",
    number: "",
  };

  if (pestFormRef.value) {
    pestFormRef.value.resetFields();
  }
};

const save = async () => {
  pestFormRef.value.validate(async (valid) => {
    if (valid) {
      ElMessage.success("保存成功");
      addDialogVisible.value = false;
      editDialogVisible.value = false;
      console.log(pest.value);
      search();
    }
  });
};

const edit = async (id) => {
  editDialogVisible.value = true;

  pest.value.name = id;
  pest.value.landName = id;
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
  <h1>害虫数据采集</h1>
  <br />

  <el-form :inline="true" :model="searchPest">
    <el-form-item label="田地名称">
      <el-input
        v-model="searchPest.landName"
        placeholder="请输入田地名称"
      ></el-input>
    </el-form-item>

    <el-form-item label="害虫名称">
      <el-input
        v-model="searchPest.name"
        placeholder="请输入害虫名称"
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

  <el-button type="primary" @click="addPest"> + 新增记录</el-button>
  <br /><br />

  <!-- 表格 -->
  <el-table :data="pestList" border style="width: 100%">
    <el-table-column
      prop="landName"
      label="田地名称"
      width="200"
      align="center"
    ></el-table-column>

    <el-table-column
      prop="name"
      label="害虫名称"
      width="200"
      align="center"
    ></el-table-column>

    <el-table-column label="害虫图片" width="200" align="center">
      <template #default="scope">
        <img :src="scope.row.image" alt="Avatar" class="avatar" />
      </template>
    </el-table-column>

    <el-table-column
      prop="number"
      label="害虫数量"
      width="130"
      align="center"
    ></el-table-column>

    <el-table-column
      prop="createTime"
      label="创建时间"
      width="250"
      align="center"
    ></el-table-column>

    <el-table-column
      prop="updateTime"
      label="最后修改时间"
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
    <el-form ref="pestFormRef" :model="pest" :rules="rules" label-width="100px">
      <!-- 基本信息 -->

      <el-form-item label="田地名称：" prop="landName">
        <el-input
          v-model="pest.landName"
          placeholder="请输入田地名称，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="害虫图片">
        <el-upload
          class="avatar-uploader"
          action="/api/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="pest.image" :src="pest.image" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
    </el-form>

    <!-- 底部按钮 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="save">上传</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 编辑记录对话框 -->
  <el-dialog v-model="editDialogVisible" :title="editDialogTitle">
    <el-form ref="pestFormRef" :model="pest" :rules="rules" label-width="100px">
      <!-- 基本信息 -->

      <el-form-item label="田地名称：" prop="landName">
        <el-input
          v-model="pest.landName"
          placeholder="请输入田地名称，2-20个字"
        ></el-input>
      </el-form-item>

      <el-form-item label="害虫名称" prop="name">
        <el-input
          v-model="pest.name"
          placeholder="请输入害虫名称，2-20个字"
        ></el-input>
      </el-form-item>
      <el-form-item label="害虫图片">
        <el-upload
          class="avatar-uploader"
          action="/api/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="pest.image" :src="pest.image" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>

      <el-form-item label="害虫数量" prop="number">
        <el-input
          v-model="pest.number"
          placeholder="请输入害虫数量，1-20个字"
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
.avatar-uploader .avatar {
  width: 78px;
  height: 78px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  text-align: center;
  /* 添加灰色的虚线边框 */
  border: 1px dashed var(--el-border-color);
}
</style>