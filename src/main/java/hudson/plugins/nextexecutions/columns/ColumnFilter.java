package hudson.plugins.nextexecutions.columns;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.DescriptorVisibilityFilter;
import jenkins.model.Jenkins;

@Extension
public class ColumnFilter extends DescriptorVisibilityFilter {
    @Override
    public boolean filter(Object context, @NonNull Descriptor descriptor) {
        if (descriptor instanceof ParameterizedNextExecutionColumn.DescriptorImpl) {
            return Jenkins.get().getPlugin("parameterized-scheduler") != null;
        }
        if (descriptor instanceof ExtendedNextExecutionColumn.DescriptorImpl) {
            return Jenkins.get().getPlugin("extended-timer-trigger") != null;
        }
        return true;
    }
}
