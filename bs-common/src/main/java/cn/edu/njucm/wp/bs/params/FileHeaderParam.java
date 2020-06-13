package cn.edu.njucm.wp.bs.params;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FileHeaderParam implements Serializable {
    private Integer defaultEnc;
    private String encryption;
    private String content;
}
