package com.toyfactory.pcb.ui.form;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.GamePatchHistory;
import com.toyfactory.pcb.domain.History;
import com.toyfactory.pcb.model.HistoryItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.LocalDateRenderer;
import com.vaadin.ui.renderers.NumberRenderer;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hikiro on 2017-08-15.
 */
public class HistoryForm extends VerticalLayout {

    Grid<HistoryItem> historyGrid;
    List<Game> games;

    List<HistoryItem> historyItemList;
    Map<String, HistoryItem> historyItemMap;

    FooterRow dayDelta;
    FooterRow weekDelta;

    public HistoryForm(List<Game> games) {
        this.games = games;

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        historyGrid = new Grid<>();

        addComponent(historyGrid);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)", Locale.KOREAN);

        historyGrid.setWidth(100, Unit.PERCENTAGE);
        historyGrid.addColumn(HistoryItem::getDate, new LocalDateRenderer(formatter)).setCaption("일자").setId("date");
        historyGrid.addColumn(HistoryItem::getPcbCnt, new NumberRenderer(new DecimalFormat("#,###"))).setCaption("등록PC방").setId("pcb_cnt");

        for(Game game : games) {
            historyGrid.addColumn(historyItem -> {
                return historyItem.getInstallPcbCntByGsn(game.getGsn());
            }, new NumberRenderer(new DecimalFormat("#,###"))).setCaption(game.getName()).setId("game_" + game.getGsn());

            historyGrid.addColumn(historyItem -> {
                return historyItem.getInstallIpCntByGsn(game.getGsn());
            }, new NumberRenderer(new DecimalFormat("#,###"))).setCaption("설치 IP수").setId("game_ip_" + game.getGsn());

        }

        HeaderRow headerRow = historyGrid.getDefaultHeaderRow();
        headerRow.getCell("date").setHtml("<b>일자<b>");
        headerRow.getCell("pcb_cnt").setHtml("<b>등록PC방<b>");

        for(Game game : games) {
            headerRow.getCell("game_" + game.getGsn()).setHtml("<b>" + game.getName() +"<b>");
            headerRow.getCell("game_ip_" + game.getGsn()).setHtml("<b>설치 IP수<b>");
        }
    }

    public void reloadGrid(List<History> historyList, List<GamePatchHistory> gamePatchHistoryList) {

        if (historyItemMap == null) {
            historyItemMap = new HashMap<>();
        }

        historyItemList = historyList.stream().map(history ->
                new HistoryItem(history.getDateKey(), history.getPcbCnt(), history.getPaidPcbCnt())).collect(Collectors.toList());

        //build historyItemMap
        for(HistoryItem historyItem : historyItemList) {
            historyItemMap.put(historyItem.getDateKey(), historyItem);
        }

        for(GamePatchHistory gamePatchHistory : gamePatchHistoryList) {
            HistoryItem historyItem = historyItemMap.get(gamePatchHistory.getDateKey());

            //null 인 경우가 없지만 오류 방어용
            if (historyItem == null) continue;

            historyItem.setInstallPcbCntByGsn(gamePatchHistory.getGsn(), gamePatchHistory.getInstall());
            historyItem.setInstallIpCntByGsn(gamePatchHistory.getGsn(), gamePatchHistory.getInstallIpCnt());
        }

        historyGrid.setItems(historyItemList);

        if (historyItemList.size() > 0) {
            historyGrid.setHeightByRows(historyItemList.size());
        }

        clearDeltaFooter();

        if (historyItemList.size() > 1) {
            buildDeltaFooter(dayDelta, historyItemList.get(0), historyItemList.get(1));

            if (historyItemList.size() > 7)
            buildDeltaFooter(weekDelta, historyItemList.get(0), historyItemList.get(7));
        }
    }

    private void buildDeltaFooter(FooterRow footerRow, HistoryItem item1, HistoryItem item2) {

        Long pcbCntDayDelta = item1.getPcbCnt() - item2.getPcbCnt();
        footerRow.getCell("pcb_cnt").setHtml(colorNumberHtml(pcbCntDayDelta));

        for(Game game : games) {
            //게임별 설치 PC방 수
            Long installDayDelta = item1.getInstallPcbCntByGsn(game.getGsn()) - item2.getInstallPcbCntByGsn(game.getGsn());
            footerRow.getCell("game_" + game.getGsn()).setHtml(colorNumberHtml(installDayDelta));

            //게임별 설치 IP수
            Long installIpCntDayDelta = item1.getInstallIpCntByGsn(game.getGsn()) - item2.getInstallIpCntByGsn(game.getGsn());
            footerRow.getCell("game_ip_" + game.getGsn()).setHtml(colorNumberHtml(installIpCntDayDelta));
        }
    }

    private void clearDeltaFooter() {
        if (dayDelta != null) {
            historyGrid.removeFooterRow(dayDelta);
        }

        if (weekDelta != null) {
            historyGrid.removeFooterRow(weekDelta);
        }

        dayDelta = historyGrid.prependFooterRow();
        dayDelta.getCell("date").setText("증감(일일)");
        weekDelta = historyGrid.appendFooterRow();
        weekDelta.getCell("date").setText("증감(주간)");
    }

    private String colorNumberHtml(Long num) {
        //if num > 0 then red color
        //<font color="red">num</font>
        //else then blue color
        //<font color="blue">num</font>

        StringBuilder sb = new StringBuilder();
        if (num > 0L) {
            sb.append("<font color=\"red\">");
        } else {
            sb.append("<font color=\"blue\">");
        }

        DecimalFormat fmt = new DecimalFormat("#,###");
        sb.append(fmt.format(num));
        sb.append("</font>");

        return sb.toString();
    }
}
