package com.clients.views.clients;

import com.clients.form.ClientForm;
import com.clients.form.ClientInvestmentForm;
import com.clients.service.ClientService;
import com.clients.views.MainLayout;
import com.model.client.ClientDto;
import com.model.clientinvest.ClientInvestmentDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Clients")
@Route(value = "", layout = MainLayout.class)
public class ClientsView extends VerticalLayout {

    private Grid<ClientDto> grid = new Grid<>(ClientDto.class);
    private ClientForm clientForm;
    private ClientInvestmentForm clientInvestmentForm;
    private TextField filterText = new TextField();
    private ClientService clientService;

    public ClientsView(ClientService clientService) {
        this.clientService = clientService;

        addClassName("list-view");/*classe css se presente*/
        setSizeFull();
        configureGrid();
        configureForm();
        configureClientInvestmentForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
        closeClientInvestmentForm();
    }

    /**
     * @see https://vaadin.com/docs/latest/components/grid
     * https://vaadin.com/docs/latest/components/grid/flow#sorting
     * Theme : https://www.youtube.com/watch?v=Swki9XXs9SA  16.27
     */
    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addClassName("contact-grid");//classe css
        grid.setSizeFull();//luncghezza grid

        // puoi scegliere le colonne che vuoi usare , di default se non specifichi verranno utilizzate tutte
        // grid.setColumns("firstName", "lastName", "emailAdress", "status");
        grid.setColumns("firstName", "lastName", "emailAdress");

        grid.addComponentColumn(clientDto -> createStatusIcon(clientDto))
                .setHeader("Payment");

        grid.addColumn(createIsClientComponentRenderer()).setHeader("Status");

        grid.getColumns().forEach(columns -> columns.setAutoWidth(true)); // gestire oggetto per oggetto

        grid.addColumn(createActionRenderer()).setAutoWidth(true);

        /*Selezionando una colonna (SELEZIONANDO LA STESSA L OGGETTO E NULL)*/
        grid.asSingleSelect().addValueChangeListener(event -> editClient(event.getValue()));
    }

    /*EVENTI PRINCIPALI DELLA FORM*/
    private void configureForm() {
        clientForm = new ClientForm();/*items per menu a tendina*/
        clientForm.setWidth("40em");
        clientForm.addListener(ClientForm.SaveEvent.class, this::saveClient);
        clientForm.addListener(ClientForm.CloseEvent.class, closeEvent -> closeEditor());
        clientForm.addListener(ClientForm.DeleteEvent.class, this::deleteClient);
    }

    private void configureClientInvestmentForm() {
        clientInvestmentForm = new ClientInvestmentForm(clientService);
        clientInvestmentForm.setVisible(false);

        clientInvestmentForm.addListener(ClientInvestmentForm.SaveEvent.class, this::saveInvestment);
        clientInvestmentForm.addListener(ClientInvestmentForm.CloseEvent.class, closeEvent -> closeClientInvestmentForm());
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Search name...");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));

        /*l elemento viene recuperato quando si e smesso di inserire input
        ('char') utile per non eseguire chiamate multiple al database */
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());/*ad ogni cambiamento chiama il metodo*/
        filterText.setWidth("45em");

        Button clientButton = new Button("New client", VaadinIcon.PLUS.create());
        clientButton.addClickListener(event -> saveClient());//Reindirizzamento sulla form
        clientButton.setAutofocus(true);

        HorizontalLayout toolbar = new HorizontalLayout(filterText, clientButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private Component getContent() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(grid, clientForm, clientInvestmentForm);
        horizontalLayout.setFlexGrow(1, grid);
        horizontalLayout.setFlexGrow(2, clientForm);
        horizontalLayout.setFlexGrow(3, clientInvestmentForm);
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

    private void closeClientInvestmentForm() {
        clientInvestmentForm.setClientInvestmentDto(null);
        clientInvestmentForm.setVisible(false);
        clientInvestmentForm.removeClassName("editing");
    }

    private void deleteClient(ClientForm.DeleteEvent event) {
        clientService.deleteContact(event.getClientDto());
        updateList();
        closeEditor();
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
            if (clientInvestmentForm != null) {/*nel caso la finestra dei investimenti e aperta*/
                closeClientInvestmentForm();
            }
            clientForm.setClient(clientDto);/*valorizzo la form con l oggetto attuale*/
            clientForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void saveInvestment(ClientInvestmentForm.SaveEvent event) {
        try {
            clientService.saveInvestment(event.getClientInvestmentDto());
            updateList();
            closeClientInvestmentForm();
        } catch (Exception e) {
            System.err.println("Something wrong with the valoriz. of the form"); ///TODO
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
                icon.addClickListener(event -> {
                    //TODO RITORNARE INVESTIMENTI PAGATI
                });
            } else if (clientDto.getClient() && (!clientDto.getPayment())) {
                /*E cliente e  non ha pagato*/
                icon = VaadinIcon.CLOSE_SMALL.create();
                icon.getElement().getThemeList().add("badge error");
                icon.addClickListener(event -> {
                    //TODO RITORNARE INVESTIMENTI NON PAGATI
                });
            }
        }
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }

    //https://lbruun.github.io/Vaadin.LitRenderer-Examples/#_create_an_icon_button
    private Renderer<ClientDto> createActionRenderer() {
        //             return LitRenderer.<ClientDto> of("<vaadin-button theme=\"primary\" @click= \"${invest}\" >Invest</vaadin-button>").withFunction("invest", clientDto -> {
        return LitRenderer.<ClientDto>of("<vaadin-button autofocus=\"true\"  @click= \"${invest}\" >Invest</vaadin-button>").withFunction("invest", clientDto -> {

            closeEditor();//form cliente
            clientInvestmentForm.setVisible(true);
            clientInvestmentForm.setClientInvestmentDto(new ClientInvestmentDto(clientDto.getFirstName(),
                    clientDto.getLastName(), 1, clientDto.getYearSalary(), 0, clientDto.getClient()));
            clientInvestmentForm.setWidth("60em");
        });
    }

}
