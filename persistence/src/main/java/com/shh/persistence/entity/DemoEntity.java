package com.shh.persistence.entity;

import com.shh.common.utils.StringUtil;
import com.shh.persistence.annotation.Id;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "test")
public class DemoEntity {

    public DemoEntity() {
        this.id = StringUtil.getUUID();
    }

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "str")
    private String str;
}
