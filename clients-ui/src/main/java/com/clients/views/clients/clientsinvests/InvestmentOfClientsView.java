package com.clients.views.clients.clientsinvests;

import com.clients.form.InvestmentsOfClientsForm;
import com.clients.service.ClientsInvestmentService;
import com.clients.views.MainLayout;
import com.model.clientinvest.SearchInvestmentDto;
import com.model.investment.InvestmentsOfClientsDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.*;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Dependency;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * TODO ICONCINA MENU
 */
@PageTitle("Investments")
@Route(value = "investments", layout = MainLayout.class)
public class InvestmentOfClientsView extends VerticalLayout {

    private ClientsInvestmentService clientsInvestmentService;
    private Grid<InvestmentsOfClientsDto> grid = new Grid<>(InvestmentsOfClientsDto.class);

    public ComboBox<String> firstName = new ComboBox<>("First name");
    public ComboBox<String> lastName = new ComboBox<>("Last name");
    public ComboBox<String> status = new ComboBox<>("Status");
    public ComboBox<String> investmentName = new ComboBox<>("InvestmentName");

    public InvestmentOfClientsView(ClientsInvestmentService clientsInvestmentService) {
        this.clientsInvestmentService = clientsInvestmentService;
        updateList();
        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());

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

        grid.addComponentColumn(investmentsOfClientsDto -> {
            Button button = new Button("Activate");
            button.setAutofocus(true);
            button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            button.addClickListener(event -> {
                clientsInvestmentService.activateExpiredInvestment(investmentsOfClientsDto);
                updateList();
                String text = "Investment " + investmentsOfClientsDto.getInvestmentName() + " made by " + investmentsOfClientsDto.getFirstName() + "  " + investmentsOfClientsDto.getLastName() + " updated successfully!";
                Notification notification = new Notification(text);
                notification.setDuration(2000);
                notification.setPosition(Position.BOTTOM_STRETCH);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.open();
            });

            return button;
        }).setHeader("Action");
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

    private ComponentRenderer<InvestmentsOfClientsForm, InvestmentsOfClientsDto> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(investimentoDto -> new InvestmentsOfClientsForm(investimentoDto, clientsInvestmentService, grid));
    }

    private Component getToolbar() {
        firstName.setWidth("25em");
        lastName.setWidth("25em");
        status.setWidth("10em");

        firstName.setClearButtonVisible(true);
        lastName.setClearButtonVisible(true);
        status.setClearButtonVisible(true);
        investmentName.setClearButtonVisible(true);

        mappingSearchParameter();
        HorizontalLayout toolbar = new HorizontalLayout(firstName, lastName, status, investmentName);
        return toolbar;
    }

    private void mappingSearchParameter() {
        ListDataProvider<InvestmentsOfClientsDto> dataProvider = (ListDataProvider<InvestmentsOfClientsDto>) grid.getDataProvider();
        List<InvestmentsOfClientsDto> investments = dataProvider.fetch(new Query<>()).collect(Collectors.toList());

        Set<String> firstNames = investments.stream()
                .map(InvestmentsOfClientsDto::getFirstName)
                .collect(Collectors.toSet());


        Set<String> lastName = investments.stream()
                .map(InvestmentsOfClientsDto::getLastName)
                .collect(Collectors.toSet());


        Set<String> investmentName = investments.stream()
                .map(InvestmentsOfClientsDto::getInvestmentName)
                .collect(Collectors.toSet());


        this.firstName.setItems(firstNames);
        this.lastName.setItems(lastName);
        this.investmentName.setItems(investmentName);
        this.status.setItems("Payd", "Expired");
        setSearchingEvent();
    }

    private void setSearchingEvent() {
        this.firstName.addValueChangeListener(event -> searchInvestments());
        this.lastName.addValueChangeListener(event -> searchInvestments());
        this.investmentName.addValueChangeListener(event -> searchInvestments());
        this.status.addValueChangeListener(event -> searchInvestments());
    }

    private void searchInvestments() {
        List<InvestmentsOfClientsDto> investmentsSearch = clientsInvestmentService.searchInvestments(
                new SearchInvestmentDto(firstName.getValue(), lastName.getValue(), status.getValue(), investmentName.getValue()));

        grid.setItems(investmentsSearch);
    }
}
