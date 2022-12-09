package com.clients.views;

import com.clients.navbar.AppNav;
import com.clients.navbar.AppNavItem;
import com.clients.views.clients.ClientsView;
import com.clients.views.clients.clientsinvests.InvestmentOfClientsView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addHeaderContent();
        addDrawerContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }
    private void addDrawerContent() {
        H1 appName = new H1("Menu");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(new VerticalLayout(header, scroller, createFooter()));

    }

    private AppNav createNavigation() {
        AppNav nav = new AppNav();
        nav.addItem(new AppNavItem("Clients", ClientsView.class, "la la-user"));
        nav.addItem(new AppNavItem("Investments", InvestmentOfClientsView.class, "vaadin:open-book"));
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

}
