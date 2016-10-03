package io.fabric8.quickstarts.camel.brms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.fabric8.quickstarts.camel.brms.model.Greeting;
import io.fabric8.quickstarts.camel.brms.model.Person;

import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.server.api.model.ServiceResponse;
import org.springframework.stereotype.Component;

@Component
public class DecisionServerHelper {

    private static final String[] NAMES = {"Seamus", "George", "Lorraine", "Marty", "Dave", "Linda"};

    /** The random. */
    private final Random random = new Random();


    public BatchExecutionCommand createRandomCommand() {
        Person person = new Person();
        String name = NAMES[random.nextInt(NAMES.length)];
        person.setName(name);

        List<Command<?>> cmds = new ArrayList<Command<?>>();
        KieCommands commands = KieServices.Factory.get().getCommands();
        cmds.add(commands.newInsert(person));
        cmds.add(commands.newFireAllRules());
        cmds.add(commands.newQuery("greetings", "get greeting"));
        BatchExecutionCommand command = commands.newBatchExecution(cmds, "HelloRulesSession");
        return command;
    }

    public Greeting extractResult(ServiceResponse<ExecutionResults> response) {
        ExecutionResults res = response.getResult();
        Greeting greeting = null;
        if (res != null) {
            QueryResults queryResults = (QueryResults) res.getValue("greetings");
            for (QueryResultsRow queryResult : queryResults) {
                greeting = (Greeting) queryResult.get("greeting");
                break;
            }
        }

        return greeting;
    }

}
