package com.toyfactory.pcb.ui;

import com.toyfactory.pcb.model.Permission;
import com.toyfactory.pcb.service.GameService;
import com.toyfactory.pcb.service.HistoryService;
import com.toyfactory.pcb.service.MemberService;
import com.toyfactory.pcb.ui.form.HeaderLayout;
import com.toyfactory.pcb.ui.form.HistoryForm;
import com.toyfactory.pcb.ui.form.SearchForm;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringUI(path = "/statistics")
@Theme("valo")
public class StatisticsUI extends UI {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsUI.class);

    @Value("${pcbservice.statistics.title}")
    private String title;

    @Autowired
    private MemberService memberService;

    @Autowired
    private GameService gameService;

    @Autowired
    private HistoryService historyService;

    private SearchForm searchForm;

    private HistoryForm historyForm;

    @Override
    protected void init(VaadinRequest request) {

        if (!verifyAuthrization((HttpServletRequest)request, Permission.PARTNER)) {
            Page.getCurrent().setLocation("/login");
            return;
        }

        final VerticalLayout screenLayout = new VerticalLayout();

        screenLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        screenLayout.addComponent(new HeaderLayout(title + " 게임 별 설치 및 패치 현황", "BACK", "/admin/gamepatch"));

        Label searchLabel = new Label("기간 별 통계");
        searchLabel.addStyleName(ValoTheme.LABEL_H3);

        screenLayout.addComponent(searchLabel);

        searchForm = new SearchForm();

        searchForm.getSearchBtn().addListener(clickEvent -> {

            reloadData(searchForm.getStartDay(), searchForm.getEndDay());
        });

        screenLayout.addComponent(searchForm);

        historyForm = new HistoryForm(gameService.findEnableGames());

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

    private boolean verifyAuthrization(HttpServletRequest request, Permission allowedPerm) {
        //쿠키에서 찾아본다.
        Cookie cookie = WebUtils.getCookie(request, "access_token");
        if(cookie == null) {
            if(logger.isDebugEnabled()) logger.debug("cookie isn't exist...");
            return false;
        }
        String accessToken;

        try {
            accessToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("fail to decode accessToken from cookie!");
            return false;
        }

        Permission userPerm = memberService.verifyAccessToken(accessToken);
        if(userPerm == Permission.NOBODY){ //check
            logger.error("uer permission is NOBODY!");
            return false;
        }

        //check permission
        if(userPerm.getLevel() < allowedPerm.getLevel()){
            logger.error("uer permission is not allowed! permission:" + userPerm.name());
            return false;
        }

        return true;
    }
}
