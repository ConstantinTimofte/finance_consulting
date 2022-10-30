package com.clients.views.clients;

import com.clients.form.ClientForm;
import com.clients.service.ClientService;
import com.clients.views.MainLayout;
import com.model.client.ClientDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Clients")
@Route(value = "", layout = MainLayout.class)
public class ClientsView extends VerticalLayout {

    private Grid<ClientDto> grid = new Grid<>(ClientDto.class);
    private ClientForm clientForm;
    private TextField filterText = new TextField();
    private ClientService clientService;

    public ClientsView(ClientService clientService) {
        this.clientService = clientService;

        addClassName("list-view");/*classe css se presente*/
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    /* https://vaadin.com/docs/latest/components/grid*/
    private void configureGrid() {
        grid.addClassName("contact-grid");//classe css
        grid.setSizeFull();//luncghezza grid

        // puoi scegliere le colonne che vuoi usare , di default se non specifichi verranno utilizzate tutte
        // grid.setColumns("firstName", "lastName", "emailAdress", "status");
        grid.setColumns("firstName", "lastName", "emailAdress");

        grid.addComponentColumn(clientDto -> createStatusIcon(clientDto))
                .setHeader("Payment");

        grid.addColumn(createIsClientComponentRenderer()).setHeader("Status");
        grid.getColumns().forEach(columns -> columns.setAutoWidth(true)); // gestire oggetto per oggetto

        /*Selezionando una colonna (SELEZIONANDO LA STESSA L OGGETTO E NULL)*/
        grid.asSingleSelect().addValueChangeListener(event -> editClient(event.getValue()));
    }

    /*EVENTI PRINCIPALI DELLA FORM*/
    private void configureForm() {
        clientForm = new ClientForm();/*items per menu a tendina*/
        clientForm.setWidth("40em");
        clientForm.addListener(ClientForm.SaveEvent.class, this::saveClient);
        clientForm.addListener(ClientForm.CloseEvent.class, closeEvent -> closeEditor());
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Search by name....");
        filterText.setClearButtonVisible(true);

        /*l elemento viene recuperato quando si e smesso di inserire input
        ('char') utile per non eseguire chiamate multiple al database */
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());/*ad ogni cambiamento chiama il metodo*/
        filterText.setWidth("45em");

        Button clientButton = new Button("Add");
        clientButton.addClickListener(event -> saveClient());//Reindirizzamento sulla form

        HorizontalLayout toolbar = new HorizontalLayout(filterText, clientButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private Component getContent() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(grid, clientForm);
        horizontalLayout.setFlexGrow(1, grid);
        horizontalLayout.setFlexGrow(2, clientForm);
        horizontalLayout.setClassName("content");
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }

    private void updateList() {
        grid.setItems(clientService.findAllContacts(filterText.getValue()));
    }

    private void closeEditor() {
        clientForm.setClient(null);
        clientForm.setVisible(false);
        clientForm.removeClassName("editing");
    }

    private void saveClient(ClientForm.SaveEvent event) {
        clientService.saveClient(event.getClientDto());
        updateList();
        closeEditor();
    }

    private void saveClient() {
        /*ogni volta che crei un contatto non appariranno dati di un vecchio contatto  */
        grid.asSingleSelect().clear();
        editClient(new ClientDto());
    }

    private void editClient(ClientDto clientDto) {
        if (clientDto == null) {/*Se non selezioni un contatto*/
            closeEditor();
        } else {
            clientForm.setClient(clientDto);/*valorizzo la form con l oggetto attuale*/
            clientForm.setVisible(true);
            addClassName("editing");
        }
    }


    private static final SerializableBiConsumer<Span, ClientDto> isClientComponentUpdater = (
            span, clientDto) -> {
        boolean isClient = clientDto.getClient();
        String theme = String.format("badge %s", isClient ? "success" : "contrast");
        span.getElement().setAttribute("theme", theme);
        span.setText(theme.equals("badge success") ? "CLIENT" : "contact");
    };

    private static ComponentRenderer<Span, ClientDto> createIsClientComponentRenderer() {
        return new ComponentRenderer<>(Span::new, isClientComponentUpdater);
    }


    private Icon createStatusIcon(ClientDto clientDto) {
        Icon icon = new Icon();
        if (clientDto.getPayment() == null) {
            /*Non e cliente*/
            icon.setVisible(false);
        } else {
            if (clientDto.getClient() && clientDto.getPayment()) {
                /*E cliente ed ha pagato*/
                icon = VaadinIcon.CHECK.create();
                icon.getElement().getThemeList().add("badge success");
            } else if (clientDto.getClient() && (!clientDto.getPayment())) {
                /*E cliente e  non ha pagato*/
                icon = VaadinIcon.CLOSE_SMALL.create();
                icon.getElement().getThemeList().add("badge error");
            }
        }
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }


}