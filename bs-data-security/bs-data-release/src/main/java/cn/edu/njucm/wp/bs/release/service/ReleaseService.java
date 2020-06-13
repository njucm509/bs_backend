package cn.edu.njucm.wp.bs.release.service;

import cn.edu.njucm.wp.bs.release.vo.HandleForm;
import cn.edu.njucm.wp.bs.release.vo.ReleaseBean;

import java.util.HashMap;

public interface ReleaseService {
    HashMap<String, Object> kAnonymous(HandleForm form);

    String kAnonymousBySpark(HandleForm form);

    Integer handleRelease(ReleaseBean bean);
}
