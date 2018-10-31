package com.toyfactory.pcb.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hikiro on 2017-08-15.
 * 통계 화면에서 사용하는 Item 정의
 * 일자/등록PC방수/게임별 설치PC방수/정산PC방 수
 */
@Data
public class HistoryItem {

    String dateKey; //for data
    LocalDate date; //for view
    Long pcbCnt;
    Long paidPcbCnt;
    Long allPatchCnt;
    Map<String, Long> installPcbMap;

    public Long getInstallPcbCntByGsn(String gsn) {
        return installPcbMap.get(gsn);
    }

    public void setInstallPcbCntByGsn(String gsn, Long installCnt) {
        installPcbMap.put(gsn, installCnt);
    }

    public HistoryItem(String dateKey, Long pcbCnt, Long paidPcbCnt, Long allPatchCnt) {
        this.dateKey = dateKey;
        this.date = LocalDate.parse(dateKey, DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.pcbCnt = pcbCnt;
        this.paidPcbCnt = paidPcbCnt;
        this.allPatchCnt = allPatchCnt;

        this.installPcbMap = new HashMap<>();
    }
}
