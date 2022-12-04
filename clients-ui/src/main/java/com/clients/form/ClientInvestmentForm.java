package com.clients.form;

import com.clients.service.ClientService;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;


public class ClientInvestmentForm extends FormLayout {
    public ClientInvestmentDto clientInvestmentDto;
    Binder<ClientInvestmentDto> clientInvestmentBinder = new BeanValidationBinder<>(ClientInvestmentDto.class);
    ClientService clientService;
/*    public ComboBox<String> clientInvestment = new ComboBox<>("Already invest");
    public IntegerField sum = new IntegerField("Invested");*/

    public TextField firstName = new TextField("First name");
    public TextField lastName = new TextField("Last name ");

    public RadioButtonGroup<String> investmentType = new RadioButtonGroup<>();
    public TextField newInverstment = new TextField("New investment");
    public ComboBox<String> investmentList = new ComboBox<>("Existing Investments");

    public IntegerField yearSalary = new IntegerField("Salary/year");
    public ComboBox<Integer> mounth = new ComboBox<>("Mounth");

    public IntegerField expenses = new IntegerField("Expenses/mounth");
    public IntegerField saving = new IntegerField("Saving/mounth");

    public IntegerField sumToInvest = new IntegerField("Max sum to invest/mounth");
    public Button generateSum = new Button("Calculate");

    public Button save = new Button("Save");
    public Button cancel = new Button("Cancel");

    public ClientInvestmentForm(ClientService clientService) {
        this.clientService = clientService;

        binder();//CONTROLLO INPUT
        add(firstName, lastName,
                setInvestmentLayout(), setYearAndMounthLayout(),
                setExpensesAndSavingLayout(), setSumInvestmentLayout(),
                createButton());
    }

    private void binder() {
        /**Esegue una mappatura fra le propieta della from e l oggetto
         * 1.Devono avere i stessi tipi*/

        /**Ordine :
         *1. Leggo le propieta dell Dto per caricarle nella  form  - con prop: Nome Cognome(!se necessario) 'read'
         *2. Eseguo una mappatura fra tutti i parametri del Dto con quelli della form 'bindInstanceFields'
         *3. Prendo le informazioni della form  e le mappo nel Dto */
        clientInvestmentBinder.bindInstanceFields(this);
    }

    private Component setInvestmentLayout() {
        investmentType.setLabel("Investment");
        investmentType.setItems("NEW", "EXISTING");
        investmentType.setValue("NEW");

        setInvestmentStyle();
        newInverstment.addValueChangeListener(event -> clientInvestmentDto.setInvestmentName(event.getValue()));
        investmentList.addValueChangeListener(event -> clientInvestmentDto.setInvestmentName(event.getValue()));

        investmentType.addValueChangeListener(check -> changeInvestmentInputByCheck(check));
        investmentType.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        HorizontalLayout horizontalLayout = new HorizontalLayout(investmentType, newInverstment, investmentList);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);/*uno da un lato - l atro dall opposto*/

        return horizontalLayout;
    }


    private Component setYearAndMounthLayout() {
        List<Integer> maxMounth = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            maxMounth.add(i);
        }
        mounth.setWidth("15em");
        mounth.setItems(maxMounth);
        mounth.setValue(1);
        yearSalary.setWidth("15em");

        HorizontalLayout horizontalLayout = new HorizontalLayout(yearSalary, mounth);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);/**uno da un lato - l atro dall opposto*/
        return horizontalLayout;
    }

    private Component setExpensesAndSavingLayout() {
        expenses.setWidth("15em");
        saving.setWidth("15em");

        HorizontalLayout horizontalLayout = new HorizontalLayout(expenses, saving);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);/**uno da un lato - l atro dall opposto*/
        return horizontalLayout;
    }

    private Component setSumInvestmentLayout() {
        sumToInvest.setWidth("15em");
        sumToInvest.setReadOnly(true);
        // ---> By field*/
        generateSum.addClickListener(gen -> maxSumToInvest(
                yearSalary.getValue(), mounth.getValue(), saving.getValue(), expenses.getValue()
        ));
        HorizontalLayout horizontalLayout = new HorizontalLayout(sumToInvest, generateSum);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        horizontalLayout.setSpacing(true);
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

        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));
        cancel.addClickShortcut(Key.ESCAPE);
        save.addClickListener(event -> validateAndSave());

        HorizontalLayout buttonsLayout = new HorizontalLayout(cancel, save);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setFlexGrow(1, save);/*ingrandesce il bottone per occupare tutto il width*/
        return buttonsLayout;
    }

    private void validateAndSave() {
        try {
            clientInvestmentBinder.writeBean(clientInvestmentDto);/**valorizza l oggetto con i valori della form*/

            fireEvent(new SaveEvent(this, clientInvestmentDto));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void setInvestmentStyle() {
        investmentList.setVisible(false);
        newInverstment.setWidth("25em");
        investmentList.setWidth("25em");
    }


    /*Viene creato l oggetto */
    public void setClientInvestmentDto(ClientInvestmentDto clientInvestmentDto) {
        this.clientInvestmentDto = clientInvestmentDto;
        clientInvestmentBinder.readBean(clientInvestmentDto);/** legge i valori dell oggetto eseguendo una mappatura Oggetto -> form*/
        currentClientValue(clientInvestmentDto);
    }

    /**
     * METODO CHE CONTIENE LO STATO ATTUALE DEL OGGETTO (VALORI /LAYOUT )
     */
    private void currentClientValue(ClientInvestmentDto clientInvestmentDto) {
        if (clientInvestmentDto != null) {
            List<String> possibleInvestment = clientService.getPossibleInvestments(clientInvestmentDto.getFirstName(), clientInvestmentDto.getSecondName());
            investmentList.setItems(possibleInvestment);

            firstName.setValue(clientInvestmentDto.getFirstName());
            firstName.setReadOnly(true);

            lastName.setValue(clientInvestmentDto.getSecondName());
            lastName.setReadOnly(true);

            yearSalary.setValue(clientInvestmentDto.getYearSalary());
            yearSalary.setReadOnly(true);
        }
    }

    private void maxSumToInvest(Integer salaryYear, Integer mounth, Integer savingMounth, Integer expencesMounth) {
        int oneMounth = salaryYear / 12;
        int max = oneMounth * mounth - savingMounth - expencesMounth;
        sumToInvest.setReadOnly(false);
        sumToInvest.setValue(max);
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

    public static class CloseEvent extends ClientInvestmentFormEvent {
        CloseEvent(ClientInvestmentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);/*Aggiunge l evento attuale */
    }
}
