package io.fabric8.quickstarts.camel.brms;

import io.fabric8.quickstarts.camel.brms.model.Person;

import com.thoughtworks.xstream.XStream;

import org.kie.internal.runtime.helper.BatchExecutionHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DecisionServerAutoConfiguration {

    private static final String HELLO_RULES_PACKAGE_NAME = "org.openshift.quickstarts.decisionserver.hellorules";

    /**
     * Adding a customized XStream marshaller to the registry.
     * @return a kie-compatible XStream marshaller
     */
    @Bean
    public XStream xstreamMarshaller() {
        XStream xstream = BatchExecutionHelper.newXStreamMarshaller();
        // Use the "model" package instead of the one used on the kie server
        xstream.aliasPackage(HELLO_RULES_PACKAGE_NAME, Person.class.getPackage().getName());

        return xstream;
    }

}
