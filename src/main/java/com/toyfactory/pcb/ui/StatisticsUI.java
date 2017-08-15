package com.toyfactory.pcb.ui;

import com.toyfactory.pcb.aop.PcbAuthorization;
import com.toyfactory.pcb.service.GameService;
import com.toyfactory.pcb.service.HistoryService;
import com.toyfactory.pcb.ui.form.HeaderLayout;
import com.toyfactory.pcb.ui.form.HistoryForm;
import com.toyfactory.pcb.ui.form.SearchForm;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringUI(path = "/statistics")
@Theme("valo")
public class StatisticsUI extends UI {

    @Autowired
    private GameService gameService;

    @Autowired
    private HistoryService historyService;

    private SearchForm searchForm;

    private HistoryForm historyForm;

    @PcbAuthorization(permission="ADMIN")
    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout screenLayout = new VerticalLayout();

        screenLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        screenLayout.addComponent(new HeaderLayout("스마일게이트 게임 별 설치 및 패치 현황", "BACK", "/admin/gamepatch"));

        Label searchLabel = new Label("기간 별 통계");
        searchLabel.addStyleName(ValoTheme.LABEL_H3);

        screenLayout.addComponent(searchLabel);

        searchForm = new SearchForm();

        searchForm.getSearchBtn().addListener(clickEvent -> {

            reloadData(searchForm.getStartDay(), searchForm.getEndDay());
        });

        screenLayout.addComponent(searchForm);

        historyForm = new HistoryForm(gameService.findGames());

        screenLayout.addComponent(historyForm);

        screenLayout.setExpandRatio(historyForm, 0.8f);

        setContent(screenLayout);

        reloadData(LocalDate.now().minusDays(7), LocalDate.now());
    }

    public void reloadData(LocalDate startDay, LocalDate endDay) {

        String startKey = startDay.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String endKey = endDay.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        historyForm.reloadGrid(historyService.getHistorysBetween(startKey, endKey),
                historyService.getGamePatchHistorysBetween(startKey, endKey));
    }
}
