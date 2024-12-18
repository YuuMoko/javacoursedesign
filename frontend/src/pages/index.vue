<template>

  <h1 style="font-style: italic;">考场信息管理</h1>
  <div class="flex flex-wrap gap-4 items-center">
    <el-input v-model="searchMessage" style="width: 240px" placeholder="请输入搜索的内容...">
      <template #prepend>搜索</template>
    </el-input>
    <el-select-v2
        v-model="value"
        :options="options"
        placeholder="请输入搜索类型..."
        style="width: 240px"
    />
    <el-button type="primary" @click="search">搜索</el-button>

    <el-button type="primary" @click="dialogCreateVisible = true">创建</el-button>

    <el-dialog v-model="dialogCreateVisible" title="创建新的考场" width="500">
      <el-form :model="createForm">
        <el-form-item label="考场编号">
          <el-input v-model="createForm.roomNumber" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="考场位置">
          <el-input v-model="createForm.location" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="考场容量">
          <el-input v-model="createForm.capacity" autocomplete="off"/>
        </el-form-item>
        <div class="my-2 ml-4">
          <el-radio-group v-model="createForm.status">
            <el-radio value="0">空闲</el-radio>
            <el-radio value="1">占用</el-radio>
          </el-radio-group>
        </div>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogCreateVisible = false">取消</el-button>
          <el-button type="primary" @click="createExam">
            创建
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="dialogEditVisible" title="修改考场信息" width="500">
      <el-form :v-model="editForm">
        <el-form-item label="考场编号">
          <el-input v-model="editForm.roomNumber" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="考场位置">
          <el-input v-model="editForm.location" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="考场容量">
          <el-input v-model="editForm.capacity" autocomplete="off"/>
        </el-form-item>
        <div class="my-2 ml-4">
          <el-radio-group v-model="editForm.status">
            <el-radio value="0">空闲</el-radio>
            <el-radio value="1">占用</el-radio>
          </el-radio-group>
        </div>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogEditVisible = false">取消</el-button>
          <el-button type="primary" @click="editExam(editForm)">
            修改
          </el-button>
        </div>
      </template>
    </el-dialog>

  </div>


  <div class="content">
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="150"/>
      <el-table-column prop="roomNumber" label="考场编号" width="120"/>
      <el-table-column prop="location" label="考场位置" width="120"/>
      <el-table-column prop="capacity" label="考场容量" width="120"/>
      <el-table-column label="考场状态" width="600">
        <template #default="scope">
          {{ scope.row.status === '0' ? '空闲' : '占用' }}
        </template>
      </el-table-column>
      <el-table-column label="Operations" min-width="120">
        <template v-slot="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
          <el-button link type="danger" size="small" @click="remove(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from "vue";
import myAxios from "../config/my-axios";
import {ElNotification} from "element-plus";

const dialogEditVisible = ref(false);
const tableData = ref([]);
let searchMessage = ref('');
const initials = ['ID', '考场编号', '考场位置', '考场容量', '考场状态'];
const value = ref();

const options = Array.from({length: 5}).map((_, idx) => ({
  value: `${idx}`,
  label: `${initials[idx]}`,
}));
const st = ['id', 'roomNumber', 'location', 'capacity', 'status'];

const dialogCreateVisible = ref(false);

const createForm = reactive({
  roomNumber: "",
  location: "",
  capacity: "",
  status: "0",
})

const createExam = async () => {
  let res = await myAxios.post("/exam/create", createForm);

  if (res.code !== 0) {
    ElNotification({
      title: '创建失败',
      message: '请求错误',
      type: 'warning',
    });
    return;
  }
  dialogCreateVisible.value = false;
  createForm.value = {
    roomNumber: "",
    location: "",
    capacity: "",
    status: "0",
  };
  await search();
}

const search = async () => {
  let res = await myAxios.get("/exam/get", {
    params: {
      type: st[value.value],
      content: searchMessage.value,
    },
  });

  if (res.code !== 0) {
    ElNotification({
      title: 'Warning',
      message: '请求错误',
      type: 'warning',
    });
    return;
  }
  tableData.value = res.data.map(row => {  // 写个不是多此一举，它那个edit显示必须要是string类型，但是后端给的是number类型，必须得转一下，不然点edit编辑不了
    row.status = String(row.status);
    return row;
  });
}

const remove = async (id) => {
  let res = await myAxios.post("/exam/remove", {id});
  if (res.code !== 0) {
    ElNotification({
      title: 'Warning',
      message: '删除失败',
      type: 'warning',
    });
    return;
  }
  tableData.value = tableData.value.filter(row => row.id !== id);
}

const editExam = async (row) => {
  let res = await myAxios.post("/exam/update", {
    id: row.id,
    roomNumber: row.roomNumber,
    location: row.location,
    capacity: row.capacity,
    status: row.status,
  });

  if (res.code !== 0) {
    ElNotification({
      title: 'Warning',
      message: '修改错误',
      type: 'warning',
    });
    return;
  }
  // 因为是返回成功的，所以改了没啥事，没必要从后端再返回一遍，浪费服务器内存，这样做是不负责的，尽量把压力都给客户端,hhh
  tableData.value = tableData.value.map(r => { // 把对应信息改了，我不知道哈希表咋找，就只能遍历了
    if (r.id !== row.id) {
      return r;
    } else {
      r.roomNumber = row.roomNumber;
      r.location = row.location;
      r.status = row.status;
      r.capacity = row.capacity;
      return r;
    }
  });
  dialogEditVisible.value = false;
}

const editIndex = ref(-1);

const editForm = ref({
  roomNumber: '',
  location: '',
  capacity: '',
  status: '',
})

const handleEdit = (index, row) => {
  editIndex.value = index;
  editForm.value = Object.assign({}, row);
  dialogEditVisible.value = true;
}


onMounted(async () => {
  await search();
})

</script>

<style scoped>

.content {
  text-align: center;
}

</style>