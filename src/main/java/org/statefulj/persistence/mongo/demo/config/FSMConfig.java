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
package org.statefulj.persistence.mongo.demo.config;

import java.util.LinkedList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.statefulj.fsm.FSM;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.StateActionPair;
import org.statefulj.fsm.model.Transition;
import org.statefulj.fsm.model.impl.StateActionPairImpl;
import org.statefulj.persistence.mongo.MongoPersister;
import org.statefulj.persistence.mongo.demo.HelloAction;
import org.statefulj.persistence.mongo.demo.foo.Foo;
import static org.statefulj.persistence.mongo.demo.foo.Foo.*;

/**
 * @author Andrew Hall
 *
 */
@Configuration
public class FSMConfig {
	
	// Actions
	//
	public final static Action<Foo> actionA = new HelloAction("World");
	public final static Action<Foo> actionB = new HelloAction("Folks");
	
	@Bean
	public MongoPersister<Foo> mongoPersister(MongoTemplate mongoTemplate) {
		
		List<State<Foo>> states = new LinkedList<State<Foo>>();

		/* Deterministic Transitions */

		// stateA(eventA) -> stateB/actionA
		//
		stateA.addTransition(eventA, stateB, actionA); 

		// stateB(eventB) -> stateC/actionB
		//
		stateB.addTransition(eventB, stateC, actionB);
		
		// stateB(eventC) -> stateA/noop
		//
		stateC.addTransition(eventC, stateB);
		
		/* Non-Deterministic Transitions */

		//                  +--> stateB/NOOP  -- loop back on itself
		// stateB(eventA) --|
		//                  +--> stateC/NOOP
		//
		stateB.addTransition(eventA, new Transition<Foo>() {
			
			@Override
			public StateActionPair<Foo> getStateActionPair(Foo stateful) {
				State<Foo> next = null;
				
				if (stateful.isBar()) {
					next = stateB;
				} else {
					next = stateC;
				}
				
				// Move to the next state without taking any action
				//
				return new StateActionPairImpl<Foo>(next, null);
			}
		});

		// Build State list
		//
		states.add(stateA);
		states.add(stateB);
		states.add(stateC);
		
		return new MongoPersister<Foo>(
				states, 
				stateA, 
				Foo.class, 
				mongoTemplate);
	}
	
	@Bean
	public FSM<Foo> fsm(MongoPersister<Foo> mongoPersister) {
		
		// FSM
		//
		return new FSM<Foo>("Foo FSM", mongoPersister);
	}

}
