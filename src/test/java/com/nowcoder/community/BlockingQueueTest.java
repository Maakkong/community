package com.nowcoder.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/29 17:17
 * @Description 阻塞队列
 */
public class BlockingQueueTest {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue=new ArrayBlockingQueue<>(10);
        new Thread(new Producer(queue)).start();
        new Thread(new Comsumer(queue)).start();
        new Thread(new Comsumer(queue)).start();
        new Thread(new Comsumer(queue)).start();

    }


}
class Producer implements Runnable{
    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue){
        this.queue=queue;
    }

    @Override
    public void run() {
        try{
            for (int i = 0; i <100 ; i++) {
                Thread.sleep(20);
                queue.put(i);
                System.out.println(Thread.currentThread().getName()+"生产:"+queue.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Comsumer implements Runnable{
    private BlockingQueue<Integer> queue;

    public Comsumer(BlockingQueue<Integer> queue){
        this.queue=queue;
    }

    @Override
    public void run() {
        try{
            while (true){
                Thread.sleep(new Random().nextInt(1000));;
                queue.take();
                System.out.println(Thread.currentThread().getName()+"消费:"+queue.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
