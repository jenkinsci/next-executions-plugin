package hudson.plugins.nextexecutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.View;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class PossibleNextExecutionsWidgetTest {

    @Test
    void testGetWidgetName(JenkinsRule j) {
        PossibleNextExecutionsWidget widget = new PossibleNextExecutionsWidget("test");
        assertEquals("Possible Next Executions", widget.getWidgetName());
    }

    // TODO: Add testShowWidget()

    @Test
    void testGetWidgetId(JenkinsRule j) {
        PossibleNextExecutionsWidget widget = new PossibleNextExecutionsWidget("test");
        assertEquals("next-exec-possible", widget.getWidgetId());
    }

    @Test
    void testType(JenkinsRule j) {
        PossibleNextExecutionsWidget.FactoryImpl factory = new PossibleNextExecutionsWidget.FactoryImpl();
        assertEquals(View.class, factory.type());
    }

    @Test
    void testWidgetType(JenkinsRule j) {
        PossibleNextExecutionsWidget.FactoryImpl factory = new PossibleNextExecutionsWidget.FactoryImpl();
        assertEquals(PossibleNextExecutionsWidget.class, factory.widgetType());
    }

    @Test
    void testCreateFor(JenkinsRule j) {
        PossibleNextExecutionsWidget.FactoryImpl factory = new PossibleNextExecutionsWidget.FactoryImpl();
        String url = "http://example.com/";
        View target = mock(View.class);
        when(target.getUrl()).thenReturn(url);
        Collection<PossibleNextExecutionsWidget> widgets = factory.createFor(target);
        assertEquals(1, widgets.size());
    }
}
