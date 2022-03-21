<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
      >
        <a-menu-item key="1">
          <template #icon>
            <MailOutlined />
          </template>
          Welcome
        </a-menu-item>
        <a-sub-menu v-for= "item in level1" :key= item.id>
          <template #icon>
            <AppstoreOutlined />
          </template>
          <template #title>{{ item.name }}</template>
          <a-menu-item v-for= "children in item.children" :key= children.id>
            {{children.name}}
          </a-menu-item>
        </a-sub-menu>


      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '730px' }"
    >
      <a-list item-layout="vertical" size="large"  :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component v-bind:is="type" style="margin-right: 8px" />
            {{ text }}
          </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {Tool} from "@/util/Tool";

export default defineComponent({
  name: 'Home',
  setup(){
    const ebooks = ref();




    const level1 = ref();
    let categorys : any;
    /**
     * category Query
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        categorys = data.content;
        level1.value = [];
        level1.value = Tool.array2Tree(categorys,0)
        console.log("level1: ",level1)
      });
    };

    const MenuClick = () =>{
      console.log("MenuClick")
    }

    onMounted(()=>{
      handleQueryCategory()
      axios.get("/ebook/list",{
        params : {
          page : 1,
          size : 10000
        }
      }).then((response)=>{
        const data = response.data;
        ebooks.value = data.content.list;
      });
    });
    return{
      level1,
      MenuClick,

      ebooks,
      actions:[
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ],
    }
  }
});
</script>
