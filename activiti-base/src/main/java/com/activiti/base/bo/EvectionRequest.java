package com.activiti.base.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenzhi.xu
 * @date 2021/10/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvectionRequest {
    private String bizSeq;
    private String applicant;
    private String manager;
    private String generalManager;
    private String finance;
}
