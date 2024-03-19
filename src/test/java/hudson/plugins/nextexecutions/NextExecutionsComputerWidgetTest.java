package hudson.plugins.nextexecutions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.Computer;
import java.util.Collection;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.mockito.Mock;

public class NextExecutionsComputerWidgetTest {
    @Mock
    private Computer computer;

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testGetWidgetName() {
        NextExecutionsComputerWidget widget = new NextExecutionsComputerWidget(computer);
        assertEquals("Next Executions tied to this agent", widget.getWidgetName());
    }

    @Test
    public void testGetWidgetEmptyMessage() {
        NextExecutionsComputerWidget widget = new NextExecutionsComputerWidget(computer);
        assertEquals("No next executions found.", widget.getWidgetEmptyMessage());
    }

    @Test
    public void testGetWidgetId() {
        NextExecutionsComputerWidget widget = new NextExecutionsComputerWidget(computer);
        assertEquals("next-exec-computer", widget.getWidgetId());
    }

    @Test
    public void testGetOwnerUrl() {
        Computer computer1 = mock(Computer.class);
        when(computer1.getUrl()).thenReturn("test");
        NextExecutionsComputerWidget widget = new NextExecutionsComputerWidget(computer1);
        assertEquals("test", widget.getOwnerUrl());
    }

    // TODO: Add testGetBuilds()

    @Test
    public void testType() {
        NextExecutionsComputerWidget.FactoryImpl factory = new NextExecutionsComputerWidget.FactoryImpl();
        assertEquals(Computer.class, factory.type());
    }

    @Test
    public void testWidgetType() {
        NextExecutionsComputerWidget.FactoryImpl factory = new NextExecutionsComputerWidget.FactoryImpl();
        assertEquals(NextExecutionsComputerWidget.class, factory.widgetType());
    }

    @Test
    public void testCreateFor() {
        NextExecutionsComputerWidget.FactoryImpl factory = new NextExecutionsComputerWidget.FactoryImpl();
        Collection<NextExecutionsComputerWidget> widgets = factory.createFor(computer);
        assertEquals(1, widgets.size());
    }
}
