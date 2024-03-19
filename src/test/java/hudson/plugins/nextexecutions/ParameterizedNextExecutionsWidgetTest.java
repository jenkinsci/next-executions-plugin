package hudson.plugins.nextexecutions;

import static org.junit.Assert.*;

import hudson.model.View;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class ParameterizedNextExecutionsWidgetTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testGetWidgetName() {
        ParameterizedNextExecutionsWidget widget = new ParameterizedNextExecutionsWidget("test");
        assertEquals("Parameterized Next Executions", widget.getWidgetName());
    }

    @Test
    public void testShowWidget() {
        ParameterizedNextExecutionsWidget factory = new ParameterizedNextExecutionsWidget("test");
        assertFalse(factory.showWidget());
    }

    @Test
    public void testGetWidgetId() {
        ParameterizedNextExecutionsWidget widget = new ParameterizedNextExecutionsWidget("test");
        assertEquals("next-exec-parameterized", widget.getWidgetId());
    }

    @Test
    public void testType() {
        ParameterizedNextExecutionsWidget.FactoryImpl factory = new ParameterizedNextExecutionsWidget.FactoryImpl();
        assertEquals(View.class, factory.type());
    }

    @Test
    public void testWidgetType() {
        ParameterizedNextExecutionsWidget.FactoryImpl factory = new ParameterizedNextExecutionsWidget.FactoryImpl();
        assertEquals(ParameterizedNextExecutionsWidget.class, factory.widgetType());
    }

    // TODO: Add testCreateFor()
}
