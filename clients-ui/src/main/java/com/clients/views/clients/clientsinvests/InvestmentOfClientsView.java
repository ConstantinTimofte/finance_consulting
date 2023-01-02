package com.clients.views.clients.clientsinvests;


import com.clients.form.ClientInvestmentForm;
import com.clients.form.InvestmentsOfClientsForm;
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
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


/**
 * TODO ICONCINA MENU
 */
@PageTitle("Investments")
@Route(value = "investments", layout = MainLayout.class)
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
        grid.setSizeFull();//lunghezza grid
        grid.setSizeFull();

        //  grid.addColumn(Person::getFullName).setHeader("Name");

        /*  grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, person) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.removeInvitation(person));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
        * */
        grid.setColumns("firstName", "lastName", "investmentName", "expiringDate", "remainingDays");

        grid.addComponentColumn(investmentsOfClientsDto -> createStatusIcon(investmentsOfClientsDto)).setHeader("Status of Payment");

        grid.setItemDetailsRenderer(createPersonDetailsRenderer());
    }

    private Component getContent() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(grid);
        horizontalLayout.setFlexGrow(1, grid);
        horizontalLayout.setSizeFull();
        return horizontalLayout;

    }

    private Icon createStatusIcon(InvestmentsOfClientsDto investmentsOfClientsDto) {
        Icon icon;
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

    /*private static ComponentRenderer<InvestmentsOfClientsForm, InvestmentsOfClientsDto> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(InvestmentsOfClientsForm::new, InvestmentsOfClientsForm::setDto);
    }*/
    private static ComponentRenderer<InvestmentsOfClientsForm, InvestmentsOfClientsDto> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(InvestmentsOfClientsForm::new, InvestmentsOfClientsForm::setDto);
    }
}
