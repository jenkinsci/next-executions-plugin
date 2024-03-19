package hudson.plugins.nextexecutions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.View;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import java.util.Collection;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class NextExecutionsWidgetTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testConstructorWithOwnerUrl() {
        String ownerUrl = "http://example.com";
        NextExecutionsWidget widget = new NextExecutionsWidget(ownerUrl);
        assertEquals(ownerUrl, widget.getOwnerUrl());
    }

    @Test
    public void testConstructorWithOwnerUrlAndTriggerClass() {
        String ownerUrl = "http://example.com";
        Class<? extends Trigger> triggerClass = TimerTrigger.class;
        NextExecutionsWidget widget = new NextExecutionsWidget(ownerUrl, triggerClass);
        assertEquals(ownerUrl, widget.getOwnerUrl());
    }

    @Test
    public void testGetWidgetName() {
        NextExecutionsWidget widget = new NextExecutionsWidget("test");
        assertEquals("Next Executions", widget.getWidgetName());
    }

    @Test
    public void testGetWidgetEmptyMessage() {
        NextExecutionsWidget widget = new NextExecutionsWidget("test");
        assertEquals("No next executions found.", widget.getWidgetEmptyMessage());
    }

    @Test
    public void testGetWidgetId() {
        NextExecutionsWidget widget = new NextExecutionsWidget("test");
        assertEquals("next-exec", widget.getWidgetId());
    }

    @Test
    public void testShowWidget() {
        NextExecutionsWidget widget = new NextExecutionsWidget("test");
        assertTrue(widget.showWidget());
    }

    @Test
    public void testGetShowParameterizedWidget() {
        NextExecutionsWidget widget = new NextExecutionsWidget("test");
        widget.getShowParameterizedWidget();
    }

    // TODO: Add testGetBuilds()

    @Test
    public void testType() {
        NextExecutionsWidget.FactoryImpl factory = new NextExecutionsWidget.FactoryImpl();
        assertEquals(View.class, factory.type());
    }

    @Test
    public void testWidgetType() {
        NextExecutionsWidget.FactoryImpl factory = new NextExecutionsWidget.FactoryImpl();
        assertEquals(NextExecutionsWidget.class, factory.widgetType());
    }

    @Test
    public void testCreateFor() {
        NextExecutionsWidget.FactoryImpl factory = new NextExecutionsWidget.FactoryImpl();
        String url = "http://example.com/";
        View target = mock(View.class);
        when(target.getUrl()).thenReturn(url);
        Collection<NextExecutionsWidget> widgets = factory.createFor(target);
        assertEquals(1, widgets.size());
    }
}
