<template>
  <div id="app">
    <router-view />
    <theme-picker />
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker"
import TitleService from '@/utils/title-service';
export default {
  name: "App",
  created() {
    // 应用启动时设置页面标题（从服务器获取最新）
    this.setDocumentTitle();
  },
  methods: {
    async setDocumentTitle() {
      try {
        // 每次进入应用都获取最新标题
        await TitleService.setDocumentTitle();
      } catch (error) {
        console.error('设置页面标题失败:', error);
      }
    }
  },
  watch: {
    // 监听路由变化，确保每个页面都有正确的标题
    $route() {
      // 可以使用缓存的标题，不需要每次都请求服务器
      // 如果需要实时性，可以设置 forceRefresh: true
      this.setDocumentTitle();
    }
  },
  components: { ThemePicker }
}
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
