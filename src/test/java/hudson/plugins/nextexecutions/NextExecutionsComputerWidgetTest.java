package hudson.plugins.nextexecutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.Computer;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import org.mockito.Mock;

@WithJenkins
class NextExecutionsComputerWidgetTest {
    @Mock
    private Computer computer;

    @Test
    void testGetWidgetName(JenkinsRule j) {
        NextExecutionsComputerWidget widget = new NextExecutionsComputerWidget(computer);
        assertEquals("Next Executions tied to this agent", widget.getWidgetName());
    }

    @Test
    void testGetWidgetEmptyMessage(JenkinsRule j) {
        NextExecutionsComputerWidget widget = new NextExecutionsComputerWidget(computer);
        assertEquals("No next executions found.", widget.getWidgetEmptyMessage());
    }

    @Test
    void testGetWidgetId(JenkinsRule j) {
        NextExecutionsComputerWidget widget = new NextExecutionsComputerWidget(computer);
        assertEquals("next-exec-computer", widget.getWidgetId());
    }

    @Test
    void testGetOwnerUrl(JenkinsRule j) {
        Computer computer1 = mock(Computer.class);
        when(computer1.getUrl()).thenReturn("test");
        NextExecutionsComputerWidget widget = new NextExecutionsComputerWidget(computer1);
        assertEquals("test", widget.getOwnerUrl());
    }

    // TODO: Add testGetBuilds()

    @Test
    void testType(JenkinsRule j) {
        NextExecutionsComputerWidget.FactoryImpl factory = new NextExecutionsComputerWidget.FactoryImpl();
        assertEquals(Computer.class, factory.type());
    }

    @Test
    void testWidgetType(JenkinsRule j) {
        NextExecutionsComputerWidget.FactoryImpl factory = new NextExecutionsComputerWidget.FactoryImpl();
        assertEquals(NextExecutionsComputerWidget.class, factory.widgetType());
    }

    @Test
    void testCreateFor(JenkinsRule j) {
        NextExecutionsComputerWidget.FactoryImpl factory = new NextExecutionsComputerWidget.FactoryImpl();
        Collection<NextExecutionsComputerWidget> widgets = factory.createFor(computer);
        assertEquals(1, widgets.size());
    }
}
