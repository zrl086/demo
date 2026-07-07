// 登录 API
import { api } from "./index.js";

/**
 * 调用后端登录接口
 * 响应格式：{ code: 200, data: { token, username, nickname, role } }
 */
export async function loginApi(username, password) {
  const res = await api.post("/api/login", { username, password });
  return res.data;
}
