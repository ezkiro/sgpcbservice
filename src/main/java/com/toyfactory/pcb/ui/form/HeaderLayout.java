package com.toyfactory.pcb.ui.form;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by hikiro on 2017-08-15.
 */
public class HeaderLayout extends HorizontalLayout {

    public HeaderLayout(String title, String backTitle, String backUrl) {

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Label titleLabel = new Label(title);
        titleLabel.addStyleName(ValoTheme.LABEL_H2);
        titleLabel.addStyleName(ValoTheme.LABEL_COLORED);

        Button backBtn = new Button(backTitle);
        backBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);

        backBtn.addClickListener(event -> {
            Page.getCurrent().setLocation(backUrl);
        });

        addComponent(titleLabel);
        addComponent(backBtn);

        setExpandRatio(titleLabel, 0.8f);
        setExpandRatio(backBtn, 0.2f);
    }
}
