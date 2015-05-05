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
package org.statefulj.concurrency;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConcurrencyRunner implements CommandLineRunner {
	
	private final static Logger logger = LoggerFactory.getLogger(ConcurrencyRunner.class);
	
	@Resource
	private SafeValueDocument safeValueDocument;

	@Resource
	private UnsafeValueDocument unsafeValueDocument;

	@Resource
	private SafeValueDocumentRepository safeValueRepository;

	@Resource
	private UnsafeValueDocumentRepository unsafeValueRepository;

	@Override
	public void run(String... args) throws Exception {
		
		unsafeValueRepository.save(unsafeValueDocument);
		
		logger.debug("+----------------------------------------------+");
		logger.debug("|                                              |");
		logger.debug("|            Start Demo                        |");
		logger.debug("|                                              |");
		logger.debug("+----------------------------------------------+");
		logger.debug("");
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				for(int i = 0; i < 150; i++) {
					unsafeValueDocument.increment();
				}
			}
		};
		
		Thread t1 = new Thread(runnable);
		Thread t2 = new Thread(runnable);
		Thread t3 = new Thread(runnable);
		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
		
		logger.debug("Value=" + this.unsafeValueRepository.findOne(unsafeValueDocument.getId()).getValue());
		
		safeValueRepository.save(safeValueDocument);
		
		runnable = new Runnable() {
			
			@Override
			public void run() {
				for(int i = 0; i < 150; i++) {
					safeValueDocument.increment();
				}
			}
		};
		
		t1 = new Thread(runnable);
		t2 = new Thread(runnable);
		t3 = new Thread(runnable);
		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
		
		logger.debug("Value=" + this.safeValueRepository.findOne(safeValueDocument.getId()).getValue());
		
		logger.debug("");
		logger.debug("+----------------------------------------------+");
		logger.debug("|                                              |");
		logger.debug("|            End Demo                          |");
		logger.debug("|                                              |");
		logger.debug("+----------------------------------------------+");
	}

}
