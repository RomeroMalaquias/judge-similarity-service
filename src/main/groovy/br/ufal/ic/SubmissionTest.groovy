package br.ufal.ic

import br.ufal.ic.commons.ClientRPC

import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.SimpleScriptContext

/**
 * Created by huxley on 18/07/15.
 */
class SubmissionTest extends ClientRPC {
    SubmissionTest(String exchangeName, String key) {
        super(exchangeName, key);
    }

    void doWork (String message){
        println "ok";
    }

    public static void main(String[] argv) {
        SubmissionTest fibonacciRpc = new SubmissionTest("EXCHANGE", "evaluator");
        fibonacciRpc.start();
        fibonacciRpc.call('{"code1":"print \'Ola mundo\';", "code2":"print \'Ola mundo\';", "threshold": 0.9}');
        fibonacciRpc.call('{"code1":"print \'Ola mundo2\';", "code2":"print \'Ola mundo23\';", "threshold": 0.9}');
        fibonacciRpc.call('{"code1":"print \'Ola mundo\';", "code2":"print \'Ola mundo32\';", "threshold": 0.9}');


    }

}





