package hudson.plugins.nextexecutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.View;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class NextExecutionsWidgetTest {

    @Test
    void testConstructorWithOwnerUrl(JenkinsRule j) {
        String ownerUrl = "http://example.com";
        NextExecutionsWidget widget = new NextExecutionsWidget(ownerUrl);
        assertEquals(ownerUrl, widget.getOwnerUrl());
    }

    @Test
    void testConstructorWithOwnerUrlAndTriggerClass(JenkinsRule j) {
        String ownerUrl = "http://example.com";
        Class<? extends Trigger> triggerClass = TimerTrigger.class;
        NextExecutionsWidget widget = new NextExecutionsWidget(ownerUrl, triggerClass);
        assertEquals(ownerUrl, widget.getOwnerUrl());
    }

    @Test
    void testGetWidgetName(JenkinsRule j) {
        NextExecutionsWidget widget = new NextExecutionsWidget("test");
        assertEquals("Next Executions", widget.getWidgetName());
    }

    @Test
    void testGetWidgetEmptyMessage(JenkinsRule j) {
        NextExecutionsWidget widget = new NextExecutionsWidget("test");
        assertEquals("No next executions found.", widget.getWidgetEmptyMessage());
    }

    @Test
    void testGetWidgetId(JenkinsRule j) {
        NextExecutionsWidget widget = new NextExecutionsWidget("test");
        assertEquals("next-exec", widget.getWidgetId());
    }

    @Test
    void testShowWidget(JenkinsRule j) {
        NextExecutionsWidget widget = new NextExecutionsWidget("test");
        assertTrue(widget.showWidget());
    }

    // TODO: Add testGetBuilds()

    @Test
    void testType(JenkinsRule j) {
        NextExecutionsWidget.FactoryImpl factory = new NextExecutionsWidget.FactoryImpl();
        assertEquals(View.class, factory.type());
    }

    @Test
    void testWidgetType(JenkinsRule j) {
        NextExecutionsWidget.FactoryImpl factory = new NextExecutionsWidget.FactoryImpl();
        assertEquals(NextExecutionsWidget.class, factory.widgetType());
    }

    @Test
    void testCreateFor(JenkinsRule j) {
        NextExecutionsWidget.FactoryImpl factory = new NextExecutionsWidget.FactoryImpl();
        String url = "http://example.com/";
        View target = mock(View.class);
        when(target.getUrl()).thenReturn(url);
        Collection<NextExecutionsWidget> widgets = factory.createFor(target);
        assertEquals(1, widgets.size());
    }
}
