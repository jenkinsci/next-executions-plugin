package hudson.plugins.nextexecutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import hudson.model.View;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class ExtendedNextExecutionsWidgetTest {

    @Test
    void testGetWidgetName(JenkinsRule j) {
        ExtendedNextExecutionsWidget widget = new ExtendedNextExecutionsWidget("test");
        assertEquals("Next Extended Executions", widget.getWidgetName());
    }

    @Test
    void testShowWidget(JenkinsRule j) {
        ExtendedNextExecutionsWidget widget = new ExtendedNextExecutionsWidget("test");
        assertFalse(widget.showWidget());
    }

    @Test
    void testGetWidgetId(JenkinsRule j) {
        ExtendedNextExecutionsWidget widget = new ExtendedNextExecutionsWidget("test");
        assertEquals("next-exec-extended", widget.getWidgetId());
    }

    @Test
    void testType(JenkinsRule j) {
        ExtendedNextExecutionsWidget.FactoryImpl factory = new ExtendedNextExecutionsWidget.FactoryImpl();
        assertEquals(View.class, factory.type());
    }

    @Test
    void testWidgetType(JenkinsRule j) {
        ExtendedNextExecutionsWidget.FactoryImpl factory = new ExtendedNextExecutionsWidget.FactoryImpl();
        assertEquals(ExtendedNextExecutionsWidget.class, factory.widgetType());
    }

    // TODO: Add testCreateFor()
}
