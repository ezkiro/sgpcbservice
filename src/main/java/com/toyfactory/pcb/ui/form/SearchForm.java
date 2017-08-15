package com.toyfactory.pcb.ui.form;

import com.vaadin.ui.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SearchForm extends HorizontalLayout {

    RadioButtonGroup<String> options;

    DateField startDateField;
    DateField endDateField;

    ComboBox<String> yearSelect;
    ComboBox<String> monthSelect;

    private Button searchBtn;

    public SearchForm() {
        options = new RadioButtonGroup<>();
        options.setItems("월별", "기간별");
        options.setSelectedItem("월별");

        options.addSelectionListener(event -> {
            if ("월별".equals(event.getValue())) {
                turnOnMonth();
            } else {
                turnOnPeriod();
            }
        });

        startDateField = new DateField();
        startDateField.setDateFormat("yyyy-MM-dd");
        endDateField = new DateField();
        endDateField.setDateFormat("yyyy-MM-dd");
        yearSelect = new ComboBox<>();
        monthSelect = new ComboBox<>();

        yearSelect.setItems("2017년", "2018년", "2019년", "2020년", "2021년", "2022년", "2023년", "2024년", "2025년", "2026년");
        monthSelect.setItems("01월", "02월", "03월", "04월", "05월", "06월", "07월", "08월", "09월", "10월", "11월", "12월");

        searchBtn = new Button("조회");

        addComponent(options);

        addComponent(yearSelect);
        addComponent(monthSelect);

        addComponent(startDateField);
        addComponent(endDateField);
        addComponent(searchBtn);

        turnOnMonth();
    }

    public void turnOnMonth() {
        yearSelect.setVisible(true);
        monthSelect.setVisible(true);
        startDateField.setVisible(false);
        endDateField.setVisible(false);

        LocalDate now = LocalDate.now();

        yearSelect.setValue(now.format(DateTimeFormatter.ofPattern("yyyy년")));
        monthSelect.setValue(now.format(DateTimeFormatter.ofPattern("MM월")));
    }

    public void turnOnPeriod() {
        yearSelect.setVisible(false);
        monthSelect.setVisible(false);
        startDateField.setVisible(true);
        endDateField.setVisible(true);

        startDateField.setValue(LocalDate.now());
        endDateField.setValue(LocalDate.now());
    }

    public Component getSearchBtn() {
        return searchBtn;
    }

    public LocalDate getStartDay() {
        if ("월별".equals(options.getValue())) {

            String year = yearSelect.getValue().substring(0,4);
            String month = monthSelect.getValue().substring(0,2);

            String startDay = year + month + "01";
            return LocalDate.parse(startDay, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else {
            return startDateField.getValue();
        }
    }

    public LocalDate getEndDay() {
        if ("월별".equals(options.getValue())) {
            //그 월의 마지막 날짜를 구하기 위해서 그 다음 월의 1일의 날짜에서 1일을 빼서 구한다.
            String prefix = yearSelect.getValue().substring(0,4) + monthSelect.getValue().substring(0,2);
            LocalDate startOfMonth = LocalDate.parse(prefix + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));

            return startOfMonth.plusMonths(1).minusDays(1);

        } else {
            return endDateField.getValue();
        }
    }
}
