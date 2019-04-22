package com.gmail.geovanny.spring.ui.views.rolelist;

import com.gmail.geovanny.spring.backend.entity.Role;
import com.gmail.geovanny.spring.backend.service.IRoleService;
import com.gmail.geovanny.spring.ui.MainView;
import com.gmail.geovanny.spring.ui.views.userlist.userList;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "roles", layout = MainView.class)
@PageTitle("Roles List")
public class roleList extends Composite<VerticalLayout> {

    private Button refresh = new Button("", VaadinIcon.REFRESH.create());
    private Button add = new Button("", VaadinIcon.PLUS.create());
    private Button edit = new Button("", VaadinIcon.PENCIL.create());

    private Grid<Role> grid = new Grid<>(Role.class);


    private final IRoleService repo;

    private class UserFormDialog extends Dialog {

        private TextField name = new TextField("Name");
        private Checkbox module1Authorized = new Checkbox("Module1 Authorized");
        private Checkbox accessModule2= new Checkbox("Module2 Authorized");

        private Button cancel = new Button("Cancel");
        private Button save = new Button("Save", VaadinIcon.CHECK.create());


        public UserFormDialog(String caption, Role role) {
            initLayout(caption);
            initBehavior(role);
        }

        private void initLayout(String caption) {
            save.getElement().setAttribute("theme", "primary");
            HorizontalLayout buttons = new HorizontalLayout(cancel, save);
            buttons.setSpacing(true);

            name.setRequiredIndicatorVisible(true);



            FormLayout formLayout = new FormLayout(new H2(caption), name, module1Authorized,accessModule2);
            VerticalLayout layout = new VerticalLayout(formLayout, buttons);
            layout.setAlignSelf(FlexComponent.Alignment.END, buttons);
            add(layout);
        }




        private void initBehavior(Role role) {
            BeanValidationBinder<Role> binder = new BeanValidationBinder<>(Role.class);
            binder.bindInstanceFields(this);
            binder.readBean(role);
            cancel.addClickListener(e -> close());
            save.addClickListener(e -> {
                try {
                    binder.validate();
                    binder.writeBean(role);
                    repo.registrar(role);
                    close();
                    refresh();
                    Notification.show("Role saved");

                } catch (ValidationException ex) {
                    Notification.show("Please fix the errors and try again");
                }
            });
        }
    }
    private class RemoveDialog extends Dialog {
        private Button cancel = new Button("Cancel");
        private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

        public RemoveDialog(Role role) {
            initLayout(role);
            initBehavior(role);
        }

        private void initLayout(Role role) {
            Span span = new Span("Do you really want to delete the user " + role.getName() + "?");

            delete.getElement().setAttribute("theme", "error");
            HorizontalLayout buttons = new HorizontalLayout(cancel, delete);

            VerticalLayout layout = new VerticalLayout(new H2("Confirm"), span, buttons);
            layout.setAlignSelf(FlexComponent.Alignment.END, buttons);
            add(layout);
        }

        private void initBehavior(Role role) {
            cancel.addClickListener(e -> close());
            delete.addClickListener(e -> {
                repo.eliminar(role.getIdRole());
                refresh();
                close();
            });
        }
    }


    public roleList(IRoleService repo) {
        this.repo = repo;
        initLayout();
        initBehavior();
        refresh();

    }

    private void initLayout() {
        HorizontalLayout header = new HorizontalLayout(refresh, add, edit);
        grid.setColumns("name", "module1Authorized");
        grid.addComponentColumn(role -> new Button("Delete", e -> deleteClicked(role)));
        grid.setSizeFull();
        getContent().add(header, grid);
        getContent().expand(grid);
        getContent().setSizeFull();
        getContent().setMargin(false);
        getContent().setPadding(false);
    }

    private void initBehavior() {
        grid.asSingleSelect().addValueChangeListener(e -> updateHeader());
        refresh.addClickListener(e -> refresh());
        add.addClickListener(e -> showAddDialog());
        edit.addClickListener(e -> showEditDialog());
    }

    public void refresh() {
        grid.setItems(repo.listar());
        updateHeader();
    }

    private void deleteClicked(Role role) {
        showRemoveDialog(role);
        refresh();
    }

    private void updateHeader() {
        boolean selected = !grid.asSingleSelect().isEmpty();
        edit.setEnabled(selected);
    }

    private void showAddDialog() {
        UserFormDialog dialog = new UserFormDialog("New Role", new Role());
        dialog.open();
    }

    private void showEditDialog() {
        UserFormDialog dialog = new UserFormDialog("Update Role", grid.asSingleSelect().getValue());
        dialog.open();
    }

    private void showRemoveDialog(Role role) {
        RemoveDialog dialog = new RemoveDialog(role);
        dialog.open();
    }

}
