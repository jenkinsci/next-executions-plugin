package hudson.plugins.nextexecutions;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.Util;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * Provides a way to get the project's next execution date.
 *
 */
@ExportedBean(defaultVisibility = 2)
@SuppressWarnings("rawtypes")
public class NextBuilds implements Comparable, Describable<NextBuilds> {
    private ParameterizedJobMixIn.ParameterizedJob project;
    private String name;
    private Calendar date;
    private String parametersTooltip;

    public NextBuilds(ParameterizedJobMixIn.ParameterizedJob project, Calendar date) {
        this(project, date, null);
    }

    public NextBuilds(ParameterizedJobMixIn.ParameterizedJob project, Calendar date, String parametersTooltip) {
        this.project = project;
        this.name = Util.escape(project.getFullDisplayName());
        this.date = date;
        this.parametersTooltip = parametersTooltip;
    }

    @Exported
    public String getParametersTooltip() {
        return parametersTooltip != null ? parametersTooltip : "";
    }

    private String formatDate(ZonedDateTime d) {
        String dateFormat = this.getDescriptor().getDateFormat();
        if (dateFormat == null) {
            dateFormat = this.getDescriptor().getDefault();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return formatter.format(d);
    }

    @Exported
    public String getDate() {
        return formatDate(ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(date.getTimeInMillis()), date.getTimeZone().toZoneId()));
    }

    public String getTimeToGo() {
        ZoneId zone = date.getTimeZone().toZoneId();
        ZonedDateTime now = ZonedDateTime.now(zone);
        ZonedDateTime zonedDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(date.getTimeInMillis()), zone);
        Duration duration = Duration.between(now, zonedDate);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("d ");
        }
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m");
        }
        return Messages.timeToGo(sb.toString());
    }

    @Exported
    public String getName() {
        return name;
    }

    @Exported
    public String getUrl() {
        Jenkins j = Jenkins.getInstanceOrNull();
        return j != null ? j.getRootUrl() + project.getUrl() : null;
    }

    public String getshortName() {
        return (name.length() > 22) ? name.substring(0, 19) + "..." : name;
    }

    @Override
    @SuppressFBWarnings(value = "EQ_COMPARETO_USE_OBJECT_EQUALS")
    // TODO: Review this SuppressFBWarnings
    public int compareTo(Object o) {
        if (o instanceof NextBuilds) {
            NextBuilds toCompare = (NextBuilds) o;
            return this.date.compareTo(toCompare.date);

        } else {
            return 0;
        }
    }

    public DescriptorImpl getDescriptor() {
        Jenkins j = Jenkins.getInstanceOrNull();
        return j != null ? (DescriptorImpl) j.getDescriptorOrDie(getClass()) : null;
    }

    @Extension
    @Symbol("nextBuilds")
    public static class DescriptorImpl extends Descriptor<NextBuilds> {
        private String dateFormat;
        private Boolean filterByView;
        private Boolean showPossibleWidget;
        private Boolean showParameterizedWidget;
        private Boolean showExtendedWidget;

        public DescriptorImpl() {
            load();
        }

        @Override
        public String getDisplayName() {
            return "TESTING ";
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public boolean getFilterByView() {
            if (filterByView == null) return getFilterByViewDefault();
            return filterByView;
        }

        public void setFilterByView(boolean filterByView) {
            this.filterByView = filterByView;
        }

        public boolean getShowPossibleWidget() {
            if (showPossibleWidget == null) return false;
            return showPossibleWidget;
        }

        public void setShowPossibleWidget(boolean showPossibleWidget) {
            this.showPossibleWidget = showPossibleWidget;
        }

        public boolean getShowParameterizedWidget() {
            if (showParameterizedWidget == null) return false;
            return showParameterizedWidget;
        }

        public void setShowParameterizedWidget(boolean showParameterizedWidget) {
            this.showParameterizedWidget = showParameterizedWidget;
        }

        public boolean getShowExtendedWidget() {
            if (showExtendedWidget == null) return false;
            return showExtendedWidget;
        }

        public void setShowExtendedWidget(boolean showExtendedWidget) {
            this.showExtendedWidget = showExtendedWidget;
        }

        public String getDefault() {
            return "dd/MM/yyyy HH:mm z";
        }

        public boolean getFilterByViewDefault() {
            return true;
        }

        @Override
        public boolean configure(StaplerRequest2 req, JSONObject json) throws hudson.model.Descriptor.FormException {
            dateFormat = json.getString("dateFormat");
            filterByView = json.getBoolean("filterByView");
            showPossibleWidget = json.getBoolean("showPossibleWidget");
            showParameterizedWidget = json.getBoolean("showParameterizedWidget");
            showExtendedWidget = json.getBoolean("showExtendedWidget");
            save();
            return true;
        }

        public FormValidation doCheckDateFormat(@QueryParameter String value) {
            try {
                // We don't really use it. It's just to check if it
                //  throws an exception
                SimpleDateFormat sdf = new SimpleDateFormat(value);
                // Call random method to avoid spotbugs DLS_DEAD_LOCAL_STORE
                sdf.getClass();
                return FormValidation.ok();
            } catch (IllegalArgumentException e) {
                return FormValidation.error(Messages.Format_Error());
            }
        }
    }
}
