package br.ufal.ic.judge

import br.ufal.ic.judge.commons.ClientRPC

/**
 * Created by huxley on 18/07/15.
 */
class SimilarityTest extends ClientRPC {
    SimilarityTest(String exchangeName, String key) {
        super(exchangeName, key);
    }

    void doWork (String message){
        println "ok";
    }

    public static void main(String[] argv) {
        SimilarityTest fibonacciRpc = new SimilarityTest("EXCHANGE", "similarity");
        fibonacciRpc.start();
        fibonacciRpc.call('[{"code":"print \'Ola mundo\';"}, {"code":"print \'Ola mundo\';"}, {"code":"print \'Ola mundo\';"}]');


    }

}





