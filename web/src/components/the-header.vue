<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="home">
        <router-link to="/">Home</router-link>
      </a-menu-item>
      <a-menu-item key="AdminEbook">
        <router-link to="/admin/ebook">Ebook Admin</router-link>
      </a-menu-item>
      <a-menu-item key="AdminCategory">
        <router-link to="/admin/category">Category Admin</router-link>
      </a-menu-item>
      <a-menu-item key="about">
        <router-link to="/about">About</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'the-header',
});
</script>