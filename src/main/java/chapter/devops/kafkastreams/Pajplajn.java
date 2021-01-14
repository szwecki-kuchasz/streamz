/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package chapter.devops.kafkastreams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * In this example, we implement a simple LineSplit program using the high-level Streams DSL
 * that reads from a source topic "streams-plaintext-input", where the values of messages represent lines of text;
 * the code split each text line in string into words and then write back into a sink topic "streams-linesplit-output" where
 * each record represents a single word.
 */
public class Pajplajn {

    public static void main(String[] args) throws Exception {

        new Pajplajn().run();

    }

    private void run() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-linesplit");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();

        final KStream<String, String>[] branches = builder.<String, String>stream("streams-plaintext-input")
                .flatMapValues(value -> Arrays.asList(value.split("\\W+"))) //
                // w tym miejscu mamy pojedyncze słowa, porozdzielajmy je po długości, podając do metody branch() 3 predykaty:...
                .peek((k, v) -> System.out.print(v + ' '))
                .branch(
                        //  do 4 liter,
                        getStringLengthPredicate(0, 4),
                        // od 5 do 8
                        getStringLengthPredicate(5, 8),
                        //  od 9 liter wzwyż
                        getStringLengthPredicate(9, Integer.MAX_VALUE));

        final KStream<String, String> stringStringKStream0 =
                branches[0].mapValues(
                        s -> s.toLowerCase() + " [<=4]");

        final KStream<String, String> stringStringKStream1 =
                branches[1].mapValues(
                        s -> s + " [5..8]");

        final KStream<String, String> stringStringKStream2 =
                branches[2]
                        .mapValues(s -> s.toUpperCase() + " [>=9]");

        final KStream<String, String> merge = stringStringKStream0
                .through("streams-linesplit-outputBelow5")
                .merge(
                        stringStringKStream1
                                .through("streams-linesplit-output5To8")
                                .merge(
                                        stringStringKStream2
                                                .through("streams-linesplit-outputAbove8")
                                ));
        merge.to("streams-linesplit-output");

        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                System.out.println("Running Shutdown Hook");
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(5);
        }
        System.exit(0);
    }

    private String poll() {
        return "" + System.currentTimeMillis();
    }

    Predicate<String, String> getStringLengthPredicate(int min, int max) {
        return new Predicate<String, String>() {
            @Override
            public boolean test(String key, String value) {
                return value != null && value.length() >= min && value.length() <= max;
            }
        };
    }

}
