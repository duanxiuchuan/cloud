package com.erongdu.server.job.runner;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.erongdu.common.core.base.BaseStartedUpRunner;

/**
 * @author erongdu.com
 */
@Component
public class StartedUpRunner extends BaseStartedUpRunner {

	public StartedUpRunner(ConfigurableApplicationContext context, Environment environment) {
		super(context, environment);
	}

}
