package com.gmail.geovanny.spring.ui;


import com.gmail.geovanny.spring.ui.views.rolelist.roleList;
import com.gmail.geovanny.spring.ui.views.userlist.userList;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;


@HtmlImport("frontend://styles/shared-styles.html")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Route(value = "")
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends Div implements RouterLayout, PageConfigurator {

    public MainView() {

        H2 title = new H2("esConference");
        title.addClassName("main-layout__title");

        RouterLink users = new RouterLink(null, userList.class);
        users.add(new Icon(VaadinIcon.LIST), new Text("Users"));
        users.addClassName("main-layout__nav-item");
        // Only show as active for the exact URL, but not for sub paths
        users.setHighlightCondition(HighlightConditions.sameLocation());


        RouterLink roles = new RouterLink(null, roleList.class);
        roles.add(new Icon(VaadinIcon.ARCHIVES), new Text("Roles"));
        roles.addClassName("main-layout__nav-item");

        Div navigation = new Div(users,roles );
        navigation.addClassName("main-layout__nav");

        Div header = new Div(title,navigation);
        header.addClassName("main-layout__header");
        add(header);

        addClassName("main-layout");

    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes");
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
    }

}
