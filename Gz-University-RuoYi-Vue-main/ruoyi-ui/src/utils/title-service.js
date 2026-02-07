import { getConfigKey } from "@/api/system/config";

class TitleService {
  // 缓存时间（毫秒）- 可以根据需要调整，比如5分钟缓存
  static CACHE_DURATION = 5 * 60 * 1000; // 5分钟
  static cache = {
    title: null,
    timestamp: 0
  };

  /**
   * 获取页面标题（优先从服务器获取）
   * @param {boolean} forceRefresh - 是否强制刷新（忽略缓存）
   * @returns {Promise<string>} 页面标题
   */
  static async getPageTitle(forceRefresh = false) {
    // 如果强制刷新或缓存过期，从服务器获取
    const now = Date.now();
    if (forceRefresh || !this.cache.title || (now - this.cache.timestamp > this.CACHE_DURATION)) {
      try {
        const response = await getConfigKey('sys.login.title');
        if (response && response.code === 200 && response.msg) {
          const title = response.msg.trim();
          if (title) {
            this.cache.title = title;
            this.cache.timestamp = now;
            return title;
          }
        }
      } catch (error) {
        console.error('获取页面标题失败:', error);
        // 如果获取失败，使用缓存的标题或默认值
      }
    }

    // 返回缓存的标题或默认值
    return this.cache.title || process.env.VUE_APP_TITLE || '信息管理系统';
  }

  /**
   * 设置页面标题到document
   * @param {boolean} forceRefresh - 是否强制从服务器刷新
   */
  static async setDocumentTitle(forceRefresh = false) {
    const title = await this.getPageTitle(forceRefresh);
    document.title = title;
    return title;
  }

  /**
   * 清除标题缓存（当管理员修改配置后调用）
   */
  static clearCache() {
    this.cache.title = null;
    this.cache.timestamp = 0;
  }

  /**
   * 获取默认标题（不请求服务器）
   */
  static getDefaultTitle() {
    return process.env.VUE_APP_TITLE || '信息管理系统';
  }
}

export default TitleService;
