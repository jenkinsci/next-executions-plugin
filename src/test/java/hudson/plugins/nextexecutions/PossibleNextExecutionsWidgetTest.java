package hudson.plugins.nextexecutions;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.View;
import java.util.Collection;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class PossibleNextExecutionsWidgetTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testGetWidgetName() {
        PossibleNextExecutionsWidget widget = new PossibleNextExecutionsWidget("test");
        assertEquals("Possible Next Executions", widget.getWidgetName());
    }

    // TODO: Add testShowWidget()

    @Test
    public void testGetWidgetId() {
        PossibleNextExecutionsWidget widget = new PossibleNextExecutionsWidget("test");
        assertEquals("next-exec-possible", widget.getWidgetId());
    }

    @Test
    public void testType() {
        PossibleNextExecutionsWidget.FactoryImpl factory = new PossibleNextExecutionsWidget.FactoryImpl();
        assertEquals(View.class, factory.type());
    }

    @Test
    public void testWidgetType() {
        PossibleNextExecutionsWidget.FactoryImpl factory = new PossibleNextExecutionsWidget.FactoryImpl();
        assertEquals(PossibleNextExecutionsWidget.class, factory.widgetType());
    }

    @Test
    public void testCreateFor() {
        PossibleNextExecutionsWidget.FactoryImpl factory = new PossibleNextExecutionsWidget.FactoryImpl();
        String url = "http://example.com/";
        View target = mock(View.class);
        when(target.getUrl()).thenReturn(url);
        Collection<PossibleNextExecutionsWidget> widgets = factory.createFor(target);
        assertEquals(1, widgets.size());
    }
}
