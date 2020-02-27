package org.cloudply.service.assessment;

import com.google.common.collect.ImmutableSet;
import org.cloudply.assessment.AssessmentOperation;
import org.cloudply.assessment.AssessmentType;
import org.cloudply.assessment.SourceCodeAssessmentResource;
import org.cloudply.model.configuration.assessment.AssessmentConfiguration;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class ZapAssessmentService extends DockerAssessmentService {
    public static final AssessmentType ASSESSMENT_TYPE_ZAP = new AssessmentType(
            "Zap",
            "Zed Attack Proxy",
            SourceCodeAssessmentResource.class,
            AssessmentConfiguration.class
    );

    static {
        ImmutableSet<String> requiredRun = ImmutableSet.of("repositoryConfiguration");
        ImmutableSet<String> requiredRefresh = ImmutableSet.of();
        Map<AssessmentOperation, Set<String>> properties =
                ASSESSMENT_TYPE_ZAP.getRequiredResourceProperties();
        properties.put(AssessmentOperation.RUN_ASSESSMENT, requiredRun);
        properties.put(AssessmentOperation.REFRESH_REPORTS, requiredRefresh);
    }

    @Override
    public AssessmentType getAssessmentType() {
        return ASSESSMENT_TYPE_ZAP;
    }

    @Override
    protected String getImageName() {
        return "zap-worker";
    }
}
