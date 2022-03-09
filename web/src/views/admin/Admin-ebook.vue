<template>


  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '730px' }"
    >

      <a-button type="primary" @click="add()" size="large">
        New Blog
      </a-button>

      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
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
              <a-button danger>
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
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="cover">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="Name">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="Category1Id">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="Category2Id">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>
      <a-form-item label="Describe">
        <a-input v-model:value="ebook.description" />
      </a-form-item>
    </a-form>
  </a-modal>

</template>


<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 3,
      total: 0
    });
    const loading = ref(false);

    const columns = [

      {
        title: 'name',
        dataIndex: 'name'
      },
      {
        title: 'category1Id',
        key: 'category1Id',
        dataIndex: 'category1Id'
      },
      {
        title: 'category2Id',
        dataIndex: 'category2Id'
      },
      {
        title: 'docCount',
        dataIndex: 'docCount'
      },
      {
        title: 'viewCount',
        dataIndex: 'viewCount'
      },
      {
        title: 'voteCount',
        dataIndex: 'voteCount'
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
      axios.get("/ebook/list", {
        params : {
          page : params.page,
          size : params.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;

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
    const ebook = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const modalCancel = () => {
      modalVisible.value = false;
    };
    const handleOk = () => {
      modalLoading.value = true;
      axios.post("/ebook/save", ebook.value).then((response) => {
        const data = response.data;
        if (data.success){
          modalVisible.value = false;
          modalLoading.value = false;
        }
        // reload data
        handleQuery({
          page: pagination.value.current,
          size: pagination.value.pageSize
        });
      });
    };

    /**
     * edit
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = record
    };

    /**
     * add
     */
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    };

    /**
     * delete
     */
    const del = (id : number) => {
      axios.delete("/ebook/delete/"+id).then((response) => {
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
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,

      edit,
      add,
      del,

      ebook,
      modalVisible,
      modalLoading,
      modalCancel,
      handleOk
    }
  }
});
</script>
