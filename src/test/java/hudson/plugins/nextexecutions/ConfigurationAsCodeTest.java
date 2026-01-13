package hudson.plugins.nextexecutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import io.jenkins.plugins.casc.misc.junit.jupiter.WithJenkinsConfiguredWithCode;
import org.junit.jupiter.api.Test;

@WithJenkinsConfiguredWithCode
class ConfigurationAsCodeTest {

    @Test
    @ConfiguredWithCode("configuration-as-code.yml")
    void should_support_configuration_as_code(JenkinsConfiguredWithCodeRule r) {
        NextBuilds.DescriptorImpl descriptor = r.jenkins.getDescriptorByType(NextBuilds.DescriptorImpl.class);
        assertEquals("dd/MM/yyyy HH:mm z", descriptor.getDateFormat());
        assertFalse(descriptor.getFilterByView());
        assertFalse(descriptor.getShowPossibleWidget());
        assertFalse(descriptor.getShowParameterizedWidget());
        assertFalse(descriptor.getShowExtendedWidget());
    }
}
