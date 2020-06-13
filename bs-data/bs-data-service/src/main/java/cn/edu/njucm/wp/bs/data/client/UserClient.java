package cn.edu.njucm.wp.bs.data.client;

import cn.edu.njucm.wp.bs.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}
