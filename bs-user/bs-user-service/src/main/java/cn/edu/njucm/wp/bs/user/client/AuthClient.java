package cn.edu.njucm.wp.bs.user.client;

import cn.edu.njucm.wp.bs.auth.api.AuthApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "auth-service")
public interface AuthClient extends AuthApi {
}
