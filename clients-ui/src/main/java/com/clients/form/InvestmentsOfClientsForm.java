package com.clients.form;

import com.clients.service.ClientsInvestmentService;
import com.model.investment.InvestmentsOfClientsDto;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.shared.Registration;


public class InvestmentsOfClientsForm extends FormLayout {
    public IntegerField sumToInvest = new IntegerField("Sum");
    public ComboBox<Integer> mounth = new ComboBox<>("Mounth");
    public Button save = new Button("Save");
    public Button delete = new Button("Delete");
    public Button reset = new Button("Reset");
    ClientsInvestmentService clientsInvestmentService;
    private Grid<InvestmentsOfClientsDto> grid;
    private Integer initialValue;

    public InvestmentsOfClientsForm(InvestmentsOfClientsDto investmentsOfClientsDto,
                                    ClientsInvestmentService clientsInvestmentService, Grid<InvestmentsOfClientsDto> grid) {

        this.grid = grid;
        this.clientsInvestmentService = clientsInvestmentService;
        this.initialValue = investmentsOfClientsDto.getSum();

        setDto(investmentsOfClientsDto);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_ERROR);
        HorizontalLayout horizontalLayout = new HorizontalLayout(save, delete, reset);
        add(sumToInvest, mounth, horizontalLayout);
    }

    /**
     * L OGGETTO DELLA FORM VIENE INIZIALIZZATO CON QUESTO METODO
     */
    public void setDto(InvestmentsOfClientsDto investmentsOfClientsDto) {
        mounth.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        mounth.setValue(investmentsOfClientsDto.getMounth());
        sumToInvest.setValue(investmentsOfClientsDto.getSum());

        sumToInvest.addValueChangeListener(value -> {
            investmentsOfClientsDto.setSum(value.getValue());
        });

        reset.addClickListener(event -> resetInitialSumValue());

        save.addClickListener(event -> validateAndSave(investmentsOfClientsDto));
        delete.addClickListener(event -> fireEvent(new InvestmentsOfClientsForm.DeleteEvent(this, investmentsOfClientsDto)));

        addEventsInvestmentsOfClientsForm();
    }



    private void validateAndSave(InvestmentsOfClientsDto investmentsOfClientsDto) {
        fireEvent(new SaveEvent(this, investmentsOfClientsDto));
    }


    private void addEventsInvestmentsOfClientsForm() {
        this.addListener(InvestmentsOfClientsForm.SaveEvent.class, this::saveChangedInvestment);
        this.addListener(InvestmentsOfClientsForm.DeleteEvent.class, this::deleteInvestment);
    }

    private void saveChangedInvestment(InvestmentsOfClientsForm.SaveEvent event) {
        try {
            clientsInvestmentService.saveChangedInvestment(event.getInvestmentsOfClientsDto());
            this.setVisible(false);
            updateList();
        } catch (Exception e) {
            System.err.println("Something wrong with the valoriz. of the form"); ///TODO
        }
    }

    private void deleteInvestment(InvestmentsOfClientsForm.DeleteEvent event) {
        try {
            clientsInvestmentService.deleteInvestment(event.getInvestmentsOfClientsDto());
            this.setVisible(false);
            updateList();
        } catch (Exception e) {
            System.err.println("Something wrong with the valoriz. of the form"); ///TODO
        }
    }
    private void resetInitialSumValue() {
        sumToInvest.setValue(this.initialValue);
    }

    private void updateList() {
        grid.setItems(clientsInvestmentService.getAll());
    }

    public static abstract class InvestmentsOfClientsFormEvent extends ComponentEvent<InvestmentsOfClientsForm> {
        private InvestmentsOfClientsDto investmentsOfClientsDto;/*Store contact*/

        protected InvestmentsOfClientsFormEvent(InvestmentsOfClientsForm source, InvestmentsOfClientsDto investmentsOfClientsDto) {
            super(source, false);
            this.investmentsOfClientsDto = investmentsOfClientsDto;
        }

        public InvestmentsOfClientsDto getInvestmentsOfClientsDto() {
            return investmentsOfClientsDto;
        }

    }

    public static class SaveEvent extends InvestmentsOfClientsForm.InvestmentsOfClientsFormEvent {
        SaveEvent(InvestmentsOfClientsForm source, InvestmentsOfClientsDto investmentsOfClientsDto) {
            super(source, investmentsOfClientsDto);
        }
    }

    public static class DeleteEvent extends InvestmentsOfClientsForm.InvestmentsOfClientsFormEvent {
        DeleteEvent(InvestmentsOfClientsForm source, InvestmentsOfClientsDto investmentsOfClientsDto) {
            super(source, investmentsOfClientsDto);
        }
    }

    public static class CloseEvent extends InvestmentsOfClientsForm.InvestmentsOfClientsFormEvent {
        CloseEvent(InvestmentsOfClientsForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);//*Aggiunge l evento attuale *//*
    }

}