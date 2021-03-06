package je.techtribes;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.*;

import java.util.Set;
import java.util.stream.Collectors;

public class CreateViews extends AbstractStructurizrModel {

    public static void main(String[] args) throws Exception {
        new CreateViews().run();
    }

    public CreateViews() throws Exception {
    }

    void run() throws Exception {
        createSystemContextView();
        createContainerView();
        createComponentViewsForContentUpdater();
        createComponentViewsForWebApplication();
        addStyles();

        writeToFile();
    }

    private void createSystemContextView() {
        SoftwareSystem techTribes = getTechTribesSoftwareSystem();
        SystemContextView contextView = workspace.getViews().createContextView(techTribes);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
    }

    private void createContainerView() {
        SoftwareSystem techTribes = getTechTribesSoftwareSystem();
        ContainerView containerView = workspace.getViews().createContainerView(techTribes);
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();
    }

    private void createComponentViewsForContentUpdater() {
        Container contentUpdater = getContentUpdater();
        Container webApplication = getWebApplication();

        ComponentView view = workspace.getViews().createComponentView(contentUpdater);
        view.setDescription("Updating information from external systems");
        view.addAllSoftwareSystems();
        view.addAllContainers();
        view.remove(webApplication);
        view.addAllComponents();
        view.remove(contentUpdater.getComponentWithName("LoggingComponent"));
        view.remove(contentUpdater.getComponentWithName("ContentSourceComponent"));
        view.remove(contentUpdater.getComponentWithName("ActivityUpdater"));
        view.remove(contentUpdater.getComponentWithName("BadgeAwarder"));
        view.removeElementsWithNoRelationships();
        view.removeElementsThatCantBeReachedFrom(contentUpdater.getComponentWithName("ScheduledContentUpdater"));
        view.addRelationships();

        view = workspace.getViews().createComponentView(contentUpdater);
        view.setDescription("Updating recent activity");
        view.addAllSoftwareSystems();
        view.addAllContainers();
        view.remove(webApplication);
        view.addAllComponents();
        view.remove(contentUpdater.getComponentWithName("LoggingComponent"));
        view.remove(contentUpdater.getComponentWithName("ContentSourceComponent"));
        view.remove(contentUpdater.getComponentWithName("TwitterConnector"));
        view.remove(contentUpdater.getComponentWithName("GitHubConnector"));
        view.remove(contentUpdater.getComponentWithName("NewsFeedConnector"));
        view.remove(contentUpdater.getComponentWithName("BadgeAwarder"));
        view.removeElementsWithNoRelationships();
        view.removeElementsThatCantBeReachedFrom(contentUpdater.getComponentWithName("ActivityUpdater"));
        view.addRelationships();

        view = workspace.getViews().createComponentView(contentUpdater);
        view.setDescription("Awarding badges");
        view.addAllSoftwareSystems();
        view.addAllContainers();
        view.remove(webApplication);
        view.addAllComponents();
        view.remove(contentUpdater.getComponentWithName("LoggingComponent"));
        view.remove(contentUpdater.getComponentWithName("ContentSourceComponent"));
        view.remove(contentUpdater.getComponentWithName("TwitterConnector"));
        view.remove(contentUpdater.getComponentWithName("GitHubConnector"));
        view.remove(contentUpdater.getComponentWithName("NewsFeedConnector"));
        view.remove(contentUpdater.getComponentWithName("ActivityUpdater"));
        view.removeElementsWithNoRelationships();
        view.removeElementsThatCantBeReachedFrom(contentUpdater.getComponentWithName("BadgeAwarder"));
        view.addRelationships();
    }

    private void createComponentViewsForWebApplication() {
        Container contentUpdater = getContentUpdater();
        Container webApplication = getWebApplication();

        // create one component view per Spring controller
        Set<Component> controllers = webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals("Spring Controller")).collect(Collectors.toSet());
        for (Component controller : controllers) {
            ComponentView view = workspace.getViews().createComponentView(webApplication);
            view.setDescription(controller.getName());
            view.addAllSoftwareSystems();
            view.addAllContainers();
            view.remove(contentUpdater);
            view.addAllComponents();
            view.remove(webApplication.getComponentWithName("LoggingComponent"));
            view.removeElementsThatCantBeReachedFrom(controller);
            view.addAllPeople();
            view.removeElementsWithNoRelationships();
        }
    }

    private void addStyles() {
        SoftwareSystem techTribes = getTechTribesSoftwareSystem();
        techTribes.addTags(TECHTRIBES_JE);

        Configuration configuration = workspace.getViews().getConfiguration();
        configuration.getStyles().add(new ElementStyle(Tags.ELEMENT, null, null, null, null, null));
        configuration.getStyles().add(new ElementStyle(TECHTRIBES_JE, null, null, "#041F37", "#ffffff", null));
        configuration.getStyles().add(new ElementStyle(Tags.SOFTWARE_SYSTEM, null, null, "#A4B7C9", "#000000", null));
        configuration.getStyles().add(new ElementStyle(Tags.PERSON, null, null, "#728da5", "#ffffff", null));
        configuration.getStyles().add(new ElementStyle(Tags.CONTAINER, null, null, "#2A4E6E", "#ffffff", null));
        configuration.getStyles().add(new ElementStyle(Tags.COMPONENT, null, null, "#728DA5", "#ffffff", null));
        configuration.getStyles().add(new RelationshipStyle(Tags.RELATIONSHIP, null, null, null, null, 500));
    }

}