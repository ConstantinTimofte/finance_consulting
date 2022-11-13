package com.clients.form;

import com.model.client.ClientDto;
import com.model.clientinvest.ClientInvestmentDto;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;


public class ClientInvestmentForm extends FormLayout {
    public ClientDto clientDto;
    Binder<ClientDto> binder = new BeanValidationBinder<>(ClientDto.class);

    public TextField firstName = new TextField("First name");
    public TextField lastName = new TextField("Last name ");

    public RadioButtonGroup<String> investmentType = new RadioButtonGroup<>();
    public TextField newInverstment = new TextField("NEW investment");
    public ComboBox<Object> investmentList = new ComboBox<>("EXISTING Investment");


    public Button save = new Button("Save");
    public Button cancel = new Button("Cancel");
    public Button delete = new Button("Delete...");

    public ClientInvestmentForm() {
        //       binder.bindInstanceFields(this);
        add(firstName, lastName, setInvestmentLayout(), createButton());
    }

    private Component setInvestmentLayout() {
        investmentType.setLabel("Investment");
        investmentType.setItems("NEW", "EXISTING");
        investmentType.setValue("NEW");

        HorizontalLayout horizontalLayout = new HorizontalLayout(investmentType, newInverstment, investmentList);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);/*uno da un lato - l atro dall opposto*/

        investmentType.addValueChangeListener(check -> changeInvestmentInputByCheck(check));
        investmentType.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        newInverstment.setWidth("25em");
        investmentList.setWidth("18em");

        return horizontalLayout;
    }


    private void changeInvestmentInputByCheck(AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String> check) {
        switch (check.getValue()) {
            case "EXISTING":
                newInverstment.setVisible(false);
                investmentList.setVisible(true);
                break;
            default:
                newInverstment.setVisible(true);
                investmentList.setVisible(false);
        }
    }

    private Component createButton() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        cancel.addClickShortcut(Key.ESCAPE);

        HorizontalLayout buttonsLayout = new HorizontalLayout(delete, cancel, save);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setFlexGrow(1, save);/*ingrandesce il bottone per occupare tutto il width*/
        return buttonsLayout;
    }

    /*Viene creato l oggetto */
    public void setClient(ClientDto clientDto) {
        this.clientDto = clientDto;
        binder.readBean(clientDto); /*vengono letti i lavori selezionati nella grid*/
        currentClientValue(getClientDto());
    }

    public ClientDto getClientDto() {
        return clientDto;
    }


    private void currentClientValue(ClientDto clientDto) {
        if (clientDto != null) {
            firstName.setValue(clientDto.getFirstName());
            firstName.setReadOnly(true);

            lastName.setValue(clientDto.getLastName());
            lastName.setReadOnly(true);
        }
    }

    // Events
    public static abstract class ClientInvestmentFormEvent extends ComponentEvent<ClientInvestmentForm> {
        private ClientInvestmentDto clientInvestmentDto;/*Store contact*/

        protected ClientInvestmentFormEvent(ClientInvestmentForm source, ClientInvestmentDto clientInvestmentDto) {
            super(source, false);
            this.clientInvestmentDto = clientInvestmentDto;
        }

        public ClientInvestmentDto getClientInvestmentDto() {
            return clientInvestmentDto;
        }
    }

    public static class SaveEvent extends ClientInvestmentFormEvent {
        SaveEvent(ClientInvestmentForm source, ClientInvestmentDto clientInvestmentDto) {
            super(source, clientInvestmentDto);
        }
    }

    public static class DeleteEvent extends ClientInvestmentFormEvent {
        DeleteEvent(ClientInvestmentForm source, ClientInvestmentDto clientInvestmentDto) {
            super(source, clientInvestmentDto);
        }
    }

    public static class CloseEvent extends ClientInvestmentFormEvent {
        CloseEvent(ClientInvestmentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);/*Aggiunge l evento attuale */
    }
}
