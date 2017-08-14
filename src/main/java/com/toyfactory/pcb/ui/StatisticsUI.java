package com.toyfactory.pcb.ui;

import com.toyfactory.pcb.aop.PcbAuthorization;
import com.toyfactory.pcb.ui.form.SearchForm;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "/statistics")
@Theme("valo")
public class StatisticsUI extends UI {

    @PcbAuthorization(permission="ADMIN")
    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout screenLayout = new VerticalLayout();

        screenLayout.setSpacing(true);
        screenLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Label titleLabel = new Label("스마일게이트 게임 별 설치 및 패치 현황");
        titleLabel.addStyleName(ValoTheme.LABEL_H2);
        titleLabel.addStyleName(ValoTheme.LABEL_COLORED);

        screenLayout.addComponent(titleLabel);

        Label searchLabel = new Label("기간 별 통계");
        titleLabel.addStyleName(ValoTheme.LABEL_H3);

        screenLayout.addComponent(searchLabel);

        screenLayout.addComponent(new SearchForm());

        setContent(screenLayout);
    }
}
