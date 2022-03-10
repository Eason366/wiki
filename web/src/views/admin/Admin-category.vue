<template>


  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '230px' }"
    >
      <a-form
          layout="inline"
          :model="searchName"
      >
        <a-form-item>
          <a-button type="primary" @click="add()" >
            New
          </a-button>
        </a-form-item>
      </a-form>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :pagination="false"
          :loading="loading"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              Edit
            </a-button>
            <a-popconfirm
                title="Are you sure delete this Category?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="del(record.id)"
            >
              <a-button type="primary" danger>
                Delete
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="Title"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleOk"
  >
    <template #footer>
      <a-button key="back" @click="modalCancel">
        Return
      </a-button>
      <a-button key="submit" type="primary" :loading="modalLoading" @click="handleOk">
        Submit
      </a-button>
    </template>
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="Name">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="Parent Category">
        <a-input v-model:value="category.parent" />
      </a-form-item>
      <a-form-item label="Sort">
        <a-input v-model:value="category.sort" />
      </a-form-item>
    </a-form>
  </a-modal>

</template>


<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { Tool } from '@/util/Tool';

export default defineComponent({
  name: 'AdminCategory',
  setup() {
    const searchName = ref();
    searchName.value = {};
    const categorys = ref();
    const loading = ref(false);
    const level1 = ref();
    const columns = [

      {
        title: 'name',
        dataIndex: 'name'
      },
      {
        title: 'parent',
        dataIndex: 'parent'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * data Query
     **/
    const handleQuery = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        categorys.value = data.content;
        level1.value = [];
        level1.value = Tool.array2Tree(categorys.value,0)
      });
    };


    // -------- model ---------
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const modalCancel = () => {
      modalVisible.value = false;
    };
    const handleOk = () => {
      modalLoading.value = true;
      axios.post("/category/save", category.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.success){
          modalVisible.value = false;
          // reload data
          handleQuery();
        }else {
          message.error(data.message);
        }
        modalLoading.value = false;
      });
    };

    /**
     * edit
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record)
    };

    /**
     * add
     */
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    };

    /**
     * delete
     */
    const del = (id : number) => {
      axios.delete("/category/delete/"+id).then((response) => {
        const data = response.data;
        if (data.success){
          // reload data
          handleQuery();
          message.success('Delete Category Success!');
        }
      });
    };


    onMounted(() => {
      handleQuery();
    });

    return {
      //categorys,
      level1,
      columns,
      loading,
      searchName,

      edit,
      add,
      del,
      handleQuery,


      category,
      modalVisible,
      modalLoading,
      modalCancel,
      handleOk
    }
  }
});
</script>
