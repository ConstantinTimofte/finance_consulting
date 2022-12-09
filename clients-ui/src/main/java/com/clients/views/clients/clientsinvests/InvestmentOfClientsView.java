package com.clients.views.clients.clientsinvests;


import com.clients.service.ClientsInvestmentService;
import com.clients.views.MainLayout;
import com.model.investment.InvestmentsOfClientsDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


/** TODO ICONCINA MENU
 *
 *
 *
 * */
@PageTitle("Investments")
@Route(value = "/clientsinvest", layout = MainLayout.class)
public class InvestmentOfClientsView extends VerticalLayout {

    private ClientsInvestmentService clientsInvestmentService;
    private Grid<InvestmentsOfClientsDto> grid = new Grid<>(InvestmentsOfClientsDto.class);

    public InvestmentOfClientsView(ClientsInvestmentService clientsInvestmentService) {
        this.clientsInvestmentService = clientsInvestmentService;
        setSizeFull();
        configureGrid();
        add(getContent());
        updateList();
    }

    private void updateList() {
        grid.setItems(clientsInvestmentService.getAll());
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();//luncghezza grid
        grid.setSizeFull();

        grid.setColumns("firstName", "lastName", "investmentName", "expiringDate", "remainingDays");

        grid.addComponentColumn(investmentsOfClientsDto -> createStatusIcon(investmentsOfClientsDto)).setHeader("Status of Payment");;
    }

    private Component getContent() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(grid);
        horizontalLayout.setFlexGrow(1, grid);
        horizontalLayout.setSizeFull();
        return horizontalLayout;

    }

    private Icon createStatusIcon(InvestmentsOfClientsDto investmentsOfClientsDto) {
        Icon icon = new Icon();
        if (investmentsOfClientsDto.getStatusOfPayment()) {
            icon = VaadinIcon.CHECK.create();
            icon.getElement().getThemeList().add("badge success");
        } else {
            icon = VaadinIcon.CLOSE_SMALL.create();
            icon.getElement().getThemeList().add("badge error");
        }
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }
}
