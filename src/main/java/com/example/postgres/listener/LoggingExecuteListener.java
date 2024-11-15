package com.example.postgres.listener;

import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import java.util.Arrays;

public class LoggingExecuteListener implements ExecuteListener {

    @Override
    public void start(ExecuteContext ctx) {
        DSLContext create = DSL.using(
                ctx.dialect(),
                new Settings().withRenderFormatted(true)
        );

        System.out.println("--------------------------------");
        if (ctx.query() != null) {
            System.out.println(create.renderInlined(ctx.query()));
        } else if (ctx.routine() != null) {
            System.out.println(create.renderInlined(ctx.routine()));
        } else {
            Arrays.stream(ctx.batchQueries())
                    .forEach(query -> System.out.println(create.renderInlined(query)));
        }
        System.out.println("--------------------------------");
    }
}
