<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo collapse-logo">
      </router-link>

      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo">
      </router-link>
    </transition>
  </div>
</template>

<script>
// 🚀 确保路径对
import logoImg from '@/assets/logo/logo.png'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      title: '信息管理系统',
      logo: logoImg
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  /* 🚀 修改1：高度加高到 80px，给图片空间 */
  height: 80px;
  line-height: 80px;
  /* 🚀 修改2：强制白色背景，哪怕侧边栏是黑的，这个头部也是白的，显得更正式 */
  background: #ffffff !important;
  text-align: center;
  overflow: hidden;
  /* 加个阴影，更有层次感 */
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  z-index: 10;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

    & .sidebar-logo {
      /* 🚀 图片样式：宽度撑满，高度自动，保持比例 */
      width: 90%;
      height: auto;
      max-height: 70px; /* 限制最大高度 */
      object-fit: contain;
    }

    /* 折叠时的样式 */
    & .collapse-logo {
      width: 40px;
      height: 40px;
      object-fit: cover;
    }
  }
}
</style>
