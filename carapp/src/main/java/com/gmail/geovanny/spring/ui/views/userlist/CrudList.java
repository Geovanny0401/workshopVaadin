package com.gmail.geovanny.spring.ui.views.userlist;

import com.gmail.geovanny.spring.backend.Service.IRoleService;
import com.gmail.geovanny.spring.backend.Service.IUserService;
import com.gmail.geovanny.spring.backend.entity.Role;
import com.gmail.geovanny.spring.backend.entity.User;
import com.gmail.geovanny.spring.ui.MainView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;


@Route(value = "users",layout = MainView.class)
@PageTitle("Users List")
public class CrudList extends Composite<VerticalLayout> {

    private Button refresh = new Button("", VaadinIcon.REFRESH.create());
    private Button add = new Button("", VaadinIcon.PLUS.create());
    private Button edit = new Button("", VaadinIcon.PENCIL.create());

    private final TextField searchField = new TextField("", "Search users");


    private Grid<User> grid = new Grid<>(User.class);

    private final IUserService repo;
    private final IRoleService role;

    private class UserFormDialog extends Dialog {

        private TextField firstName = new TextField("First name");
        private TextField lastName = new TextField("Last name");
        private TextField email = new TextField("Email");
        private PasswordField password = new PasswordField("Password");
        private CheckboxGroup<Role> roles = new CheckboxGroup<>();
        private ComboBox<Role> mainRole = new ComboBox<>("Main Role", role.listar());
        private Checkbox blocked = new Checkbox("Blocked");

        private Button cancel = new Button("Cancel");
        private Button save = new Button("Save", VaadinIcon.CHECK.create());

        public UserFormDialog(String caption, User user) {
            initLayout(caption);
            initBehavior(user);
        }

        private void initLayout(String caption) {
            roles.setItems(role.listar());

            save.getElement().setAttribute("theme", "primary");

            HorizontalLayout buttons = new HorizontalLayout(cancel, save);
            buttons.setSpacing(true);

            FormLayout formLayout = new FormLayout(new H2(caption), firstName, lastName, email, password, roles, mainRole, blocked);

            VerticalLayout layout = new VerticalLayout(formLayout, buttons);
            layout.setAlignSelf(FlexComponent.Alignment.END, buttons);
            add(layout);
        }

        private void initBehavior(User user) {
            BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
            binder.bindInstanceFields(this);
            binder.readBean(user);

            cancel.addClickListener(e -> close());
            save.addClickListener(e -> {
                try {
                    binder.validate();
                    binder.writeBean(user);
                    repo.registrar(user);
                    close();
                    refresh();
                    Notification.show("User saved");

                } catch (ValidationException ex) {
                    Notification.show("Please fix the errors and try again");
                }
            });
        }
    }

    private class RemoveDialog extends Dialog {
        private Button cancel = new Button("Cancel");
        private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

        public RemoveDialog(User user) {
            initLayout(user);
            initBehavior(user);
        }

        private void initLayout(User user) {
            Span span = new Span("Do you really want to delete the user " + user.getFirstName() + " " + user.getLastName() + "?");

            delete.getElement().setAttribute("theme", "error");
            HorizontalLayout buttons = new HorizontalLayout(cancel, delete);

            VerticalLayout layout = new VerticalLayout(new H2("Confirm"), span, buttons);
            layout.setAlignSelf(FlexComponent.Alignment.END, buttons);
            add(layout);
        }

        private void initBehavior(User user) {
            cancel.addClickListener(e -> close());

            delete.addClickListener(e -> {
                repo.eliminar(user.getId());
                refresh();
                close();
            });
        }


    }


    public CrudList(IUserService repo,IRoleService role) {
        this.repo = repo;
        this.role=role;
       // addSearchBar();
        initLayout();
        initBehavior();
        refresh();

    }

    //nuevo buscar

    private void addSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");
        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
        //searchField.addValueChangeListener(e -> listUsers(Integer.parseInt(e.getValue())));
        //searchField.addValueChangeListener(e -> listUsers(e.getValue()));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        viewToolbar.add(searchField);
        getContent().add(viewToolbar);

    }

    void listUsers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.listar());
        }
        else {
            //grid.setItems(repo.listarId(filterText));
        }
    }


    private void initLayout() {
        HorizontalLayout header = new HorizontalLayout(refresh, add, edit);
        grid.setColumns("firstName", "lastName", "email", "mainRole");
        grid.addComponentColumn(user -> new Button("Delete", e -> deleteClicked(user)));
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

    private void deleteClicked(User user) {
        showRemoveDialog(user);
        refresh();
    }

    private void updateHeader() {
        boolean selected = !grid.asSingleSelect().isEmpty();
        edit.setEnabled(selected);
    }

    private void showAddDialog() {
        UserFormDialog dialog = new UserFormDialog("Add", new User());
        dialog.open();
    }

    private void showEditDialog() {
        UserFormDialog dialog = new UserFormDialog("Edit", grid.asSingleSelect().getValue());
        dialog.open();
    }

    private void showRemoveDialog(User user) {
        RemoveDialog dialog = new RemoveDialog(user);
        dialog.open();
    }
}
