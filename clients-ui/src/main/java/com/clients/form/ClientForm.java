package com.clients.form;

import com.model.client.ClientDto;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.shared.Registration;

public class ClientForm extends FormLayout {

    Binder<ClientDto> binder = new BeanValidationBinder<>(ClientDto.class);
    public TextField firstName = new TextField("First name");
    public TextField lastName = new TextField("Last name ");
    public EmailField emailAdress = new EmailField("Email");
    public TextField phoneNumber = new TextField("Phone");
    public IntegerField yearSalary = new IntegerField("salary/year");

    public TextField cnp = new TextField("Cnp");
    public TextField city = new TextField("City");
    public TextArea notes = new TextArea("Notes");

    public Button save = new Button("Save");
    public Button cancel = new Button("Cancel");
    public Button delete = new Button("Delete...");

    public ClientDto clientDto;

    public ClientForm() {
        //  setBinder();
        binder.bindInstanceFields(this);
        binder.forField(phoneNumber)
                .withValidator(new RegexpValidator("Invalid phone number", "^[0-9]*$"))
                .bind(ClientDto::getPhoneNumber, ClientDto::setPhoneNumber);
        //  binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));  ---> copre il bottone nel caso non passa i controlli del binding*/

        setInputFiledsStyle();
        setInputClearButton();

        HorizontalLayout horizontalLayout = new HorizontalLayout(phoneNumber, yearSalary);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);/*uno da un lato - l atro dall opposto*/

        HorizontalLayout horizontalLayout2 = new HorizontalLayout(cnp, city);
        horizontalLayout2.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, emailAdress, horizontalLayout, horizontalLayout2, notes);

        add(formLayout, createButton());
    }

    //https://www.youtube.com/watch?v=Efv_cPHEqdQ  --->  Master Vaadin VerticalLayout and HorizontalLayout
    private Component createButton() {
        save.getStyle().set("margin-left", "auto");
        /* save.setWidth("18em");*/

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        save.addClickListener(event -> validateAndSave());
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        HorizontalLayout buttonsLayout = new HorizontalLayout(delete, cancel, save);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setFlexGrow(1, save);/*ingrandesce il bottone per occupare tutto il width*/
        return buttonsLayout;
    }

    private void validateAndSave() {
        try {
            binder.writeBean(clientDto);
            fireEvent(new SaveEvent(this, clientDto));
        } catch (ValidationException e) {
            e.printStackTrace();
        }

    }

    /*Viene creato l oggetto */
    public void setClient(ClientDto clientDto) {
        this.clientDto = clientDto;
        binder.readBean(clientDto);/*lettura del binder quando valorizzi i valori dell oggetto*/
    }

    private void setBinder() {
//VALIDAZIONE PROP OGG
        binder.forField(firstName).bind(ClientDto::getFirstName, ClientDto::setFirstName);// ---> By field*/
        binder.forField(lastName).bind(ClientDto::getLastName, ClientDto::setLastName);
        binder.forField(yearSalary).bind(ClientDto::getYearSalary, ClientDto::setYearSalary);
        binder.forField(emailAdress)
                .withValidator(new EmailValidator("Invalid email address"))
                // .withValidator(email -> email.isBlank(), "Only acme.com email addresses are allowed")
                .bind(ClientDto::getEmailAdress, ClientDto::setEmailAdress);

        binder.forField(phoneNumber)
                .withValidator(new RegexpValidator("Invalid phone number", "^[0-9]*$"))
                /* .withValidator( number -> !number.matches("^[0-9]*$") ,"Invalid phone number")*/
                .bind(ClientDto::getPhoneNumber, ClientDto::setPhoneNumber);

        binder.forField(city).bind(ClientDto::getCity, ClientDto::setCity);
        binder.forField(cnp).bind(ClientDto::getCnp, ClientDto::setCnp);
        binder.forField(notes).bind(ClientDto::getNotes, ClientDto::setNotes);
    }

    private void setInputClearButton() {
        //  phoneNumber.setPattern("^[0-9]*$");
        firstName.setClearButtonVisible(true);
        lastName.setClearButtonVisible(true);
        yearSalary.setClearButtonVisible(true);
        emailAdress.setClearButtonVisible(true);
        phoneNumber.setClearButtonVisible(true);
        city.setClearButtonVisible(true);
        cnp.setClearButtonVisible(true);
        notes.setClearButtonVisible(true);
    }

    private void setInputFiledsStyle() {
        phoneNumber.setWidth("14em");
        cnp.setWidth("14em");
        yearSalary.setWidth("12em");
        notes.setHeight("10em");
        addClassName("contact-form");
    }

    // Events
    public static abstract class ClientFormEvent extends ComponentEvent<ClientForm> {
        private ClientDto clientDto;/*Store contatct*/

        protected ClientFormEvent(ClientForm source, ClientDto clientDto) {
            super(source, false);
            this.clientDto = clientDto;
        }

        public ClientDto getClientDto() {
            return clientDto;
        }
    }

    public static class SaveEvent extends ClientFormEvent {
        SaveEvent(ClientForm source, ClientDto clientDto) {
            super(source, clientDto);
        }
    }

    public static class DeleteEvent extends ClientFormEvent {
        DeleteEvent(ClientForm source, ClientDto clientDto) {
            super(source, clientDto);
        }
    }

    public static class CloseEvent extends ClientFormEvent {
        CloseEvent(ClientForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);/*Aggiunge l evento attuale */
    }
}