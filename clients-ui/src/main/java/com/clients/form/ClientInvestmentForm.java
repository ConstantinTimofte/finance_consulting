package com.clients.form;

import com.clients.service.ClientService;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;


public class ClientInvestmentForm extends FormLayout {
    public ClientDto clientDto;
    public Binder<ClientDto> binder = new BeanValidationBinder<>(ClientDto.class);

    public ComboBox<String> clientInvestment = new ComboBox<>("Already invest");
    public IntegerField sum = new IntegerField("Invested");

    public TextField firstName = new TextField("First name");
    public TextField lastName = new TextField("Last name ");

    public RadioButtonGroup<String> investmentType = new RadioButtonGroup<>();
    public TextField newInverstment = new TextField("New investment");
    public ComboBox<Object> investmentList = new ComboBox<>("Existing Investments");

    public IntegerField yearSalary = new IntegerField("Salary/year");
    public ComboBox<Integer> everyMounth = new ComboBox<>("Mounth");

    public IntegerField expenses = new IntegerField("Expenses/mounth");
    public IntegerField saving = new IntegerField("Saving/mounth");

    public IntegerField sumToInvest = new IntegerField("Sum to invest");
    public Button generateSum = new Button("Calculate");

    public Button save = new Button("Save");
    public Button cancel = new Button("Cancel");

    public ClientInvestmentForm() {
        add(firstName, lastName, setClientInvestment(),
                setInvestmentLayout(), setYearAndMounthLayout(),
                setExpensesAndSavingLayout(), setSumInvestmentLayout(),
                createButton());
    }


    private Component setClientInvestment() {
        clientInvestment.setWidth("25em");
        sum.setReadOnly(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout(sum, clientInvestment);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        return horizontalLayout;
    }

    private Component setInvestmentLayout() {
        investmentType.setLabel("Investment");
        investmentType.setItems("NEW", "EXISTING");
        investmentType.setValue("NEW");
        setInvestmentStyle();

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
        everyMounth.setWidth("15em");
        everyMounth.setItems(maxMounth);
        everyMounth.setValue(1);
        yearSalary.setWidth("15em");
        HorizontalLayout horizontalLayout = new HorizontalLayout(yearSalary, everyMounth);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);/*uno da un lato - l atro dall opposto*/
        return horizontalLayout;
    }

    private Component setExpensesAndSavingLayout() {
        expenses.setWidth("15em");
        saving.setWidth("15em");
        HorizontalLayout horizontalLayout = new HorizontalLayout(expenses, saving);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);/*uno da un lato - l atro dall opposto*/
        return horizontalLayout;
    }

    private Component setSumInvestmentLayout() {
        sumToInvest.setWidth("15em");
        generateSum.addClickListener(gen -> maxSumToInvest(
                yearSalary.getValue(), everyMounth.getValue(), saving.getValue(), expenses.getValue()
        ));
        HorizontalLayout horizontalLayout = new HorizontalLayout(sumToInvest, generateSum);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        // horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        // horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
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
        //generateSum.addClickListener(gen -> sumToInvest.setValue(2));
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        cancel.addClickShortcut(Key.ESCAPE);

        HorizontalLayout buttonsLayout = new HorizontalLayout(cancel, save);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setFlexGrow(1, save);/*ingrandesce il bottone per occupare tutto il width*/
        return buttonsLayout;
    }

    private void setInvestmentStyle() {
        investmentList.setVisible(false);
        newInverstment.setWidth("25em");
        investmentList.setWidth("25em");
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

            yearSalary.setValue(clientDto.getYearSalary());
            yearSalary.setReadOnly(true);
        }
    }

    private void maxSumToInvest(Integer salaryYear, Integer mounth, Integer savingMounth, Integer expencesMounth) {
        int oneMounth = salaryYear / 12;
        int max = oneMounth * mounth - (savingMounth - expencesMounth);
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
