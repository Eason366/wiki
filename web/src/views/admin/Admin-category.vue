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
          <a-input v-model:value="searchName.name" placeholder="searchName">
            <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery({
          page : 1,
          size : pagination.pageSize,
          name : searchName})" >
            Search
          </a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add()" >
            New Blog
          </a-button>
        </a-form-item>
      </a-form>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="categorys"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              Edit
            </a-button>
            <a-popconfirm
                title="Are you sure delete this Blog?"
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
    const pagination = ref({
      current: 1,
      pageSize: 8,
      total: 0
    });
    const loading = ref(false);

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
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/category/list", {
        params : {
          page : params.page,
          size : params.size,
          name : searchName.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        categorys.value = data.content.list;

        // Reset pagination buttons
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
      });
    };

    /**
     * Triggered when the page number is clicked
     */
    const handleTableChange = (pagination: any) => {
      //console.log("Clicked Success");
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
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
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
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
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
          message.success('Delete Blog Success!');
        }
      });
    };


    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    });

    return {
      categorys,
      pagination,
      columns,
      loading,
      searchName,
      handleTableChange,

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
