package cn.edu.njucm.wp.bs.encrypt.client;

import cn.edu.njucm.wp.bs.data.api.DataApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "data-service")
public interface DataClient extends DataApi {

}
