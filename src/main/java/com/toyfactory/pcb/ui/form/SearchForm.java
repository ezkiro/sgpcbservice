package com.toyfactory.pcb.ui.form;

import com.vaadin.ui.*;

public class SearchForm extends HorizontalLayout {

    DateTimeField startDateField;
    DateTimeField endDateField;

    private Button searchBtn;

    public SearchForm() {
        RadioButtonGroup<String> options = new RadioButtonGroup<>();
        options.setItems("월별", "기간별");

        startDateField = new DateTimeField("시작일");
        endDateField = new DateTimeField("종료일");
        searchBtn = new Button("조회");

        addComponent(options);
        addComponent(startDateField);
        addComponent(endDateField);
        addComponent(searchBtn);
    }
}
