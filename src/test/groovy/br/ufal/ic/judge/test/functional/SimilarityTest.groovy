package br.ufal.ic.judge.test.functional

import br.ufal.ic.judge.commons.ClientRPC

/**
 * Created by huxley on 18/07/15.
 */
class SimilarityTest extends ClientRPC {
    private String response
    SimilarityTest(String exchangeName, String key) {
        super(exchangeName, key);
    }

    void doWork (String message){
        response = message
        println message
        this.close()
    }

    public String getResponse() {
        return response
    }

    public boolean getLoop() {
        return this.listen
    }

    public static void main(String[] argv) {
        SimilarityTest fibonacciRpc = new SimilarityTest("EXCHANGE", "similarity");
        fibonacciRpc.start();
        fibonacciRpc.call('[{"code":"print \'Ola mundo\';"}, {"code":"print \'Ola mundo\';"}, {"code":"print \'Ola mundo\';"}]');


    }

}





