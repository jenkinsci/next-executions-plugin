package hudson.plugins.nextexecutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import hudson.model.View;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class ParameterizedNextExecutionsWidgetTest {

    @Test
    void testGetWidgetName(JenkinsRule j) {
        ParameterizedNextExecutionsWidget widget = new ParameterizedNextExecutionsWidget("test");
        assertEquals("Parameterized Next Executions", widget.getWidgetName());
    }

    @Test
    void testShowWidget(JenkinsRule j) {
        ParameterizedNextExecutionsWidget factory = new ParameterizedNextExecutionsWidget("test");
        assertFalse(factory.showWidget());
    }

    @Test
    void testGetWidgetId(JenkinsRule j) {
        ParameterizedNextExecutionsWidget widget = new ParameterizedNextExecutionsWidget("test");
        assertEquals("next-exec-parameterized", widget.getWidgetId());
    }

    @Test
    void testType(JenkinsRule j) {
        ParameterizedNextExecutionsWidget.FactoryImpl factory = new ParameterizedNextExecutionsWidget.FactoryImpl();
        assertEquals(View.class, factory.type());
    }

    @Test
    void testWidgetType(JenkinsRule j) {
        ParameterizedNextExecutionsWidget.FactoryImpl factory = new ParameterizedNextExecutionsWidget.FactoryImpl();
        assertEquals(ParameterizedNextExecutionsWidget.class, factory.widgetType());
    }

    // TODO: Add testCreateFor()
}
