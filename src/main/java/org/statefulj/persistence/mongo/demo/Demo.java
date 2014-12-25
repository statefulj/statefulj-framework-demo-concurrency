/***
 * 
 * Copyright 2014 Andrew Hall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.statefulj.persistence.mongo.demo;

import javax.annotation.Resource;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.statefulj.fsm.FSM;
import org.statefulj.persistence.mongo.demo.foo.Foo;
import org.statefulj.persistence.mongo.demo.foo.FooRepository;

import static org.statefulj.persistence.mongo.demo.foo.Foo.*;

@Component
public class Demo implements CommandLineRunner {
	
	Logger logger = LoggerFactory.getLogger(Demo.class);

	@Resource
	FSM<Foo> fsm;

	@Resource
	FooRepository fooRepository;
	
	@Override
	public void run(String... args) throws Exception {
		
		// Instantiate the Stateful Entity
		//
		Foo foo = new Foo();

		fooRepository.save(foo);
		
		logger.debug("Foo={}", foo);
		Assert.assertEquals(stateA.getName(), foo.getStateDocument().getState());

		// Drive the FSM with a series of events: eventA, eventA, eventA
		//
		fsm.onEvent(foo, eventA);  // stateA(EventA) -> stateB/actionA
		
		foo = fooRepository.findOne(foo.getId());
		logger.debug("Foo={}", foo);
		Assert.assertEquals(stateB.getName(), foo.getStateDocument().getState());

		fsm.onEvent(foo, eventB);  // stateA(EventB) -> stateB/actionB

		foo = fooRepository.findOne(foo.getId());
		logger.debug("Foo={}", foo);
		Assert.assertEquals(stateC.getName(), foo.getStateDocument().getState());

		fsm.onEvent(foo, eventC);  // stateB(EventC) -> stateA/noop

		foo = fooRepository.findOne(foo.getId());
		logger.debug("Foo={}", foo);
		Assert.assertEquals(stateB.getName(), foo.getStateDocument().getState());

		foo.setBar(true);
		fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateB/NOOP

		foo = fooRepository.findOne(foo.getId());
		logger.debug("Foo={}", foo);
		Assert.assertEquals(stateB.getName(), foo.getStateDocument().getState());

		foo.setBar(false);
		fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateC/NOOP

		foo = fooRepository.findOne(foo.getId());
		logger.debug("Foo={}", foo);
		Assert.assertEquals(stateC.getName(), foo.getStateDocument().getState());

	}

}
